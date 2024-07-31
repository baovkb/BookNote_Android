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
    public interface OnItemSwipe<Note, NoteLabel> {
        public void onSwipe(Note note, List<NoteLabel> noteLabelList);
    }

    private final String TAG = "NoteItemTouch";
    private NoteViewModel noteViewModel;
    private NoteLabelViewModel noteLabelViewModel;
    private NoteAdapter noteAdapter;
    private OnItemSwipe listener;

    public NoteItemTouch(NoteViewModel noteViewModel, NoteLabelViewModel noteLabelViewModel, NoteAdapter noteAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.noteViewModel = noteViewModel;
        this.noteLabelViewModel = noteLabelViewModel;
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
            long note_id = ((GetSetAttributeItemAdapter<Long>)viewHolder).get();
            noteViewModel.getNoteByID(note_id, (noteResult) -> {
                noteLabelViewModel.getNoteLabelByNoteID(note_id, (noteLabelList) -> {
                    listener.onSwipe(noteResult, noteLabelList);
                });
            });
        }
    }

    public void setOnSwipeListener(OnItemSwipe<Note, NoteLabel> listener) {
        this.listener = listener;
    }
}
