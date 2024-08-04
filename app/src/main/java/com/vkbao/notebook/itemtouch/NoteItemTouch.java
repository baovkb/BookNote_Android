package com.vkbao.notebook.itemtouch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.notebook.adapters.NoteAdapter;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.helper.GetSetAttributeItemAdapter;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;
import com.vkbao.notebook.viewmodels.LabelViewModel;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.List;

public class NoteItemTouch extends ItemTouchHelper.SimpleCallback {
    public interface OnItemSwipe<T> {
        public void onSwipe(T id);
    }

    private final String TAG = "NoteItemTouch";
    private NoteAdapter noteAdapter;
    private OnItemSwipe listener;

    public NoteItemTouch(NoteAdapter noteAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.noteAdapter = noteAdapter;
        this.listener = null;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null) {
            int position_swpipe = viewHolder.getAdapterPosition();
            noteAdapter.notifyItemChanged(position_swpipe);
            Long note_id = Long.valueOf(((GetSetAttributeItemAdapter<Long>)viewHolder).get());
            listener.onSwipe(note_id);
        }
    }

    public void setOnSwipeListener(OnItemSwipe<Long> listener) {
        this.listener = listener;
    }
}
