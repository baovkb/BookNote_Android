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
import com.vkbao.notebook.R;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.viewmodels.ImageViewModel;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

public class ViewNoteFragment extends Fragment {
    private NoteViewModel noteViewModel;
    private TextInputEditText noteTitle;
    private TextInputEditText noteDescription;
    private NoteLabelViewModel noteLabelViewModel;
    private ImageViewModel imageViewModel;
    private Note note;

    public ViewNoteFragment() {
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
        View view = inflater.inflate(R.layout.fragment_view_note, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.view_note_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        noteTitle = view.findViewById(R.id.view_note_title_text_input_field);
        noteDescription = view.findViewById(R.id.view_note_description_text_input_field);

        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        updateNoteLayout();

        return view;
    }

    private MenuProvider getMenuProvider() {
        return new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.view_note_actions, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                FragmentManager fragmentManager = getParentFragmentManager();

                if (id == android.R.id.home) {
                    fragmentManager.popBackStackImmediate();
                } else if (id == R.id.edit_note) {
                    if (note != null) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("note", note);
                        EditNoteFragment editNoteFragment = new EditNoteFragment();
                        editNoteFragment.setArguments(bundle);
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.main_screen_fragment, editNoteFragment)
                                .addToBackStack(null)
                                .commit();
                    }

                } else if (id == R.id.delete_note) {
                    noteViewModel.delete(note);
                    fragmentManager.popBackStack();
                }
                return true;
            }
        };
    }

    public void updateNoteLayout() {
        if (note != null) {
            noteTitle.setText(note.getTitle());
            noteDescription.setText(note.getDescription());
        }
    }

    public void updateNoteData(Note newNote) {
        this.note = newNote;
    }

}