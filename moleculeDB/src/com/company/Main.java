package com.company;

import java.util.Scanner;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to MoleculeDB");
        System.out.println("This program stores chemical molecules, enter a command to continue \n");
        System.out.println("Commands should be md followed by either -addMolecule fileName, -findMolecule fileName, -findSubgraph fileName, -findMostsimilar fileName, or gui ");

        isodatabase DB = new isodatabase();

        // This opens the database. Preference is for local database. If non exists, pulls from remote starter
        DB.openDB();


        // These compounds are to show functionality
        DB.addCompound("water.txt");
        DB.addCompound("ammonia.txt");
        DB.addCompound("glucose.txt");
        DB.addCompound("isomeric.txt");
        DB.addCompound("acetylene.txt");


        DB.findCompound("water.txt",false);
        DB.findCompound("water2.txt",false);
        DB.findCompound("Sulfuric_Acid.txt",false);

        DB.findSubgraph("CH.txt");
        DB.findMostSimilar("isomeric2.txt");

        //DB.printDB(); // To be used for debugging

        Scanner scanner = new Scanner(System.in);

        String input;

        input = scanner.nextLine();
        try {
            while (!input.isEmpty()) {
                //System.out.println(input);
                String[] input_splitted = input.trim().split("\\s+");
                if (input_splitted[1].equals("-addMolecule")) {
                    DB.addCompound(input_splitted[2]);
                } else if (input_splitted[1].equals("-findMolecule")) {
                    DB.findCompound(input_splitted[2], false);
                } else if (input_splitted[1].equals("-findSubgraph")) {
                    DB.findSubgraph(input_splitted[2]);
                } else if (input_splitted[1].equals("-findMostsimilar")){
                    DB.findMostSimilar(input_splitted[2]);
                } else if (input_splitted[1].equals("gui")){
                    gui myGUI = new gui(DB);
                }

                input = scanner.nextLine();

            }
        }catch(Exception Ex){
            System.out.println("Invalid Input Format");
            DB.saveDB();
            return;
        }
        DB.saveDB();
    }

}
