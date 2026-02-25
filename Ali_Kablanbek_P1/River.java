/**
 * Models a river ecosystem as a circular one-dimensional array of Animal objects.
 * Cells may contain a Bear, a Fish or null (empty).
 *
 * @author Ali Kablanbek
 * @version 2/23/2026
 */
public class River {

    // The array of animals (or null) representing the river.
    public Animal[] river;

    /**
     * Generates a random river ecosystem of the given length.
     * Each cell is independently filled: k=0 -> null, k=1 -> Fish, k=2 -> Bear.
     * Sex and age of each animal are chosen uniformly at random within valid ranges.
     *
     * @param length the number of cells in the river
     */
    public River(int length) {
        river = new Animal[length];
        for (int i = 0; i < length; i++) {
            int k = (int)(Math.random() * 3); // 0, 1 or 2
            if (k == 0) {
                river[i] = null;
            } else if (k == 1) {
                river[i] = new Fish();
            } else {
                river[i] = new Bear();
            }
        }
    }

    /**
     * Returns the length of the river.
     *
     * @return the number of cells
     */
    public int getLength() {
        return river.length;
    }

    /**
     * Returns the number of empty (null) cells in the river.
     *
     * @return count of null cells
     */
    public int numEmpty() {
        int count = 0;
        for (Animal a : river) {
            if (a == null) count++;
        }
        return count;
    }

    /**
     * Adds a new animal of age 0, randomly chosen sex and same type as the given animal
     * to a randomly chosen empty cell. Does nothing and returns false if no empty cells exist.
     *
     * @param animal the animal whose type (Bear or Fish) will be used for the new animal
     * @return true if an animal was added, false if the river is full
     */
    public boolean addRandom(Animal animal) {
        int empty = numEmpty();
        if (empty == 0) return false;

        // Pick a random empty cell index
        int target = (int)(Math.random() * empty);
        int count = 0;
        for (int i = 0; i < river.length; i++) {
            if (river[i] == null) {
                if (count == target) {
                    // Create a new animal of the same type, age 0, random sex
                    Animal.Sex newSex = ((int)(Math.random() * 2) == 0)
                            ? Animal.Sex.FEMALE : Animal.Sex.MALE;
                    if (animal instanceof Bear) {
                        river[i] = new Bear(0, newSex);
                    } else {
                        river[i] = new Fish(0, newSex);
                    }
                    return true;
                }
                count++;
            }
        }
        return false;
    }

    /**
     * Processes the animal at cell i for one simulation step.
     * Handles movement, "combat" and reproduction according to the rules.
     * Aging and lifespan death are handled by updateRiver before this is called.
     *
     * @param i            the index of the cell to update
     * @param alreadyMoved boolean array; set to true at the destination whenever an animal
     *                     moves successfully, so updateRiver can skip re-processing it
     */
    public void updateCell(int i, boolean[] alreadyMoved) {
        // Nothing to do for an empty cell
        if (river[i] == null) return;

        Animal current = river[i];

        // Choose a direction: 0 = stay, 1 = left, 2 = right
        int direction = (int)(Math.random() * 3);
        if (direction == 0) return; // stay in place

        int n = river.length;
        int target;
        if (direction == 1) {
            target = (i - 1 + n) % n; // left (circular)
        } else {
            target = (i + 1) % n;     // right (circular)
        }

        Animal neighbor = river[target];

        if (neighbor == null) {
            // Rule 1: empty cell - simply move
            river[target] = current;
            river[i] = null;
            alreadyMoved[target] = true;
        } else if ((current instanceof Bear && neighbor instanceof Fish)
                || (current instanceof Fish && neighbor instanceof Bear)) {
            // Rule 2: bear and fish collide - fish dies
            if (current instanceof Bear) {
                // Bear moves into fish's cell; fish dies
                river[target] = current;
                river[i] = null;
                alreadyMoved[target] = true;
            } else {
                // Fish tries to move into bear's cell; fish dies
                river[i] = null;
            }
        } else if (current.getClass() == neighbor.getClass()) {
            // Same species
            if (current.sex == neighbor.sex) {
                // Same sex
                if (current instanceof Fish) {
                    // Rule 3a: both stay nothing happens
                } else {
                    // Bears, same sex
                    Bear bCurr = (Bear) current;
                    Bear bNeigh = (Bear) neighbor;
                    if (bCurr.getStrength() == bNeigh.getStrength()) {
                        // Rule 3b: same strength - both stay
                    } else {
                        // Rule 3c: weaker one dies
                        if (bCurr.getStrength() < bNeigh.getStrength()) {
                            // current (moving bear) dies
                            river[i] = null;
                        } else {
                            // neighbor dies; current takes its place
                            river[target] = current;
                            river[i] = null;
                            alreadyMoved[target] = true;
                        }
                    }
                }
            } else {
                // Rule 4: same species, different sex —> both stay, new animal born
                addRandom(current);
            }
        }
    }

    /**
     * Performs one full cycle of the simulation:
     * 1. All animals age by one year; those that exceed their lifespan die.
     * 2. Cells are processed from left to right (updateCell).
     *    An alreadyMoved[] array prevents any animal from being processed twice
     *    in the same cycle (e.g. after moving right or wrapping around circularly).
     */
    public void updateRiver() {
        int n = river.length;

        // Step 1: Age all animals; remove those that have exceeded their lifespan
        for (int i = 0; i < n; i++) {
            if (river[i] != null) {
                boolean alive = river[i].incrAge();
                if (!alive) {
                    // incrAge returned false: animal was already at max age and dies
                    river[i] = null;
                }
            }
        }

        // Step 2: Process each cell left to right, skipping cells already moved into
        boolean[] alreadyMoved = new boolean[n]; // all false initially
        for (int i = 0; i < n; i++) {
            if (!alreadyMoved[i]) {
                updateCell(i, alreadyMoved);
            }
        }
    }

    /**
     * Returns a string representation of the river.
     * Each cell is either "---" (null) or a 3 character string like "BF7".
     * Cells are separated by single spaces.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < river.length; i++) {
            if (i > 0) sb.append(" ");
            if (river[i] == null) {
                sb.append("---");
            } else {
                sb.append(river[i].toString());
            }
        }
        return sb.toString();
    }
}