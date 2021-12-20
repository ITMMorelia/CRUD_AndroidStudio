package com.example.employeerecord;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRecord  extends RecyclerView.Adapter<AdapterRecord.MyRecordViewHolder>{

    private Context context;
    private ArrayList<ModelRecord> recordList;

    public AdapterRecord(Context context, ArrayList<ModelRecord> recordList) {
        this.context = context;
        this.recordList = recordList;
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
        holder.profileIv.setImageURI(Uri.parse(profileImage));

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

            }
        });

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
}
