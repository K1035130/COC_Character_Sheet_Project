package model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SkillTest {

    private Skill s1;
    private Skill s2;

    @BeforeEach
    void runBefore() {
        s1 = new Skill("Sk1");
        s2 = new Skill("Sk2");
    }

    @Test
    void testConstructor() {
        assertEquals(0, s1.getAddValue());
        assertEquals(5, s1.getTotalValue());
        assertEquals("Sk1", s1.getName());

        assertEquals(0, s2.getAddValue());
        assertEquals(5, s2.getTotalValue());
        assertEquals("Sk2", s2.getName());

        s1 = new Skill("A", 50);
        s2 = new Skill("B", 30);
        assertEquals(50, s1.getAddValue());
        assertEquals(55, s1.getTotalValue());
        assertEquals("A", s1.getName());

        assertEquals(30, s2.getAddValue());
        assertEquals(35, s2.getTotalValue());
        assertEquals("B", s2.getName());
    }

    @Test
    void testGetAddValue() {
        assertEquals(0, s1.getAddValue());
        assertEquals(0, s2.getAddValue());

        s1 = new Skill("A", 50);
        s2 = new Skill("B", 30);
        assertEquals(50, s1.getAddValue());
        assertEquals(30, s2.getAddValue());

        s1.setAddValue(1);
        s2.setAddValue(0);
        assertEquals(1, s1.getAddValue());
        assertEquals(0, s2.getAddValue());
    }

    @Test
    void testGetName() {
        assertEquals("Sk1", s1.getName());
        assertEquals("Sk2", s2.getName());

        s1 = new Skill("A", 50);
        s2 = new Skill("B", 30);
        assertEquals("A", s1.getName());
        assertEquals("B", s2.getName());

        s1.setName("C");
        assertEquals("C", s1.getName());

        s1.setName("");
        assertEquals("", s1.getName());
    }

    @Test
    void testGetTotalValue() {
        assertEquals(5, s1.getTotalValue());
        assertEquals(5, s2.getTotalValue());

        s1.setAddValue(1);
        s2.setAddValue(99);
        assertEquals(6, s1.getTotalValue());
        assertEquals(104, s2.getTotalValue());
    }

    @Test
    void testSetName() {
        assertEquals("Sk1", s1.getName());
        assertEquals("Sk2", s2.getName());
        s1.setName("C");
        assertEquals("C", s1.getName());

        s1.setName("");
        assertEquals("", s1.getName());

        s1.setName(" ");
        assertEquals(" ", s1.getName());
    }

    @Test
    void testSetAddvalue() {
        assertEquals(0, s1.getAddValue());
        assertEquals(0, s2.getAddValue());
        assertEquals(0, s1.getAddValue());
        assertEquals(5, s2.getTotalValue());

        s1.setAddValue(1);
        s2.setAddValue(5);
        assertEquals(1, s1.getAddValue());
        assertEquals(5, s2.getAddValue());
        assertEquals(6, s1.getTotalValue());
        assertEquals(10, s2.getTotalValue());

        s1.setAddValue(0);
        s2.setAddValue(100);
        assertEquals(0, s1.getAddValue());
        assertEquals(100, s2.getAddValue());
        assertEquals(5, s1.getTotalValue());
        assertEquals(105, s2.getTotalValue());
    }

    @Test
    void testToString() {
        assertEquals("Sk1 : 5", s1.toString());
    }
}
