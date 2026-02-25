import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the River Ecosystem simulation classes.
 * Tests Animal, Fish, Bear, and River functionality.
 *
 * @author Ali Kablanbek
 * @version 2/23/2026
 */

public class RiverTest {

    // Fish tests

    @Test
    public void testFishCreation() {
        Fish f = new Fish(2, Animal.Sex.FEMALE);
        assertEquals(2, f.getAge(), "Fish age set correctly");
        assertEquals(Animal.Sex.FEMALE, f.getSex(), "Fish sex set correctly");
        assertTrue(f.toString().startsWith("F"), "Fish toString starts with F");

        Fish rf = new Fish();
        assertTrue(rf.getAge() >= 0 && rf.getAge() <= 4, "Random fish age in [0,4]");
    }

    // Bear tests

    @Test
    public void testBearCreation() {
        Bear b = new Bear(5, Animal.Sex.MALE);
        assertEquals(5, b.getAge(), "Bear age set correctly");
        assertEquals(Animal.Sex.MALE, b.getSex(), "Bear sex set correctly");
        assertTrue(b.toString().startsWith("B"), "Bear toString starts with B");

        Bear rb = new Bear();
        assertTrue(rb.getAge() >= 0 && rb.getAge() <= 9, "Random bear age in [0,9]");
    }

    // Fish aging

    @Test
    public void testFishAging() {
        Fish f = new Fish(3, Animal.Sex.FEMALE);
        assertTrue(f.incrAge(), "Fish incrAge returns true when not at max");
        assertEquals(4, f.getAge(), "Fish age incremented to 4");

        assertTrue(f.incrAge(), "Fish incrAge returns true going from 4 to 5");
        assertEquals(5, f.getAge(), "Fish age incremented to 5");

        assertTrue(f.maxAge(), "Fish maxAge() true at age 5");

        assertFalse(f.incrAge(), "Fish incrAge returns false at max age");
        assertEquals(5, f.getAge(), "Fish age stays at 5 when at max");
    }

    // Bear aging

    @Test
    public void testBearAging() {
        Bear b = new Bear(8, Animal.Sex.MALE);
        assertTrue(b.incrAge(), "Bear incrAge returns true from 8");
        assertEquals(9, b.getAge(), "Bear age incremented to 9");

        assertTrue(b.maxAge(), "Bear maxAge() true at age 9");
        assertFalse(b.incrAge(), "Bear incrAge returns false at max");
        assertEquals(9, b.getAge(), "Bear age stays at 9");
    }

    // Bear strength

    @Test
    public void testBearStrength() {
        int[] expectedStrengths = {1, 2, 3, 4, 5, 4, 3, 2, 1, 0};
        for (int age = 0; age <= 9; age++) {
            Bear b = new Bear(age, Animal.Sex.FEMALE);
            assertEquals(expectedStrengths[age], b.getStrength(),
                "Bear strength at age " + age);
        }
    }

    // toString

    @Test
    public void testAnimalToString() {
        assertEquals("FF2", new Fish(2, Animal.Sex.FEMALE).toString(), "Fish FF2 toString");
        assertEquals("FM0", new Fish(0, Animal.Sex.MALE).toString(),   "Fish FM0 toString");
        assertEquals("BF7", new Bear(7, Animal.Sex.FEMALE).toString(), "Bear BF7 toString");
        assertEquals("BM3", new Bear(3, Animal.Sex.MALE).toString(),   "Bear BM3 toString");
    }

    // River length

    @Test
    public void testRiverLength() {
        River r = new River(10);
        assertEquals(10, r.getLength(), "River length is 10");
    }

    // numEmpty

    @Test
    public void testRiverNumEmpty() {
        River r = new River(5);
        for (int i = 0; i < 5; i++) r.river[i] = new Fish(0, Animal.Sex.FEMALE);
        assertEquals(0, r.numEmpty(), "numEmpty is 0 when all cells filled");

        r.river[2] = null;
        assertEquals(1, r.numEmpty(), "numEmpty is 1 after clearing one cell");
    }

    // addRandom

    @Test
    public void testRiverAddRandom() {
        River r = new River(3);
        r.river[0] = new Bear(0, Animal.Sex.MALE);
        r.river[1] = new Fish(0, Animal.Sex.FEMALE);
        r.river[2] = new Bear(1, Animal.Sex.FEMALE);

        assertFalse(r.addRandom(new Bear(0, Animal.Sex.MALE)),
            "addRandom returns false when river is full");

        r.river[1] = null;
        assertTrue(r.addRandom(new Fish(0, Animal.Sex.MALE)),
            "addRandom returns true when space available");
        assertTrue(r.river[1] instanceof Fish, "addRandom placed a Fish");
        assertEquals(0, r.river[1].getAge(), "addRandom fish has age 0");
    }

    // River toString

    @Test
    public void testRiverToString() {
        River r = new River(3);
        r.river[0] = new Bear(5, Animal.Sex.FEMALE);
        r.river[1] = null;
        r.river[2] = new Fish(2, Animal.Sex.MALE);
        assertEquals("BF5 --- FM2", r.toString(), "River toString format correct");
    }

    // update cycle

    @Test
    public void testRiverUpdateCycle() {
        River r = new River(5);
        for (int i = 0; i < 5; i++) r.river[i] = null;
        r.river[2] = new Bear(9, Animal.Sex.MALE);
        r.updateRiver();
        assertNull(r.river[2], "Bear at max age dies after cycle");

        River r2 = new River(1);
        r2.river[0] = new Fish(0, Animal.Sex.FEMALE);
        r2.updateRiver();
        assertNotNull(r2.river[0], "Fish is still alive after one cycle");
        assertEquals(1, r2.river[0].getAge(), "Fish ages from 0 to 1 after cycle");
    }
}