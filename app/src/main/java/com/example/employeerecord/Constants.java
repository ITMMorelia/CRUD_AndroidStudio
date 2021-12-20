package com.example.employeerecord;

public class Constants {

    //db name
    public static final String DB_NAME = "MY_RECORD_DB";
    //db version
    public static final int DB_VERSION = 1;
    //table name
    public static final String TABLE_NAME = "MY_RECORD_TABLE";
    //column or field name
    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_PHONE = "PHONE";
    public static final String C_EMAIL = "EMAIl";
    public static final String C_DOB = "DOB";
    public static final String C_BIO = "BIO";
    public static final String C_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";
    public static final String C_UPDATE_TIMESTAMP = "UPDATE_TIME_STAMP";

    //create table query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            +C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +C_NAME + " TEXT, "
            +C_IMAGE + " TEXT, "
            +C_PHONE + " TEXT, "
            +C_EMAIL + " TEXT, "
            +C_DOB + " TEXT, "
            +C_BIO + " TEXT, "
            +C_ADDED_TIMESTAMP + " TEXT, "
            +C_UPDATE_TIMESTAMP +" TEXT"
            + " )";

}

// create Database helper class Which Contain Database CRUD Functionality
