package com.vkbao.notebook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.notebook.R;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.adapters.AddNoteLabelAdapter;
import com.vkbao.notebook.dialogs.NoteLabelDialogFragment;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.viewmodels.ImageViewModel;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewNoteFragment extends Fragment {
    private NoteViewModel noteViewModel;
    private TextInputEditText noteTitle;
    private TextInputEditText noteDescription;
    private NoteLabelViewModel noteLabelViewModel;
    private ImageViewModel imageViewModel;
    private Note note;
    public List<Label> chosenLabel;
    private AddNoteLabelAdapter viewNoteLabelAdapter;
    private RecyclerView viewNoteLabelRecyclerView;

    public ViewNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        note = null;
        chosenLabel = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_note, container, false);

        if (getArguments() != null) {
            note = getArguments().getParcelable("note");
            chosenLabel = getArguments().getParcelableArrayList("labels");
        }

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.view_note_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        noteTitle = view.findViewById(R.id.view_note_title_text_input_field);
        noteDescription = view.findViewById(R.id.view_note_description_text_input_field);

        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        updateNoteLayout();

        //set label feature
        noteLabelViewModel = new ViewModelProvider(requireActivity()).get(NoteLabelViewModel.class);
        viewNoteLabelAdapter = new AddNoteLabelAdapter();
        viewNoteLabelRecyclerView = view.findViewById(R.id.view_note_label_list);
        viewNoteLabelRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        viewNoteLabelRecyclerView.setAdapter(viewNoteLabelAdapter);
        viewNoteLabelAdapter.setLabel(chosenLabel);

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