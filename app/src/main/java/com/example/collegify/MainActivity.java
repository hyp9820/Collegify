package com.example.collegify;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegify.Lists.common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button submit;
    private EditText email;
    private EditText passwd;
    private TextView signup;
    private TextView forgotpass;
    private TextView phone;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //To Blur the Background image
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.loginbg);//Drawable to bitmap
        Bitmap blurredBitmap = blurRenderScript(this, bitmap, 10);//second parametre is radius
        findViewById(R.id.loginpg).setBackground(new BitmapDrawable(getResources(), blurredBitmap));//Assign the bitmap as background

        submit = (Button) findViewById(R.id.submit);
        email = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.passwd);
        signup = (TextView) findViewById(R.id.signup);
        signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotpass = (TextView) findViewById(R.id.forgotpass);
        forgotpass.setPaintFlags(forgotpass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        phone = (TextView) findViewById(R.id.phone);
        phone.setPaintFlags(phone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        submit.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgotpass.setOnClickListener(this);
        phone.setOnClickListener(this);


    }

    private void loginUser() {
        final String eml = email.getText().toString().trim();
        final String pass = passwd.getText().toString().trim();

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
        //if validations are ok
        //Register the user
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(eml, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "CP1", Toast.LENGTH_SHORT).show();

                            //open the profile activity
                            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String email = "";
                                    String usrname = "";

                                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                                        if (postsnapshot.child("email").getValue().equals(eml)) {
                                            email = postsnapshot.child("email").getValue(String.class);
                                            usrname = postsnapshot.child("uname").getValue(String.class);
                                        }
                                    }

                                    Intent startIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startIntent.putExtra("email", email);
                                    startIntent.putExtra("uname", usrname);
                                    startActivity(startIntent);
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    String e = databaseError.getMessage();
                                    Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                loginUser();
                break;
            case R.id.signup:
                //open Signup activity

                Intent startIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startIntent);


                //finish();
                break;
            case R.id.forgotpass:
                //open forgot password activity

                Intent forgotpassIntent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(forgotpassIntent);
                break;
            case R.id.phone:
                //open phone login activity

                Intent phoneLoginIntent = new Intent(getApplicationContext(), PhoneLogin.class);
                startActivity(phoneLoginIntent);
                break;
        }

    }

    // For blurring Images
    @SuppressLint("NewApi")
    public static Bitmap blurRenderScript(Context context, Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }
}
