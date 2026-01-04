package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerCharacterTest {

    private PlayerCharacter ch1;
    private PlayerCharacter ch2;

    @BeforeEach
    void runBefore() {
        ch1 = new PlayerCharacter("A", "B", 1, "male");
        ch2 = new PlayerCharacter("C", "D", 2, "female");
    }

    @Test
    void testConstructor() {
        assertEquals("A", ch1.getName());
        assertEquals("B", ch1.getOccupation());
        assertEquals(1, ch1.getAge());
        assertEquals(0, ch1.getAttribute("STR"));
        assertEquals(0, ch1.getAttribute("POW"));
        assertEquals(0, ch1.getAttribute("SIZ"));
        assertEquals(0, ch1.getSkills().size());
    }

    @Test
    void testGetName() {
        assertEquals("A", ch1.getName());
        assertEquals("C", ch2.getName());

        ch1.setName("Barry");
        assertEquals("Barry", ch1.getName());

    }

    @Test
    void testGetGender() {
        assertEquals("male", ch1.getGender());
    }

    @Test
    void testGetOccupation() {
        assertEquals("B", ch1.getOccupation());
        assertEquals("D", ch2.getOccupation());
    }

    @Test
    void testGetAge() {
        assertEquals(1, ch1.getAge());
        assertEquals(2, ch2.getAge());
    }

    @Test
    void testGetAttribute() {
        assertEquals(0, ch1.getAttribute("STR"));
        assertEquals(0, ch1.getAttribute("CON"));
        assertEquals(0, ch2.getAttribute("HP"));

        ch1.setAttribute("CON", 50);
        assertEquals(50, ch1.getAttribute("CON"));
        assertEquals(5, ch1.getAttribute("HP"));
    }

    @Test
    void testGetAllAttributes() {
        Map<Attribute, Integer> testAttributes = new EnumMap<>(Attribute.class);
        for (Attribute attr : Attribute.values()) {
            testAttributes.put(attr, 0);
        }
        assertEquals(testAttributes, ch1.getAllAttributes());

        ch1.setAttribute("CON", 50);
        testAttributes.put(Attribute.CON, 50);
        testAttributes.put(Attribute.HP, 5);
        assertEquals(testAttributes, ch1.getAllAttributes());
    }

    @Test
    void testGetSkills() {
        Skill testSkill1 = new Skill("Listen", 20);
        Skill testSkill2 = new Skill("History", 50);
        Skill testSkill3 = new Skill("Search", 0);

        assertEquals(0, ch1.getSkills().size());
        assertEquals(0, ch2.getSkills().size());

        ch1.addSkill("Listen", 20);
        ch2.addSkill("History", 50);
        ch2.addSkill("Search", 0);

        assertEquals(1, ch1.getSkills().size());
        assertEquals(2, ch2.getSkills().size());

        assertEquals(testSkill1.getAddValue(), ch1.getSkills().get(0).getAddValue());
        assertEquals(testSkill1.getTotalValue(), ch1.getSkills().get(0).getTotalValue());
        assertEquals(testSkill1.getName(), ch1.getSkills().get(0).getName());

        assertEquals(testSkill2.getAddValue(), ch2.getSkills().get(0).getAddValue());
        assertEquals(testSkill2.getTotalValue(), ch2.getSkills().get(0).getTotalValue());
        assertEquals(testSkill2.getName(), ch2.getSkills().get(0).getName());

        assertEquals(testSkill3.getAddValue(), ch2.getSkills().get(1).getAddValue());
        assertEquals(testSkill3.getTotalValue(), ch2.getSkills().get(1).getTotalValue());
        assertEquals(testSkill3.getName(), ch2.getSkills().get(1).getName());
    }

    @Test
    void testSetName() {
        assertEquals("A", ch1.getName());

        ch1.setName("Barry");
        assertEquals("Barry", ch1.getName());
        ch1.setName("Kevin");
        assertEquals("Kevin", ch1.getName());
    }

    @Test
    void testSetGender() {
        assertEquals("male", ch1.getGender());
        ch1.setGender("Female");
        assertEquals("female", ch1.getGender());
    }

    @Test
    void testSetOccupation() {
        assertEquals("B", ch1.getOccupation());

        ch1.setOccupation("Writer");
        assertEquals("Writer", ch1.getOccupation());
        ch1.setOccupation("AAAA");
        assertEquals("AAAA", ch1.getOccupation());
    }

    @Test
    void testSetAge() {
        assertEquals(1, ch1.getAge());

        ch1.setAge(15);
        assertEquals(15, ch1.getAge());
        ch1.setAge(0);
        assertEquals(0, ch1.getAge());
    }

    @Test
    void testSetAttribute() {
        assertEquals(0, ch1.getAttribute("STR"));

        ch1.setAttribute("CON", 50);
        assertEquals(50, ch1.getAttribute("CON"));
        assertEquals(0, ch1.getAttribute("POW"));
        assertEquals(5, ch1.getAttribute("HP"));

        assertEquals(0, ch2.getAttribute("HP"));
        ch2.setAttribute("HP", 50);
        assertEquals(0, ch2.getAttribute("HP"));
    }

    @Test
    void testCalcDerivedAttr() {

        ch1.setAttribute("CON", 50);
        ch1.calcDerivedAttr();
        assertEquals(5, ch1.getAttribute("HP"));

        ch2.setAttribute("SIZ", 20);
        ch2.calcDerivedAttr();
        assertEquals(2, ch2.getAttribute("HP"));

        ch2.setAttribute("HP", 50);
        ch2.calcDerivedAttr();
        assertEquals(2, ch2.getAttribute("HP"));

        ch2.setAttribute("CON", 50);
        ch2.calcDerivedAttr();
        assertEquals(7, ch2.getAttribute("HP"));

        ch1.setAttribute("POW", 51);
        ch1.calcDerivedAttr();
        assertEquals(10, ch1.getAttribute("MP"));
        assertEquals(99, ch1.getAttribute("SAN"));
    }

    @Test
    void testAddSkill() {
        ch1.addSkill("Listen", 50);
        assertEquals(1, ch1.getSkills().size());
        assertEquals("Listen", ch1.getSkills().get(0).getName());
        assertEquals(50, ch1.getSkills().get(0).getAddValue());
        assertEquals(55, ch1.getSkills().get(0).getTotalValue());
        ch2.addSkill("Listen", 0);
        ch2.addSkill("Draw", 1);
        assertEquals(2, ch2.getSkills().size());
        assertEquals("Listen", ch2.getSkills().get(0).getName());
        assertEquals(0, ch2.getSkills().get(0).getAddValue());
        assertEquals(5, ch2.getSkills().get(0).getTotalValue());
        assertEquals("Draw", ch2.getSkills().get(1).getName());
        assertEquals(1, ch2.getSkills().get(1).getAddValue());
        assertEquals(6, ch2.getSkills().get(1).getTotalValue());
        ch2.addSkill("DRaw", 20);
        assertEquals(2, ch2.getSkills().size());
        assertEquals("Listen", ch2.getSkills().get(0).getName());
        assertEquals(0, ch2.getSkills().get(0).getAddValue());
        assertEquals(5, ch2.getSkills().get(0).getTotalValue());
        assertEquals("Draw", ch2.getSkills().get(1).getName());
        assertEquals(20, ch2.getSkills().get(1).getAddValue());
        assertEquals(25, ch2.getSkills().get(1).getTotalValue());
    }

    @Test
    void testDeleteSkill() {
        ch1.addSkill("Listen", 50);
        ch1.addSkill("Read", 0);
        assertEquals(2, ch1.getSkills().size());

        ch1.deleteSkill("Listen");
        assertEquals(1, ch1.getSkills().size());
        assertEquals("Read", ch1.getSkills().get(0).getName());

        ch1.deleteSkill("reading");
        assertEquals(1, ch1.getSkills().size());
        assertEquals("Read", ch1.getSkills().get(0).getName());

        ch1.deleteSkill("read");
        assertEquals(0, ch1.getSkills().size());
    }

    @Test
    void testFindSkill() {
        ch1.addSkill("Listen", 20);
        ch1.addSkill("Read", 0);
        Skill testSkill1 = new Skill("Listen", 20);
        Skill testSkill2 = new Skill("Read", 0);

        assertEquals(testSkill1.getName(), ch1.findSkill("Listen").getName());
        assertEquals(testSkill1.getAddValue(), ch1.findSkill("Listen").getAddValue());
        assertEquals(testSkill1.getTotalValue(), ch1.findSkill("Listen").getTotalValue());

        assertEquals(testSkill2.getName(), ch1.findSkill("read").getName());
        assertEquals(testSkill2.getAddValue(), ch1.findSkill("Read").getAddValue());
        assertEquals(testSkill2.getTotalValue(), ch1.findSkill("READ").getTotalValue());

        assertNull(ch1.findSkill("sing"));
    }

    @Test
    void testCheckHaveSkill() {
        ch1.addSkill("Listen", 50);
        ch2.addSkill("Listen", 0);
        ch2.addSkill("Draw", 1);

        assertFalse(ch1.checkHaveSkill("draw"));
        assertTrue(ch1.checkHaveSkill("LISTEN"));
        assertTrue(ch2.checkHaveSkill("Listen"));
        assertTrue(ch2.checkHaveSkill("Draw"));
        assertFalse(ch2.checkHaveSkill("Dance"));
    }
}
