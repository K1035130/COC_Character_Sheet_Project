package persistence;

import model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport

class JsonWriterTest extends JsonTest {
    PlayerCharacter c1;
    PlayerCharacter c2;
    List<PlayerCharacter> characters;

    @BeforeEach
    void runBefore() {
        c1 = new PlayerCharacter("Man", "student", 34, "Male");
        c1.setAttribute("INT", 80);
        c1.setAttribute("STR", 45);
        c1.addSkill("Library", 75);
        c1.addSkill("Hidden", 50);

        c2 = new PlayerCharacter("Henry", "Professor", 52, "male");
        c2.setAttribute("EDU", 90);
        c2.addSkill("Occult", 60);
        
        characters = new ArrayList<>();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyCharacterList() {
        try {
            List<PlayerCharacter> characters = new ArrayList<>();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCharacters.json");
            writer.open();
            writer.write(characters);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCharacters.json");
            List<PlayerCharacter> readCharacters = reader.read();
            assertEquals(0, readCharacters.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralCharacters() {
        try {
            characters.add(c1);
            characters.add(c2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCharacters.json");
            writer.open();
            writer.write(characters);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCharacters.json");
            List<PlayerCharacter> readCharacters = reader.read();

            assertEquals(2, readCharacters.size());

            PlayerCharacter rc1 = readCharacters.get(0);
            PlayerCharacter rc2 = readCharacters.get(1);

            checkCharacterBasics("Man", "student", 34, rc1);
            checkAttribute("INT", 80, rc1);
            checkAttribute("STR", 45, rc1);

            checkHasSkill("Library", 80, rc1);
            checkHasSkill("Hidden", 55, rc1);

            checkCharacterBasics("Henry", "Professor", 52, rc2);
            checkAttribute("EDU", 90, rc2);
            checkHasSkill("Occult", 65, rc2);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}