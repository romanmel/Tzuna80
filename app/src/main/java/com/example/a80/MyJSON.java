package com.example.a80;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class MyJSON {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void  DeleteByName (Context context , String fileName, String name) throws JSONException {
        boolean firstExist=false;
        JSONArray array = new JSONArray(read(context, fileName));
        for (int i = 0; i < array.length(); i++) {
            JSONObject currObject = array.getJSONObject(i);
            if (currObject.getString("name").equals(name)) {
                Toast.makeText(context,   currObject.getString("name")+" נמחק מהמאגר ", Toast.LENGTH_LONG).show();
                firstExist=true;
                array.remove(i);
                try {
                    String jsonString1 = array.toString();
                    byte[] tb = jsonString1.getBytes();
                    FileOutputStream fos = new FileOutputStream(context.getFileStreamPath(fileName));
                    fos.write(tb);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (firstExist)
                break;;
        }

    }
    /*
    this function delete product from json Array storage just by name.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void  DeleteByNameFromMenu (Context context , String fileName, String name, String gram) throws JSONException {
        boolean firstExist=false;
        JSONArray array = new JSONArray(read(context, fileName));
        for (int i = 0; i < array.length(); i++) {
            JSONObject currObject = array.getJSONObject(i);
            if (currObject.getString("name").equals(name)&&currObject.getString("gram").equals(gram)) {
                Toast.makeText(context,   currObject.getString("name")+" נמחק מהתפריט", Toast.LENGTH_LONG).show();
                firstExist=true;
                array.remove(i);
                try {
                    String jsonString1 = array.toString();
                    byte[] tb = jsonString1.getBytes();
                    FileOutputStream fos = new FileOutputStream(context.getFileStreamPath(fileName));
                    fos.write(tb);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (firstExist)
                break; }

    }
    /*
this function delete product from json Array menu just by name.
 */
    @SuppressLint("DefaultLocale")
    static JSONObject ReturnValuesByGram(Context context, JSONObject current, double gram) throws JSONException {
       // Toast.makeText(context,current.toString() , Toast.LENGTH_LONG).show();
        JSONObject AfterTrans=new JSONObject();
        double DoubleGram=gram/100.0;
        DoubleGram=  Double.parseDouble(String.format("%.2f", DoubleGram));
        AfterTrans.put("name",(current.getString("name") ));
        AfterTrans.put("gram",gram);
        AfterTrans.put("cal",Double.parseDouble(current.getString("cal"))*DoubleGram);
        AfterTrans.put("carb",Double.parseDouble(current.getString("carb"))*DoubleGram);
        AfterTrans.put("protein",Double.parseDouble(current.getString("protein"))*DoubleGram);
        AfterTrans.put("fat",Double.parseDouble(current.getString("fat"))*DoubleGram);
        AfterTrans.put("sugar",Double.parseDouble(current.getString("sugar"))*DoubleGram);
        return  AfterTrans;
    }
    /*
    calculate the food value by the grams that the user fill
     */
    static  boolean IfExist (Context context , String fileName, String name) throws JSONException {
        JSONArray array = new JSONArray(read(context, fileName));
        for (int i = 0; i < array.length(); i++) {
            JSONObject currObject = array.getJSONObject(i);
            if(currObject.getString("name").equals(name))
                return true;
        }
        return false;
    }
    // check if the name exist on json product storage
    static int CaloriesSum(Context context, String fileName,String parameter) throws JSONException {
        int CaloriesSum1=0;
        JSONArray array = new JSONArray(read(context, fileName));
        for (int i = 0; i < array.length(); i++) {
            JSONObject currObject = array.getJSONObject(i);
            if (currObject==null)
                break;
            CaloriesSum1= CaloriesSum1+((currObject.getInt(parameter)));
        }
        return CaloriesSum1;
    }//calculate all the calories of all the products that appear in a menu.
   public static JSONObject FindOBJbyName(Context context, String fileName, String objname) throws JSONException {
        int i;
        JSONArray array = new JSONArray(read(context, fileName));
        for (i = 0; i < array.length(); i++) {
            JSONObject currObject = array.getJSONObject(i);
            if(currObject.getString("name").equals(objname))
                return currObject;
            if (i==array.length()){
                return null;
            }
        }
        return null;
    }//find product by name in the storage json array or menu array
    static String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            Log.i("tag","read func problem -file Not found");
            return null;
        } catch (IOException ioException) {
            Log.i("tag","read func problem");
            return null;
        }
    }//read json file and return string
    static void write(Context context, String fileName, JSONObject jsonObj ) throws IOException, JSONException {
            JSONArray objMainList = new JSONArray(read( context,  fileName));
            objMainList.put(jsonObj);
            String jsonString1 = objMainList.toString();
            byte[] tb = jsonString1.getBytes();
            FileOutputStream fos = new FileOutputStream(context.getFileStreamPath(fileName));
            fos.write(tb);
            fos.close();
    }
    static void writeAll(Context context, String fileName, String jsonArrayStorage ) {
        try {
            byte[] tb = jsonArrayStorage.getBytes();
            FileOutputStream fos = new FileOutputStream(context.getFileStreamPath(fileName));
            fos.write(tb);
            fos.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
    static boolean create(Context context, String fileName, String jsonString) throws JSONException {
        try {

            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            Log.i("tag"," create function -file not found");
            return false;
        } catch (IOException ioException) {
            Log.i("tag"," create function IO");
            return false;
        }

    }
    public static boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    public static void ExistOrCreateMenu(Context context, String Path){
        boolean MenuFilePresent = MyJSON.isFilePresent( context,Path);
        if(MenuFilePresent) { }else {
            try {
                boolean isFileCreated = false;
                isFileCreated = MyJSON.create(context, Path, "[]");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }





    public static void ExistOrCreateStorage(Context context, String Path){
        boolean MenuFilePresent = MyJSON.isFilePresent(context,Path);
        if(MenuFilePresent) { }else {
            try {
                boolean isFileCreated = false;
                isFileCreated = MyJSON.create(context, Path,context.getString(R.string.first_storage));
                Toast.makeText(context, "ргьк+"+context.getString(R.string.first_storage), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static void OpenJSONArray(Context context , String fileName,JSONObject jsonObj) throws FileNotFoundException {
        JSONArray array = new JSONArray();
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonObj != null) {
                fos.write(array.toString().getBytes());
            }
            fos.close();
    } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readJSONFromAsset(String fileName,Context context) {
        String json = null;
       // JSONObject obj = new JSONObject(loadJSONFromAsset());
        try {
            InputStream is =  context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public static JSONObject loadJSONFromAsset(Context context, String fileName) throws JSONException {
        String json = null;

        try {

            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        JSONObject obj = new JSONObject(json);

        return obj;

    }
    static  JSONObject ReturnJsonObjectByIndex (Context context , String fileName, int i) throws JSONException {
        JSONArray array = new JSONArray(read(context, fileName));

        JSONObject currObject = array.getJSONObject(i);
        return currObject;

    }
public static void PostFromJsonFileToMap(Context context, String filename){
    Map newPost = new HashMap();

    String stringJsonArray =read(context,filename);
    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    Map<String, Object> retMap = new Gson().fromJson(
            stringJsonArray, new TypeToken<HashMap<String, Object>>() {}.getType()
    );
    newPost.put("storage",2);

    current_user_db.setValue(newPost);

}

}
