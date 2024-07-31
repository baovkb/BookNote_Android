package com.vkbao.notebook.dialogs;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.vkbao.notebook.R;
import com.vkbao.notebook.adapters.LabelArrayAdapter;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.viewmodels.LabelViewModel;

import java.util.ArrayList;
import java.util.List;

public class NoteLabelDialogFragment extends DialogFragment {
    private ListView labelListView;
    private List<Label> labelList;
    private List<Label> chosenLabelList;
    private LabelArrayAdapter labelArrayAdapter;
    private LabelViewModel labelViewModel;
    private NoticeDialogListener listener;

    public NoteLabelDialogFragment() {
        this.listener = null;
        this.labelList = new ArrayList<>();
        this.chosenLabelList = new ArrayList<>();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_note_label_dialog, null);
        builder.setView(view)
                .setPositiveButton(R.string.common_action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            //get all clicked label -> update chosenLabelList
                            chosenLabelList.clear();
                            List<Label> getLabel = labelArrayAdapter.getCheckedLabelList();
                            chosenLabelList.addAll(getLabel);
                            listener.onDialogPositiveClick(NoteLabelDialogFragment.this, chosenLabelList);
                        }

                    }
                })
                .setNegativeButton(R.string.common_action_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        labelViewModel = new ViewModelProvider(requireActivity()).get(LabelViewModel.class);

        labelArrayAdapter = new LabelArrayAdapter(requireActivity(), R.layout.note_label_label_dialog_item, R.id.label_checkbox, labelList);
        labelListView = view.findViewById(R.id.note_label_dialog_list_view);

        labelListView.setAdapter(labelArrayAdapter);
        labelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = view.findViewById(R.id.label_checkbox);
                cb.setChecked(!cb.isChecked());
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList tmp = bundle.getParcelableArrayList("chosen_labels");
            chosenLabelList.clear();
            chosenLabelList.addAll(tmp);
            labelArrayAdapter.updateCheckedLabelList(chosenLabelList);
        }

        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_label_dialog, container, false);

        labelViewModel.getAllLabel().observe(getViewLifecycleOwner(), new Observer<List<Label>>() {
            @Override
            public void onChanged(List<Label> labels) {
                labelList.clear();
                if (labels != null) {
                    labelList.addAll(labels);
                }
                labelArrayAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, List<Label> chosenLabels);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    public void OnListenDialogBtnClick(NoticeDialogListener listener) {
        this.listener = listener;
    }
}