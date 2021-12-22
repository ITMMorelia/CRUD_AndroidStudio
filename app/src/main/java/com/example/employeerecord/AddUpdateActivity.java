package com.example.employeerecord;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUpdateActivity extends AppCompatActivity {


    // view
    private CircleImageView profileIv;
    private EditText nameEt, phoneEt, emailEt, dobEt, bioEt;
    private FloatingActionButton fab;

    private String id,name,phone,email,dob,bio,addedTime,updatedTime,profileImage;

    //db helper
    private DbHelper dbHelper;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GAllERY_CODE = 400;

    //array of permission
    private String[] cameraPermission; // camera and storage
    private String[] storagePermission; // only storage


    //variable to save image
    private Uri imageUri;

    //action bar
    ActionBar actionBar;

    private Boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        //init db helper
        dbHelper = new DbHelper(this);

        //init permission array
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init
        actionBar = getSupportActionBar();

        //back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init view
        profileIv = findViewById(R.id.profileIv);
        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        dobEt = findViewById(R.id.dobEt);
        bioEt = findViewById(R.id.BioEt);
        fab = findViewById(R.id.fab);

        //get data from intent
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode",false);
        if (isEditMode){
            //upgrade data
            actionBar.setTitle("Update Dta");
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            email= intent.getStringExtra("EMAIL");
            phone = intent.getStringExtra("PHONE");
            bio = intent.getStringExtra("BIO");
            dob= intent.getStringExtra("DOB");
            addedTime= intent.getStringExtra("ADDEDTIME");
            updatedTime = intent.getStringExtra("UPDATEDTIME");
            profileImage = intent.getStringExtra("IMAGE");

            //set data to view
            nameEt.setText(name);
            phoneEt.setText(phone);
            emailEt.setText(email);
            bioEt.setText(bio);
            dobEt.setText(dob);

            imageUri = Uri.parse(profileImage);

            if(profileImage.equals("null")){
                profileIv.setImageResource(R.drawable.ic_baseline_person_24);
            }else {
                profileIv.setImageURI(Uri.parse(profileImage));
            }

            profileIv.setImageURI(imageUri);


        }else {
            //add data
            actionBar.setTitle("Add data");
        }


        // image picker listener
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show ImagePickerDialog
                imagePickerDialog();
            }
        });

        //add note listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });


    }

    private void inputData() {

        //get data
        name = nameEt.getText().toString();
        phone = phoneEt.getText().toString();
        email = emailEt.getText().toString();
        dob = dobEt.getText().toString();
        bio = bioEt.getText().toString();

        String timestamp = ""+System.currentTimeMillis();
        if (isEditMode){
            //update data
            dbHelper.updateRecord(
                    ""+id,
                    ""+name,
                    ""+imageUri,
                    ""+bio,
                    ""+dob,
                    ""+phone,
                    ""+email,
                    ""+addedTime,
                    ""+timestamp
            );
            Toast.makeText(getApplicationContext(), "Updated...", Toast.LENGTH_SHORT).show();
        }else {

            //save to database

            long id = dbHelper.insertRecord(
                    ""+name,
                    ""+imageUri,
                    ""+bio,
                    ""+dob,
                    ""+phone,
                    ""+email,
                    ""+timestamp,
                    ""+timestamp
            );

            Toast.makeText(getApplicationContext(), "Record Added against ID: "+id, Toast.LENGTH_SHORT).show();
        }



    }

    private void imagePickerDialog() {
        // option to display in dialog

        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set title
        builder.setTitle("Pick Image From");
        //set option item and listener
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // handle item click
                if (which == 0) {
                    //camera option selected

                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    //gallery option selected
                    if (!checkStoragePermission()) {
                        //request for permission
                        requestStoragePermission();
                    } else {
                        pickFromStorage();
                    }

                }
            }
        });
        builder.create();
        builder.show();
    }

    private void pickFromCamera() {

        // intent to pick image from camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Image Description");

        // put image uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        // intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);


    }

    private void pickFromStorage() {
        // intent to pick image from gallery
        Intent galleryIntent  = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); // only image

        startActivityForResult(galleryIntent,IMAGE_PICK_GAllERY_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GAllERY_CODE){
                //picked image from gallery

                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);


            }else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //picked image from camera
                //crop image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AddUpdateActivity.this);

            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                //cropped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK){
                        Uri resultUri = result.getUri();
                        imageUri = resultUri;
                        profileIv.setImageURI(imageUri);
                    }
                    else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                        Exception error = result.getError();
                        Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }

//    ActivityResultLauncher<Intent> activityResultLauncherForGallery = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK){
//                        Intent data = result.getData();
//
//
//                    }
//                }
//
//            }
//    );
//
//    ActivityResultLauncher<Intent> activityResultLauncherForCamera = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK){
//                        Intent data = result.getData();
//
//                    }
//                }
//
//            }
//    );

    private boolean checkStoragePermission() {
        //check if storage permission is enable or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        //request storage permission
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //check if camera permission enable or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result & result1;
    }

    private void requestCameraPermission() {
        //request storage request code
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //request of permission grated or denied
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {

                if (grantResults.length > 0) {
                    //if allowed return true otherwise false
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        // both permission granted
                        pickFromCamera();
                    } else {
                        Toast.makeText(getApplicationContext(), "Camera & Storage Permission are required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {

                if (grantResults.length > 0) {
                    //if allowed return true otherwise false
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        // both permission granted
                        pickFromStorage();
                    } else {
                        Toast.makeText(getApplicationContext(), "Storage Permission are required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    // create class for database, table field title
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}