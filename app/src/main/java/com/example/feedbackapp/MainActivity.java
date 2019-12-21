package com.example.feedbackapp;

import android.content.Intent;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Spinner subjects;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref;
    private Button btnSumbit;
    private EditText rollNumber;
    private String rollNum;
    String selectedSubject;
    RadioButton radioButtonA;
    RadioButton radioButtonB;
    RadioButton radioButtonC;
    RadioButton radioButtonD;
    RadioButton radioButtonE;
    RadioButton radioButtonF;
    RadioButton radioButtonG;
    RadioButton radioButtonH;
    RadioGroup radioGroupA;
    RadioGroup radioGroupB;
    RadioGroup radioGroupC;
    RadioGroup radioGroupD;
    RadioGroup radioGroupE;
    RadioGroup radioGroupF;
    RadioGroup radioGroupG;
    RadioGroup radioGroupH;
   private LinearLayout mLinearLayout;
   private Button yes;
   private Button no;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSumbit = findViewById(R.id.btn_submit);
        ref=FirebaseDatabase.getInstance().getReference();
        radioGroupA = findViewById(R.id.rg_a);
        radioGroupB = findViewById(R.id.rg_b);
        radioGroupC = findViewById(R.id.rg_c);
        radioGroupD = findViewById(R.id.rg_d);
        radioGroupE = findViewById(R.id.rg_e);
        radioGroupF = findViewById(R.id.rg_f);
        radioGroupG = findViewById(R.id.rg_g);
        radioGroupH = findViewById(R.id.rg_h);
        mLinearLayout=findViewById(R.id.alertDialog);
         yes=findViewById(R.id.AcceptButton);
         no=findViewById(R.id.cancelButton);
        firebaseAuth = FirebaseAuth.getInstance();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,StudentDashBoard.class);
                startActivity(intent);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,StudentDashBoard.class);
                ref.child(selectedSubject).child(rollNum).child("Feedback").removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Toast.makeText(MainActivity.this,"Feedback Deleted",Toast.LENGTH_SHORT).show();
                    }
                });

                startActivity(intent);
            }
        });

        subjects = findViewById(R.id.spinner_subjects);
        List<String> subjectsList = new ArrayList<String>();
        subjectsList.add("Computer Network");
        subjectsList.add("Theory Of Computation");
        subjectsList.add("Distributed Systems");
        subjectsList.add("Database Management System");
        subjectsList.add("Computer Graphics");
        final String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,subjectsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjects.setAdapter(arrayAdapter);


        subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ref.child("Students").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("Name"))) {
                            rollNum = dataSnapshot.child("RollNumber").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedRG_A = radioGroupA.getCheckedRadioButtonId();
                int selectedRG_B = radioGroupB.getCheckedRadioButtonId();
                int selectedRG_C = radioGroupC.getCheckedRadioButtonId();
                int selectedRG_D = radioGroupD.getCheckedRadioButtonId();
                int selectedRG_E = radioGroupE.getCheckedRadioButtonId();
                int selectedRG_F = radioGroupF.getCheckedRadioButtonId();
                int selectedRG_G = radioGroupG.getCheckedRadioButtonId();
                int selectedRG_H = radioGroupH.getCheckedRadioButtonId();

                radioButtonA = findViewById(selectedRG_A);
                radioButtonB = findViewById(selectedRG_B);
                radioButtonC = findViewById(selectedRG_C);
                radioButtonD = findViewById(selectedRG_D);
                radioButtonE = findViewById(selectedRG_E);
                radioButtonF = findViewById(selectedRG_F);
                radioButtonG = findViewById(selectedRG_G);
                radioButtonH = findViewById(selectedRG_H);





                if (rollNum != null) {
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(selectedSubject).child(rollNum);
                    DatabaseReference subDatabaseReference = mDatabaseReference.child("Feedback");
                    subDatabaseReference.child("A").setValue(radioButtonA.getText());
                    subDatabaseReference.child("B").setValue(radioButtonB.getText());
                    subDatabaseReference.child("C").setValue(radioButtonC.getText());
                    subDatabaseReference.child("D").setValue(radioButtonD.getText());
                    subDatabaseReference.child("E").setValue(radioButtonE.getText());
                    subDatabaseReference.child("F").setValue(radioButtonF.getText());
                    subDatabaseReference.child("G").setValue(radioButtonG.getText());
                    subDatabaseReference.child("H").setValue(radioButtonH.getText());
                    Toast.makeText(MainActivity.this, "Feedback is updated", Toast.LENGTH_LONG).show();
                    mLinearLayout.setVisibility(View.VISIBLE);


                    HashMap<String,String> map =new HashMap<>();
                    map.put("RollNo",rollNum);
                    FirebaseDatabase.getInstance().getReference().child("Submitted By").setValue(map);
                }

                else{
                    Toast.makeText(MainActivity.this, "An Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            Intent startAuthActivity = new Intent(MainActivity.this,AuthActivity.class);
            startActivity(startAuthActivity);
            finish();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                firebaseAuth.signOut();
                startActivity(new Intent(this,AuthActivity.class));
                Toast.makeText(this, "Sign outed", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
