package com.example.a80.data;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.a80.GoMenu;
import com.example.a80.MyJSON;
import com.example.a80.NameAndDescription;
import com.example.a80.R;
import org.json.JSONException;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
     Context context;
     ArrayList<NameAndDescription> array;
  //  ArrayList<String> arrayDescription;
    public ListAdapter(Context context, ArrayList<NameAndDescription> array){
        this.context=context;
        this.array=array;
      //  this.arrayDescription=arrayDescription;
    }

    @Override
    public int getCount() {
        return array.size();
    }
    //return the size of the List array.
    @Override
    public NameAndDescription getItem(int position){
        return array.get(position);
    }
    //return the item when i press it on ascreen
    @Override
    public long getItemId(int position) {
        return array.indexOf(array.get(position));
    }
    static class ViewHolder{
        TextView name;
        TextView description;
        ImageView delete;
    }
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder =new ViewHolder();
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.product,null);
            holder.name=(TextView) convertView.findViewById(R.id.tv_exm);
            holder.description=(TextView) convertView.findViewById(R.id.tv_exm1);
            holder.delete= convertView.findViewById(R.id.btn_delete_product);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.name.setText(array.get(position).getName());
        holder.description.setText(array.get(position).getDescription());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    String wight=((array.get(position)).getName().substring((array.get(position)).getName().indexOf("(")+1,(array.get(position)).getName().indexOf(")")).replaceAll("\\s.*", ""));
                    MyJSON.DeleteByNameFromMenu(context,"menu.json",(array.get(position)).getName().replaceAll("\\s.*", ""),wight);
                    //when press agrbage button it delete the field .
                    if (context instanceof  GoMenu){
                        ((GoMenu)context).updateMenu();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
