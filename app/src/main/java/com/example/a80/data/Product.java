package com.example.a80.data;

import java.io.Serializable;

public class Product implements Serializable {
    String name="";
    double cal;
    double pro;
    double carb;
    double sugar;
    double fat;


    public Product() {}


    public Product(String name,double cal,double pro,double carb,double sugar,double fat) {
        this.name=name;
        this.cal=cal;
        this.pro=pro;
        this.carb=carb;
        this.sugar=sugar;
        this.fat=fat;
    }

    public String getName() {
        return name;
    }

    public double getCal() {
        return cal;
    }

    public double getCarb() {
        return carb;
    }

    public double getFat() {
        return fat;
    }

    public double getPro() {
        return pro;
    }

    public double getSugar() {
        return sugar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }

    public void setPro(double pro) {
        this.pro = pro;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }


}
