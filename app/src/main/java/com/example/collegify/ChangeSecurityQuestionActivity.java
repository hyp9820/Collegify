package com.example.collegify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegify.Lists.common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeSecurityQuestionActivity extends AppCompatActivity {

    private TextView user;



    private String email;
    private TextView security_question;
    private EditText ans;
    private EditText newans;
    private EditText question;
    private Button submit;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_security_question);

        //To Blur the Background image
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.loginbg);//Drawable to bitmap
        Bitmap blurredBitmap = blurRenderScript(this,bitmap, 10);//second parametre is radius
        findViewById(R.id.ChangeSecurityQuestions).setBackground(new BitmapDrawable(getResources(), blurredBitmap));//Assign the bitmap a


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Security Question");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        security_question = (TextView)findViewById(R.id.quest);
        ans = (EditText) findViewById(R.id.oldanswer);
        question = (EditText) findViewById(R.id.squestion);
        newans = (EditText) findViewById(R.id.answer);
        email = common.e_mail;

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if(email.equals(postSnapshot.child("email").getValue(String.class))){
                        security_question.setText(postSnapshot.child("question").getValue(String.class));
                        submit = (Button) findViewById(R.id.submit);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeSQ();
                            }
                        });
                        break;
                    }else {
                        Toast.makeText(ChangeSecurityQuestionActivity.this,"User doesn't exist" ,Toast.LENGTH_LONG ).show();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage() ,Toast.LENGTH_LONG ).show();
            }
        });

    }
    private void changeSQ(){
        final String answer = ans.getText().toString();
        if (TextUtils.isEmpty(answer)) {
            //answer is empty
            ans.setError("Please enter the answer");
            ans.requestFocus();
            //stopping the function execution further
            return;
        }
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if(email.equals(postSnapshot.child("email").getValue(String.class))){
                        if(answer.equals(postSnapshot.child("answer").getValue(String.class))){
                            final String q = question.getText().toString().trim();
                            final String a = newans.getText().toString().trim();
                            mDatabase.child(common.username).child("question").setValue(q);
                            mDatabase.child(common.username).child("answer").setValue(a);
                            Toast.makeText(ChangeSecurityQuestionActivity.this,"Security Question Changed Successfully!!!" ,Toast.LENGTH_LONG ).show();

                            return;
                        }else{
                            Toast.makeText(ChangeSecurityQuestionActivity.this,"The Answer for the Old Security Question may be incorrect" ,Toast.LENGTH_LONG ).show();
                        }
                        break;
                    }else {
                        Toast.makeText(ChangeSecurityQuestionActivity.this,"User doesn't exist" ,Toast.LENGTH_LONG ).show();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage() ,Toast.LENGTH_LONG ).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

