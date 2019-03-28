# EC504 MoleculeDB

Group 8



Group Members: Andrew Stoycos, Eric Li, David Payne, Matthew Hormis, Jacob Zweig



## Prototype



Our working prototype is a HashMap database with command line functionality. The main script will initialize the database, and will wait for the user to type an add or search function. For adding a molecule, the program will read a text file name (via command), and parse through the lines of the text files. Based on the format given on the requirements, the first line will be the molecule name, which is the value of the HashMap. The rest of the lines (until end-of-file) will be used to create a multigraph, which is the key of the HashMap. This key-value pair will be stored into the data structure through a built-in “put” function. For finding a molecule, we read through a command line given text file to create a multigraph (for now we are just using the same text files for add and find, so we will ignore the first line when reading in the file). The multigraph will used to search the HashMap through a built-in “get” function. If there is a value in the HashMap that corresponds to this multigraph, the program will output the name of the unknown molecule.




## Getting Started



1. Choose a location or create a folder to clone the repository. Open up a terminal and "cd" to your location. To clone, type in the terminal:


```

git clone https://agile.bu.edu/bitbucket/scm/ec504proj/group8.git

```


2. In another terminal tab, open the IntelliJ IDE but typing

```

IntelliJ

```


3. Select import project, and find the "moleculeDB" folder that was cloned to your computer. Please select moleculeDB folder, NOT the group8 folder. Press OK to continue.


4. There's no need for any extra feature yet, so keep clicking next to continue. Please make sure you are using Java JDK 1.8.0.


5. On the top right, go to File > Project Structure > Global Libraries. Click the green plus sign in the middle column > "From maven..." to add a new global library.


6. In the search bar, type the following and press search (may take some seconds): 


```

org.jgrapht

```


7. Find and select "org.jgrapht:jgrapht-core:1.3.0" in the dropdown options and press OK to add.


8. Repeat steps 5 - 7 again (same search), but look for and add "org.jgrapht:jgrapht-io:1.3.0". 


9. On the bottom right of the project structure, press Apply and then OK.


10. Build the project (Ctrl + F9).


11. Look through the project directory on the left side of the IDE, and find the Main.java file. Right click this file and select 'Run Main.main()'. In the main script, water and ammonia files have already been added to the database. There was also a search for the water molecule. 


12. To list out the molecules in the database, type in the bottom command terminal:

```

