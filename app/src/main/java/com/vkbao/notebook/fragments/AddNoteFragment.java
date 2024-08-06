package com.vkbao.notebook.fragments;

import static android.app.Activity.RESULT_OK;

import static com.vkbao.notebook.helper.Helper.getImgPath;
import static com.vkbao.notebook.helper.Helper.insertImgToEditTextView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.notebook.R;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.adapters.AddNoteLabelAdapter;
import com.vkbao.notebook.dialogs.NoteLabelDialogFragment;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.helper.CustomClickableSpan;
import com.vkbao.notebook.helper.CustomImageSpan;
import com.vkbao.notebook.helper.Helper;
import com.vkbao.notebook.helper.TimeConvertor;
import com.vkbao.notebook.models.Image;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;
import com.vkbao.notebook.viewmodels.AddNoteLabelViewModel;
import com.vkbao.notebook.viewmodels.ImageViewModel;
import com.vkbao.notebook.viewmodels.LabelViewModel;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddNoteFragment extends Fragment
        implements View.OnClickListener{
    private NoteViewModel noteViewModel;
    private TextInputEditText noteTitle;
    private TextInputEditText noteDescription;
    private Button addNoteLabelAddBtn;
    private NoteLabelViewModel noteLabelViewModel;
    private LabelViewModel labelViewModel;
    private ImageViewModel imageViewModel;
    private AddNoteLabelAdapter addNoteLabelAdapter;
    private RecyclerView addNoteLabelRecyclerView;
    private List<Label> tmpChosenLabel;
    private ImageButton insertImgBtn;

    private final int REQUEST_GALLERY_CODE = 1;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setUpEvent();
    }

    public void init(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.add_note_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteLabelViewModel = new ViewModelProvider(requireActivity()).get(NoteLabelViewModel.class);
        imageViewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);
        tmpChosenLabel = new ArrayList<>();

        noteTitle = view.findViewById(R.id.add_note_title_text_input_field);
        noteDescription = view.findViewById(R.id.add_note_description_text_input_field);
        noteDescription.setMovementMethod(LinkMovementMethod.getInstance());
        addNoteLabelAddBtn = view.findViewById(R.id.add_note_label_add_btn);
        addNoteLabelRecyclerView = view.findViewById(R.id.add_note_label_list);
        addNoteLabelAdapter = new AddNoteLabelAdapter();
        insertImgBtn = view.findViewById(R.id.add_note_insert_img_btn);

        addNoteLabelRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        addNoteLabelRecyclerView.setAdapter(addNoteLabelAdapter);

        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        addNoteLabelAddBtn.setOnClickListener(this);
        insertImgBtn.setOnClickListener(this);
    }

    public void setUpEvent() {
        noteDescription.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean isDeleteDetect = false;
                SpannableStringBuilder spannableStringBuilderInner = new SpannableStringBuilder(noteDescription.getText());

                for (CustomClickableSpan span : spannableStringBuilderInner.getSpans(0, spannableStringBuilderInner.length(), CustomClickableSpan.class)) {
                    if ((span != null && span instanceof CustomClickableSpan)) {
                        isDeleteDetect = span.runOnIconClick(event.getX(), event.getY());
                        if (isDeleteDetect) break;
                    }
                }
            }
            return false;
        });
    }

    private MenuProvider getMenuProvider() {
        return new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.add_note_actions, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                FragmentManager fragmentManager = getParentFragmentManager();

                if (id == android.R.id.home) {
                    fragmentManager.popBackStack();
                } else if (id == R.id.save_note) {
                    String title = noteTitle.getText().toString();
                    String description = noteDescription.getText().toString();
                    if (!title.trim().isEmpty() || !description.trim().isEmpty()) {

                        final List<String> imgAbsolutePathList = new ArrayList<>();
                        final List<String> imgNameList = new ArrayList<>();
                        Thread copyImgThread = new Thread(() -> {
                            imgNameList.addAll(Helper.getImgIDFromText(description));
                            imgAbsolutePathList.addAll(copyImg(imgNameList));
                        });
                        copyImgThread.start();

                        long currentUnix = TimeConvertor.getCurrentUnixSecond();
                        Note note = new Note(title, description, currentUnix, currentUnix);
                        noteViewModel.insert(result -> {
                            if (result != null && result.length == 1) {

                                //insert note label to db
                                NoteLabel[] noteLabelArray = new NoteLabel[tmpChosenLabel.size()];
                                for (int i = 0; i < tmpChosenLabel.size(); ++i) {
                                    noteLabelArray[i] = new NoteLabel(result[0], tmpChosenLabel.get(i).getLabel_id());
                                }
                                noteLabelViewModel.insert(noteLabelArray);

                                //wait for copy img thread done
                                try {
                                    copyImgThread.join();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //insert image to db
                                Image[] images = new Image[imgNameList.size()];
                                for (int i = 0; i < imgNameList.size(); ++i) {
                                    images[i] = new Image(result[0], imgAbsolutePathList.get(i), imgNameList.get(i));
                                }
                                imageViewModel.insert(images);

                                //delete temporary image
                                Helper.cleanDirectory(new File(Helper.getTmpImgPath(requireActivity())));

                                fragmentManager.popBackStackImmediate();
                            }
                        }, note);
                    }

                }
                return true;
            }
        };
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == addNoteLabelAddBtn.getId()) {
            NoteLabelDialogFragment noteLabelDialogFragment = new NoteLabelDialogFragment();
            noteLabelDialogFragment.OnListenDialogBtnClick(new NoteLabelDialogFragment.NoticeDialogListener() {
                @Override
                public void onDialogPositiveClick(DialogFragment dialog, List<Label> chosenLabels) {
                    tmpChosenLabel = chosenLabels;
                    addNoteLabelAdapter.setLabel(tmpChosenLabel);
                }

                @Override
                public void onDialogNegativeClick(DialogFragment dialog) {

                }
            });
            Bundle bundle = new Bundle();
            ArrayList tmp = new ArrayList<>(tmpChosenLabel);
            bundle.putParcelableArrayList("chosen_labels", tmp);
            noteLabelDialogFragment.setArguments(bundle);
            noteLabelDialogFragment.show(getParentFragmentManager(), null);
        } else if (id == insertImgBtn.getId()) {
            launchGallery();
        }
    }

    public void launchGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            //chose multiple images
            List<Uri> uriList = new ArrayList<>();
            if (data.getClipData()!= null) {
                ClipData clipData = data.getClipData();

                for (int i = 0; i < clipData.getItemCount(); ++i) {
                    if (clipData.getItemAt(i) != null)
                        uriList.add(clipData.getItemAt(i).getUri());
                }
            } else {
                uriList.add(data.getData());
            }
            insertImgToEditTextView(requireActivity(), uriList, noteDescription);
        }
    }

    public List<String> copyImg(List<String> idImgeList) {
        File destDir = new File(getImgPath(requireActivity()));
        List<String> imgPathList = new ArrayList<>();
        for (String idImg: idImgeList) {
            File SrcImg = new File(Helper.getTmpImgPath(requireActivity()), idImg +".jpeg");
            String absolutePath = Helper.copyFileToInternalStorage(SrcImg, destDir, idImg +".jpeg");
            imgPathList.add(absolutePath);
        }

        return imgPathList;
    }
}
