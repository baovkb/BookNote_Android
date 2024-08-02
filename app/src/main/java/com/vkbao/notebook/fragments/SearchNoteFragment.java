package com.vkbao.notebook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.notebook.R;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.adapters.NoteAdapter;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.ArrayList;

public class SearchNoteFragment extends Fragment {
    private TextInputEditText searchTextSearchBar;
    private RecyclerView searchNoteRCV;
    private NoteViewModel noteViewModel;
    private NoteAdapter noteAdapter;
    private NoteLabelViewModel noteLabelViewModel;

    public SearchNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setUpEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_note, container, false);
    }

    public void init(View view) {
        Toolbar searchNoteToolbar = (Toolbar) view.findViewById(R.id.search_note_toolbar);
        ((MainActivity)requireActivity()).setSupportActionBar(searchNoteToolbar);
        ((MainActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)requireActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        searchTextSearchBar = view.findViewById(R.id.search_note_search_bar);
        searchNoteRCV = view.findViewById(R.id.search_note_result_recycler_view);
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteLabelViewModel = new ViewModelProvider(requireActivity()).get(NoteLabelViewModel.class);
        noteAdapter = new NoteAdapter();
        searchNoteRCV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        searchNoteRCV.setAdapter(noteAdapter);
    }

    public void setUpEvent() {
        searchTextSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String keyword = searchTextSearchBar.getText().toString().trim();
                if (keyword.isEmpty()) {
                    noteAdapter.setNotes(new ArrayList<>());
                } else {
                    noteViewModel.searchNote(keyword, (noteList) -> {
                        noteAdapter.setNotes(noteList);
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        noteAdapter.setOnItemClickListener((note) -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            ViewNoteFragment viewNoteFragment = new ViewNoteFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("note", note);
//          get all label of note
            noteLabelViewModel.getLabelsByNoteID(note.getNote_id(), (labelList) -> {
                ArrayList tmp = new ArrayList<>(labelList);
                bundle.putParcelableArrayList("labels", tmp);
                viewNoteFragment.setArguments(bundle);

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_screen_fragment, viewNoteFragment)
                        .addToBackStack(null)
                        .commit();
            });
        });
    }

    public MenuProvider getMenuProvider() {
        return new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == android.R.id.home) {
                    getParentFragmentManager().popBackStackImmediate();
                }

                return true;
            }
        };
    }
}