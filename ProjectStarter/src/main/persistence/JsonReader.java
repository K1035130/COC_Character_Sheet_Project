package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.json.*;

// Represents a reader that reads characters from JSON data stored in file
// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads player characters from file and returns as a list;
    // throws IOException if an error occurs reading data from file
    public List<PlayerCharacter> read() throws IOException {
        String jsonData = readFile(source);
        JSONArray jsonArray = new JSONArray(jsonData);
        return parseCharacters(jsonArray);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses list of PlayerCharacters from JSON array and returns it
    private List<PlayerCharacter> parseCharacters(JSONArray jsonArray) {
        List<PlayerCharacter> characters = new ArrayList<>();

        for (Object obj : jsonArray) {
            JSONObject jsonChar = (JSONObject) obj;
            PlayerCharacter pc = parseCharacter(jsonChar);
            characters.add(pc);
        }

        return characters;
    }

    // EFFECTS: parses a single PlayerCharacter from JSON object
    private PlayerCharacter parseCharacter(JSONObject jsonChar) {
        String name = jsonChar.getString("name");
        String occupation = jsonChar.getString("occupation");
        int age = jsonChar.getInt("age");
        String gender = jsonChar.getString("gender");

        PlayerCharacter pc = new PlayerCharacter(name, occupation, age, gender);

        // parse attributes
        JSONObject attrObj = jsonChar.getJSONObject("attributes");
        for (Attribute attr : Attribute.values()) {
            if (attrObj.has(attr.name())) {
                int value = attrObj.getInt(attr.name());
                pc.setAttribute(attr.name(), value);
            }
        }

        // parse skills
        JSONArray skillArr = jsonChar.getJSONArray("skills");
        for (Object obj : skillArr) {
            JSONObject s = (JSONObject) obj;
            String skillName = s.getString("name");
            int skillValue = s.getInt("value");
            pc.addSkill(skillName, skillValue);
        }
        EventLog.getInstance().logEvent(new Event("Loaded character " + name));
        return pc;
    }
}
