package com.example.collegify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button submit;
    private EditText uname;
    private EditText email;
    private EditText passwd;
    private EditText confpasswd;
    private TextView login;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        submit = (Button) findViewById(R.id.submit);
        uname = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.passwd);
        confpasswd = (EditText) findViewById(R.id.confpasswd);
        login = (TextView) findViewById(R.id.log);
        login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        submit.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private void registerUser() {
        final String unm = uname.getText().toString().trim();
        final String eml = email.getText().toString().trim();
        final String pass = passwd.getText().toString().trim();
        String confpass = confpasswd.getText().toString().trim();

        if (TextUtils.isEmpty(eml)) {
            //email is empty
            //Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            email.setError("Please enter email");
            email.requestFocus();
            //stopping the function execution further
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            //password is empty
            //Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            passwd.setError("Please enter password");
            passwd.requestFocus();
            //stopping the function execution further
            return;
        }
        if (TextUtils.isEmpty(confpass)) {
            //password is empty
            //Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            confpasswd.setError("Please re-enter password");
            confpasswd.requestFocus();
            //stopping the function execution further
            return;
        }
        if(confpass.equals(pass)){
            //if validations are ok
            //Register the user
            progressDialog.setMessage("Registering User...");
            progressDialog.show();

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(unm).exists()) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Username is already taken,Enter a different Username" , Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        progressDialog.dismiss();
                        mDatabase.child(unm).child("email").setValue(eml);
                        mDatabase.child(unm).child("uname").setValue(unm);
                        mDatabase.child(unm).child("Bookmarks").child("Sample").setValue("sample");
                        firebaseAuth.createUserWithEmailAndPassword(eml, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    Intent startIntent = new Intent(getApplicationContext(), SecurityQuestionActivity.class);
                                    startIntent.putExtra("uname", unm);
                                    startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//doesnot allow the previous activity to lose its edit text contrnts
                                    startActivity(startIntent);
                                    finish();
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        });
                    }
                    return;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),databaseError.getMessage() ,Toast.LENGTH_LONG ).show();
                }
            });

        }
        else {
            Toast.makeText(this, "The re-entered password doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    @Override
    public void onClick(View view) {
        if (view == submit) {
            registerUser();
        }
        if (view == login) {
            //open login activity
            Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//doesnot allow the previous activity to lose its edit text contrnts
            startActivity(startIntent);
        }
    }



}

