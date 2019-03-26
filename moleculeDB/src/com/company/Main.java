package com.company;

//import com.company.database;

public class Main {

    public static void main(String[] args) {
	// write your code here
        //try {
        //String filename = "water.txt";
        database DB = new database();
        DB.addCompound("water.txt");
        DB.addCompound("ammonia.txt");
        DB.addCompound("carbon_dioxide.txt");
        DB.findCompound("acetylene.txt");
        DB.findCompound("water.txt");
        //} catch() {
            //e.printStackTrace();
        //}
    }
}
