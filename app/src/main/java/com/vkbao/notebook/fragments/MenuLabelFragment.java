package com.vkbao.notebook.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.notebook.R;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.adapters.MenuLabelAdapter;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.viewmodels.LabelViewModel;

import java.util.List;

public class MenuLabelFragment extends DialogFragment {
    private RecyclerView recyclerView;
    private MenuLabelAdapter labelListAdapter;
    private LabelViewModel labelViewModel;
    private static final String TAG = "MenuLabelFragment";

    public MenuLabelFragment() {
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//
//        return dialog;
//    }

    // The system calls this to get the DialogFragment's layout, regardless of
    // whether it's being displayed as a dialog or an embedded fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_label, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.menu_label_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        labelViewModel = new ViewModelProvider(requireActivity()).get(LabelViewModel.class);
        labelViewModel.getAllLabel().observe(getViewLifecycleOwner(), new Observer<List<Label>>() {
            @Override
            public void onChanged(List<Label> labels) {
                labelListAdapter.setLabelList(labels);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView_menu_label);
        labelListAdapter = new MenuLabelAdapter(new MenuLabelAdapter.OnItemClickListener<Label>() {
            @Override
            public void onDeleteClick(Label label) {
                labelViewModel.delete(label);
            }
            public void onItemClick(Label label) {
                Dialog editLabelDialog = getInputLabelDialog();
                editLabelDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positiveBtn = ((AlertDialog)editLabelDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        TextInputEditText labelInput = editLabelDialog.findViewById(R.id.dialog_label_input);
                        TextView labelInputError = editLabelDialog.findViewById(R.id.dialog_label_input_error);
                        TextView headerMenuLabel = editLabelDialog.findViewById(R.id.dialog_label_header);

                        labelInputError.setVisibility(TextView.GONE);
                        headerMenuLabel.setText(R.string.menu_label_header_edit);

                        labelInput.setText(label.getName());

                        positiveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String labelInputValue = labelInput.getText().toString();
                                labelViewModel.getLabelByName(labelInputValue, new CallBack<Label>() {
                                    @Override
                                    public void onResult(Label result) {
                                        if (result != null) {
                                            Handler mainHandler = new Handler(Looper.getMainLooper());
                                            mainHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    labelInput.setBackgroundResource(R.drawable.menu_label_input_bg_error);
                                                    labelInputError.setVisibility(TextView.VISIBLE);
                                                }
                                            });
                                        } else {
                                            labelViewModel.update(new Label(label.getLabel_id(), labelInputValue));
                                            editLabelDialog.dismiss();
                                        }
                                    }
                                });

                            }
                        });
                    }
                });
                editLabelDialog.show();
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(labelListAdapter);
    }

    public MenuProvider getMenuProvider() {
        return new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_label_actions, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                FragmentManager fragmentManager = getParentFragmentManager();

                if (id == android.R.id.home) {
                    fragmentManager.popBackStackImmediate();
                } else if (id == R.id.add_label) {
                    Dialog labelDialog = getInputLabelDialog();
                    labelDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Button positiveBtn = ((AlertDialog)labelDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                            TextView headerMenuLabel = labelDialog.findViewById(R.id.dialog_label_header);
                            TextInputEditText labelInput = labelDialog.findViewById(R.id.dialog_label_input);
                            TextView labelInputError = labelDialog.findViewById(R.id.dialog_label_input_error);

                            labelInputError.setVisibility(TextView.GONE);
                            headerMenuLabel.setText(R.string.menu_label_header_add);

                            positiveBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String labelInputValue = labelInput.getText().toString();
                                    labelViewModel.getLabelByName(labelInputValue, new CallBack<Label>() {
                                        @Override
                                        public void onResult(Label result) {
                                            if (result != null) {
                                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                                mainHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        labelInput.setBackgroundResource(R.drawable.menu_label_input_bg_error);
                                                        labelInputError.setVisibility(TextView.VISIBLE);
                                                    }
                                                });
                                            } else {
                                                labelViewModel.insert(new Label(labelInputValue));
                                                labelDialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                    labelDialog.show();
                }

                return true;
            }
        };
    }

    public Dialog getInputLabelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        Dialog labelDialog = builder.setView(inflater.inflate(R.layout.dialog_input_label, null))
                // Add action buttons
                .setPositiveButton(R.string.common_action_ok, null)
                .setNegativeButton(R.string.common_action_close, null)
                .create();

        return labelDialog;
    }

}