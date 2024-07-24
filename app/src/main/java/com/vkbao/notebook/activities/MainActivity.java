package com.vkbao.notebook.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.vkbao.notebook.fragments.ListNoteFragment;
import com.vkbao.notebook.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarToggle;
    private NavigationView navigationView;
    private FloatingActionButton mainBtn;
    private FloatingActionButton addLabelBtn;
    private FloatingActionButton addNoteBtn;
    private TextView addLabelTextView;
    private TextView addNoteTextView;
    private View fabModal;

    private boolean isHidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = findViewById(R.id.activity_main_drawer);
        actionBarToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarToggle);

        //Navigation drawer item click event
        navigationView = findViewById(R.id.activity_main_nav);
        navigationView.setNavigationItemSelectedListener(this);

        //Floating Action Button
        mainBtn = findViewById(R.id.activity_main_add_btn);
        addLabelBtn = findViewById(R.id.activity_main_add_label_btn);
        addNoteBtn = findViewById(R.id.activity_main_add_note_btn);
        fabModal = findViewById(R.id.activity_main_modal);

        addLabelTextView = findViewById(R.id.activity_main_add_label_tv);
        addNoteTextView = findViewById(R.id.activity_main_add_note_tv);

        setStateFAB(View.GONE);

        mainBtn.setOnClickListener(this);
        addLabelBtn.setOnClickListener(this);
        addNoteBtn.setOnClickListener(this);
        fabModal.setOnClickListener(this);

//        Display fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //        Portrait mode
        if (findViewById(R.id.note_fragment) != null) {
            Log.d(TAG, "Portrait mode");
            if (savedInstanceState != null) {
                getSupportFragmentManager().executePendingTransactions();
                Fragment note_fragment = fragmentManager.findFragmentById(R.id.note_fragment);
                if (note_fragment != null) {
                    fragmentManager.beginTransaction().remove(note_fragment).commit();
                }
            }
            fragmentManager.beginTransaction().add(R.id.note_fragment, new ListNoteFragment()).commit();

        } else {
//            Landscape mode
            Log.d(TAG, "Landscape mode");
            if (savedInstanceState != null) {
                getSupportFragmentManager().executePendingTransactions();

                //remove note list fragment
                Fragment noteListFragment = fragmentManager.findFragmentById(R.id.note_list_fragment);
                if (noteListFragment != null) {
                    fragmentManager.beginTransaction().remove(noteListFragment).commit();
                }

                //remove note detail fragment
                Fragment noteDetailFragment = fragmentManager.findFragmentById(R.id.note_detail_fragment);
                if (noteDetailFragment != null) {
                    fragmentManager.beginTransaction().remove(noteDetailFragment).commit();
                }
            }
            //add fragment to activity
            fragmentManager.beginTransaction().add(R.id.note_list_fragment, new ListNoteFragment()).commit();
        }

    }

//    Menu ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

//  Handle click event on Menu ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (actionBarToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (itemId == R.id.search) {
            Toast.makeText(this, "Search button selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.about) {
            Intent intent = new Intent(this, ContactActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ContactActivity.SHOW_WHAT.KEY_SHOW_WHAT, ContactActivity.SHOW_WHAT.VALUE_SHOW_ABOUT);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.help) {
            Intent intent = new Intent(this, ContactActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ContactActivity.SHOW_WHAT.KEY_SHOW_WHAT, ContactActivity.SHOW_WHAT.VALUE_SHOW_HELP);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(this, ContactActivity.class);
        Bundle bundle = new Bundle();

        if (id == R.id.activity_main_drawer_item_info) {
            bundle.putString(ContactActivity.SHOW_WHAT.KEY_SHOW_WHAT, ContactActivity.SHOW_WHAT.VALUE_SHOW_ABOUT);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.activity_main_drawer_item_help) {
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
        Log.d(TAG, "btn  is click: " + id);
        if (id == mainBtn.getId() || id == fabModal.getId()){
            if (isHidden) {
                setStateFAB(View.VISIBLE);
            } else {
                setStateFAB(View.GONE);
            }
            isHidden = !isHidden;
        } else if (id == addLabelBtn.getId()) {

        } else if (id == addNoteBtn.getId()) {

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