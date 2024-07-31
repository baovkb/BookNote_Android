package com.vkbao.notebook.adapters;

import android.location.GnssAntennaInfo;
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

public class MenuLabelAdapter extends RecyclerView.Adapter<MenuLabelAdapter.LabelViewHolder> {
    public interface OnItemClickListener<T> {
        public void onDeleteClick(T label);
        public void onItemClick(T label);
    }

    private List<Label> labelList;
    OnItemClickListener listener;

    public MenuLabelAdapter(OnItemClickListener<Label> listener) {
        this.labelList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_item, parent, false);
        return new LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, int position) {
        holder.getNameLabelTV().setText(labelList.get(position).getName());
        holder.getDeleteBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteClick(labelList.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(labelList.get(holder.getAdapterPosition()));
            }
        });
    }

    public void setLabelList(List<Label> labelList) {
        this.labelList = labelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

    class LabelViewHolder extends RecyclerView.ViewHolder {
        private Button deleteBtn;
        private TextView nameLabelTV;

        public Button getDeleteBtn() {
            return deleteBtn;
        }

        public TextView getNameLabelTV() {
            return nameLabelTV;
        }

        public LabelViewHolder(@NonNull View itemView) {
            super(itemView);
            nameLabelTV = itemView.findViewById(R.id.label_item_tv);
            deleteBtn = itemView.findViewById(R.id.label_item_delete);
        }
    }
}
