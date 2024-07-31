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
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;
import com.vkbao.notebook.viewmodels.LabelViewModel;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListNoteFragment extends Fragment {
    private RecyclerView recyclerViewListNote;
    private ViewGroup emptyNoteViewGroup;
    private NoteAdapter noteAdapter;
    private NoteViewModel noteViewModel;
    private LabelViewModel labelViewModel;
    private NoteLabelViewModel noteLabelViewModel;
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
        recyclerViewListNote = view.findViewById(R.id.recyclerView_note_list);
        emptyNoteViewGroup = view.findViewById(R.id.empty_note);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        labelViewModel = new ViewModelProvider(requireActivity()).get(LabelViewModel.class);
        noteLabelViewModel = new ViewModelProvider(requireActivity()).get(NoteLabelViewModel.class);

        noteAdapter = new NoteAdapter(new NoteAdapter.OnItemClickListener<Note>() {
            @Override
            public void onClick(Note note) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                ViewNoteFragment viewNoteFragment = new ViewNoteFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("note", note);
//                get all label of note
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
            }
        });
        recyclerViewListNote.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewListNote.setAdapter(noteAdapter);

        NoteItemTouch noteItemTouch = new NoteItemTouch(noteViewModel, noteLabelViewModel, noteAdapter);
        noteItemTouch.setOnSwipeListener(new NoteItemTouch.OnItemSwipe<Note, NoteLabel>() {
            @Override
            public void onSwipe(Note note, List<NoteLabel> noteLabels) {
                NoteLabel[] noteLabelArray = noteLabels.toArray(new NoteLabel[0]);
                noteLabelViewModel.delete(noteLabelArray);
                noteViewModel.delete(note);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(noteItemTouch);
        itemTouchHelper.attachToRecyclerView(recyclerViewListNote);

        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (notes.isEmpty()) {
                    recyclerViewListNote.setVisibility(View.GONE);
                    emptyNoteViewGroup.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewListNote.setVisibility(View.VISIBLE);
                    emptyNoteViewGroup.setVisibility(View.GONE);
                    noteAdapter.setNotes(notes);
                }
            }
        });

        return view;
    }


}