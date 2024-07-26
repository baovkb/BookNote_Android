package com.vkbao.notebook.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.vkbao.notebook.R;
import com.vkbao.notebook.fragments.AboutFragment;
import com.vkbao.notebook.fragments.HelpFragment;

public class ContactActivity extends AppCompatActivity {
    public static final class SHOW_WHAT {
        public static final String KEY_SHOW_WHAT = "SHOW_WHAT";
        public static final String VALUE_SHOW_HELP = "SHOW_HELP";
        public static final String VALUE_SHOW_ABOUT = "SHOW_ABOUT";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar contactToolbar = findViewById(R.id.contact_toolbar);
        setSupportActionBar(contactToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String valueShow = bundle.getString(SHOW_WHAT.KEY_SHOW_WHAT, "");
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (valueShow.equals(SHOW_WHAT.VALUE_SHOW_ABOUT)) {
                fragmentManager.beginTransaction().replace(R.id.contact_fragment, new AboutFragment()).commit();
            } else if (valueShow.equals(SHOW_WHAT.VALUE_SHOW_HELP)) {
                fragmentManager.beginTransaction().replace(R.id.contact_fragment, new HelpFragment()).commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}