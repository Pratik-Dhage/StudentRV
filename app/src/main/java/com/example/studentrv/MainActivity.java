package com.example.studentrv;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentrv.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {



    ActivityMainBinding binding; //for binding purpose
    ProgressDialog dialog;


 FirebaseDatabase firebaseDatabase; //for Realtime Database
 FirebaseStorage firebaseStorage; //for storage purpose
 DatePickerDialog datePickerDialog; //Object of DatePickerDialog class
 Calendar calendar; // Object of Calender class

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());  //for ViewBinding
        setContentView(binding.getRoot());


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        datePickerDialog = new DatePickerDialog(MainActivity.this);
         calendar = Calendar.getInstance();

         final int day = calendar.get(Calendar.DAY_OF_MONTH);
         final int month = calendar.get(Calendar.MONTH);
         final int year = calendar.get(Calendar.YEAR);



       binding.studentDob.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                       // adding the selected date in the edittext
                       binding.studentDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                   }
               }, year, month, day);


               // set maximum date to be selected as today
               datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

               // show the dialog
               datePickerDialog.show();

           }
       });

        // for Progress dailog purpose
        dialog = new ProgressDialog(this);
        dialog.setTitle("Updating..");
        dialog.setMessage("Almost Done");
        dialog.setCancelable(false);


        // to change profile_pic
        binding.addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If Id/name/lastname/dob fields are empty then profile_pic will not be set
                if(binding.studentId.getText().toString().trim().isEmpty() || binding.studentName.getText().toString().trim().isEmpty()

                        || binding.studentLastname.getText().toString().trim().isEmpty()  || binding.studentDob.getText().toString().trim().isEmpty() )
                {
                    Toast.makeText(MainActivity.this, "First Enter All Details", Toast.LENGTH_SHORT).show();
                    return;

                }

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT); // to Access Phone's Gallery
                intent.setType("image/*");  // to show images in phone's gallery
                startActivityForResult(intent,33);  // to see all files use */* inside setType() and request code put any integer

                 dialog.show(); // display dialogBox


            }
        });





        // Add Student Info
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
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
                   Toast.makeText(getApplicationContext(),"Details Added",Toast.LENGTH_SHORT).show();


                   // Make Fields Clear After Add button is clicked
                   binding.studentId.setText("");
                   binding.studentName.setText("");
                   binding.studentLastname.setText("");
                   binding.studentDob.setText("");
                   binding.studentProfilePic.setImageResource(R.drawable.profile_pic);




               }
           });




            }
        });


        binding.showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,StudentRecyclerView.class);
                 intent.putExtra("id",binding.studentId.getText().toString());

                startActivity(intent);




            }
        });





    } // onCreate Ends here



    // After profile_pic is selected
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // // data.getData() stores URL(path) of the file and its by DEFAULT value is null
           if(data.getData()!=null)
           {



             Uri filePath = data.getData(); // Uri stores file path/address of the file

             binding.studentProfilePic.setImageURI(filePath); // to set profile_pic

               // to store profile_pic in Firebase Storage with Parent Node Student Profile Pictures and child node as Student Full Name
            final StorageReference reference = firebaseStorage.getReference()
                    .child("Student Profile Pictures")
                    .child(binding.studentName.getText().toString().trim()+" "+binding.studentLastname.getText().toString().trim());

             reference.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                     if(task.isSuccessful())
                     {

                        // to store filepath(address) of selected file in Firebase Realtime Database
                              reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                  public void onSuccess(Uri uri) {

                                      String ImageAddress = uri.toString();

                                      firebaseDatabase.getReference().child("Student Details")
                                              .child("Profile Pictures").child(binding.studentId.getText().toString().trim())
                                              .child("profile_pic_address").setValue(ImageAddress)
                                              .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void unused) {

                                                      dialog.dismiss(); //dialogbox disappear
                                                      Toast.makeText(MainActivity.this, " Profile Picture Updated", Toast.LENGTH_SHORT).show();

                                                  }
                                              });
                                  }
                              });


                     }
                 }
             });

           }

    }


}


