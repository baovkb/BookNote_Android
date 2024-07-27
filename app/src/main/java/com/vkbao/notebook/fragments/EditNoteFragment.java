package com.vkbao.notebook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.helper.DrawerLocker;
import com.vkbao.notebook.R;
import com.vkbao.notebook.helper.TimeConvertor;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.List;

public class EditNoteFragment extends Fragment {
    static final class SHOW_TITLE {
        static final String KEY_SHOW_WHAT = "SHOW_WHAT";
        static final String VALUE_SHOW_ADD_NOTE = "VALUE_SHOW_ADD_NOTE";
        static final String VALUE_SHOW_EDIT_NOTE = "VALUE_SHOW_EDIT_NOTE";
    }

    private NoteViewModel noteViewModel;
    private TextInputEditText noteTitle;
    private TextInputEditText noteDescription;

    public EditNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.edit_note_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        String title = getArguments().getString(SHOW_TITLE.KEY_SHOW_WHAT, "");
        if (title == SHOW_TITLE.VALUE_SHOW_ADD_NOTE) {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.main_screen_add_note);
        } else if (title == SHOW_TITLE.VALUE_SHOW_EDIT_NOTE) {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.main_screen_edit_note);
        } else {

        }

        requireActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        noteTitle = view.findViewById(R.id.edit_note_title_text_input_field);
        noteDescription = view.findViewById(R.id.edit_note_description_text_input_field);

        return view;
    }

    //Handle Action Menu
    private MenuProvider getMenuProvider() {
        return new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.edit_note_actions, menu);
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
                    if (title != "" && description != "") {
                        long currentUnix = TimeConvertor.getCurrentUnixSecond();
                        Note[] notes = {new Note(title, description, currentUnix, currentUnix)};
                        noteViewModel.insert(notes);
                    }
                    fragmentManager.popBackStack();
                }
                return true;
            }
        };
    }

    public void showTitle(final String showValue) {
        switch (showValue) {
            case SHOW_TITLE.VALUE_SHOW_ADD_NOTE:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.main_screen_add_note);
                break;
            case SHOW_TITLE.VALUE_SHOW_EDIT_NOTE:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.main_screen_edit_note);
                break;
            default:
                break;
        }
    }
}

