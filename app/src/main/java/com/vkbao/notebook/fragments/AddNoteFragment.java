package com.vkbao.notebook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.notebook.R;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.adapters.AddNoteLabelAdapter;
import com.vkbao.notebook.dialogs.NoteLabelDialogFragment;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.helper.TimeConvertor;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;
import com.vkbao.notebook.viewmodels.AddNoteLabelViewModel;
import com.vkbao.notebook.viewmodels.ImageViewModel;
import com.vkbao.notebook.viewmodels.LabelViewModel;
import com.vkbao.notebook.viewmodels.NoteLabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddNoteFragment extends Fragment
        implements View.OnClickListener{
    private NoteViewModel noteViewModel;
    private TextInputEditText noteTitle;
    private TextInputEditText noteDescription;
    private Button addNoteLabelAddBtn;
    private NoteLabelViewModel noteLabelViewModel;
    private LabelViewModel labelViewModel;
    private ImageViewModel imageViewModel;
    private AddNoteLabelAdapter addNoteLabelAdapter;
    private RecyclerView addNoteLabelRecyclerView;
    public List<Label> tmpChosenLabel;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.add_note_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteLabelViewModel = new ViewModelProvider(requireActivity()).get(NoteLabelViewModel.class);
        tmpChosenLabel = new ArrayList<>();

        noteTitle = view.findViewById(R.id.add_note_title_text_input_field);
        noteDescription = view.findViewById(R.id.add_note_description_text_input_field);
        addNoteLabelAddBtn = view.findViewById(R.id.add_note_label_add_btn);
        addNoteLabelRecyclerView = view.findViewById(R.id.add_note_label_list);
        addNoteLabelAdapter = new AddNoteLabelAdapter();

        addNoteLabelRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        addNoteLabelRecyclerView.setAdapter(addNoteLabelAdapter);

        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        addNoteLabelAddBtn.setOnClickListener(this);
    }

    private MenuProvider getMenuProvider() {
        return new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.add_note_actions, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                FragmentManager fragmentManager = getParentFragmentManager();

                if (id == android.R.id.home) {
                    fragmentManager.popBackStack();
                } else if (id == R.id.save_note) {
                    String title = noteTitle.getText().toString();
                    String description = noteDescription.getText().toString();
                    if (!title.trim().isEmpty() || !description.trim().isEmpty()) {
                        long currentUnix = TimeConvertor.getCurrentUnixSecond();
                        Note note = new Note(title, description, currentUnix, currentUnix);
                        noteViewModel.insert(result -> {
                            if (result != null && result.length == 1) {
                                NoteLabel[] noteLabelArray = new NoteLabel[tmpChosenLabel.size()];
                                for (int i = 0; i < tmpChosenLabel.size(); ++i) {
                                    noteLabelArray[i] = new NoteLabel(result[0], tmpChosenLabel.get(i).getLabel_id());
                                }
                                noteLabelViewModel.insert(noteLabelArray);
                            }
                        }, note);
                    }
                    fragmentManager.popBackStack();
                }
                return true;
            }
        };
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == addNoteLabelAddBtn.getId()) {
            NoteLabelDialogFragment noteLabelDialogFragment = new NoteLabelDialogFragment();
            noteLabelDialogFragment.OnListenDialogBtnClick(new NoteLabelDialogFragment.NoticeDialogListener() {
                @Override
                public void onDialogPositiveClick(DialogFragment dialog, List<Label> chosenLabels) {
                    tmpChosenLabel = chosenLabels;
                    addNoteLabelAdapter.setLabel(tmpChosenLabel);
                }

                @Override
                public void onDialogNegativeClick(DialogFragment dialog) {

                }
            });
            Bundle bundle = new Bundle();
            ArrayList tmp = new ArrayList<>(tmpChosenLabel);
            bundle.putParcelableArrayList("chosen_labels", tmp);
            noteLabelDialogFragment.setArguments(bundle);
            noteLabelDialogFragment.show(getParentFragmentManager(), null);
        }
    }
}