package com.company;

import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;
import java.util.*;

// Class for creating a Hashmap capable of storing molecules and searching, efficiently, for isomorphisms

public class isodatabase {

    //Main Database structure
    static HashMap<molecularProperty,HashMap<Graph<String, DefaultEdge>,String>> data;

    //Default constructor for the main Database class
    public isodatabase() {
        data = new HashMap<>();
    }

        //Member function to Serialize and save the database HashMap "data" structure to the hashmap.ser file
        public static void saveDB(String database_file) {
        System.out.println("Starting save. This can take a few seconds");
        try {
            FileOutputStream filestream = new FileOutputStream(database_file);
            ObjectOutputStream outputstream = new ObjectOutputStream(filestream);
            outputstream.writeObject(data);
            outputstream.close();
            filestream.close();
            System.out.println("HashMap serialized");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Member function to populate the database HashMap "data" from the serialized hashmap.ser file
    //If there is no hashmap.ser file in the working directory the function pulls a pre-populated, with 10000 unique molecules,
    //serialized version of the hashmap
    public static void openDB(String database_file) {
        // first tries to open a local database
        try {
            FileInputStream filestream = new FileInputStream(database_file);
            ObjectInputStream inputstream = new ObjectInputStream(filestream);
            data = (HashMap) inputstream.readObject();
        } catch (IOException ex) {
            System.out.println("Creating new local database from remote");
            // If no local database exists, pulls from remote to start new local database
            try {
            URL url = new URL("http://73.238.218.108/hashmap.ser");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            ObjectInputStream inputstream = new ObjectInputStream(conn.getInputStream());
            data = (HashMap) inputstream.readObject();

            saveDB(database_file);
            } catch (IOException ex3){System.out.println("Remote inaccesible");
            } catch (ClassNotFoundException ex4) {
                ex4.printStackTrace();
            }

        } catch (ClassNotFoundException ex2) {
            ex2.printStackTrace();
        }

    }

    //Class to use backtracking to check for isomorphism
    // Implements the algorithm described in the peer reviewed paper: Optimized Bactracking for SubGraph IsoMorphism
    class equal_graphs {
        boolean found;
        boolean eob;
        //Vector used to map similar verticies across Graphs to eachother
        Vector<Pair<java.lang.Object, java.lang.Object>> Mapping;
        Graph<String, DefaultEdge> G;
        Graph<String, DefaultEdge> SG;

        //Constructor
        public equal_graphs(Graph<String, DefaultEdge> SG2, Graph<String, DefaultEdge> G2){
            G = G2;
            SG = SG2;
            found = false;
            eob = false;
            Mapping = new Vector<>();

        }

        //Top level function of recursion Tree
        //Returns true if graph G contains or is equal to graph SG
        public boolean check_SG() {
            Set verticesSG = SG.vertexSet();
            Set verticesG = G.vertexSet();

            Iterator A = verticesSG.iterator();
            Iterator B = verticesG.iterator();
            java.lang.Object U = A.next();
            while (B.hasNext()) {
                java.lang.Object V = B.next();
                if (to_map(U, V)) {
                    found = true;
                    return true;

                }

            }
            if (!found) {
                return false;
            }
            return false;
        }

        //Main Recursive function to Map "Equal" verticies to eachother between the two graphs
        boolean to_map(java.lang.Object U, java.lang.Object V) {
            boolean eob = false;
            if (Mapping.size() == SG.vertexSet().size()) { /// MAPPING IS FULL
                found = true;
                return (true);
            }
            if (equal_Nodes(U, V)) {
                Vector<Pair<java.lang.Object, java.lang.Object>> Pairs = get_equal_Nodes(U, V);
                if (Pairs == null) return false;
                else {
                    Pair A = new Pair(U,V);
                    Mapping.add(A);
                    for (int i = 0; i < Pairs.size(); i++) {
                        if (found) break;
                        if (eob) {
                            eob = false;
                            continue;
                        }
                        to_map(Pairs.get(i).getKey(),Pairs.get(i).getValue());
                    }
                    if (Mapping.size() == SG.vertexSet().size()) { //MAPPING is full
                        Mapping.remove(Mapping.size() - 1);
                        return false;
                    } else return true;
                }
            }
            return false;
        }

        //Finds compatible neighbors of a a single compatible vertex in G and SG
        Vector<Pair<java.lang.Object, java.lang.Object>> get_equal_Nodes(java.lang.Object U, java.lang.Object V) {

            Vector<Pair<java.lang.Object, java.lang.Object>> out = new Vector<>();

            List u_neighbors = Graphs.neighborListOf(SG, U.toString());

            List v_neighbors = Graphs.neighborListOf(G, V.toString());

            for(int i=0;i<u_neighbors.size();i++){
                java.lang.Object u_2 = u_neighbors.get(i);
                for(int j=0;j<v_neighbors.size();j++){
                    java.lang.Object v_2 = v_neighbors.get(j);
                    if(equal_Nodes(u_2,v_2)){
                        if(!Mapping.contains(u_2) || !Mapping.contains(v_2)){
                            Pair A = new Pair(u_2,v_2);
                            out.add(A);
                        }
                        if(SG.vertexSet().size() == Mapping.size()){
                            eob = true;
                            return out;
                        }
                    }
                }
            }
            return out;
        }

        // helper function that returns if two verticies are equal
        // Only returns true if they are the same element and the Degree of the SG vertex(U) is less than Degree of the G vertex(V)
        boolean equal_Nodes(java.lang.Object U, java.lang.Object V){
            if(U.toString().charAt(0) == V.toString().charAt(0)){
                if(SG.degreeOf(U.toString()) <= G.degreeOf(V.toString())){
                    return true;
                }else return false;
            }else{
                return false;
            }
        }
    }

    //Memberfunction to find a Compound in the database if it is there
    //Searches the database using an unknown molecular graph as the key
    //Instansiates equal_graph subclass to decide weather Graph in data is equal or isomorphic to the graph being searched
    public boolean findCompound(String TextFile) {
        //code to parse input textfile, this is the same for every member function
        String linefind = null;
        int count = 0;
        int numberAtoms = 0;
        int numberBonds = 0;
        String moleculeName = null;
        HashMap<String, Integer> formula = new HashMap<>();
        Vector<String> key = new Vector<>();
        Graph<String, DefaultEdge> key2 = new Multigraph<>(DefaultEdge.class);

        System.out.println("Searching for: "+ TextFile);
        try {
            BufferedReader read = new BufferedReader(new FileReader(TextFile));
            while ((linefind = read.readLine()) != null) {
              //Allows the user to search for a textfile reguardless of wether the molecule name is at the top of the file or not
                if (count == 0) {
                    try {
                        numberAtoms = Integer.valueOf(linefind);
                       //moleculeName = linefind;
                        count++;
                    }catch(Exception ex){
                        moleculeName = linefind;
                        //count++;
                    }
                }
                if (count == 1) {

                    numberAtoms = Integer.valueOf(linefind);
                } else if (count >= 2){
                    key.addElement(linefind + (count - 2));
                    if (count > 0 & count <= numberAtoms + 1) {

                        key2.addVertex(linefind + (count - 2));
                        Integer currentCount = formula.get(linefind);
                        if (currentCount == null) {
                            formula.put(linefind, 1);

                        } else {
                            formula.put(linefind, currentCount + 1);
                        }
                    } else {
                        String[] edge = linefind.split(" ");
                        key2.addEdge(key.get(Integer.parseInt(edge[0])), key.get(Integer.parseInt(edge[1])));
                        numberBonds++;
                    }

                }
                count++;
            }

            //Creates mainkey that us used to prune branches before recursively comparing graphs with equal_graphs class
            molecularProperty mainKey = new molecularProperty(formula, numberBonds,numberAtoms);

            if (data.get(mainKey) == null) {
                System.out.println("Molecule not found");
                return false;
            } else {
                for (Graph<String, DefaultEdge> possibleMatch: data.get(mainKey).keySet()) {
                    equal_graphs one = new equal_graphs(key2, possibleMatch);
                    if(one.check_SG()){
                        System.out.println("Graph " + TextFile + " found in: " + data.get(mainKey).get(possibleMatch));
                        return true;
                    }

                }
                System.out.println("Graph " + TextFile + " not found in MoleculeDB ");
                return false;

            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");

            return false;
        } catch (IOException ex) {
            System.out.println("IO");

            return false;
        } catch (Exception ex2) {
            System.out.print("Incorrect format");

        }
        return false;
    }

    // Finds the molecules with the subgraph provided by Textfile
    public void findSubgraph(String Textfile) {
        String linefind = null;
        int count = 0;
        int numberAtoms = 0;
        int numberBonds = 0;
        String moleculeName = null;
        HashMap<String, Integer> formula = new HashMap<>();
        Vector<String> key = new Vector<>();
        Graph<String, DefaultEdge> key2 = new Multigraph<>(DefaultEdge.class);

        //Read in the Textfile

        try {
            BufferedReader read = new BufferedReader(new FileReader(Textfile));
            while ((linefind = read.readLine()) != null) {
                if (count == 0) {
                    try {
                        numberAtoms = Integer.valueOf(linefind);
                        //moleculeName = linefind;
                        count++;
                    }catch(Exception ex){
                        moleculeName = linefind;
                        //count++;
                    }
                }
                if (count == 1) {

                    numberAtoms = Integer.valueOf(linefind);
                } else if (count >= 2){
                    key.addElement(linefind + (count - 2));
                    if (count > 0 & count <= numberAtoms + 1) {

                        key2.addVertex(linefind + (count - 2));
                        Integer currentCount = formula.get(linefind);
                        if (currentCount == null) {
                            formula.put(linefind, 1);

                        } else {
                            formula.put(linefind, currentCount + 1);
                        }
                    } else {
                        String[] edge = linefind.split(" ");
                        key2.addEdge(key.get(Integer.parseInt(edge[0])), key.get(Integer.parseInt(edge[1])));
                        numberBonds++;
                    }

                }
                count++;
            }


            molecularProperty mainKey = new molecularProperty(formula, numberBonds,numberAtoms);
            Vector<molecularProperty> mustSearch = new Vector<>();
            boolean bonds_and_elements = false;

            //Expands the possible graphs to compare Textfile too by looking at all the MolecularProperty Keys in data
            for(molecularProperty name: data.keySet()) {

                if(name.numEdges >= numberBonds && name.numAtoms >= numberAtoms) {
                    for (String element : formula.keySet()) {
                        if(name.molecularFormula.keySet().contains(element)){
                            bonds_and_elements = true;
                        }else bonds_and_elements = false;
                    }
                }
                //If numEdges and numAtoms of G is greater than SG continue
                if(bonds_and_elements){
                    for (Graph<String, DefaultEdge> possibleMatch: data.get(name).keySet()) {
                        //If graphs are equal or isomorphic return G
                        equal_graphs one = new equal_graphs(key2, possibleMatch);
                        if(one.check_SG()){
                            System.out.println("Subgraph " + Textfile +" found in: " + data.get(name).get(possibleMatch));
                        }
                    }
                }
                bonds_and_elements = false;
            }






        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            //return null;
        } catch (IOException ex2) {
            System.out.println("IO");
            //return null;
        } catch (Exception ex3) {
            System.out.print("Incorrect format");
        }



    }

    //Member function to add a molecule to the database
    public boolean addCompound(String TextFile) {
        String linefind = null;
        int count = 0;
        int numberAtoms = 0;
        int numberBonds = 0;
        String moleculeName = null;
        HashMap<String, Integer> formula = new HashMap<>();
        Vector<String> key = new Vector<>();
        Graph<String, DefaultEdge> key2 = new Multigraph<>(DefaultEdge.class);

        //Textfile processing into a JgraphT Object
        try {
            BufferedReader read = new BufferedReader(new FileReader(TextFile));
            while ((linefind = read.readLine()) != null) {
                if (count == 0) {
                    try {
                        numberAtoms = Integer.valueOf(linefind);
                        //moleculeName = linefind;
                        count++;
                    }catch(Exception ex){
                        moleculeName = linefind;
                        //count++;
                    }
                }
                if (count == 1) {

                    numberAtoms = Integer.valueOf(linefind);
                } else if (count >= 2){
                    key.addElement(linefind + (count - 2));
                    if (count > 0 & count <= numberAtoms + 1) {

                        key2.addVertex(linefind + (count - 2));
                        Integer currentCount = formula.get(linefind);
                        if (currentCount == null) {
                            formula.put(linefind, 1);

                        } else {
                            formula.put(linefind, currentCount + 1);
                        }
                    } else {
                        String[] edge = linefind.split(" ");
                        key2.addEdge(key.get(Integer.parseInt(edge[0])), key.get(Integer.parseInt(edge[1])));
                        numberBonds++;
                    }

                }
                count++;
            }

            molecularProperty mainKey = new molecularProperty(formula, numberBonds,numberAtoms);
            HashMap<Graph<String, DefaultEdge>,String> secondLayer = new HashMap<>();

            //Will not add molecule if it is already in the database
            if (findCompound(TextFile)) {
                System.out.println("Molecule " + moleculeName + " already there");
                return false;
            } else {
                System.out.println("Molecule " + moleculeName + " added");
                secondLayer.put(key2,moleculeName);
                data.put(mainKey,secondLayer);
                return true;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            return false;
        } catch (IOException ex) {
            System.out.println("IO");
            return false;
        } catch (Exception ex2) {
            System.out.print("Incorrect format");
            return false;
        }

    }

    // Finds the most similar molecule to the one represented by TextFile
    public void findMostSimilar(String TextFile) {
        String linefind = null;
        int count = 0;
        int numberAtoms = 0;
        int numberBonds = 0;
        String moleculeName = null;
        HashMap<String, Integer> formula = new HashMap<>();
        Vector<String> key = new Vector<>();
        Graph<String, DefaultEdge> key2 = new Multigraph<>(DefaultEdge.class);

        System.out.println("Looking for Molecule most similar to: " + TextFile);


        try {
            BufferedReader read = new BufferedReader(new FileReader(TextFile));
            while ((linefind = read.readLine()) != null) {
                if (count == 0) {
                    try {
                        numberAtoms = Integer.valueOf(linefind);
                        //moleculeName = linefind;
                        count++;
                    }catch(Exception ex){
                        moleculeName = linefind;
                        //count++;
                    }
                }
                if (count == 1) {

                    numberAtoms = Integer.valueOf(linefind);
                } else if (count >= 2){
                    key.addElement(linefind + (count - 2));
                    if (count > 0 & count <= numberAtoms + 1) {

                        key2.addVertex(linefind + (count - 2));
                        Integer currentCount = formula.get(linefind);
                        if (currentCount == null) {
                            formula.put(linefind, 1);

                        } else {
                            formula.put(linefind, currentCount + 1);
                        }
                    } else {
                        String[] edge = linefind.split(" ");
                        key2.addEdge(key.get(Integer.parseInt(edge[0])), key.get(Integer.parseInt(edge[1])));
                        numberBonds++;
                    }

                }
                count++;
            }

            //There are 4 cases to finding the most similar molecule
            molecularProperty mainKey = new molecularProperty(formula, numberBonds,numberAtoms);
            molecularProperty mostSimilarMainKey = new molecularProperty();
            boolean atomFlag = false;
            int totalAtomDifferential = Integer.MAX_VALUE;
            int totalBondDifferential;
            //If the mainKey is not found (chemical formula is not in the database), we look for through the database for molecules containing with the same exact elements
            if (data.get(mainKey) == null) {
                // If there is a molecule with the exact same elements, we total up the amount of atoms and compare it to the amount of atoms in the input molecule
                // We look for the molecule which the number of atoms closest to the input molecule
                for (molecularProperty possibleSimilar : data.keySet()) {
                    if (possibleSimilar.molecularFormula.keySet().equals(formula.keySet())) {
                        int totalAtoms = 0;
                        for (Integer atoms: possibleSimilar.molecularFormula.values()) {
                            totalAtoms += atoms;
                        }
                        int totalAtoms2 = 0;
                        for (Integer atoms: formula.values()) {
                            totalAtoms2 += atoms;
                        }
                        if (totalAtomDifferential > Math.abs(totalAtoms - totalAtoms2)) {
                            totalAtomDifferential = Math.abs(totalAtoms - totalAtoms2);
                            mostSimilarMainKey = possibleSimilar;
                            atomFlag = true;
                        }
                    }
                }
                // Case 1: Print the most similar molecule found based on number of atoms and exact elements
                // If there is multiple molecules with this criteria, we arbitrarily pick a molecule from this set
                if (atomFlag == true) {
                    for (String mostSimilarMolecule : data.get(mostSimilarMainKey).values()) {
                        System.out.println("Similar molecule found: " + mostSimilarMolecule);
                        return;
                    }
                }
                // Case 2: There is no molecule that has the exact elements of the input molecule, so there is no similar molecule
                System.out.println("Similar molecule not found");
                return;
            } else {
                // Case 3: There is a chemical formula in database, and we do a graph isomorphism test until we find the exact molecule
                for (Graph<String, DefaultEdge> possibleMatch: data.get(mainKey).keySet()) {
                    equal_graphs one = new equal_graphs(key2, possibleMatch);
                    if (one.check_SG()) {
                        System.out.println("Similar molecule found: " + data.get(mainKey).get(possibleMatch));
                        return;
                    }
                }
                // Case 4: There is a chemical formula in database, but the exact molecule isn't detected.
                // We then arbitrarily pick a molecule from the set
                for (String mostSimilarMolecule : data.get(mostSimilarMainKey).values()) {
                    System.out.println("Similar molecule found: " + mostSimilarMolecule);
                    return;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            //return null;
        } catch (IOException ex) {
            System.out.println("IO");
            //return null;
        }

    }

    // A function for producing database statistics
    public int[] database_statistics(){
        int statistics[] = new int[2];
        int number_of_entries;
        number_of_entries = data.values().size();

        int size_of_db_file;
        File file_for_sizing = new File("hashmap.ser");
        size_of_db_file = (int) file_for_sizing.length();

        statistics[0] = number_of_entries;
        statistics[1] = size_of_db_file;
        return statistics;
    }

    //Custom Class to assist in the branch pruning of recursive call in equal_graphs
    public static class molecularProperty implements Serializable {
        HashMap<String, Integer> molecularFormula;
        int numEdges;
        int numAtoms;

        //constructor
        public molecularProperty() {
            molecularFormula = new HashMap<>();
            numEdges = 0;
        }

        public molecularProperty(HashMap<String, Integer> inputFormula, int inputEdges, int inputAtoms) {
            molecularFormula = inputFormula;
            numEdges = inputEdges;
            numAtoms = inputAtoms;
        }

        //Since we are using molecularProperty as hashmap data's key we have to overwrite the equals fcn and the hashcode function to ensure it is hashing properly
        @Override
        public boolean equals(Object b){
            if(b == null) return false;
            if(!(b instanceof molecularProperty)) return false;
            if(b == this ) return true;
            if(numEdges == ((molecularProperty) b).numEdges && numAtoms == ((molecularProperty) b).numAtoms &&molecularFormula.equals(((molecularProperty) b).molecularFormula)){
                return true;
            }else return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(numEdges, molecularFormula,numAtoms);
        }
    }

}
