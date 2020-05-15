package com.example.collegify;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class PhoneLogin extends AppCompatActivity {
    private Button buttonContinue ;
    private Button submit;
    private EditText editTextMobile;
    private TextView emailLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        mAuth = FirebaseAuth.getInstance();

        emailLogin = (TextView) findViewById(R.id.emailLogin);
        emailLogin.setPaintFlags(emailLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        buttonContinue = (Button) findViewById(R.id.buttonContinue);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = "91";
                String phone = editTextMobile.getText().toString().trim();
                if(phone.isEmpty() || phone.length() < 10){
                    editTextMobile.setError("Valid Phone number is required");
                    editTextMobile.requestFocus();
                    return;
                }
                String phoneNumber = "+" + code + phone;
                Intent intent = new Intent(getApplicationContext(),VerifyPhone.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);
            }
        });

        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open E-mail login activity
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//doesnot allow the previous activity to lose its edit text contrnts
                startActivity(startIntent);
            }
        });

    }

}
