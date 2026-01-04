package persistence;

import model.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport

public class JsonTest {
    // EFFECTS: Check that the names and values of skills are consistent with
    // expectations
    protected void checkSkill(String expectedName, int expectedValue, Skill actualSkill) {
        assertEquals(expectedName, actualSkill.getName());
        assertEquals(expectedValue, actualSkill.getTotalValue());
    }

    // EFFECTS: Check that the property values are as expected
    protected void checkAttribute(String attr, int expectedValue, PlayerCharacter character) {
        int actual = character.getAttribute(attr);
        assertEquals(expectedValue, actual);
    }

    // EFFECTS: Check basic character information (name, occupation, age)
    protected void checkCharacterBasics(String expectedName, String expectedOccupation,
            int expectedAge, PlayerCharacter actualCharacter) {
        assertEquals(expectedName, actualCharacter.getName());
        assertEquals(expectedOccupation, actualCharacter.getOccupation());
        assertEquals(expectedAge, actualCharacter.getAge());
    }

    // EFFECTS: Check if the character contains the specified skill and its value
    protected void checkHasSkill(String skillName, int expectedValue, PlayerCharacter character) {
        boolean found = false;
        for (Skill s : character.getSkills()) {
            if (s.getName().equals(skillName)) {
                assertEquals(expectedValue, s.getTotalValue());
                found = true;
                break;
            }
        }
        if (!found) {
            fail("Skill " + skillName + " not found in character " + character.getName());
        }
    }

}
