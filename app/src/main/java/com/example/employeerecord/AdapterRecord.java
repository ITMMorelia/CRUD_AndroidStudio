package com.example.employeerecord;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterRecord  extends RecyclerView.Adapter<AdapterRecord.MyRecordViewHolder>{

    private Context context;
    private ArrayList<ModelRecord> recordList;

    //database helper
    private DbHelper dbHelper;

    public AdapterRecord(Context context, ArrayList<ModelRecord> recordList) {
        this.context = context;
        this.recordList = recordList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public MyRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_record,parent,false);
        return new MyRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecordViewHolder holder, int position) {

        ModelRecord modelRecord = recordList.get(position);

        //get data
        String id = modelRecord.getId();
        String name = modelRecord.getName();
        String phone = modelRecord.getPhone();
        String email = modelRecord.getEmail();
        String bio = modelRecord.getBio();
        String dob = modelRecord.getDob();
        String profileImage = modelRecord.getImage();
        String addedTime = modelRecord.getAddedTime();
        String updatedTime = modelRecord.getUpdatedTime();

        //set data
        holder.nameTv.setText(name);
        holder.phoneTv.setText(phone);
        holder.emailTv.setText(email);

        //set image
        if(profileImage.equals("null")){
            holder.profileIv.setImageResource(R.drawable.ic_baseline_person_24);
        }else {
            holder.profileIv.setImageURI(Uri.parse(profileImage));
        }

        //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass record id to next activity
                Intent intent = new Intent(context,Record_Details.class);
                intent.putExtra("RECORD_ID",id);
                context.startActivity(intent);
            }
        });

        //handle more button clicked
        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(id,name,phone,email,dob,bio,profileImage,addedTime,updatedTime);
            }
        });

    }

    private void showMoreDialog(String id, String name, String phone, String email, String dob,  String bio,String profileImage, String addedTime, String updatedTime) {
        //option to display in dialog
        String[] option = {"Edit","Delete"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select An Action");
        //add item to dialog
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item click
                if (which == 0){
                    //edit is clicked
                    Intent intent = new Intent(context,AddUpdateActivity.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("NAME",name);
                    intent.putExtra("PHONE",phone);
                    intent.putExtra("EMAIL",email);
                    intent.putExtra("DOB",dob);
                    intent.putExtra("BIO",bio);
                    intent.putExtra("ADDEDTIME",addedTime);
                    intent.putExtra("UPDATEDTIME",updatedTime);
                    intent.putExtra("IMAGE",profileImage);
                    intent.putExtra("isEditMode",true);
                    context.startActivity(intent);
                }else if (which == 1) {
                    //delete is clicked
                    dbHelper.deleteData(id);

                    //refresh record by calling activity onResume method
                    ((MainActivity)context).onResume();

                }

            }
        });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class MyRecordViewHolder extends RecyclerView.ViewHolder{

        //views
        ImageView profileIv,moreIv;
        TextView nameTv,phoneTv,emailTv,dobTv,bioTv;
        public MyRecordViewHolder(@NonNull View itemView) {
            super(itemView);


            //init view
            profileIv = itemView.findViewById(R.id.profileIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            moreIv = itemView.findViewById(R.id.moreIv);


        }
    }

    public void setFilter(ArrayList<ModelRecord> newList){
        recordList = new ArrayList<>();
        recordList.addAll(newList);
        notifyDataSetChanged();
    }
}

