package com.vkbao.notebook.adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.notebook.R;
import com.vkbao.notebook.models.Label;

import java.util.ArrayList;
import java.util.List;

public class AddNoteLabelAdapter extends RecyclerView.Adapter<AddNoteLabelAdapter.AddNoteLabelViewHolder> {
    private List<Label> labelList;

    public AddNoteLabelAdapter() {
        this.labelList = new ArrayList<>();
    }

    @NonNull
    @Override
    public AddNoteLabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_note_label_list_item, parent, false);
        return new AddNoteLabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddNoteLabelViewHolder holder, int position) {
        holder.getAddNoteLabelTV().setText(labelList.get(position).getName());
    }

    public void setLabel(List<Label> labelList) {
        this.labelList = labelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

    class AddNoteLabelViewHolder extends RecyclerView.ViewHolder {
        private TextView addNoteLabelTV;

        public TextView getAddNoteLabelTV() {
            return addNoteLabelTV;
        }

        public AddNoteLabelViewHolder(@NonNull View itemView) {
            super(itemView);
            this.addNoteLabelTV = itemView.findViewById(R.id.add_note_label_item_tv);
        }
    }
}
