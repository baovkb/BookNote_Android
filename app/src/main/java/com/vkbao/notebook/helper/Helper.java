package com.vkbao.notebook.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.vkbao.notebook.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class Helper {

    private static final String TMP_IMG_DIR_NAME = "Temporary Images";
    private static final String IMG_DIR_NAME = "Images";

    public static String copyFileToInternalStorage(File srcFile, File desDirectory, String newFileName) {
        if (!srcFile.exists()) return null;

        if (!desDirectory.exists()) {
            desDirectory.mkdirs();
        }

        File destinationFile = new File(desDirectory, newFileName);
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
                    //image
                    Drawable imageDrawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource(activity.getContentResolver(), uriImg));
                    if (imageDrawable == null) continue;

                    String uniqueStr = Long.toString(TimeConvertor.getCurrentUnixMiliSecond()) + Helper.randomInt(1, 100);
                    String replacedStr = "[img_ " + uniqueStr + "]";

                    //copy selected images to temporary directory
                    copyFileToInternalStorage(
                            activity.getApplicationContext(),
                            uriImg,
                            activity.getApplicationContext().getFilesDir() + "/" + TMP_IMG_DIR_NAME,
                            uniqueStr + ".jpeg");

                    float ratio = getImgRatio(editText.getWidth(), imageDrawable.getIntrinsicWidth());
                    int imgWidth = (int)(imageDrawable.getIntrinsicWidth() * ratio);
                    int imgHeight = (int)(imageDrawable.getIntrinsicHeight() * ratio);

                    imageDrawable.setBounds(0, 0, imgWidth, imgHeight);

                    int iconDeleteSize = 50;
                    Drawable iconDeleteDrawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_action_delete_x, null);
                    iconDeleteDrawable.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

                    iconDeleteDrawable.setBounds(0, 0, iconDeleteSize, iconDeleteSize);

                    CustomImageSpan imageSpan = new CustomImageSpan(
                            imageDrawable,
                            iconDeleteDrawable,
                            cursorPosition,
                            cursorPosition + replacedStr.length(),
                            replacedStr
                    );

                    imageSpan.setOnIconClick(() -> {
                        int start = imageSpan.getStart();
                        int end = imageSpan.getEnd();
                        SpannableStringBuilder tmpStringBuilder = new SpannableStringBuilder(editText.getText());
                        tmpStringBuilder.delete(start, end);
                        editText.setText(tmpStringBuilder);
                    });

                    spannableStringBuilder.insert(cursorPosition, replacedStr + "\n");
                    spannableStringBuilder.setSpan(imageSpan, imageSpan.getStart(), imageSpan.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    CustomClickableSpan customClickableSpan = new CustomClickableSpan(imageSpan);
                    spannableStringBuilder.setSpan(customClickableSpan, imageSpan.getStart(), imageSpan.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    cursorPosition += 1 + replacedStr.length();
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
}
