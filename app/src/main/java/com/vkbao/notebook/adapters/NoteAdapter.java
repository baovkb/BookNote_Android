package com.vkbao.notebook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.notebook.R;
import com.vkbao.notebook.helper.GetSetAttributeItemAdapter;
import com.vkbao.notebook.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesViewHolder> {
    private List<Note> notes = new ArrayList<>();

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_row, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.getNoteTitleTextView().setText(notes.get(position).getTitle());
        holder.getNoteContentTextView().setText(notes.get(position).getDescription());
        holder.set(notes.get(position).getNote_id());
//        urlImage
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder implements GetSetAttributeItemAdapter {
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
    }
}
