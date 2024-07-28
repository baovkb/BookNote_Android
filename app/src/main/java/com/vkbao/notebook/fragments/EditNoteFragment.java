package com.vkbao.notebook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.R;
import com.vkbao.notebook.helper.TimeConvertor;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.List;

public class EditNoteFragment extends Fragment {
    private NoteViewModel noteViewModel;
    private TextInputEditText noteTitle;
    private TextInputEditText noteDescription;
    private Note note;

    public EditNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable("note");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.edit_note_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        noteTitle = view.findViewById(R.id.edit_note_title_text_input_field);
        noteDescription = view.findViewById(R.id.edit_note_description_text_input_field);
        if (note != null) {
            noteTitle.setText(note.getTitle());
            noteDescription.setText(note.getDescription());
        }

        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return view;
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
                        Note newNote = new Note(
                                note.getNote_id(),
                                title, description,
                                note.getCreate_at(),
                                TimeConvertor.getCurrentUnixSecond());
                        noteViewModel.update(newNote);

                        fragmentManager.popBackStackImmediate();

                        List<Fragment> fragmentList = fragmentManager.getFragments();
                        for (Fragment fragment : fragmentList) {
                            if (fragment instanceof ViewNoteFragment) {
                                ((ViewNoteFragment)fragment).updateNoteData(newNote);
                                ((ViewNoteFragment)fragment).updateNoteLayout();
                            }
                        }

                    } else {
                        noteViewModel.delete(note);
                        fragmentManager.popBackStack();
                    }

                }
                return true;
            }
        };
    }
}

