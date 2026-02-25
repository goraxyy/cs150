/**
 * Represents a fish in the river ecosystem.
 * A fish lives at most to the end of its 5th year (age 0-5, 6 simulation cycles).
 *
 * @author Ali Kablanbek
 * @version 2/23/2026
 */
public class Fish extends Animal {

    /**
     * Maximum age for a fish (inclusive).
     * A fish lives to the end of its 5th year (ages 0-5 = 6 simulation cycles).
     * Random initialization only uses 0-4 to avoid fish that die immediately.
     */
    public static final int MAX_AGE = 5;

    /**
     * Creates a fish with a random age (0 to MAX_AGE) and random sex.
     */
    public Fish() {
        super();
        this.age = (int)(Math.random() * (MAX_AGE + 1)); // 0..5 inclusive, per spec
    }

    /**
     * Creates a fish with the specified age and sex.
     *
     * @param age the age of the fish (0 to MAX_AGE)
     * @param sex the sex of the fish
     */
    public Fish(int age, Sex sex) {
        super(age, sex);
    }

    /**
     * Returns true if this fish has reached its maximum age (5).
     *
     * @return true if age >= MAX_AGE
     */
    @Override
    boolean maxAge() {
        return age >= MAX_AGE;
    }

    /**
     * Increments the fish's age by one if it has not yet reached MAX_AGE.
     *
     * @return true if age was incremented, false otherwise
     */
    @Override
    boolean incrAge() {
        if (age < MAX_AGE) {
            age++;
            return true;
        }
        return false;
    }
}