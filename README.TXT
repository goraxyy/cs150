------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all they need to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE: Transport Simulation

PURPOSE OF PROJECT: Simulate trucks picking up and delivering shipments
between warehouses on a 2D map using custom data structures and a
central clock system.

VERSION or DATE: 5/15/2026

HOW TO START THIS PROJECT:
1. Open BlueJ and create a new project.
2. Copy all .java files into the project folder.
3. Click Compile All in BlueJ.
4. Right-click the Main class and run void main(String[] args).
   - Leave args empty to use the default seed (42) or enter a number for a different world.
5. Output files will appear in the project folder:
   - config.txt: the generated simulation world
   - simulation.log: human-readable event log
   - simulation_data.csv: machine-readable event data

To run unit tests right-click TransportSimulationTest and call runAll().

AUTHORS: Ali Kablanbek

USER INSTRUCTIONS:
- On the first run the program generates a random world and saves it to config.txt.
- On subsequent runs it reads the same config.txt so results are reproducible.
- To get a different world delete config.txt and rerun with a different seed argument.
- Do not edit config.txt manually unless you follow the exact format shown in the comments at the top of the file.
- Check simulation.log for a readable trace of every event and check simulation_data.csv for data analysis.


Reflection

(a) Functionalities Planned

- Randomly generate warehouses, trucks and shipments and save to a config file
- Read the config file and reconstruct all objects at the start of each run
- Simulate truck movement across a 2D map using Euclidean distance
- Implement LIFO cargo loading and unloading on trucks
- Implement a priority-ordered pickup manifest (distance ascending,
  shipment ID descending)
- Model warehouse loading docks with a FIFO arrival queue
- Drive the simulation with a central SimClock that ticks hour by hour
- Write a human-readable simulation.log and a machine-readable simulation_data.csv
- Implement all data structures from scratch with no Java API collections
- Unit test as many classes as possible

(b) Functionalities Finished

All planned features were implemented and are working:
- Random world generation with seed support and reproducible config.txt
- Full config file parsing with error messages that include line numbers
- Truck movement using Euclidean geometry with no overshoot
- LIFO cargo stack using a custom CustomStack<T> backed by
  CustomLinkedList<T>
- Manifest priority queue using a custom min-heap CustomPriorityQueue<T>
- Warehouse dock management with FIFO CustomQueue<T> arrival queue
- SimClock driving warehouses and trucks each hour via the Schedule
  interface
- Logger and CSVWriter singletons writing formatted output to files
- 50+ unit tests across all major classes in TransportSimulationTest

(c) Challenges and Unfinished Work

Nothing was left unfinished but a few things were harder than expected:

- Manifest reordering was tricky because the priority changes as the
  truck moves. The solution was to extract all entries, update their
  distances and re-insert them into the heap each time reorderManifest()
  is called. A cleaner fix would be a lazy-update heap that marks stale
  entries instead of rebuilding but that was out of scope.
- Dock assignment timing caused a bug early on where trucks would try
  to load before the warehouse assigned them a dock. This was fixed by
  always running Warehouse.action() before Truck.action() inside each
  tick.
- Logger and CSVWriter singletons do not reset between runs if the JVM
  is not restarted. A reset() method was added to handle this for
  testing purposes.

(d) Lessons Learned

- Building data structures from scratch makes it very clear why the
  Java API exists. Writing a doubly-linked list, stack, queue and heap
  by hand took a lot longer than expected but made the concepts click
  in a way that just using ArrayList never would.
- Separating concerns into small classes such as LoadingDock separate
  from Warehouse and ManifestEntry separate from Shipment made
  debugging much easier since each class had one job.
- The singleton pattern for Logger and CSVWriter is convenient but
  causes problems during unit testing because state leaks between
  tests. In the future dependency injection would be a better approach.
- Writing unit tests first or at least alongside the code would have
  caught the dock timing bug much earlier.