/**
 * Represents a bear in the river ecosystem.
 * A bear lives at most to the end of its 9th year (age 0-9, 10 cycles).
 * Strength varies with age: {1,2,3,4,5,4,3,2,1,0} for ages 0-9.
 *
 * @author Ali Kablanbek
 * @version 2/23/2026
 */
public class Bear extends Animal {

    public static final int MAX_AGE = 9;

    private static final int[] STRENGTH = {1, 2, 3, 4, 5, 4, 3, 2, 1, 0};

    /**
     * Creates a bear with a random age (0 to MAX_AGE) and random sex.
     */
    public Bear() {
        super();
        this.age = (int)(Math.random() * (MAX_AGE + 1)); // 0..9
    }

    /**
     * Creates a bear with the specified age and sex.
     *
     * @param age the age of the bear (0 to MAX_AGE)
     * @param sex the sex of the bear
     */
    public Bear(int age, Sex sex) {
        super(age, sex);
    }

    /**
     * Returns the current strength of the bear based on its age.
     *
     * @return the strength value
     */
    public int getStrength() {
        if (age < 0 || age > MAX_AGE) return 0;
        return STRENGTH[age];
    }

    /**
     * Returns true if this bear has reached its maximum age (9).
     *
     * @return true if age == MAX_AGE
     */
    @Override
    boolean maxAge() {
        return age >= MAX_AGE;
    }

    /**
     * Increments the bear's age by one if it has not yet reached MAX_AGE.
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