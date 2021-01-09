package com.example.a80;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONException;
public class HelloUser extends AppCompatActivity {
    int CaloriesSum=0;
    View viewSumCalories;
    TextView tvFatLevel;
    Button btnChangeParameters,btnGoMenu,btnLogOut,btnBMIinfo,btnEMRinfo,btnBMRinfo,btnShowZigma,btnTip;
    @SuppressLint("SetTextI18n")
    public void onResume() {
        super.onResume();
        try {
            TextView viewMenuValue=findViewById(R.id.MenuValue);
            viewSumCalories=findViewById(R.id.viewEMR);
            TextView tvCalories = (TextView) findViewById(R.id.tv_menu_calories);
            CaloriesSum = MyJSON.CaloriesSum(getApplicationContext(), "menu.json", "cal");
            tvCalories.setText("התפריט שלך מכיל " + CaloriesSum + " קלוריות");
            viewMenuValue.setText(String.valueOf(CaloriesSum));
            viewSumCalories=findViewById(R.id.viewSumCalories);
            ViewGroup.LayoutParams params = viewSumCalories.getLayoutParams();
            params = viewSumCalories.getLayoutParams();
            params.height =setChart(CaloriesSum);
        } catch (JSONException e) { e.printStackTrace(); }
    }
    @SuppressLint({"SetTextI18n", "CutPasteId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_user);
        final User user= (User)getIntent().getSerializableExtra("user");
        final TextView tvData,tvBMI,tvBMR,tvEMR,tvName,tvCalories,viewMenuValue,viewEmrValue;
        View viewEMR,viewSumCalories;
        String strName="שלום, "+user.getName()+"!";
        String strData="מין: "+user.getGender()+"\n"+"גיל: "+user.getAge()+" שנים"+"\n"+"גובה: "+user.getHeight()+" מטר"+"\n"
                +"משקל: "+user.getWeight()+" קילוגרם";
        @SuppressLint("DefaultLocale") String strBMI="BMI: "+String.format("%.1f",user.getBmi());
        String strBMR="הBMR שלך הוא: "+user.getBmr()+" קלוריות";
        String strEMR="הEMR שלך הוא: "+user.getEmr()+" קלוריות";
        viewEMR=findViewById(R.id.viewEMR);
        viewSumCalories=findViewById(R.id.viewSumCalories);
        viewMenuValue=findViewById(R.id.MenuValue);
        viewEmrValue=findViewById(R.id.EmrValue);
        viewEmrValue.setText(String.valueOf(user.getEmr()));
        btnBMIinfo=findViewById(R.id.btn_BMIinfo);
        btnTip=findViewById(R.id.btn_tip1);
        btnEMRinfo=findViewById(R.id.btn_EMRinfo);
        btnBMRinfo=findViewById(R.id.btn_BMRinfo);
        btnShowZigma=findViewById(R.id.btn_Zigma);
        btnGoMenu=findViewById(R.id.menu);
        btnChangeParameters=findViewById(R.id.button2);
        btnLogOut=findViewById(R.id.logout);
        tvFatLevel=findViewById(R.id.tv_fat_level);
        tvCalories=findViewById((R.id.tv_menu_calories));
        tvData=findViewById(R.id.textView);
        tvBMI=findViewById(R.id.tv_bmi);
        tvEMR=findViewById((R.id.tv_EMR));
        tvBMR=findViewById(R.id.tv_BMR);
        tvName=findViewById(R.id.tv_Name);
        tvFatLevel.setText(BMIcheck(user.getBmi()));
        BMIcheck(user.getBmi());
        tvBMR.setText(strBMR);
        tvEMR.setText(strEMR);
        tvBMI.setText(strBMI);
        tvData.setText(strData);
        tvName.setText(strName);
        MyJSON.ExistOrCreateMenu(getApplicationContext(),"menu.json");
        MyJSON.ExistOrCreateMenu(getApplicationContext(),"storage.json");
        try {
             CaloriesSum=MyJSON.CaloriesSum(getApplicationContext(),"menu.json","cal");
             viewMenuValue.setText(String.valueOf(CaloriesSum));
            tvCalories.setText("התפריט שלך כרגע מכיל: "+CaloriesSum+" קלוריות");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ViewGroup.LayoutParams params = viewEMR.getLayoutParams();
        params.height = setChart((user.getEmr()));
        viewEMR.setLayoutParams(params);
        params = viewSumCalories.getLayoutParams();
        params.height =setChart(CaloriesSum);
        viewSumCalories.setLayoutParams(params);
        btnTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("טיפ חשוב!","מומלץ לשלב אימוני משקולות 3 פעמים בשבוע ואירובי לפחות 3 פעמים בשבוע,תזונה זה 80 % אז בואו ניתן עוד 20 % אימונים");

            }
        });
        btnShowZigma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sum=CaloriesSum-user.getEmr();
                openDialog("מאזן קלורי שלך הוא "+sum+" קלוריות" ,"\n"+"*לצורך ירידה באחוזי שומן וירידה במשקל מומלץ להיות במאזן קלורי של עד 500- קלוריות "+"\n"+"*לצורך עלייה במשקל ומסת שריר מומלץ לצרוך יותר קלוריות ממה שצורכים בפועל כלומר מאזן קלורי חיובי, לא פחות מ500 קלוריות");
            }
        });
        btnBMIinfo.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("BMI"," מדד הנותן הערכה כמותית האם אדם נמצא במשקל תקין, בעודף משקל או בתת משקל. המדד מחושב באמצעות נתוני הגובה  והמשקל."+"\n\n"+"*רמת השמנה בהתאם לBMI ולא מתאימה לבעלי מסת שריר גדולה");
            }
        }));
        btnBMRinfo.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("BMR"," מדד המייצג את כמות האנרגיה שהגוף שורף במצב מנוחה מוחלטת. כלומר, כמות האנרגיה הנחוצה לשם שמירה על תפקודים גופניים הכרחיים בלבד, כגון: הכפלת תאים, הפעלת שרירי הנשימה, שריר הלב ושרירים חלקים, העברת גירויים עצביים והעברה פעילה דרך קרומי תאים.");
            }
        }));
        btnEMRinfo.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("EMR","מדד הנותן הערכה לכמות האנרגיה שנצרכת ביום יום, כלומר התייחסות לבן אדם שמבצע פעולות מינוריות בבית או בעבודה(תלמיד שיושב רוב היום או עובד במשרד ");
            }
        }));
        btnChangeParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HelloUser.this,com.example.a80.MainActivity.class);
                intent.putExtra("user",user);
                startActivity((intent));
            }
        });
        btnGoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HelloUser.this,com.example.a80.GoMenu.class);
                startActivity((intent));
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(HelloUser.this,com.example.a80.Login.class);
                startActivity((intent));
            }
        });
    }
    @SuppressLint("ResourceAsColor")
    String   BMIcheck(double bmi){
        String meaning = null;
        if(bmi<18.5) { meaning="תת משקל"; }
        if(18.5<bmi&&bmi<24.9) meaning = "תקין";
        if(25<bmi&&bmi<29.9) meaning="עודף משקל";
        if(30<bmi&&bmi<34.9) meaning="השמנה";
        if(bmi>35) meaning="השמנה חמורה";
        return meaning;
    }
    public void openDialog(String Title,String Dialog){
        Dialog dialog = new Dialog(Title,Dialog);
        dialog.show(getSupportFragmentManager(),"example");
    }

    public int setChart(int Calories){
        Calories = Calories/6;
        return Calories;
    }

    void Get(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jsonArrayStorage = dataSnapshot.child("jsonArrayStorage").getValue(String.class);
                try {
                    MyJSON.create(getApplicationContext(),"storage.json",jsonArrayStorage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HelloUser.this, "החיבור לשרת נכשל", Toast.LENGTH_SHORT).show();
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);

    }
}
