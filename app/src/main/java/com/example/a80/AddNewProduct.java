package com.example.a80;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddNewProduct extends AppCompatActivity {
    Button btnAddNewProduct,btnAddLoadProduct,btnDeleteProduct;
    AutoCompleteTextView acTextView;
    EditText etCal,etPro,etCarb,etFat,etSugar,etName;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        btnAddLoadProduct=findViewById(R.id.btn_load_new_product);
        btnAddNewProduct=findViewById(R.id.btn_add_new_product);
        btnDeleteProduct=findViewById(R.id.btn_delete_product);
        etCal=findViewById(R.id.et_cal);
        etPro=findViewById(R.id.et_pro);
        etCarb=findViewById(R.id.et_carb);
        etFat=findViewById(R.id.et_fat);
        etSugar=findViewById(R.id.et_sugar);
        etName=findViewById(R.id.et_name);
        acTextView= findViewById(R.id.atv_all_products);
        MyJSON.ExistOrCreateMenu(getApplicationContext(),"storage.json");
        listView=(ListView)findViewById(R.id.list);
        AutoTextView();
        showList();
        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    MyJSON.DeleteByName(getApplicationContext(), "storage.json", acTextView.getText().toString());
                    post( getApplicationContext());
                    AutoTextView();
                    showList();
                } catch (JSONException e) {
                    System.out.println ("Array Index is Out Of Bounds");
                    e.printStackTrace();
                }
            }
        });
        btnAddLoadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject LoadObJson = null;
                if(!acTextView.toString().isEmpty()) {
                    try {
                        LoadObJson = MyJSON.FindOBJbyName(getApplicationContext(), "storage.json", acTextView.getText().toString());
                        if(LoadObJson==null){
                            Toast.makeText(getApplicationContext(), "אין מוצר כזה :/", Toast.LENGTH_LONG).show();
                        }
                        else{
                            try {
                                acTextView.setText(LoadObJson.getString("name"));
                            } catch (JSONException e) { e.printStackTrace(); }
                            try {
                                etCal.setText(LoadObJson.getString("cal"));
                            } catch (JSONException e) { e.printStackTrace(); }
                            try {
                                etPro.setText(LoadObJson.getString("protein"));
                            } catch (JSONException e) { e.printStackTrace(); }
                            try {
                                etCarb.setText(LoadObJson.getString("carb"));
                            } catch (JSONException e) { e.printStackTrace(); }
                            try {
                                etSugar.setText(LoadObJson.getString("sugar"));
                            } catch (JSONException e) { e.printStackTrace(); }
                            try {
                                etFat.setText(LoadObJson.getString("fat"));
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                    } catch (JSONException e) { }
                }
                else {
                    Toast.makeText(getApplicationContext(), "לחיפוש המוצר, אנא הזן את השם" + LoadObJson.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 JSONObject currObject=new JSONObject();
                int itemPosition =position;
                String itemValue=(String)listView.getItemAtPosition(position);
                try {

                    currObject= MyJSON.FindOBJbyName(getApplicationContext(),"storage.json",itemValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    acTextView.setText(currObject.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    etCal.setText(currObject.getString("cal"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    etCarb.setText(currObject.getString("carb"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    etPro.setText(currObject.getString("protein"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    etFat.setText(currObject.getString("fat"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    etSugar.setText(currObject.getString("sugar"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            //String PRODUCT_NAME=etName.getText().toString();

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(!acTextView.getText().toString().isEmpty()) {
                    boolean FileExist = false;
                    try {
                        FileExist = MyJSON.IfExist(getApplicationContext(), "storage.json", acTextView.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!FileExist) {

                        JSONObject obj1 = new JSONObject();
                        try {
                            obj1.put("name", acTextView.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            obj1.put("cal", equalZero(etCal.getText().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            obj1.put("carb",  equalZero(etCarb.getText().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            obj1.put("protein",  equalZero(etPro.getText().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            obj1.put("fat",  equalZero(etFat.getText().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            obj1.put("sugar",  equalZero(etSugar.getText().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            MyJSON.write(getApplicationContext(), "storage.json", obj1);
                        } catch (IOException e) {
                            Log.i("tag", "btn add nee product");
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("tag", "btn add new product");
                        }
                        post(getApplicationContext());
                        GetJsonArrayStorage();
                        showList();
                    } else
                        Toast.makeText(getApplicationContext(), "המוצר הזה קיים כבר במאגר שלך", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "נא הכנס שם מוצר", Toast.LENGTH_LONG).show();
                }
            }
        });
        }
    public void AutoTextView(){
        JSONArray array = null;
        try {
            array = new JSONArray(MyJSON.read(getApplicationContext(), "storage.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            try {
                arr[i]=array.getJSONObject(i).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, arr);
        acTextView.setThreshold(1);
        acTextView.setAdapter(adapter);
    }
    public void post(Context context){
        Map newPost = new HashMap();
        Toast.makeText(getApplicationContext(),"ניכנס למאגר מוצרים", Toast.LENGTH_LONG).show();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String jsonArrayStorage =MyJSON.read(context,"storage.json");
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("jsonArrayStorage").setValue(jsonArrayStorage);
    }

    void GetJsonArrayStorage(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jsonArrayStorage = dataSnapshot.child("jsonArrayStorage").getValue(String.class);
                assert jsonArrayStorage != null;
                MyJSON.writeAll(getApplicationContext(),"storage.json",jsonArrayStorage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddNewProduct.this, "החיבור לשרת נכשל", Toast.LENGTH_SHORT).show();
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }
    void showList(){
        JSONArray array = null;
        try {
            array = new JSONArray(MyJSON.read(getApplicationContext(), "storage.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] values1 = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject currObject = null;
            try {
                currObject = array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                assert currObject != null;
                values1[i]=currObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
          ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values1 );
          listView.setAdapter(adapter);
    }
    String equalZero(String etString){
        if(etString.isEmpty())
           return etString="0";
        return etString;
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
