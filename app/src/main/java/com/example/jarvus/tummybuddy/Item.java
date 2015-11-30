package com.example.jarvus.tummybuddy;

/**
 * Created by Minh on 11/22/15.
 */
public class Item {
    private String name;
    private String price;
    private String id;
    private String diningHall;

    public Item(String n) {
        name = n;
        id = null;
        price = "No Price Indicated";
        diningHall = "";

    }
    public void setPrice(String p) {
        price = p;
    }
    public void setID(String i) {
        id = i;
    }
    public String getID() {
        return id;
    }
    public boolean hasID() {
        return (id != null);
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }

    public String getDiningHall(){ return diningHall; }
    public void setDiningHall(String d){ diningHall = d;}

}
