package com.example.studentrv;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolderClass> {

    ArrayList<Student> student_details;
    Context context;
    onItemClickListener listener;

    //This constructor is used in Activity that has RecyclerView
    public AdapterClass(ArrayList<Student> student_details, Context context) {
        this.student_details = student_details;
        this.context = context;
    }


    // for itemClick Listener on clicking item in RecyclerView
    public interface onItemClickListener
    {
        void onItemClick(int position);
    }

   // we need a method to setOnClickListener
    public void setOnClickListener(onItemClickListener itemClickListener)
    {
        listener = itemClickListener;
    }


    // method for filtering Recycler View
    public void filteringList(ArrayList<Student> fil_ter)
    {

        student_details = fil_ter;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_design,parent,false);

       //pass listener along with view
        return new ViewHolderClass(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {

    Student student = student_details.get(position);
    holder.tv_id.setText(student.getId());
    holder.tv_name.setText(student.getName());
    holder.tv_lastname.setText(student.getLastname());
    holder.tv_dob.setText(student.getDateOfBirth());




    }

    @Override
    public int getItemCount() {
        return student_details.size();
    }


    //View Holder Class
    class ViewHolderClass extends RecyclerView.ViewHolder {

              TextView tv_id,tv_name,tv_lastname,tv_dob;

        public ViewHolderClass(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);

            tv_id = itemView.findViewById(R.id.item_id);
            tv_name = itemView.findViewById(R.id.item_name);
            tv_lastname = itemView.findViewById(R.id.item_lastname);
            tv_dob = itemView.findViewById(R.id.item_dateofBirth);


            // on Clicking an item to delete / perform other stuff
          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  listener.onItemClick(getBindingAdapterPosition()); // to get the position

              }
          });


        }
    }



}

