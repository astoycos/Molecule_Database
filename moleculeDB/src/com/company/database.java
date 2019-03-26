package com.company;
import java.util.HashMap;
import java.util.Vector;
import java.io.*;
//import java.sql.*;

public class database {

    // JDBC driver name and database URL
//    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    static final String DB_URL = "jdbc:mysql://localhost/EMP";
//
//    //  Database credentials
//    static final String USER = "username";
//    static final String PASS = "password";
    public void writeToFile(String fileContent) throws IOException
    {
        // Create a variable and check if database file already exists
        File tempFile = new File("database.txt");
        boolean exists = tempFile.exists();
        // If yes, then append to it
        if (exists == true) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("database.txt", true));
            writer.newLine();
            writer.write(fileContent);
            writer.close();
        } else {
            // Else create file, add headers, then add content
            BufferedWriter writer = new BufferedWriter(new FileWriter("database.txt"));
            writer.write("Structure" + " " + "Name");
            writer.newLine();
            writer.write(fileContent);
            writer.close();
        }
    }

    //Constructor: make database by making new HashMap
    public database() {
        data = new HashMap<>();
    }

    //Function: Add molecule to database by reading in text file
    //Read first line (name) and store as value
    //Read all other lines and store as vector (to be used as key)
    public void addCompound(String TextFile) {
        String line = null;
        Vector<String> key = new Vector<>();
        String value = new String();
        int count = 0;
        try {
            BufferedReader read = new BufferedReader(new FileReader(TextFile));
            while ((line = read.readLine())!= null) {
                if (count == 0) {
                    value = line;
                } else {
                    key.addElement(line);
                }
                count += 1;
            }
            data.put(key, value);
            writeToFile(key + " " + value);
            System.out.println("Molecule " + value+ " added");
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("IO");
        }
    }

    //Function (WIP):
    public void findCompound(String TextFile) {
        String line = null;
        Vector<String> key = new Vector<>();
        String value = new String();
        int count = 0;
        try {
            BufferedReader read = new BufferedReader(new FileReader(TextFile));
            while ((line = read.readLine())!= null) {
                if (count != 0) {
                    key.addElement(line);
                }
                count += 1;
            }
            value = data.get(key);
            if (value == null) {
                System.out.println("No molecule found");
            } else {
                System.out.println("Molecule name: " + value);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("IO");
        }
    }

    //Field: HashMap with key as vector of the vertices/structure and value as molecule name(data)
    private HashMap<Vector<String>, String> data;
}
