package com.example.a80;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.a80.data.ListAdapter;
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
import java.util.ArrayList;
import java.util.Objects;
public class GoMenu extends AppCompatActivity {
    TextView tvCal,tvProtien,tvCarb,tvSugars,tvFat;
    double cal=1,pro,carb,sug,fat;
    AutoCompleteTextView acTextView;
    Button btnAddProduct,btnAddMenuProduct;
    String MenuPath = "menu.json";
    String StoragePath = "storage.json";
    EditText etGrams;
    ListView listView;
    ArrayList<String> arrayList;
    android.widget.ListAdapter adapter ;
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        GetJsonArrayStorage();
        updateMenu();//עובד
        listUpdate();//עובד
        autoTextView();    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_menu);
        //final int emr= (int)getIntent().getSerializableExtra("EMR");

        //reset(getApplicationContext());

        tvCal=findViewById(R.id.tv_cal_show);
        tvProtien=findViewById(R.id.tv_pro_show);
        tvCarb=findViewById(R.id.tv_carb_show);
        tvSugars=findViewById(R.id.tv_sugar_show);
        tvFat=findViewById(R.id.tv_fat_show);
        listView=(ListView)findViewById(R.id.listView_products);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        acTextView= findViewById(R.id.atv_menu_products);
        btnAddProduct=findViewById(R.id.btn_addProduct);
        btnAddMenuProduct=findViewById(R.id.btn_add_menu_product);
        etGrams=findViewById((R.id.et_product_gram));
        arrayList = new ArrayList<>();
        MyJSON.ExistOrCreateMenu(getApplicationContext(),"menu.json");
        MyJSON.ExistOrCreateMenu(getApplicationContext(),"storage.json");
        GetJsonArrayStorage();
        updateMenu();//עובד
        listUpdate();//עובד
        autoTextView();
        btnAddMenuProduct.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    JSONObject currentObject=new JSONObject();
                    if(MyJSON.FindOBJbyName(getApplicationContext(),StoragePath,acTextView.getText().toString())!=null){
                        if(!etGrams.getText().toString().equals("")) {
                            currentObject = MyJSON.ReturnValuesByGram(getApplicationContext(), Objects.requireNonNull(MyJSON.FindOBJbyName(getApplicationContext(), StoragePath,
                                    acTextView.getText().toString())), Double.parseDouble(etGrams.getText().toString()));
                            MyJSON.write(getApplicationContext(), MenuPath, currentObject);
                            listUpdate();
                            updateMenu();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "נא לציין כמות בגרמים", Toast.LENGTH_LONG).show();
                            etGrams.setHintTextColor(R.color.red);
                            etGrams.requestFocus();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(),"נא להוסיף למאגר המוצרים", Toast.LENGTH_LONG).show();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GoMenu.this,com.example.a80.AddNewProduct.class);
                startActivity((intent));
            }
        });
    }
    public  void ExistOrCreate(String Path){
        boolean MenuFilePresent = MyJSON.isFilePresent( getApplicationContext(),Path);
        if(MenuFilePresent) { }else {
            try {
                boolean isFileCreated = false;
                isFileCreated = MyJSON.create(getApplicationContext(), Path,"[]");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public  void updateMenu(){
        try {
            cal=MyJSON.CaloriesSum(getApplicationContext(),MenuPath,"cal");
        } catch (JSONException e) { e.printStackTrace(); }
        try {
            carb=MyJSON.CaloriesSum(getApplicationContext(),MenuPath,"carb");
        } catch (JSONException e) { e.printStackTrace(); }
        try {
            pro=MyJSON.CaloriesSum(getApplicationContext(),MenuPath,"protein");
        } catch (JSONException e) { e.printStackTrace(); }
        try {
            fat=MyJSON.CaloriesSum(getApplicationContext(),MenuPath,"fat");
        } catch (JSONException e) { e.printStackTrace(); }
        try {
            sug=MyJSON.CaloriesSum(getApplicationContext(),MenuPath,"sugar");
        } catch (JSONException e) { e.printStackTrace(); }
        tvCal.setText(String.valueOf(cal));
        tvProtien.setText(String.valueOf(pro));
        tvCarb.setText(String.valueOf(carb));
        tvSugars.setText(String.valueOf(sug));
        tvFat.setText(String.valueOf(fat));
    }
    public void listUpdate(){
        ArrayList<NameAndDescription> arrayList = new ArrayList<>();
        JSONArray arrayProduct = null;
        try {
            arrayProduct = new JSONArray(MyJSON.read(getApplicationContext(), MenuPath));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < arrayProduct.length(); i++) {

            JSONObject jsonObject = null;
            try {
                jsonObject = arrayProduct.getJSONObject(i);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                String name = jsonObject.getString("name")+" ("+jsonObject.getString("gram")+" גרם"+")  "+"קלוריות-"+jsonObject.getString("cal");
                String description =" חלבונים:"+ jsonObject.getInt("protein") +" פחמימות:" +jsonObject.getInt("carb")+"\n"+ " מתוכם סוכרים:"
                        +jsonObject.getInt("sugar")+" שומנים:"+jsonObject.getInt("fat");
                NameAndDescription nameAndDescription=new NameAndDescription(name,description);
                arrayList.add(nameAndDescription);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        adapter= new ListAdapter(GoMenu.this,arrayList);
        listView.setAdapter(adapter);
    }
    public void autoTextView(){
        JSONArray array = null;
        try {
            array = new JSONArray(MyJSON.read(getApplicationContext(), "storage.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!(array ==null)) {
            String[] arr = new String[array.length()];
            for (int i = 0; i < arr.length; i++) {
                try {
                    arr[i] = array.getJSONObject(i).getString("name");
                } catch (JSONException e) { e.printStackTrace(); }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, arr);
            acTextView.setThreshold(1);
            acTextView.setAdapter(adapter);
        }
    }
    void GetJsonArrayStorage(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jsonArrayStorage = dataSnapshot.child("jsonArrayStorage").getValue(String.class);
                    MyJSON.writeAll(getApplicationContext(),"storage.json",jsonArrayStorage);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GoMenu.this, "החיבור לשרת נכשל", Toast.LENGTH_SHORT).show();
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);

    }
    public void openDialog(String Title,String Dialog){
        Dialog dialog = new Dialog(Title,Dialog);
        dialog.show(getSupportFragmentManager(),"example");
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
