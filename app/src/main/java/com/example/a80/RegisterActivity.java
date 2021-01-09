package com.example.a80;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    Button btnCreateNewUser;
    EditText etPersonalName,etPassword,etPasswordRepeat,etEmail;
    FirebaseAuth mFirebaseAuth;
    boolean password=false;
    boolean LengthPassword=false;
    boolean Empty=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth =FirebaseAuth.getInstance();
        etEmail=findViewById(R.id.editText3);
        etPersonalName=findViewById(R.id.editText2);
        etPassword=findViewById(R.id.editText4);
        etPasswordRepeat=findViewById(R.id.editText);
        btnCreateNewUser=findViewById((R.id.button1));
        etPersonalName.requestFocus();
        btnCreateNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmail.getText().toString().isEmpty()||etPersonalName.getText().toString().isEmpty()||etPassword.getText().toString().isEmpty()||etPasswordRepeat.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "נא למלא את כל השדות",Toast.LENGTH_SHORT).show(); }
                else{ Empty=false; }
                if(etPasswordRepeat.getText().toString().equals( etPassword.getText().toString())) { password=true; }
                else{ Toast.makeText(RegisterActivity.this, "הסיסמא לא תואמת",Toast.LENGTH_SHORT).show(); }
                if((password==true)&&(etPasswordRepeat.getText().toString().length()<6)&&password){ Toast.makeText(RegisterActivity.this, "הסיסמא קצרה מידי",Toast.LENGTH_SHORT).show(); }
                else
                    LengthPassword=true;
                if((LengthPassword==true)&&(password==true)&&(Empty==false)) {
                    mFirebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "משהו השתבש,נסה שנית מאוחר יותר", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "המשתמש נוצר בהצלחה", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, Login.class));
                            }
                        }
                    });
                }
            }
        });
    }
}
/*
this activity check all the input is incorrect and create anew user by sending the data like email and password to firebase server.
if the user is exist so its show us "The user exist".
 */