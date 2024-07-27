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
import com.vkbao.notebook.helper.TimeConvertor;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.viewmodels.ImageViewModel;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

public class AddNoteFragment extends Fragment {
    private NoteViewModel noteViewModel;
    private TextInputEditText noteTitle;
    private TextInputEditText noteDescription;
    private NoteLabelViewModel noteLabelViewModel;
    private ImageViewModel imageViewModel;

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

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.add_note_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        noteTitle = view.findViewById(R.id.add_note_title_text_input_field);
        noteDescription = view.findViewById(R.id.add_note_description_text_input_field);

        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return view;
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
                        long currentUnix = TimeConvertor.getCurrentUnixSecond();
                        Note[] notes = {new Note(title, description, currentUnix, currentUnix)};
                        noteViewModel.insert(notes);
                    }
                    fragmentManager.popBackStack();
                }
                return true;
            }
        };
    }
}