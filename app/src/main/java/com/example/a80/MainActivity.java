package com.example.a80;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etName,etAge,etHeight,etWeight;
    Button btnMale,btnFemale,btnNext,btnExit;
    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    Map<String, String> newPost = new HashMap<String, String>();
    int gender;
     User user= new User();
    double height=0,weight =0,age=0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //reset(getApplicationContext());

        etName=findViewById(R.id.et_name);
        etAge=findViewById(R.id.et_age);
        etHeight=findViewById(R.id.et_height);
        etWeight=findViewById(R.id.et_weight);
        btnFemale=findViewById((R.id.btn_female));
        btnMale=findViewById(R.id.btn_male);
        btnNext=findViewById(R.id.btn_next);
        btnExit=findViewById(R.id.btn_exit);
        MyJSON.ExistOrCreateMenu(getApplicationContext(),"menu.json");//explain on JsonClass
        MyJSON.ExistOrCreateMenu(getApplicationContext(),"storage.json");
        try {
            User user1 = (User) getIntent().getSerializableExtra("user");
            user = new User(user1);
            SetCurrentUserParameters(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });//exit from application
        btnMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender=1;
                btnMale.setText("נבחר");
                btnFemale.setText("אישה");
            }
        });
        btnFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender=0;
                btnFemale.setText("נבחרה");
                btnMale.setText("גבר");
            }
        });//set gender by click on man photo or woman photo
        user.setGender(gender);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().isEmpty()||etHeight.getText().toString().isEmpty()||etWeight.getText().toString().isEmpty()||etAge.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"נא למלא את כל השדות!",Toast.LENGTH_SHORT).show(); }
                else {
                    weight = Double.parseDouble(etWeight.getText().toString().trim());
                    user.setWeight(weight);
                    height = Double.parseDouble(etHeight.getText().toString().trim());
                    user.setHeight(height);
                    age = Double.parseDouble(etAge.getText().toString().trim());
                    user.setAge(age);
                    user.setGender(gender);
                    user.setName(etName.getText().toString().trim());
                    String jsonArrayStorage=MyJSON.read(getApplicationContext(),"storage.json");
                    SendUserPost(etName.getText().toString().trim(),
                            etAge.getText().toString().trim(),
                            etWeight.getText().toString().trim(),
                            etHeight.getText().toString().trim(),
                            String.valueOf(gender),
                            jsonArrayStorage);
                   Intent intent=new Intent(MainActivity.this,com.example.a80.HelloUser.class);
                   intent.putExtra("user",user);
                   startActivity((intent));
                }
            }
        });
    }
    void SendUserPost(String name, String age, String weight, String height, String gender ,String jsonArrayStorageLocal){
        newPost.put("name",name);
        newPost.put("gender",gender);
        newPost.put("age",age);
        newPost.put("weight",weight);
        newPost.put("height",height);
        newPost.put("jsonArrayStorage",jsonArrayStorageLocal);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String jsonArrayStorage=dataSnapshot.child("jsonArrayStorage").getValue(String.class);
                        if (jsonArrayStorage == "") {
                             String jsonArrayStorage1 = (String) getIntent().getStringExtra("jsonArrayStorage");
                            newPost.put("jsonArrayStorage",jsonArrayStorage1);
                            current_user_db.setValue(newPost);
                        }
                        else{
                            newPost.put("jsonArrayStorage",jsonArrayStorage);
                            current_user_db.setValue(newPost);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
        uidRef.addListenerForSingleValueEvent(eventListener);
        current_user_db.setValue(newPost);
    }
    /*
    When we do some changes on the personal parmeters like (name,weight..),
    this function update the changes on the Firebase server on real time
     */
    void SetCurrentUserParameters(User user){
        etName.setText(user.getName());
        etAge.setText(String.valueOf(user.getAge()));
        etHeight.setText(String.valueOf(user.getHeight()));
        etWeight.setText(String.valueOf(user.getWeight()));
        if(user.getGender()=="גבר"){
            btnMale.setText("נבחר");
            btnFemale.setText("אישה");
        }
        else{
            btnFemale.setText("נבחרה");
            btnMale.setText("גבר");
        }
    }
    /*
    set updated parameters of the user on user class.
     */



     void setStorage(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jsonArrayStorage = dataSnapshot.child("jsonArrayStorage").getValue(String.class);
                if (jsonArrayStorage.isEmpty()) {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "החיבור לשרת נכשל", Toast.LENGTH_SHORT).show();
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
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
    public  void ExistOrCreate(String Path){
        boolean MenuFilePresent = MyJSON.isFilePresent( getApplicationContext(),Path);
        if(MenuFilePresent) { }else {
            try {
                boolean isFileCreated = false;
                isFileCreated = MyJSON.create(getApplicationContext(), Path, "[]");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void  post(){
        try {
            String jsonArrayStorage = (String) getIntent().getStringExtra("jsonArrayStorage");
            newPost.put("jsonArrayStorage",jsonArrayStorage);
            // getIntent().removeExtra("jsonArrayStorage");
            //setIntent(null);
            Toast.makeText(MainActivity.this,"111",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
