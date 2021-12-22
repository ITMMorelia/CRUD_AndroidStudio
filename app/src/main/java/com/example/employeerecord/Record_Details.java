package com.example.employeerecord;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class Record_Details extends AppCompatActivity {


    //views
    private ImageView profileIv;
    private TextView nameTv,phoneTv,emailTv,dobTv,bioTv,timeAddedTv,timeUpdateTv;

    private ActionBar actionBar;

    private String stringId;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);

        //setting up actionbar with title and back button
        actionBar = getSupportActionBar();
        actionBar.setTitle("Record Details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //get record id from adapter through intent
        Intent intent = getIntent();
        stringId = intent.getStringExtra("RECORD_ID");

        //init db
        dbHelper = new DbHelper(this);
        

        //init view
        profileIv = findViewById(R.id.profileIv);
        nameTv = findViewById(R.id.nameTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        dobTv = findViewById(R.id.dobTv);
        bioTv = findViewById(R.id.bioTv);
        timeAddedTv = findViewById(R.id.addedTimeTv);
        timeUpdateTv = findViewById(R.id.updatedTimeTv);

        
        showDetails();

    }

    private void showDetails() {
        //get record details
        //query to select data
        String selectedQuery = "SELECT * FROM "+ Constants.TABLE_NAME + " WHERE " + Constants.C_ID + " =\"" + stringId+ "\"";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectedQuery,null);

        //keep checking in whole db for that record
        if (cursor.moveToFirst()){
            do {
                //get data
                String name = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME));
                String image = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                String bio = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_BIO));
                String phone = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE));
                String email = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL));
                String dob = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DOB));
                String timestampAdded = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_TIMESTAMP));
                String timestampUpdated = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATE_TIMESTAMP));

                //convert timestamp to dd//mm//yy
                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(timestampAdded));
                String timeAdded = ""+ DateFormat.format("dd/MM/yy hh:mm:aa",calendar1);

                //convert timestamp to dd//mm//yy
                Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
                calendar2.setTimeInMillis(Long.parseLong(timestampUpdated));
                String timeUpdated = ""+ DateFormat.format("dd/MM/yy hh:mm:aa",calendar1);


                //set data
                nameTv.setText(name);
                bioTv.setText(bio);
                phoneTv.setText(phone);
                emailTv.setText(email);
                dobTv.setText(dob);
                timeAddedTv.setText(timeAdded);
                timeUpdateTv.setText(timeUpdated);

                if(image.equals("null")){
                    profileIv.setImageResource(R.drawable.ic_baseline_person_24);
                }else {
                    profileIv.setImageURI(Uri.parse(image));
                }
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}