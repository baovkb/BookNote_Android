package com.vkbao.notebook.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.vkbao.notebook.R;
import com.vkbao.notebook.models.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface OnDeleteIconClick {
    public void onClick(CustomImageSpan customImageSpan);
}

public class Helper {

    private static final String TMP_IMG_DIR_NAME = "Temporary Images";
    private static final String IMG_DIR_NAME = "Images";

    public static String getImgPath(Context context) {
        return context.getFilesDir() + "/" + IMG_DIR_NAME;
    }

    public static String getTmpImgPath(Context context) {
        return context.getFilesDir() + "/" + TMP_IMG_DIR_NAME;
    }

    public static String copyFileToInternalStorage(File srcFile, File desDirectory, String newFileName) {
        if (!srcFile.exists()) return null;

        if (!desDirectory.exists()) {
            desDirectory.mkdirs();
        }

        File destinationFile = new File(desDirectory, newFileName);
        if (destinationFile.exists()) return null;

        try (InputStream inputStream = new FileInputStream(srcFile);
             OutputStream outputStream = new FileOutputStream(destinationFile)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Cannot obtain input stream from URI");
            }

            byte[] buffer = new byte[4096];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return destinationFile.getAbsolutePath();
    }

    public static String copyFileToInternalStorage(Context context, Uri srcUri, String desDirectoryPath, String newFileName) {
        ContentResolver resolver = context.getContentResolver();
        File destinationDir = new File(desDirectoryPath);
        if (!destinationDir.exists()) destinationDir.mkdirs();

        File destinationFile = new File(destinationDir, newFileName);
        try (InputStream inputStream = resolver.openInputStream(srcUri);
             OutputStream outputStream = new FileOutputStream(destinationFile)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Cannot obtain input stream from URI");
            }

            byte[] buffer = new byte[4096];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return destinationFile.getAbsolutePath();
    }

    public static String copyFileToInternalStorage(String srcFilePath, String desDirectoryPath, String newFileName) {
        File sourceFile = new File(srcFilePath);
        File destinationDir = new File(desDirectoryPath);
        return copyFileToInternalStorage(sourceFile, destinationDir, newFileName);
    }

    public static int randomInt(int start, int end) {
        Random rand = new Random();
        return rand.nextInt(end) + start;
    }

    private static float getImgRatio(int widthView, int imgWidth) {
        if (imgWidth > widthView) {
            return widthView / imgWidth;
        }
        return 1;
    }

