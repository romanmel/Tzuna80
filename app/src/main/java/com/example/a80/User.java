package com.example.a80;
import org.json.JSONArray;

import java.io.Serializable;
public class User implements Serializable {

    String name="";
    int gender;
    double age;
    double height;
    double weight;
    double bmi;
    int bmr;
    int emr;
    String jsonArrayStorage;
//define gor user object
    public User(){};
    public User(double age, double height,double weight,int gender,String name,String jsonArrayStorage) {
        this.name=name;
        this.age=age;
        this.height=height;
        this.weight=weight;
        this.gender=gender;
        this.jsonArrayStorage=jsonArrayStorage;

    }
    public User (User user){
        this.name=user.name;
        this.age=user.age;
        this.height=user.height;
        this.weight=user.weight;
        this.gender=user.gender;
        this.jsonArrayStorage=user.jsonArrayStorage;
    }
    public void setName(String name) { this.name = name; }
    public void setGender(int gender){
        if (gender == 1) {
           this.bmr = (int) (66 + 13.8 * weight + height * 5 * 100 - age * 6.8);//male
        }
        if (gender == 0) {
            this.bmr = (int) (665 + 9.6 * weight + height * 1.8 * 100 - age * 4.7);//female
        }
        this.gender=gender;
    }



    public String getJsonArrayStorage() {
        return jsonArrayStorage;
    }

    public void setJsonArrayStorage(String jsonArrayStorage) {
        this.jsonArrayStorage = jsonArrayStorage;
    }

    public void setAge(double age) {
        this.age = age;
    }
    public void setHeight(double height) {
        if (height > 3.0) {
            this.height = height / 100;
        } else {
            this.height = height;
        }
    }
    public void setWeight(double weight) { this.weight = weight;}


    public double getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() { return weight; }

    public int getBmr() { return bmr; }

    public String getGender(){
        if (this.gender == 1) { return "גבר";}//male
        else if (this.gender == 0) { return "אישה";}//female
        else return "לא נבחר";
    }

    public double getBmi(){
        this.bmi= (weight / ((height) * (height)));
        return  bmi;
    }
    public int getEmr(){
        this.emr=(int)(1.2*bmr);
        return emr;
    }

    public String getName() { return name; }
}


