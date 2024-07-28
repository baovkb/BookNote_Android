package com.vkbao.notebook.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.notebook.R;
import com.vkbao.notebook.helper.GetSetAttributeItemAdapter;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesViewHolder> {
    public interface OnItemClickListener<V> {
        public void onClick(V note);
    }

    private List<Note> notes;
    OnItemClickListener listener;

    public NoteAdapter(OnItemClickListener<Note> listener) {
        this.notes = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_row, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note noteItem = notes.get(position);
        holder.getNoteTitleTextView().setText(noteItem.getTitle());
        holder.getNoteContentTextView().setText(noteItem.getDescription());
        holder.set(noteItem.getNote_id());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(notes.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder implements GetSetAttributeItemAdapter {
        private TextView noteTitleTextView;
        private TextView noteContentTextView;
        private long note_id;

        NotesViewHolder(View itemView) {
            super(itemView);
            noteTitleTextView = itemView.findViewById(R.id.item_note_title_row);
            noteContentTextView = itemView.findViewById(R.id.item_note_content_row);
        }

        public TextView getNoteTitleTextView() {
            return noteTitleTextView;
        }
        public TextView getNoteContentTextView() {
            return noteContentTextView;
        }

        @Override
        public Object get() {
            return this.note_id;
        }

        @Override
        public void set(Object value) {
            this.note_id = (long)value;
        }

        public long getNote_id() {
            return note_id;
        }

        public void setNote_id(long note_id) {
            this.note_id = note_id;
        }
    }
}
