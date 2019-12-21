package com.example.feedbackapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentDashBoard extends AppCompatActivity {
    private DatabaseReference ref;
    private CardView feedback;
    private CardView profile;
    private CardView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dash_board);

        ref= FirebaseDatabase.getInstance().getReference();
        feedback=findViewById(R.id.feedback);
        profile=findViewById(R.id.profile);
        logout=findViewById(R.id.logOut);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentDashBoard.this,MainActivity.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentDashBoard.this,Profile.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(StudentDashBoard.this,AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        super.onStart();
        if(user==null){
            Intent intent=new Intent(StudentDashBoard.this,AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            VerifyStudentExistance();
        }
    }

    private void VerifyStudentExistance() {
        String phoneNumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        ref.child("Students").child(phoneNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Name").exists()){
                           // Toast.makeText(StudentDashBoard.this,"Welcome",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent=new Intent(StudentDashBoard.this,Profile.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
