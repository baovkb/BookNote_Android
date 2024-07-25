package com.vkbao.notebook.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vkbao.notebook.R;
import com.vkbao.notebook.adapters.NotesAdapter;
import com.vkbao.notebook.viewmodels.NotesViewModel;

public class ListNoteFragment extends Fragment {
    private RecyclerView recyclerView;
    private ViewGroup emptyNoteViewGroup;
    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;
    private static final String TAG = "ListNoteFragment";

    public ListNoteFragment() {
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
        View view = inflater.inflate(R.layout.fragment_list_note, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_note_list);
        emptyNoteViewGroup = view.findViewById(R.id.empty_note);

        notesAdapter = new NotesAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notesAdapter);

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        notesViewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            Log.d(TAG, "notes:" + notes);
            if (notes.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyNoteViewGroup.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyNoteViewGroup.setVisibility(View.GONE);
                notesAdapter.setNotes(notes);
            }
        });

        return view;
    }
}