package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.io.*;

// Uses Swing to build a basic Graphical User Interface (GUI)

public class gui extends JFrame implements ActionListener {
    JTextArea text_area;
    JButton button_add;
    JButton button_find;
    JButton button_statistics;
    JFrame frame;
    isodatabase DB;

    // Constructor. Takes in a database so it can call on those functions
    public gui(isodatabase DB_init) {
        DB = DB_init;
        frame = new JFrame("MoleculeDB GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        // Creates a button for each of the three primary functions
        button_add = new JButton("Add Molecule");
        button_find = new JButton("Find Molecule");
        button_statistics = new JButton("Show Database Statistics");

        // Places those buttons right, left, and center
        frame.add(button_add, BorderLayout.EAST);
        frame.getContentPane().add(button_find, BorderLayout.WEST);
        frame.add(button_statistics, BorderLayout.CENTER);

        // Puts a listener on each button so that an action can be triggered
        button_add.addActionListener(this);
        button_find.addActionListener(this);
        button_statistics.addActionListener(this);

        text_area = new JTextArea(800, 800);
        text_area.setBounds(0, 20, 800, 800);
        add(text_area);
        frame.setVisible(true);
    }

    // Creates the actions
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button_add) {
            JFileChooser fc = new JFileChooser();
            int i = fc.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                String filepath = f.getPath();
                try {

                    if(DB.addCompound(filepath)==true) {
                        JOptionPane.showMessageDialog(null, "Compound Added", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Compound already in there", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        else if (e.getSource() == button_find) {
            JFileChooser fc = new JFileChooser();
            int i = fc.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                String filepath = f.getPath();
                try {
                    // Displays result of find
                    if(DB.findCompound(filepath, false)==false){
                        JOptionPane.showMessageDialog(null, "Compound NOT Found", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Compound Found", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        else if (e.getSource() == button_statistics) {
            int number_of_entries;
            // Shows how many compounds are in the database
            number_of_entries = DB.database_statistics();
            try {
                JOptionPane.showMessageDialog(null, "There are " + number_of_entries + " compounds in database", "Statistics", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
