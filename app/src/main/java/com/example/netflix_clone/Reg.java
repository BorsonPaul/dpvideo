package com.example.netflix_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Reg extends AppCompatActivity {

    public static final String Tag="TAG";
    EditText name,email,pass,con_pass;
    ProgressDialog pd;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reg);
        name=(EditText) findViewById(R.id.name_et);
        email=(EditText) findViewById(R.id.email_et);
        pass=(EditText) findViewById(R.id.pass_et);
        con_pass=(EditText) findViewById(R.id.Conpass_et);
        pd=new ProgressDialog(this);
        pd.setTitle("Waiting----");
        pd.setMessage("Please waiting for ");
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        fstore=FirebaseFirestore.getInstance();

    }

    public void registration(View view) {
        if(pass.getText().toString().equals(con_pass.getText().toString())){

            
            reg();
            
        }
    }

    private void reg() {
        String mname,memail,mpass;
        mname=name.getText().toString();
        memail=email.getText().toString();
        mpass=pass.getText().toString();


        if(TextUtils.isEmpty(mname)){
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(memail)){
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(mpass)){
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(memail,mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Reg.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                        nextPage();
                        finish();

                        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Reg.this, "Varification has send!", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(Tag,"Email not sent"+e.getMessage());

                            }
                        });

                        String userID =firebaseAuth.getCurrentUser().getUid();
                        DocumentReference documentReference=fstore.collection("Data").document(userID);
                        Map<String,Object> user=new HashMap<>();
                        user.put("fNAME",mname);
                        user.put("email",memail);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(Tag," Profile is Successfully Created for "+userID);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(Tag,"Profile is not createed"+e.getMessage());

                            }
                        });











                        
                    }
                    else {
                        Toast.makeText(Reg.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }










                }

                private void nextPage() {
                    Intent intent=new Intent(Reg.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


        }
    }


    public void regis(View view) {
        Intent intent=new Intent(Reg.this, Login.class);
        startActivity(intent);
    }
}