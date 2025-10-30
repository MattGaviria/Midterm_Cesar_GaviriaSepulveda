package com.example.midterm_cesar_gaviriasepulveda;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TimesViewModel vm;

    private static final int MENU_TABLE = 1;
    private static final int MENU_HISTORY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vm = new ViewModelProvider(this).get(TimesViewModel.class);

        BottomNavigationView nav = findViewById(R.id.bottomNav);

        // Build bottom-nav items with NO XML menu
        Menu m = nav.getMenu();
        m.clear();
        m.add(0, MENU_TABLE, 0, "Table");
        m.add(0, MENU_HISTORY, 1, "History");

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == MENU_TABLE) {
                show(new TableFragment());
                return true;
            } else if (id == MENU_HISTORY) {
                show(new HistoryFragment());
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            // Select first tab
            nav.setSelectedItemId(MENU_TABLE);
        }
    }

    public void switchToTable() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(MENU_TABLE);
    }

    private void show(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainer, f).commit();
    }
}

