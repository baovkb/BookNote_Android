package com.vkbao.notebook.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.ImageDecoder;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
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
            return (float)widthView / imgWidth;
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
        editText.post(() -> {
            int cursorPosition = 0;
            if (editText.isFocused()) {
                cursorPosition = editText.getSelectionStart() != -1 ? editText.getSelectionStart() : 0;
            } else {
                cursorPosition = editText.getText().toString().length() != -1 ? editText.getText().toString().length() : 0;
            }

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getText());
            for (Uri uriImg: uriList) {
                //main image
                Drawable imageDrawable = createImagDrawable(activity, uriImg, editText.getWidth() - 50);
                if (imageDrawable == null) continue;

                String uniqueStr = Long.toString(TimeConvertor.getCurrentUnixMiliSecond()) + Helper.randomInt(1, 100);
                String replacedStr = "[img_" + uniqueStr + "]";

                //copy selected images to temporary directory
                copyFileToInternalStorage(
                        activity.getApplicationContext(),
                        uriImg,
                        getTmpImgPath(activity),
                        uniqueStr + ".jpeg");


                SpannableStringBuilder getImgString = createImgString(activity, imageDrawable, replacedStr, (imageSpan) -> {
                    String uniqueString = imageSpan.getUniqueString();
                    SpannableStringBuilder tmpStringBuilder = new SpannableStringBuilder(editText.getText());
                    String tmpStr = tmpStringBuilder.toString();
                    int start = tmpStr.indexOf(uniqueString);
                    if (start != -1)
                        tmpStringBuilder.delete(start, start + uniqueString.length());
                    editText.setText(tmpStringBuilder);
                });

                try {
                    Log.d("test", "string: " + spannableStringBuilder.toString());
                    Log.d("test", "cursor: " + cursorPosition);
                    spannableStringBuilder.insert(cursorPosition, getImgString);
                    cursorPosition += replacedStr.length();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            editText.setText(spannableStringBuilder);
        });
    }

    public static SpannableStringBuilder createImgString(Activity activity,
                                                             Drawable mainDrawable,
                                                             String uniqueString,
                                                             OnDeleteIconClick listener) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(uniqueString);
        if (mainDrawable == null) return spannableStringBuilder;

        if (listener != null) {
            Drawable iconDeleteDrawable = createDeleteIcon(activity);

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

        editText.post(() -> {
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

                        Drawable imgDrawable = createImgDrawable(imgFile, editText.getWidth());
                        if (imgDrawable == null) break;

                        SpannableStringBuilder imgText = createImgString(activity, imgDrawable, replacedString, (imageSpan) -> {
                            String uniqueString = imageSpan.getUniqueString();
                            SpannableStringBuilder tmpStringBuilder = new SpannableStringBuilder(editText.getText());
                            String tmpStr = tmpStringBuilder.toString();
                            int startIndex = tmpStr.indexOf(uniqueString);
                            if (startIndex != -1)
                                tmpStringBuilder.delete(startIndex, startIndex + uniqueString.length());
                            editText.setText(tmpStringBuilder);
                        });

                        try {
                            stringBuilder.replace(start, end, imgText);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;

                    }
                }
            }
            editText.setText(stringBuilder);
        });

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

    public static SpannableStringBuilder parseText(String text, int maxAllowedWidth, List<Image> imageList) {
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

                    Drawable imgDrawable = createImgDrawable(imgFile, maxAllowedWidth);
                    if (imgDrawable == null) break;

                    ImageSpan imageSpan = new ImageSpan(imgDrawable);
                    stringBuilder.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    break;

                }
            }
        }
        return stringBuilder;
    }

    private static Drawable createImgDrawable(File imgFile, int maxAllowedWidth) {
        try {
            Drawable imgDrawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource(imgFile));
            float ratio = getImgRatio(maxAllowedWidth, imgDrawable.getIntrinsicWidth());
            int width = (int)(imgDrawable.getIntrinsicWidth() * ratio);
            int height = (int)(imgDrawable.getIntrinsicHeight() * ratio);
            imgDrawable.setBounds(0, 0, width, height);

            return imgDrawable;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Drawable createImagDrawable(Activity activity, Uri uri, int maxAllowedWidth) {
        try {
            Drawable imageDrawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource(activity.getContentResolver(), uri));
            if (imageDrawable == null) return null;

            float ratio = getImgRatio(maxAllowedWidth, imageDrawable.getIntrinsicWidth());
            int imgWidth = (int)(imageDrawable.getIntrinsicWidth() * ratio);
            int imgHeight = (int)(imageDrawable.getIntrinsicHeight() * ratio);
            imageDrawable.setBounds(0, 0, imgWidth, imgHeight);

            return imageDrawable;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Drawable createDeleteIcon(Activity activity) {
        int iconDeleteSize = 64;
        Drawable iconDeleteDrawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_action_delete_x, null);
        iconDeleteDrawable.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        iconDeleteDrawable.setBounds(0, 0, iconDeleteSize, iconDeleteSize);

        return iconDeleteDrawable;
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
