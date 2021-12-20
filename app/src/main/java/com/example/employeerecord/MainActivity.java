package com.example.employeerecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recordRv;
    private AdapterRecord adapterRecord;
    private DbHelper dbHelper;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        dbHelper = new DbHelper(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddUpdateActivity.class);
                intent.putExtra("isEditMode",false); //want to add data
                startActivity(intent);
            }
        });

        recordRv = findViewById(R.id.recordRv);
        recordRv.setHasFixedSize(true);

        loadRecord();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecord(); // refresh record list
    }

    private void loadRecord() {
        adapterRecord = new AdapterRecord(this,dbHelper.getAllRecords(Constants.C_ADDED_TIMESTAMP + " DESC"));
        recordRv.setAdapter(adapterRecord);
//        actionBar.setSubtitle(dbHelper.getRecordCount());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main,menu);

        //searchView
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //search when search button on keyboard clicked
                searchRecord(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search as type
                searchRecord(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchRecord(String query) {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu item clicked
        return super.onOptionsItemSelected(item);
    }
}