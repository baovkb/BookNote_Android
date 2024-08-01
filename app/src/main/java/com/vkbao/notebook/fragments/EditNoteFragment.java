package com.vkbao.notebook.fragments;

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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.R;
import com.vkbao.notebook.adapters.AddNoteLabelAdapter;
import com.vkbao.notebook.dialogs.NoteLabelDialogFragment;
import com.vkbao.notebook.helper.TimeConvertor;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditNoteFragment extends Fragment implements View.OnClickListener {
    private NoteViewModel noteViewModel;
    private NoteLabelViewModel noteLabelViewModel;
    private TextInputEditText noteTitle;
    private TextInputEditText noteDescription;
    private Note note;
    private List<Label> chosenLabels;
    private AddNoteLabelAdapter editNoteLabelAdapter;
    private RecyclerView editNoteLabelRecyclerView;
    private Button editNoteLabelBtn;

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

        if (getArguments() != null) {
            note = getArguments().getParcelable("note");
            chosenLabels = getArguments().getParcelableArrayList("labels") != null ? getArguments().getParcelableArrayList("labels") : new ArrayList<>();
        }

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.edit_note_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteLabelViewModel = new ViewModelProvider(requireActivity()).get(NoteLabelViewModel.class);

        noteTitle = view.findViewById(R.id.edit_note_title_text_input_field);
        noteDescription = view.findViewById(R.id.edit_note_description_text_input_field);
        if (note != null) {
            noteTitle.setText(note.getTitle());
            noteDescription.setText(note.getDescription());
        }

        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);

//        Label feature
        editNoteLabelRecyclerView = view.findViewById(R.id.edit_note_label_list);
        editNoteLabelAdapter = new AddNoteLabelAdapter();
        editNoteLabelRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        editNoteLabelRecyclerView.setAdapter(editNoteLabelAdapter);

        editNoteLabelBtn = view.findViewById(R.id.edit_note_label_add_btn);
        editNoteLabelBtn.setOnClickListener(this);
        editNoteLabelAdapter.setLabel(chosenLabels);
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

                            //quite edit note fragment
                            fragmentManager.popBackStackImmediate();

                            //request update data and layout to view note fragment
                            List<Fragment> fragmentList = fragmentManager.getFragments();
                            for (Fragment fragment : fragmentList) {
                                if (fragment instanceof ViewNoteFragment) {
                                    ((ViewNoteFragment)fragment).updateNoteData(newNote, chosenLabels);
                                    ((ViewNoteFragment)fragment).updateNoteLayout();
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
        }
    }
}

