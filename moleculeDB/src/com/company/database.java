package com.company;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import java.util.HashMap;
import java.util.Vector;
import java.io.*;
//import java.sql.*;

public class database {

    //Field: HashMap with key as vector of the vertices/structure and value as molecule name(data)
    HashMap<Graph<String,DefaultEdge>,String> data;
    //Constructor: make database by making new HashMap

    public database() {

        data = new HashMap<Graph<String,DefaultEdge>,String>();

    }

    //Constructor: make database by making new HashMap


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



    //Function: Add molecule to database by reading in text file
    //Read first line (name) and store as value
    //Read all other lines and store as vector (to be used as key)
    public void addCompound(String TextFile) {
        String line = null;
        Graph<String, DefaultEdge> key2 = new Multigraph<>(DefaultEdge.class);
        Vector<String> key = new Vector<>();
        String value = new String();
        int count = 0;
        try {
            BufferedReader read = new BufferedReader(new FileReader(TextFile));
            while ((line = read.readLine())!= null) {
                if (count == 0) {
                    value = line;
                } else if(count > 1){
                    key.addElement(line + (count - 2));
                    if(line.length() == 1){
                        key2.addVertex(line + (count - 2));
                    }else{
                        String[] edge = line.split(" ");
                        key2.addEdge(key.get(Integer.parseInt(edge[0])),key.get(Integer.parseInt(edge[1])));
                    }

                }
                count += 1;
            }

            //data.put(key2, value);
            System.out.println(key2.toString());


            //System.out.println("Molecule " + value+ " added");
            if (findCompound(value + ".txt") == false) { // only add when the molecule does not exist
                data.put(key2, value);
                writeToFile(key2.toString() + " " + value);
                System.out.println("Molecule " + value + " added");
            }
            else {
                System.out.println("Molecule " + value + " already exists");
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("IO");
        }
    }

    //Function (WIP):
    public boolean findCompound(String TextFile) {
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
                //System.out.println("No molecule found");
                return false;
            } else {
                //System.out.println("Molecule name: " + value);
                return true;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("IO");
        }
        return false;
    }

    public void printDB(){
        for (Graph name: data.keySet()){

            String key =name.toString();
            String value = data.get(name).toString();
            System.out.println(key + " " + value);


        }
    }

    //Field: HashMap with key as vector of the vertices/structure and value as molecule name(data)

}

