package com.company;

//import com.company.database;

import java.io.File;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        // write your code here
        //try {
        //String filename = "water.txt";
        System.out.println("Welcome to MoleculeDB");
        System.out.println("This program stores chemical molecules, enter a command to continue");
        System.out.println("Commands should be md followed by either -addMolecule fileName, -findMolecule fileName, -findSubgraph fileName, -findMostsimilar fileName ");

        isodatabase DB = new isodatabase();




        DB.addCompound("water.txt");
        DB.addCompound("ammonia.txt");
        DB.addCompound("glucose.txt");
        DB.addCompound("isomeric.txt");
        DB.addCompound("acetylene.txt");


        DB.findCompound("water.txt");
        DB.findCompound("water2.txt");
        DB.findCompound("Sulfuric_Acid.txt");


        DB.findSubgraph("CH.txt");
        DB.findMostSimilar("isomeric2.txt");

        DB.openDB();
        //DB.printDB();


        File molecules = new File("molecules/");

        File[] listOfFiles = molecules.listFiles();

        //int count = 0;


        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile() && DB.data.values().size() < 10000) {
                DB.addCompound("molecules/" + listOfFiles[i].getName());
                //count++;
            }else break;
        }
        System.out.println(DB.addcount + " molecules added" + DB.data.values().size());

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
                    DB.findCompound(input_splitted[2]);
                } else if (input_splitted[1].equals("-findSubgraph")) {
                    DB.findSubgraph(input_splitted[2]);
                } else if (input_splitted[1].equals("-findMostsimilar")){
                    DB.findMostSimilar(input_splitted[2]);
                }

                // DB.addCompound("carbon_dioxide.txt");
                // DB.findCompound("acetylene.txt");
                // DB.findCompound("water.txt");

                input = scanner.nextLine();

            }
        }catch(Exception Ex){
            System.out.println("Invalid command Format");
            DB.saveDB();
            return;
        }
        DB.saveDB();
    }

}
