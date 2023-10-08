package com.example.netflix_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email,pass;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        email=(EditText) findViewById(R.id.email_et);
        pass=(EditText) findViewById(R.id.pass_et);
        firebaseAuth=FirebaseAuth.getInstance();

    }

    public void login(View view) {
        String Email,Pass;
        Email=email.getText().toString();
        Pass=pass.getText().toString();
        if (TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Enter Your Email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(Pass)){
            Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        firebaseUser=firebaseAuth.getCurrentUser();
                        assert firebaseUser!=null;
                        if(firebaseUser.isEmailVerified()){
                            startActivit();
                            finish();

                        }
                        else {
                            Toast.makeText(Login.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }



                    }
                    else {
                        Toast.makeText(Login.this, "Try Again", Toast.LENGTH_SHORT).show();
                    }

                }

                private void startActivit() {
                    Intent intent=new Intent(Login.this, UserActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

        }



    }

    public void regis(View view) {
        Intent intent=new Intent(Login.this, Reg.class);
        startActivity(intent);
    }

    public void forgetPass(View view) {
        final  EditText resetMail=new EditText(view.getContext());
        final AlertDialog.Builder passdialog=new AlertDialog.Builder(view.getContext());
        passdialog.setTitle("Reseting Email");
        passdialog.setMessage("Enter your email for reset password");
        passdialog.setView(resetMail);

        passdialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mail=resetMail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Login.this, "Email Send for Reset Password", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Error! Email is not send", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        passdialog.show();
    }
}