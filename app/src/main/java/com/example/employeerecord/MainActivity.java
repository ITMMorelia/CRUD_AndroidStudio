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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recordRv;
    private AdapterRecord adapterRecord;
    private DbHelper dbHelper;
    private ActionBar actionBar;
    private ArrayList<ModelRecord> recordList;

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

        recordList = new ArrayList<>();
        recordList.addAll(dbHelper.getAllRecords(Constants.C_ADDED_TIMESTAMP + " DESC"));

        loadRecord();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecord(); // refresh record list
    }

    private void loadRecord() {
        adapterRecord = new AdapterRecord(this,recordList);
        recordRv.setAdapter(adapterRecord);
        actionBar.setSubtitle(""+dbHelper.getRecordCount());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main,menu);

        //searchView
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                ArrayList<ModelRecord> newList = new ArrayList<>();

                for (ModelRecord modelRecord: recordList){
                    String name = modelRecord.getName().toLowerCase();
                    if (name.contains(newText)){
                        newList.add(modelRecord);
                    }

                }
                adapterRecord.setFilter(newList);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu item clicked
        return super.onOptionsItemSelected(item);
    }
}