package com.example.feedbackapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText rollNumber;
    private Button updateProfile;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name=findViewById(R.id.full_name);

        email=findViewById(R.id.email);
        rollNumber=findViewById(R.id.rollNumber);
        updateProfile=findViewById(R.id.UpdateProfile);
        mAuth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProfile();
            }
        });
        RetriveUserInfo();
    }

    private void RetriveUserInfo() {
        ref.child("Students").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("Name"))) {
                            String Rname = dataSnapshot.child("Name").getValue().toString();
                            String REmail = dataSnapshot.child("Email").getValue().toString();
                            String RrollNumber = dataSnapshot.child("RollNumber").getValue().toString();

                            name.setText(Rname);
                            email.setText(REmail);
                            rollNumber.setText(RrollNumber);
                        }
                        else{
                            Toast.makeText(Profile.this,"Please Update Your Profile",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void UpdateProfile() {
        String Name=name.getText().toString();
        String Email=email.getText().toString();
        String RollNumber=rollNumber.getText().toString();

        if(TextUtils.isEmpty(Name)||TextUtils.isEmpty(Email)||TextUtils.isEmpty(RollNumber)){
            Toast.makeText(Profile.this,"Please Fill In All The Details",Toast.LENGTH_SHORT).show();
        }

        HashMap<String,String> profileMap=new HashMap<>();
        profileMap.put("Name",Name);
        profileMap.put("Email",Email);
        profileMap.put("RollNumber",RollNumber);
        ref.child("Students").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).setValue(profileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(Profile.this,StudentDashBoard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(Profile.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String message=task.getException().toString();
                            Toast.makeText(Profile.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onStart(){
        super.onStart();
        if(user==null){
            Intent intent=new Intent(Profile.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}
