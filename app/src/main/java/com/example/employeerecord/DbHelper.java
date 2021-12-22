package com.example.employeerecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


// database helper class that contain all CRUD function
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table on database
        db.execSQL(Constants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // upgrade database - if there any structure change db version

        // drop older table if exits
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);

        // create table again
        onCreate(db);
    }

    // Insert function to save data on database
    public long insertRecord(String name,String image, String bio,String dob, String phone, String email, String addedTime, String updatedTime){

        //get writable database to write on database
        SQLiteDatabase db = this.getWritableDatabase();


        // id will auto increment as we set AUTOINCREMENT in query
        ContentValues values = new ContentValues();
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_IMAGE, image);
        values.put(Constants.C_BIO,bio);
        values.put(Constants.C_DOB,dob);
        values.put(Constants.C_PHONE,phone);
        values.put(Constants.C_EMAIL,email);
        values.put(Constants.C_ADDED_TIMESTAMP,addedTime);
        values.put(Constants.C_UPDATE_TIMESTAMP,updatedTime);

        //insert Row. It will return id of saved record
        long id = db.insert(Constants.TABLE_NAME,null,values);

        //close db
        db.close();

        //return id of inserted row
        return id;

    }



    // Upgrade function to save data on database
    public void updateRecord(String id,String name,String image, String bio,String dob, String phone, String email, String addedTime, String updatedTime){

        //get writable database to write on database
        SQLiteDatabase db = this.getWritableDatabase();


        // id will auto increment as we set AUTOINCREMENT in query
        ContentValues values = new ContentValues();

        //insert data
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_IMAGE, image);
        values.put(Constants.C_BIO,bio);
        values.put(Constants.C_DOB,dob);
        values.put(Constants.C_PHONE,phone);
        values.put(Constants.C_EMAIL,email);
        values.put(Constants.C_ADDED_TIMESTAMP,addedTime);
        values.put(Constants.C_UPDATE_TIMESTAMP,updatedTime);

        //update Row.
        db.update(Constants.TABLE_NAME,values,Constants.C_ID + " = ?",new String[]{id});

        //close db
        db.close();

    }


    //get all data from database
    public ArrayList<ModelRecord> getAllRecords(String orderBy){
        //orderBy query will allow to sort data
        //it will return list
        ArrayList<ModelRecord> recordList = new ArrayList<>();
        //query to select record
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " +orderBy;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all records and add to list
        if (cursor.moveToFirst()){
            do {
                ModelRecord modelRecord = new ModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_BIO)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DOB)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATE_TIMESTAMP))
                );
                //add record to list
                recordList.add(modelRecord);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();
        return recordList;
    }

    //search data from database
    public ArrayList<ModelRecord> searchRecord(String query){
        //orderBy query will allow to sort data
        //it will return list

        ArrayList<ModelRecord> recordList = new ArrayList<>();
        //query to select record
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE  " +Constants.C_NAME + " LIKE '%"+query+"%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all records and add to list
        if (cursor.moveToFirst()){
            do {
                ModelRecord modelRecord = new ModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_BIO)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DOB)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATE_TIMESTAMP))
                );
                //add record to list
                recordList.add(modelRecord);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();
        return recordList;
    }

    //get number of record
    public int getRecordCount(){
        String countQuery = "SELECT * FROM "+ Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //delete data by using id
    public void deleteData(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.C_ID + " = ? ",new String[]{id});
        db.close();
    }

    //delete all record
    public void deleteAllRecord(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+Constants.TABLE_NAME);
        db.close();

    }

}
