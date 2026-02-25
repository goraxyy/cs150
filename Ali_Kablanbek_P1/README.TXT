PROJECT TITLE:
  ALi_Kablanbek_P1

PURPOSE OF PROJECT:
  This project simulates a river ecosystem populated by bears and fish.
  The river is modeled as a circular, one-dimensional array of cells.
  Each cell holds a Bear, a Fish or is empty (null). The simulation
  runs for a user-specified number of cycles. In each cycle, all animals
  age by one year (those that exceed their species lifespan die) and
  then each animal independently chooses to stay put, move left or move
  right. Collisions between animals are resolved according to a set of
  rules: bears kill fish on contact, same-sex same-species animals may
  fight or do nothing and opposite-sex same-species animals reproduce,
  placing a new animal of age 0 in a random empty cell.

  The goal is to demonstrate object-oriented design in Java, including
  inheritance (Animal -> Bear / Fish), polymorphism, and simulation logic.

VERSION or DATE:
  2/23/2026

HOW TO START THIS PROJECT:
  Step 1 - Compile:
    Open a terminal, navigate to the folder containing the .java files,
    and run:
        javac Animal.java Fish.java Bear.java River.java RiverSimulator.java RiverTest.java

  Step 2 - Run the simulator: java RiverSimulator

  Step 3 - Run the unit tests: java RiverTest

AUTHORS:
  Ali Kablanbek

USER INSTRUCTIONS:
  When you launch RiverSimulator, the program immediately begins
  creating a random river. Follow the on-screen prompts:

  1. Enter the river length (a positive integer, e.g. 12).
     This determines how many cells the river array will have.

  2. Enter the number of cycles to simulate (a positive integer, e.g. 10).
     If you enter zero or a negative number the program will keep asking
     until you provide a valid positive value.

  3. The program prints the initial river, then prints the state of the
     river after each cycle. Each cell is displayed as a 3-character code:
       - "---"  means the cell is empty.
       - "BF4"  means a female Bear of age 4.
       - "BM7"  means a male Bear of age 7.
       - "FF2"  means a female Fish of age 2.
       - "FM0"  means a male Fish of age 0.
     Format key: [B=Bear / F=Fish] [F=Female / M=Male] [age digit]

  4. After the simulation finishes, the main menu appears:
       River Ecosystem Simulator
       Please choose: 1 (random river) 2 (exit)
     Enter 1 to run another simulation, or 2 to quit.

  Animal lifespan reference:
    Fish — lives up to the end of its 5th year (6 cycles, ages 0–5).
    Bear — lives up to the end of its 9th year (10 cycles, ages 0–9).

  Bear strength by age:
    Age:      0  1  2  3  4  5  4  3  2  1  0
    Strength: 1  2  3  4  5  4  3  2  1  0
    (When two same-sex bears collide, the weaker one dies; equal strength
     means both stay.)
--------------------
     EXPERIENCE
--------------------
     - the double-processing bug, the early death bug, the wrong Fish MAX_AGE, the output format
     mismatches.
     - everything should be fine, as far as I am concerned
     - I've learned to start working on a project early and put effort consistently