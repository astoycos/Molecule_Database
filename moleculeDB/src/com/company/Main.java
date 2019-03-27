package com.company;


//import com.company.database;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        //try {
        //String filename = "water.txt";
        database DB = new database();
        DB.addCompound("water.txt");
        DB.addCompound("water.txt");
        DB.addCompound("glucose.txt");
//        find molecule
        if ((DB.findCompound("water.txt")) == true) {
            System.out.println("Molecule found");
        }
        //} catch() {
            //e.printStackTrace();
        //}
    }
}
