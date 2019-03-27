package com.company;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import java.util.HashMap;
import java.util.Vector;
import java.io.*;

public class database {
    //Field: HashMap with key as vector of the vertices/structure and value as molecule name(data)
    HashMap<Graph<String,DefaultEdge>,String> data;
    //Constructor: make database by making new HashMap

    public database() {
        data = new HashMap<Graph<String,DefaultEdge>,String>();

    }

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
            data.put(key2, value);
            System.out.println(key2.toString());
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
}
