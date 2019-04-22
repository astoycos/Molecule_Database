package com.company;

import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.*;

public class isodatabase {
    static HashMap<molecularProperty,HashMap<Graph<String, DefaultEdge>,String>> data;

    public isodatabase() {
        data = new HashMap<>();
    }

        public static void saveDB() {
        try {
            FileOutputStream filestream = new FileOutputStream("hashmap.ser");
            ObjectOutputStream outputstream = new ObjectOutputStream(filestream);
            outputstream.writeObject(data);
            outputstream.close();
            filestream.close();
            System.out.printf("HashMap serialized");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void openDB() {
        try {
            FileInputStream filestream = new FileInputStream("hashmap.ser");
            ObjectInputStream inputstream = new ObjectInputStream(filestream);
            data = (HashMap) inputstream.readObject();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex2) {
            ex2.printStackTrace();
        }

    }

    //Class to use backtracking to check for isomorphism

    class equal_graphs {

        boolean found;
        boolean eob;
        Vector<Pair<java.lang.Object, java.lang.Object>> Mapping;
        Graph<String, DefaultEdge> G;
        Graph<String, DefaultEdge> SG;

        public equal_graphs(Graph<String, DefaultEdge> SG2, Graph<String, DefaultEdge> G2){
            G = G2;
            SG = SG2;
            found = false;
            eob = false;
            Mapping = new Vector<>();

        }
        public boolean check_SG() {
            // boolean found = false;

            Set verticesSG = SG.vertexSet();
            Set verticesG = G.vertexSet();

            Iterator A = verticesSG.iterator();
            Iterator B = verticesG.iterator();
            //while(A.hasNext()){
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

            //}

        }

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


    public void findCompound(String TextFile, boolean is_Hash) {
        String linefind = null;
        int count = 0;
        int numberAtoms = 0;
        int numberBonds = 0;
        HashMap<String, Integer> formula = new HashMap<>();
        Vector<String> key = new Vector<>();
        Graph<String, DefaultEdge> key2 = new Multigraph<>(DefaultEdge.class);

        System.out.println("Searching for: "+ TextFile);
        try {
            BufferedReader read = new BufferedReader(new FileReader(TextFile));
            while ((linefind = read.readLine()) != null) {
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


            Iterator it = data.keySet().iterator();

            /*
            for(molecularProperty name: data.keySet()) {

                System.out.println(name.equals(mainKey));
                System.out.println(data.get(mainKey));
                System.out.println(data.get(name));
                System.out.println(name.numEdges);
                System.out.println(name.molecularFormula);
            }

            /*
            System.out.println("Formula is: ");
            System.out.println(formula);

            System.out.println("numbonds is: ");
            System.out.println(numberBonds);

            System.out.println("Main key is: ");
            System.out.println(mainKey);

            System.out.println("Edgeset is: ");
            System.out.println(key2.edgeSet());
            System.out.println("Vertexset is: ");
            System.out.println(key2.vertexSet());
            */


            if (data.get(mainKey) == null) {
                System.out.println("Molecule not found");
                //return null;
            } else {
                for (Graph<String, DefaultEdge> possibleMatch: data.get(mainKey).keySet()) {
                    equal_graphs one = new equal_graphs(key2, possibleMatch);
                    if(one.check_SG()){
                        System.out.println("Graph " + TextFile + " found in: " + data.get(mainKey).get(possibleMatch));
                        return;
                    }

                }
                System.out.println("Graph " + TextFile + " not found in MoleculeDB ");

            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            //return null;
        } catch (IOException ex) {
            System.out.println("IO");
            //return null;
        }
    }

    public void findSubgraph(String Textfile) {
        String linefind = null;
        int count = 0;
        int numberAtoms = 0;
        int numberBonds = 0;
        HashMap<String, Integer> formula = new HashMap<>();
        Vector<String> key = new Vector<>();
        Graph<String, DefaultEdge> key2 = new Multigraph<>(DefaultEdge.class);

        System.out.println("Searching for: " + Textfile);
        try {
            BufferedReader read = new BufferedReader(new FileReader(Textfile));
            while ((linefind = read.readLine()) != null) {
                if (count == 1) {
                    numberAtoms = Integer.valueOf(linefind);
                } else if (count >= 2) {
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

            for(molecularProperty name: data.keySet()) {

                //System.out.println(name.equals(mainKey));
                //System.out.println(data.get(mainKey));
                //System.out.println(data.get(name));
                //System.out.println(name.numEdges);
                //System.out.println(name.molecularFormula);

                if(name.numEdges >= numberBonds && name.numAtoms >= numberAtoms) {
                    for (String element : formula.keySet()) {
                        if(name.molecularFormula.keySet().contains(element)){
                            bonds_and_elements = true;
                        }else bonds_and_elements = false;
                    }
                }

                if(bonds_and_elements){
                    for (Graph<String, DefaultEdge> possibleMatch: data.get(name).keySet()) {
                        equal_graphs one = new equal_graphs(key2, possibleMatch);
                        if(one.check_SG()){
                            System.out.println("Subgraph " + Textfile +" found in: " + data.get(name).get(possibleMatch));
                        }
                    }
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


    public static void addCompound(String TextFile) {
        String linefind = null;
        int count = 0;
        int numberAtoms = 0;
        int numberBonds = 0;
        String moleculeName = null;
        HashMap<String, Integer> formula = new HashMap<>();
        Vector<String> key = new Vector<>();
        Graph<String, DefaultEdge> key2 = new Multigraph<>(DefaultEdge.class);

        try {
            BufferedReader read = new BufferedReader(new FileReader(TextFile));
            while ((linefind = read.readLine()) != null) {
                if (count == 0) {
                    moleculeName = linefind;
                } else if (count == 1) {
                    numberAtoms = Integer.valueOf(linefind);
                } else {
                    key.addElement(linefind + (count - 2));
                    if (count > 0 & count <= numberAtoms + 1) {
                        Integer currentCount = formula.get(linefind);
                        key2.addVertex(linefind + (count - 2));
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


            if (data.get(mainKey) == null) {

                /*
                System.out.println("Molecule " + moleculeName + " added");
                System.out.println("Main key is: ");
                System.out.println(mainKey);

                System.out.println("Edgeset is: ");
                System.out.println(key2.edgeSet());
                System.out.println("Vertexset is: ");
                System.out.println(key2.vertexSet());
                */
                //System.out.println(formula);
                System.out.println("Molecule " + moleculeName + " added");
                secondLayer.put(key2,moleculeName);

                data.put(mainKey,secondLayer);

                return;
            } else {
                /*
                for (Graph<String, DefaultEdge> possibleMatch: data.get(mainKey).keySet()) {
                    if (backtrackSearch(possibleMatch, key2) == true) {

                        System.out.println(data.get(mainKey).get(possibleMatch));
                        return;
                    }
                }
                System.out.println("Molecule " + moleculeName + " added");
                data.get(mainKey).put(key2, moleculeName);
                */
                System.out.println("Molecule " + moleculeName + " already there");
                return;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            return;
        } catch (IOException ex) {
            System.out.println("IO");
            return;
        }

    }

    public void findMostSimilar(String TextFile) {
        String linefind = null;
        int count = 0;
        int numberAtoms = 0;
        int numberBonds = 0;
        HashMap<String, Integer> formula = new HashMap<>();
        Vector<String> key = new Vector<>();
        Graph<String, DefaultEdge> key2 = new Multigraph<>(DefaultEdge.class);
        try {
            BufferedReader read = new BufferedReader(new FileReader(TextFile));
            while ((linefind = read.readLine()) != null) {
                if (count == 1) {
                    numberAtoms = Integer.valueOf(linefind);
                } else if (count >= 2) {
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
            molecularProperty mostSimilarMainKey = new molecularProperty();
            boolean atomFlag = false;
            int totalAtomDifferential = Integer.MAX_VALUE;
            int totalBondDifferential;
            if (data.get(mainKey) == null) {
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
                    //subgraph search?
                }
                if (atomFlag == true) {
                    for (String mostSimilarMolecule : data.get(mostSimilarMainKey).values()) {
                        System.out.println("Similar molecule found: " + mostSimilarMolecule);
                        return;
                    }
                }
                System.out.println("Similar molecule not found");
                return;
                //return null;
            } else {
                for (Graph<String, DefaultEdge> possibleMatch: data.get(mainKey).keySet()) {
                    equal_graphs one = new equal_graphs(key2, possibleMatch);
                    if (one.check_SG()) {
                        System.out.println("Similar molecule found: " + data.get(mainKey).get(possibleMatch));
                        return;
                    }
                }
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

    /*
    public void printDB(){
        for (String name: data.keySet()){

            String key =name.toString();
            String value = data.get(name);
            System.out.println(key + " " + value);


        }
    }
    */

    public void printkeys(){
        System.out.println(data.keySet());
    }

    public static class molecularProperty {
        HashMap<String, Integer> molecularFormula;
        int numEdges;
        int numAtoms;
//        int numRepeats;

        public molecularProperty() {
            molecularFormula = new HashMap<>();
            numEdges = 0;
        }

        public molecularProperty(HashMap<String, Integer> inputFormula, int inputEdges, int inputAtoms) {
            molecularFormula = inputFormula;
            numEdges = inputEdges;
            numAtoms = inputAtoms;
        }

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