    public static void cleanDirectory(File directory) {
        if (directory == null || !directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file: files) {
                if (file.isDirectory()) {
                    cleanDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) return;

        file.delete();
    }

    public static void insertImgToEditTextView(Activity activity, List<Uri> uriList, EditText editText) {
        if (editText != null) {
            try {
                int cursorPosition = 0;
                if (editText.isFocused()) {
                    cursorPosition = editText.getSelectionStart();
                } else {
                    cursorPosition = editText.getText().length();
                }

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getText());
                for (Uri uriImg: uriList) {
                    //main image
                    Drawable imageDrawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource(activity.getContentResolver(), uriImg));
                    if (imageDrawable == null) continue;

                    String uniqueStr = Long.toString(TimeConvertor.getCurrentUnixMiliSecond()) + Helper.randomInt(1, 100);
                    String replacedStr = "[img_" + uniqueStr + "]";

                    //copy selected images to temporary directory
                    copyFileToInternalStorage(
                            activity.getApplicationContext(),
                            uriImg,
                            getTmpImgPath(activity),
                            uniqueStr + ".jpeg");

                    float ratio = getImgRatio(editText.getWidth(), imageDrawable.getIntrinsicWidth());
                    int imgWidth = (int)(imageDrawable.getIntrinsicWidth() * ratio);
                    int imgHeight = (int)(imageDrawable.getIntrinsicHeight() * ratio);
                    imageDrawable.setBounds(0, 0, imgWidth, imgHeight);

                    SpannableStringBuilder getImgString = createImgString(activity, imageDrawable, replacedStr, (imageSpan) -> {
                        String uniqueString = imageSpan.getUniqueString();
                        SpannableStringBuilder tmpStringBuilder = new SpannableStringBuilder(editText.getText());
                        String tmpStr = tmpStringBuilder.toString();
                        int start = tmpStr.indexOf(uniqueString);
                        if (start != -1)
                            tmpStringBuilder.delete(start, start + uniqueString.length());
                        editText.setText(tmpStringBuilder);
                    });

                    spannableStringBuilder.insert(cursorPosition, getImgString);
                    cursorPosition += replacedStr.length();
                }

                editText.setText(spannableStringBuilder);
                editText.setSelection(cursorPosition);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static SpannableStringBuilder createImgString(Activity activity,
                                                             Drawable mainDrawable,
                                                             String uniqueString,
                                                             OnDeleteIconClick listener) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(uniqueString);
        if (mainDrawable == null) return spannableStringBuilder;

        if (listener != null) {
            int iconDeleteSize = 50;
            Drawable iconDeleteDrawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_action_delete_x, null);
            iconDeleteDrawable.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            iconDeleteDrawable.setBounds(0, 0, iconDeleteSize, iconDeleteSize);

            CustomImageSpan imageSpan = new CustomImageSpan(
                    mainDrawable,
                    iconDeleteDrawable,
                    uniqueString
            );

            imageSpan.setOnIconClick(() -> {
               listener.onClick(imageSpan);
            });

            //setSpan -> [start:end)
            CustomClickableSpan customClickableSpan = new CustomClickableSpan(imageSpan);
            spannableStringBuilder.setSpan(imageSpan, 0, uniqueString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(customClickableSpan, 0, uniqueString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ImageSpan imageSpan = new ImageSpan(mainDrawable);
            spannableStringBuilder.setSpan(imageSpan, 0, uniqueString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableStringBuilder;
    }

    public static void parseTextModeEdit(Activity activity, List<Image> imageList, EditText editText, String text) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);

        Pattern pattern = Pattern.compile("\\[img_(\\d+)\\]");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String replacedString = matcher.group();
            String nameImg = replacedString.replace("[img_", "").replace("]", "");

            for (Image image: imageList) {
                if (image.getName().equals(nameImg)) {
                    File imgFile = new File(image.getUrl());

                    try {
                        Drawable imgDrawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource(imgFile));
                        if (imgDrawable == null) break;
                        imgDrawable.setBounds(0, 0, imgDrawable.getIntrinsicWidth(), imgDrawable.getIntrinsicHeight());
                        SpannableStringBuilder imgText = createImgString(activity, imgDrawable, replacedString, (imageSpan) -> {
                            String uniqueString = imageSpan.getUniqueString();
                            SpannableStringBuilder tmpStringBuilder = new SpannableStringBuilder(editText.getText());
                            String tmpStr = tmpStringBuilder.toString();
                            int startIndex = tmpStr.indexOf(uniqueString);
                            if (startIndex != -1)
                                tmpStringBuilder.delete(startIndex, startIndex + uniqueString.length());
                            editText.setText(tmpStringBuilder);
                        });

                        stringBuilder.replace(start, end, imgText);

                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        editText.setText(stringBuilder);
        editText.setSelection(stringBuilder.length());
    }

    public static List<String> getImgIDFromText(String text) {
        List<String> idImgList = new ArrayList<>();
        String regex = "\\[img_(\\d+)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while(matcher.find()) {
            idImgList.add(matcher.group().replace("[img_", "").replace("]", ""));
        }

        return idImgList;
    }

    public static SpannableStringBuilder parseText(String text, List<Image> imageList) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);

        Pattern pattern = Pattern.compile("\\[img_(\\d+)\\]");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String nameImg = matcher.group().replace("[img_", "").replace("]", "");

            for (Image image: imageList) {
                if (image.getName().equals(nameImg)) {
                    File imgFile = new File(image.getUrl());

                    try {
                        Drawable imgDrawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource(imgFile));
                        imgDrawable.setBounds(0, 0, imgDrawable.getIntrinsicWidth(), imgDrawable.getIntrinsicHeight());
                        if (imgDrawable == null) break;

                        ImageSpan imageSpan = new ImageSpan(imgDrawable);
                        stringBuilder.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return stringBuilder;
    }

    public static SpannableStringBuilder parseText(Context context, String text) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
        Pattern pattern = Pattern.compile("\\[img_(\\d+)\\]");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            try {
                Drawable imgDrawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource(context.getResources(), R.drawable.ic_action_image));
                if (imgDrawable == null) continue;
                imgDrawable.setBounds(0, 0, 60, 60);
                imgDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

                ImageSpan imageSpan = new ImageSpan(imgDrawable);
                stringBuilder.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return stringBuilder;
    }
}
