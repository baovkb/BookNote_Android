package com.vkbao.notebook.itemtouch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.notebook.adapters.NoteAdapter;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.helper.GetSetAttributeItemAdapter;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.viewmodels.NoteViewModel;

public class NoteItemTouch extends ItemTouchHelper.SimpleCallback {
    public interface OnItemSwipe<T> {
        public void onSwipe(T note);
    }

    private final String TAG = "NoteItemTouch";
    private NoteViewModel noteViewModel;
    private NoteAdapter noteAdapter;
    private OnItemSwipe listener;

    public NoteItemTouch(NoteViewModel noteViewModel, NoteAdapter noteAdapter, OnItemSwipe<Note> listener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.noteViewModel = noteViewModel;
        this.noteAdapter = noteAdapter;
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position_swpipe = viewHolder.getAdapterPosition();
        noteAdapter.notifyItemChanged(position_swpipe);
        long note_id = ((GetSetAttributeItemAdapter<Long>)viewHolder).get();
        noteViewModel.getNoteByID(note_id, new CallBack<Note>() {
            @Override
            public void onResult(Note result) {
                listener.onSwipe(result);
            }
        });

    }
}
