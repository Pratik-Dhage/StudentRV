package com.example.studentrv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.studentrv.databinding.ActivityStudentRecyclerViewBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StudentRecyclerView extends AppCompatActivity  {

    ActivityStudentRecyclerViewBinding binding; //for binding purpose

    RecyclerView recyclerView;
   ArrayList<Student> list ;
    LinearLayoutManager layoutManager ;
    AdapterClass adapter ;

  FirebaseDatabase firebaseDatabase;

    String id,id1,name,lastname,dob; // id is used as Firebase Database child // id1 is used as student id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityStudentRecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

     //   list = new ArrayList<>();
     //   adapter = new AdapterClass(list,getApplicationContext());

//        Intent intent = getIntent();
  //     String id = intent.getStringExtra("id");

      initData();
      initRecylerView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

// for search ID
        MenuItem menuItem = menu.findItem(R.id.searchId);
         SearchView sv = (SearchView)menuItem.getActionView();

         sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String s) {

                 return false;
             }

             @Override
             public boolean onQueryTextChange(String s) {

                 if(s.isEmpty())
                 {
                     adapter.filteringList(list);
                 }

                 else {
                     filter(s);
                 }
                // Toast.makeText(StudentRecyclerView.this, "Hi", Toast.LENGTH_SHORT).show();

                 return false;
             }

             private void filter(String s) {

             ArrayList<Student> filteredList = new ArrayList<>();

             // for each loop to search name/id in list
                 for(Student item : list)
                 {
                     // to Check Name or lastname or Id Matches with User Entered String s
                     if( item.getName().toLowerCase().contains(s.toLowerCase()) || item.getLastname().toLowerCase().contains(s.toLowerCase()))

                     {
                         //if this condition is true
                         // if User entered String matches data(name) in Student
                         // then Add that item to filteredlist
                         filteredList.add(item);
                     }
                   else if ( item.getId().toLowerCase().contains(s.toLowerCase())  )
                     {
                         filteredList.add(item);
                     }

                     else
                     {
                         adapter.filteringList(filteredList);
                     }

                 }

             }
         });


        return super.onCreateOptionsMenu(menu);
    }






    private void initData() {

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //String id is declared Above


        firebaseDatabase = FirebaseDatabase.getInstance();

        // to display data from Firebase Database
        firebaseDatabase.getReference().child("Student Details")
                .child("students").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear(); // acts like refresh / clear the data first

                //for each loop
                   for(DataSnapshot dataSnapshot : snapshot.getChildren())
                   {
                    Student students = dataSnapshot.getValue(Student.class);



                     list.add(students);

                   }



                   adapter.notifyDataSetChanged(); // refresh adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void initRecylerView() {

        recyclerView=findViewById(R.id.recyclerView); // id from student_recycler_view.xml

        layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL); //Vertical View

        recyclerView.setLayoutManager(layoutManager);  // Vertical View passed to RecyclerView

      //  ArrayList<Student> students = new ArrayList<>();

       // adapter = new AdapterClass(students,this);

        list = new ArrayList<>();
        adapter = new AdapterClass(list,getApplicationContext());

        recyclerView.setAdapter(adapter); //set adapter

       // for item click in RecyclerView
        adapter.setOnClickListener(new AdapterClass.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

               AlertDialog.Builder builder = new AlertDialog.Builder(StudentRecyclerView.this);
               builder.setTitle("Choose Your Action");
               builder.setMessage("Do you want to Delete Record?");


               //Positive button // to delete selected Item
               builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                        Student student =  list.get(position) ; // to get position



                       Toast.makeText(StudentRecyclerView.this, "Record "+student.getId()+" Deleted", Toast.LENGTH_SHORT).show();


                       firebaseDatabase = FirebaseDatabase.getInstance();

                    //   Intent intent = getIntent();
                    //   String id = intent.getStringExtra("id");


                     // delete selected item/ node
             firebaseDatabase.getReference().child("Student Details")
                            .child("students").child(student.getId()).setValue(null);



                   }
               });

               //Negative Button // To Edit the Item selected
                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                  //  dialogInterface.dismiss();
                     //   Toast.makeText(StudentRecyclerView.this, "Fixed", Toast.LENGTH_SHORT).show();

                        Student student =  list.get(position) ; // to get position of selected item

                        // String id1,name,lastname,dob are declared above(instance variables)
                          id1 = student.getId();
                          name = student.getName();
                          lastname = student.getLastname();
                          dob = student.getDateOfBirth();


                        Intent intent = new Intent(StudentRecyclerView.this,Edit_StudentDetails.class);
                        intent.putExtra("id1",id1);
                        intent.putExtra("name",name);
                        intent.putExtra("lastname",lastname);
                        intent.putExtra("dob",dob);

                         startActivity(intent);

                    }
                }).show();

              //  builder.show(); this can also be used to display Alert Dialog Box
            }
        });


        adapter.notifyDataSetChanged();






    }

}