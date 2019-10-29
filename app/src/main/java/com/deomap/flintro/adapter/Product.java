package com.deomap.flintro.adapter;

public class Product {
    private String name;
    private int count;
    private String unit;

    public Product(String name, String unit){
        this.name = name;
        this.count=0;
        this.unit = unit;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}

/*

likes - 2 - взаимно (

 */