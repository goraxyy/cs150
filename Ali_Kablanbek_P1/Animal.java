/**
 * Abstract base class representing an animal in the river ecosystem.
 * Each animal has a sex and an age.
 *
 * @author Ali Kablanbek
 * @return 2/23/2026
 */
public abstract class Animal {

    protected enum Sex {
        FEMALE, MALE
    }

    protected Sex sex;

    protected int age;

    /**
     * Creates an animal with a random age and sex.
     * Age is bounded by the subclass's maximum age.
     */
    public Animal() {
        // Sex: 0 = Female, 1 = Male
        this.sex = (int)(Math.random() * 2) == 0 ? Sex.FEMALE : Sex.MALE;
        // Age will be set by subclass constructors using their max age
        this.age = 0;
    }

    /**
     * Creates an animal with the specified age and sex.
     *
     * @param age the age of the animal
     * @param sex the sex of the animal
     */
    public Animal(int age, Sex sex) {
        this.age = age;
        this.sex = sex;
    }

    /**
     * Returns the age of this animal.
     *
     * @return the current age
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns the sex of this animal.
     *
     * @return the sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Returns true if the animal's age has reached the maximum for its species.
     *
     * @return true if at max age, false otherwise
     */
    abstract boolean maxAge();

    /**
     * If the current age is less than the maximum, increments the age by one
     * and returns true. otherwise, leaves the age unchanged and returns false.
     *
     * @return true if age was incremented, false if already at max
     */
    abstract boolean incrAge();

    /**
     * Returns a string representation such as "BF7" for a 7-year-old female bear.
     * Format: [B/F][F/M][age]
     *
     * @return string representation of the animal
     */
    @Override
    public String toString() {
        String species = (this instanceof Bear) ? "B" : "F";
        String sexStr  = (sex == Sex.FEMALE)    ? "F" : "M";
        return species + sexStr + age;
    }
}