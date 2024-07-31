package com.vkbao.notebook.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.vkbao.notebook.R;
import com.vkbao.notebook.models.Label;

import java.util.ArrayList;
import java.util.List;

public class LabelArrayAdapter extends ArrayAdapter<Label> {
    private List<Label> checkedLabelList;

    public LabelArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
        checkedLabelList = new ArrayList<>();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater viewInflator = LayoutInflater.from(getContext());
            view = viewInflator.inflate(R.layout.note_label_label_dialog_item, parent,false);
        }
        Label labelItem = (Label) getItem(position);
        CheckBox checkBox = view.findViewById(R.id.label_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("test", "check is change: " + checkBox.isChecked());
                if (checkBox.isChecked() && !checkedLabelList.contains(labelItem)) {
                    checkedLabelList.add(labelItem);
                } else if (!checkBox.isChecked() && checkedLabelList.contains(labelItem)) {
                    checkedLabelList.remove(labelItem);
                }
            }
        });
        checkBox.setText(labelItem.getName());

        if (checkedLabelList.contains(labelItem)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        return view;
    }

    public void updateCheckedLabelList(List<Label> newCheckedLabelList) {
        checkedLabelList.clear();
        checkedLabelList.addAll(newCheckedLabelList);

        notifyDataSetChanged();
    }

    public List<Label> getCheckedLabelList() {
        return checkedLabelList;
    }
}

