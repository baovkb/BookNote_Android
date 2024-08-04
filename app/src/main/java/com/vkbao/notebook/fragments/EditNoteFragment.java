package com.vkbao.notebook.fragments;

import static android.app.Activity.RESULT_OK;
import static com.vkbao.notebook.helper.Helper.deleteFile;
import static com.vkbao.notebook.helper.Helper.getImgPath;
import static com.vkbao.notebook.helper.Helper.insertImgToEditTextView;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.internal.ParcelableSparseArray;
import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.R;
import com.vkbao.notebook.adapters.AddNoteLabelAdapter;
import com.vkbao.notebook.dialogs.NoteLabelDialogFragment;
import com.vkbao.notebook.helper.CustomClickableSpan;
import com.vkbao.notebook.helper.Helper;
import com.vkbao.notebook.helper.TimeConvertor;
import com.vkbao.notebook.models.Image;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;
import com.vkbao.notebook.viewmodels.ImageViewModel;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditNoteFragment extends Fragment implements View.OnClickListener {
    private NoteViewModel noteViewModel;
    private NoteLabelViewModel noteLabelViewModel;
    private ImageViewModel imageViewModel;
    private TextInputEditText noteTitle;
    private TextInputEditText noteDescription;
    private Note note;
    private List<Label> chosenLabels;
    private List<Image> imageList;
    private AddNoteLabelAdapter editNoteLabelAdapter;
    private RecyclerView editNoteLabelRecyclerView;
    private Button editNoteLabelBtn;
    private ImageButton addImgBtn;

    private final int REQUEST_GALLERY_CODE = 1;

    public EditNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setUpEvent();
    }

    public void init(View view) {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            note = bundle.getParcelable("note");
            chosenLabels = bundle.getParcelableArrayList("labels") != null ? bundle.getParcelableArrayList("labels") : new ArrayList<>();
            imageList = bundle.getParcelableArrayList("images") != null ? bundle.getParcelableArrayList("images") : new ArrayList<>();
        }

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.edit_note_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteLabelViewModel = new ViewModelProvider(requireActivity()).get(NoteLabelViewModel.class);
        imageViewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);

        noteTitle = view.findViewById(R.id.edit_note_title_text_input_field);
        noteDescription = view.findViewById(R.id.edit_note_description_text_input_field);
        addImgBtn = view.findViewById(R.id.edit_note_insert_img_btn);

        if (note != null) {
            noteTitle.setText(note.getTitle());
            Helper.parseTextModeEdit(requireActivity(), imageList, noteDescription, note.getDescription());
        }

//        Label feature
        editNoteLabelRecyclerView = view.findViewById(R.id.edit_note_label_list);
        editNoteLabelAdapter = new AddNoteLabelAdapter();
        editNoteLabelRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        editNoteLabelRecyclerView.setAdapter(editNoteLabelAdapter);

        editNoteLabelBtn = view.findViewById(R.id.edit_note_label_add_btn);
        editNoteLabelBtn.setOnClickListener(this);
        editNoteLabelAdapter.setLabel(chosenLabels);
        addImgBtn.setOnClickListener(this);
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

    //Handle Action Menu
    private MenuProvider getMenuProvider() {
        return new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.edit_note_actions, menu);
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
                        noteLabelViewModel.getNoteLabelByNoteID(note.getNote_id(), (noteLabelList) -> {
                            //delete old note label
                            noteLabelViewModel.delete(noteLabelList.toArray(new NoteLabel[0]));

                            //update new note
                            Note newNote = new Note(
                                    note.getNote_id(),
                                    title, description,
                                    note.getCreate_at(),
                                    TimeConvertor.getCurrentUnixSecond());
                            noteViewModel.update(newNote);

                            //insert new note label
                            NoteLabel[] newNoteLabelArray = new NoteLabel[chosenLabels.size()];
                            for (int i = 0; i < chosenLabels.size(); ++i) {
                                newNoteLabelArray[i] = new NoteLabel(newNote.getNote_id(),
                                                                    chosenLabels.get(i).getLabel_id());
                            }
                            noteLabelViewModel.insert(newNoteLabelArray);

                            //save img in description
                            saveImg(noteDescription.getText().toString(), note.getNote_id());

                            //quite edit note fragment
                            fragmentManager.popBackStackImmediate();

                            //request update data and layout to view note fragment
                            List<Fragment> fragmentList = fragmentManager.getFragments();
                            for (Fragment fragment : fragmentList) {
                                if (fragment instanceof ViewNoteFragment) {
                                    try {
                                        imageList = imageViewModel.getImagesByNoteID(note.getNote_id()).get();
                                        ((ViewNoteFragment)fragment).updateNoteData(newNote, chosenLabels, imageList);
                                        ((ViewNoteFragment)fragment).updateNoteLayout();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        noteLabelViewModel.getNoteLabelByNoteID(note.getNote_id(), (noteLabelList) -> {
                            noteLabelViewModel.delete(noteLabelList.toArray(new NoteLabel[0]));
                        });
                        noteViewModel.delete(note);

                        //return to list note fragment
                        fragmentManager.popBackStackImmediate();
                        fragmentManager.popBackStackImmediate();
                    }

                }
                return true;
            }
        };
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == editNoteLabelBtn.getId()) {
            NoteLabelDialogFragment noteLabelDialogFragment = new NoteLabelDialogFragment();
            noteLabelDialogFragment.OnListenDialogBtnClick(new NoteLabelDialogFragment.NoticeDialogListener() {
                @Override
                public void onDialogPositiveClick(DialogFragment dialog, List<Label> newChosenLabels) {
                    chosenLabels = newChosenLabels;
                    editNoteLabelAdapter.setLabel(chosenLabels);
                }

                @Override
                public void onDialogNegativeClick(DialogFragment dialog) {

                }
            });
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("chosen_labels", new ArrayList(chosenLabels));
            noteLabelDialogFragment.setArguments(bundle);
            noteLabelDialogFragment.show(getParentFragmentManager(), null);
        } else if (id == addImgBtn.getId()) {
            launchGallery();
        }
    }

    public void launchGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, REQUEST_GALLERY_CODE);
    }

    public void saveImg(String description, long note_id) {
        List<String> newNameImgList = Helper.getImgIDFromText(description);

        try {
            List<Image> oldImageList = imageViewModel.getImagesByNoteID(note_id).get();

            //remove deleted img
            for (Image image: oldImageList) {
                if (!newNameImgList.contains(image.getName())) {
                    //delete in internal storage
                    deleteFile(image.getUrl());

                    //delete in db
                    imageViewModel.deleteByID(image.getImage_id());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //copy new img from tmp dir
        List<String> newPathList = copyImg(newNameImgList);
        //insert to db
        List<Image> newImageList = new ArrayList<>();
        for (int i = 0; i < newPathList.size(); ++i) {
            if (newPathList.get(i) != null) {
                newImageList.add(new Image(note_id, newPathList.get(i), newNameImgList.get(i)));
            }
        }

        imageViewModel.insert(newImageList.toArray(new Image[0]));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            //chose multiple images
            List<Uri> uriList = new ArrayList<>();
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();

                for (int i = 0; i < clipData.getItemCount(); ++i) {
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

