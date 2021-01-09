package com.example.a80;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.File;

public class Login extends AppCompatActivity {
    Button btnSignUp,btnSignIn;
    EditText etEmail,etPwd;
    TextView btnForgetPass;
    FirebaseAuth mFirebaseAuth;
    int gender=0;
    double height=0,weight =0,age=0;
    final User user=new User();
    String email,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //reset(getApplicationContext());
        mFirebaseAuth =FirebaseAuth.getInstance();
        btnForgetPass=findViewById(R.id.tvBtn_forget_password);
        etEmail=findViewById(R.id.editText2);
        etPwd=findViewById(R.id.editText);
        btnSignUp=findViewById((R.id.button1));//להרשם
        btnSignIn=findViewById((R.id.button));//להיכנס
        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });
        btnSignIn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=etEmail.getText().toString();
                pwd=etPwd.getText().toString();
                if(!email.isEmpty()&&!pwd.isEmpty()) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (mFirebaseAuth.getCurrentUser() != null) {
                                    CheckDataByUID();
                                } else {
                                    Toast.makeText(Login.this, "דואר אלקטרוני או סיסמא שגואים", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "דואר אלקטרוני או סיסמא שגואים", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this, "נא למלא את כל השדות!", Toast.LENGTH_SHORT).show();
                }
            }
        }));
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (Login.this,RegisterActivity.class));
            }
        });
    }
    void CheckDataByUID(){
            final ProgressDialog dialog = ProgressDialog.show(Login.this, "מתחבר", "בבקשה להמתין", true);

            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference uidRef = rootRef.child("users").child(uid);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.child(uid).exists()&&!dataSnapshot.child("name").exists()){
                            String jsonArrayStorage="[]";
                            Intent intent = new Intent(Login.this, com.example.a80.MainActivity.class);
                            intent.putExtra("jsonArrayStorage", jsonArrayStorage);
                            startActivity((intent));
                            dialog.dismiss();
                            finish();
                        }
                        else {
                            String ageTxt = dataSnapshot.child("age").getValue(String.class);
                            String genderTxt = dataSnapshot.child("gender").getValue(String.class);
                            String weightTxt = dataSnapshot.child("weight").getValue(String.class);
                            String heightTxt = dataSnapshot.child("height").getValue(String.class);
                            String nameTxt = dataSnapshot.child("name").getValue(String.class);
                            String jsonArrayStorage = dataSnapshot.child("jsonArrayStorage").getValue(String.class);
                            if (!ageTxt.isEmpty() && !genderTxt.isEmpty() && !weightTxt.isEmpty() && !heightTxt.isEmpty() && !nameTxt.isEmpty() ) {
                                CreateParametersUser(nameTxt, ageTxt, genderTxt, weightTxt, heightTxt,jsonArrayStorage);
                                Intent intent = new Intent(Login.this, com.example.a80.HelloUser.class);
                                intent.putExtra("user", user);
                                startActivity((intent));
                                dialog.dismiss();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Login.this, "החיבור לשרת נכשל", Toast.LENGTH_SHORT).show();
                    }
                };
                uidRef.addListenerForSingleValueEvent(eventListener);
/*
This function check if account exist in the Firebase server, if there is such acount
(uid) in the server, the function define USER class and get from the server
all the data like name, age, weight, height and pass this object to next activity.

 */
    }
    public void openDialog(){
        RepassDialog repassDialog=new RepassDialog();
        repassDialog.setContext(Login.this);
        repassDialog.show(getSupportFragmentManager(),"example dialog");
/*
This function open , the "forget password dialog" by get the E-mail acount and send
link to E-mail box to set anew password for the account.
 */
    }
    void CreateParametersUser(String nameTxt,String ageTxt,String genderTxt,String weightTxt,String heightTxt,String jsonArrayStorage){
        weight = Double.parseDouble(weightTxt.trim());
        user.setWeight(weight);
        height = Double.parseDouble(heightTxt.trim());
        user.setHeight(height);
        age = Double.parseDouble(ageTxt.trim());
        user.setAge(age);
        gender= Integer.parseInt(genderTxt);
        user.setName(nameTxt);
        user.setGender(gender);
        user.setJsonArrayStorage(jsonArrayStorage);
    }
    public static void reset(Context context){

        try {
            boolean isFileCreated = MyJSON.create(context, "storage.json", "[]");//איפוס קובץ
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            boolean isFileCreated = MyJSON.create(context, "menu.json", "[]");//איפוס קובץ
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
/*
*This activity is manage all what refer to login account and get the data from Fire base server
*This Activity checks if the account is exist or not, if yes so it's automatically login to the APP
and get the storage and data from the fireBase server.
 */