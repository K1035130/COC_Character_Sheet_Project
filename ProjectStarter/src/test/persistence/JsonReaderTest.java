package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyCharacterList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCharacters.json");
        try {
            List<PlayerCharacter> characters = reader.read();
            assertEquals(0, characters.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralCharacters() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralCharacters.json");
        try {
            List<PlayerCharacter> characters = reader.read();
            assertEquals(2, characters.size());

            PlayerCharacter c1 = characters.get(0);
            PlayerCharacter c2 = characters.get(1);

            checkCharacterBasics("Man", "student", 34, c1);
            checkAttribute("STR", 45, c1);
            checkAttribute("DEX", 60, c1);
            checkAttribute("INT", 80, c1);
            checkAttribute("POW", 55, c1);

            checkHasSkill("Library", 80, c1);
            checkHasSkill("Hidden", 55, c1);

            checkCharacterBasics("Henry", "Professor", 52, c2);
            checkAttribute("STR", 40, c2);
            checkAttribute("EDU", 90, c2);
            checkAttribute("INT", 85, c2);
            checkAttribute("POW", 0, c2);
            
            checkHasSkill("Occult", 65, c2);

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
