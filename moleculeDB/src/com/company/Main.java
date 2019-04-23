package com.company;

import java.util.Scanner;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to MoleculeDB");

        String database_filename = "hashmap.ser";
        isodatabase DB = new isodatabase();

        System.out.println("Loading database. This can take 10-15 seconds");
        // This opens the database. Preference is for local database. If non exists, pulls from remote starter
        DB.openDB(database_filename);
        System.out.println("\nStarting test cases:");
        System.out.println("Adding compound function. Find compound a part of each add to prevent duplicates");
        long startTime = System.nanoTime();
        // These compounds are to show functionality
        DB.addCompound("water.txt");
        DB.addCompound("ammonia.txt");
        DB.addCompound("glucose.txt");
        DB.addCompound("isomeric.txt");
        DB.addCompound("acetylene.txt");
        DB.addCompound("sulfuric_acid.txt");
        DB.addCompound("water2.txt");
        DB.addCompound("carbon_dioxide.txt");
        DB.addCompound("glucose.txt");
        DB.addCompound("sodium_chloride.txt");
        long timeEnd = System.nanoTime();

        System.out.println("\nTest Statistics");
        System.out.println("10 operations (Add Compounds) took "+((timeEnd-startTime)/1000000)+ " milliseconds");
        int number_of_entries;
        int size_of_db_file;
        // Shows how many compounds are in the database
        number_of_entries = DB.database_statistics()[0];
        size_of_db_file = DB.database_statistics()[1];
        System.out.print("There are " + number_of_entries + " compounds in database"
                +"\n"+"The database, hashmap.ser, is " + size_of_db_file + " bytes\n\n");

        System.out.println("Finding compound function");
        DB.findCompound("water.txt",false);
        DB.findCompound("water2.txt",false);
        DB.findCompound("Sulfuric_Acid.txt",false);

        System.out.println("\nSubgraph search function");
        DB.findSubgraph("Mastoparan.txt");
        System.out.println("\nFind most similar search function");
        DB.findMostSimilar("isomeric2.txt");

        //DB.printDB(); // To be used for debugging

        System.out.println("\nThis program stores chemical molecules, enter a command to continue");
        System.out.println("Commands should be md followed by either exit, -addMolecule fileName, -findMolecule fileName, -findSubgraph fileName, -findMostsimilar fileName, or gui ");
        Scanner scanner = new Scanner(System.in);

        String input;

        input = scanner.nextLine();
            while (input != "exit") {
                try {
                    //System.out.println(input);
                    String[] input_splitted = input.trim().split("\\s+");
                    if (input_splitted[1].equals("-addMolecule")) {
                        DB.addCompound(input_splitted[2]);
                    } else if (input_splitted[1].equals("-findMolecule")) {
                        DB.findCompound(input_splitted[2], false);
                    } else if (input_splitted[1].equals("-findSubgraph")) {
                        DB.findSubgraph(input_splitted[2]);
                    } else if (input_splitted[1].equals("-findMostsimilar")) {
                        DB.findMostSimilar(input_splitted[2]);
                    } else if (input_splitted[1].equals("gui")) {
                        gui myGUI = new gui(DB);
                    } else if (input_splitted[1].equals("exit")) {
                        System.out.println("exiting...");
                        DB.saveDB(database_filename);
                        return;
                    }
                } catch(Exception Ex){
                    System.out.println("Invalid Input Format");
                }

                input = scanner.nextLine();

            }

        DB.saveDB(database_filename);
    }

}
