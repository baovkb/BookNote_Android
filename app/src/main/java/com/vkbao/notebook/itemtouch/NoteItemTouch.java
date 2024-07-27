package com.vkbao.notebook.itemtouch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.notebook.adapters.NoteAdapter;
import com.vkbao.notebook.helper.GetSetAttributeItemAdapter;
import com.vkbao.notebook.viewmodels.NoteViewModel;

public class NoteItemTouch extends ItemTouchHelper.SimpleCallback {
    private final String TAG = "NoteItemTouch";
    private NoteViewModel noteViewModel;
    private NoteAdapter noteAdapter;

    public NoteItemTouch(NoteViewModel noteViewModel, NoteAdapter noteAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.noteViewModel = noteViewModel;
        this.noteAdapter = noteAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position_swpipe = viewHolder.getAdapterPosition();
        noteAdapter.notifyItemChanged(position_swpipe);
        long note_id = (long)((GetSetAttributeItemAdapter)viewHolder).get();
        noteViewModel.deleteByID(note_id);
    }
}
