package com.vkbao.notebook.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Database;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vkbao.notebook.activities.ContactActivity;
import com.vkbao.notebook.activities.MainActivity;
import com.vkbao.notebook.R;
import com.vkbao.notebook.adapters.ViewPagerAdapter;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.respository.NoteRepository;
import com.vkbao.notebook.viewmodels.LabelViewModel;
import com.vkbao.notebook.viewmodels.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainScreenFragment
        extends Fragment
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener  {

    private FloatingActionButton mainBtn;
    private FloatingActionButton addLabelBtn;
    private FloatingActionButton addNoteBtn;
    private TextView addLabelTextView;
    private TextView addNoteTextView;
    private View fabModal;
    private boolean isHidden;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarToggle;
    private NavigationView navigationView;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private LabelViewModel labelViewModel;

    public MainScreenFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_screen_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = view.findViewById(R.id.main_screen_fragment_drawer);
        actionBarToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarToggle);

        //Navigation drawer item click event
        navigationView = view.findViewById(R.id.main_screen_fragment_nav);
        navigationView.setNavigationItemSelectedListener(this);
        //        Floating button
        mainBtn = view.findViewById(R.id.main_screen_fragment_add_btn);
        addLabelBtn = view.findViewById(R.id.main_screen_fragment_add_label_btn);
        addNoteBtn = view.findViewById(R.id.main_screen_fragment_add_note_btn);
        fabModal = view.findViewById(R.id.main_screen_fragment_modal);

        addLabelTextView = view.findViewById(R.id.main_screen_fragment_add_label_tv);
        addNoteTextView = view.findViewById(R.id.main_screen_fragment_add_note_tv);

        setStateFAB(View.GONE);
        isHidden = true;

        mainBtn.setOnClickListener(this);
        addLabelBtn.setOnClickListener(this);
        addNoteBtn.setOnClickListener(this);
        addLabelTextView.setOnClickListener(this);
        addNoteTextView.setOnClickListener(this);
        fabModal.setOnClickListener(this);

        getActivity().addMenuProvider(getMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        //        Portrait mode
//        if (view.findViewById(R.id.note_fragment) != null) {
//            if (savedInstanceState != null) {
//                fragmentManager.executePendingTransactions();
//                Fragment note_fragment = fragmentManager.findFragmentById(R.id.note_fragment);
//                if (note_fragment != null) {
//                    fragmentManager.beginTransaction().remove(note_fragment).commit();
//                }
//            }
//            fragmentManager.beginTransaction().add(R.id.note_fragment, new ListNoteFragment()).commit();
//
//        } else {
////            Landscape mode
//            if (savedInstanceState != null) {
//                fragmentManager.executePendingTransactions();
//
//                //remove note list fragment
//                Fragment noteListFragment = fragmentManager.findFragmentById(R.id.note_list_fragment);
//                if (noteListFragment != null) {
//                    fragmentManager.beginTransaction().remove(noteListFragment).commit();
//                }
//
//                //remove note detail fragment
//                Fragment noteDetailFragment = fragmentManager.findFragmentById(R.id.note_detail_fragment);
//                if (noteDetailFragment != null) {
//                    fragmentManager.beginTransaction().remove(noteDetailFragment).commit();
//                }
//            }
//            //add child fragment to this fragment
//            fragmentManager.beginTransaction().add(R.id.note_list_fragment, new ListNoteFragment()).commit();
//        }

        tabLayout = view.findViewById(R.id.main_screen_tablayout);
        viewPager = view.findViewById(R.id.main_screen_viewpage);
        viewPagerAdapter = new ViewPagerAdapter(requireContext(),this);
        viewPager.setAdapter(viewPagerAdapter);

        labelViewModel = new ViewModelProvider(requireActivity()).get(LabelViewModel.class);
        labelViewModel.getAllLabel().observe(getViewLifecycleOwner(), new Observer<List<Label>>() {
            @Override
            public void onChanged(List<Label> labelList) {
                List<Fragment> fragmentList = new ArrayList<>();
                //label tabs
                for(Label label: labelList) {
                    Bundle tmpBundle = new Bundle();
                    tmpBundle.putString("tab_name", label.getName());
                    ListNoteFragment labelTabFragment = new ListNoteFragment();
                    labelTabFragment.setArguments(tmpBundle);
                    fragmentList.add(labelTabFragment);
                }

                viewPagerAdapter.setFragmentList(fragmentList);
            }
        });
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getTabName(position))
        ).attach();

        if (savedInstanceState != null) {
            int currentTabIndex = savedInstanceState.getInt("CURRENT_TAB_INDEX", 0); // Mặc định là tab 0
            viewPager.setCurrentItem(currentTabIndex, false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CURRENT_TAB_INDEX", tabLayout.getSelectedTabPosition());
    }

    private MenuProvider getMenuProvider() {
        return new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_actions, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (actionBarToggle.onOptionsItemSelected(item)) {
                    return true;
                }

                if (itemId == R.id.search) {
                    Toast.makeText(getActivity(), "Search button selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.about) {
                    Intent intent = new Intent(getActivity(), ContactActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactActivity.SHOW_WHAT.KEY_SHOW_WHAT, ContactActivity.SHOW_WHAT.VALUE_SHOW_ABOUT);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.help) {
                    Intent intent = new Intent(getActivity(), ContactActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactActivity.SHOW_WHAT.KEY_SHOW_WHAT, ContactActivity.SHOW_WHAT.VALUE_SHOW_HELP);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        };
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarToggle.onConfigurationChanged(newConfig);
    }

    //Handle select item navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        Bundle bundle = new Bundle();

        if (id == R.id.main_screen_fragment_drawer_item_info) {
            bundle.putString(ContactActivity.SHOW_WHAT.KEY_SHOW_WHAT, ContactActivity.SHOW_WHAT.VALUE_SHOW_ABOUT);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.main_screen_fragment_drawer_item_help) {
            bundle.putString(ContactActivity.SHOW_WHAT.KEY_SHOW_WHAT, ContactActivity.SHOW_WHAT.VALUE_SHOW_HELP);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mainBtn.getId() || id == fabModal.getId()){
            if (isHidden) {
                setStateFAB(View.VISIBLE);
            } else {
                setStateFAB(View.GONE);
            }
            isHidden = !isHidden;
        } else if (id == addLabelBtn.getId() || id == addLabelTextView.getId()) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_screen_fragment, new MenuLabelFragment())
                    .addToBackStack(null)
                    .commit();

            setStateFAB(View.GONE);
            isHidden = true;
        } else if (id == addNoteBtn.getId() || id == addNoteTextView.getId()) {
            setStateFAB(View.GONE);
            isHidden = true;

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_screen_fragment, new AddNoteFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void setStateFAB(int visibility) {
        if (visibility == View.GONE) {
            mainBtn.setImageResource(R.drawable.ic_action_add);
            addLabelBtn.setVisibility(View.GONE);
            addNoteBtn.setVisibility(View.GONE);
            addLabelTextView.setVisibility(View.GONE);
            addNoteTextView.setVisibility(View.GONE);
            fabModal.setVisibility(View.GONE);
        } else if (visibility == View.VISIBLE) {
            mainBtn.setImageResource(R.drawable.ic_action_close);
            addLabelBtn.setVisibility(View.VISIBLE);
            addNoteBtn.setVisibility(View.VISIBLE);
            addLabelTextView.setVisibility(View.VISIBLE);
            addNoteTextView.setVisibility(View.VISIBLE);
            fabModal.setVisibility(View.VISIBLE);
        }
    }
}