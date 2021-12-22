package com.example.employeerecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

    //For storage permission
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private static final int STORAGE_REQUEST_CODE_IMPORT = 2;
    private String[] storagePermission;


    //sort option
    private String sortByNewest = Constants.C_ADDED_TIMESTAMP + " DESC";
    private String sortByOldest = Constants.C_ADDED_TIMESTAMP + " ASC";
    private String sortByTitleAsc = Constants.C_NAME + " ASC";
    private String sortByTitleDes = Constants.C_NAME + " DESC";

    //for refreshing record, refresh with last choosen sort option
    String currentOrderByStatus = sortByNewest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        dbHelper = new DbHelper(this);

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddUpdateActivity.class);
                intent.putExtra("isEditMode",false); //want to add data
                startActivity(intent);
            }
        });

        //setup recyclerview
        recordRv = findViewById(R.id.recordRv);
        recordRv.setHasFixedSize(true);

        //load record (by default newest)
        loadRecord(sortByNewest);

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadRecord(currentOrderByStatus); // refresh record list
    }

    private void loadRecord(String orderBy) {
        currentOrderByStatus = orderBy;
        adapterRecord = new AdapterRecord(this, dbHelper.getAllRecords(orderBy));
        recordRv.setAdapter(adapterRecord);
        actionBar.setSubtitle(""+dbHelper.getRecordCount());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main,menu);

//        //searchView
//        MenuItem item = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                newText = newText.toLowerCase();
//                ArrayList<ModelRecord> newList = new ArrayList<>();
//
//                for (ModelRecord modelRecord: recordList){
//                    String name = modelRecord.getName().toLowerCase();
//                    if (name.contains(newText)){
//                        newList.add(modelRecord);
//                    }
//
//                }
//                adapterRecord.setFilter(newList);
//                return true;
//            }
//        });
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecord(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecord(newText);
                return true;
            }
        });

        return true;
    }

    private void searchRecord(String query){
        AdapterRecord adapterRecord = new AdapterRecord(this,dbHelper.searchRecord(query));
        recordRv.setAdapter(adapterRecord);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu item clicked

        switch (item.getItemId()){
            case R.id.sort:
                sortOptionDialog();
                break;
            case R.id.delete:
                dbHelper.deleteAllRecord();
                onResume();
                break;
            case R.id.backup:

                break;
            case R.id.restore:

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void sortOptionDialog() {

        //option
        String[] options = {"Newest","Oldest","Title Ascending", "Title Descending"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            loadRecord(sortByNewest);
                        }
                        else if (which == 1){
                            loadRecord(sortByOldest);
                        }else if (which == 2){
                            loadRecord(sortByTitleAsc);
                        }else if (which == 3){
                            loadRecord(sortByTitleDes);
                        }
                    }
                })
                .create().show();
    }

    private boolean checkStoragePermission() {
        //check if storage permission is enable or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermissionImport() {
        //request storage permission
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE_IMPORT);
    }
    private void requestStoragePermissionExport() {
        //request storage permission
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE_EXPORT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //handle permission result
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case STORAGE_REQUEST_CODE_EXPORT:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission granted
                }else {
                    //permission denied
                    Toast.makeText(this, "Storage per", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}