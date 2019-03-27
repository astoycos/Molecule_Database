package com.company;

//import com.company.database;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        // write your code here
        //try {
        //String filename = "water.txt";
        System.out.println("Welcome to MoleculeDB");
        System.out.println("This program stores chemical molecules, enter a command to continue \n");

        database DB = new database();

        DB.addCompound("water.txt");
        DB.addCompound("ammonia.txt");
        DB.findCompound("water.txt",false);
        //DB.printDB();

        Scanner scanner = new Scanner(System.in);

        String input;

        input = scanner.nextLine();

        while(!input.isEmpty()) {
            //System.out.println(input);
            String[] input_splitted = input.trim().split("\\s+");
            if (input_splitted[1].equals( "-addMolecule")){
                DB.addCompound(input_splitted[2]);
            }else if (input_splitted[1].equals("-findMolecule")){
                DB.findCompound(input_splitted[2],false);
            }else if (input_splitted[1].equals("print")){
                DB.printDB();
            }

            // DB.addCompound("carbon_dioxide.txt");
            // DB.findCompound("acetylene.txt");
            // DB.findCompound("water.txt");

            input = scanner.nextLine();

        }

    }
}
