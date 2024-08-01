package com.vkbao.notebook.adapters;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vkbao.notebook.R;
import com.vkbao.notebook.fragments.ListNoteFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList;
    private List<String> fragmentListName;
    private ListNoteFragment allNoteFragment;
    private Context context;

    public ViewPagerAdapter(Context context, @NonNull Fragment fragment) {
        super(fragment);
        this.context = context;
        fragmentList = new ArrayList<>();
        fragmentListName = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putString("tab_name", context.getString(R.string.tab_name_all));
        allNoteFragment = new ListNoteFragment();
        allNoteFragment.setArguments(bundle);
        this.fragmentList.add(allNoteFragment);
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList.clear();
        this.fragmentList.add(this.allNoteFragment);
        this.fragmentList.addAll(fragmentList);
        notifyDataSetChanged();
    }

    public String getTabName(int position) {
        Fragment fragment = fragmentList.get(position);
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getString("tab_name", "");
        }
        return "";
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
