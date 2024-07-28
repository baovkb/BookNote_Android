package com.vkbao.notebook.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vkbao.notebook.R;
import com.vkbao.notebook.adapters.NoteAdapter;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.itemtouch.NoteItemTouch;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.List;

public class ListNoteFragment extends Fragment {
    private RecyclerView recyclerView;
    private ViewGroup emptyNoteViewGroup;
    private NoteAdapter noteAdapter;
    private NoteViewModel noteViewModel;
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

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        noteAdapter = new NoteAdapter(new NoteAdapter.OnItemClickListener<Note>() {
            @Override
            public void onClick(Note note) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                ViewNoteFragment viewNoteFragment = new ViewNoteFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("note", note);
                viewNoteFragment.setArguments(bundle);

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_screen_fragment, viewNoteFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(noteAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NoteItemTouch(
                noteViewModel,
                noteAdapter,
                new NoteItemTouch.OnItemSwipe<Note>() {
                    @Override
                    public void onSwipe(Note note) {
                        noteViewModel.delete(note);
                    }
                }));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (notes.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyNoteViewGroup.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyNoteViewGroup.setVisibility(View.GONE);
                    noteAdapter.setNotes(notes);
                }
            }
        });

        return view;
    }


}