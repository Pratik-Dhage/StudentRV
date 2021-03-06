package com.example.studentrv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.studentrv.databinding.ActivityEditStudentDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Edit_StudentDetails extends AppCompatActivity {

    ActivityEditStudentDetailsBinding binding;
    FirebaseDatabase firebaseDatabase ;
    String id1,name,lastname,dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityEditStudentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();  // get Student Details from StudentRecyclerView
        id1 = intent.getStringExtra("id1");
        name = intent.getStringExtra("name");
        lastname = intent.getStringExtra("lastname");
        dob = intent.getStringExtra("dob");

        // Display Student Details
        binding.studentId.setText(id1);
        binding.studentName.setText(name);
        binding.studentLastname.setText(lastname);
        binding.studentDob.setText(dob);


/* to See Profile pic from Firebase Realtime Database
        firebaseDatabase.getReference().child("Student Details")
                .child("Profile Pictures").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      String imageUrl = snapshot.getValue().toString();
                     //   Toast.makeText(Edit_StudentDetails.this, ""+imageUrl, Toast.LENGTH_SHORT).show();
                           binding.studentProfilePic.setImageURI(Uri.parse(imageUrl));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
*/

         // onClicking Update Button / to change Student Details if Any
        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If Id field is empty
                if(binding.studentId.getText().toString().trim().isEmpty())
                {
                    binding.studentId.setError("Enter ID ");
                    return;

                }

                // if Name field is empty
                if(binding.studentName.getText().toString().trim().isEmpty())
                {
                    binding.studentName.setError("Enter Name");
                    return;
                }

                // if last Name field is empty
                if(binding.studentLastname.getText().toString().trim().isEmpty())
                {
                    binding.studentLastname.setError("Enter LastName");
                    return;
                }

                //if Date of Birth field is empty
                if(binding.studentDob.getText().toString().trim().isEmpty())
                {
                    binding.studentDob.setError("Enter Date Of Birth");
                    return;
                }

                //Constructor called from Student.java
                Student stud = new Student(binding.studentId.getText().toString().trim(),binding.studentName.getText().toString().trim(),binding.studentLastname.getText().toString().trim(),binding.studentDob.getText().toString().trim());

                // to Insert ID,NAME,LASTNAME,DATEOFBIRTH in FirebaseRealtime Database
                // with Parent Node as Student Details ,child node as students , child as ID
                firebaseDatabase.getReference().child("Student Details").child("students")
                        .child(binding.studentId.getText().toString())
                        .setValue(stud).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"Details Updated",Toast.LENGTH_SHORT).show();


                                // Make Fields Clear After Add button is clicked
                                binding.studentId.setText("");
                                binding.studentName.setText("");
                                binding.studentLastname.setText("");
                                binding.studentDob.setText("");


                            }
                });

            }
        });



        binding.showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Edit_StudentDetails.this,StudentRecyclerView.class);

                startActivity(intent);


            }

                 });


    }


}