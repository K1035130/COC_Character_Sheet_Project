package persistence;

import model.*;

import org.json.*;

import java.io.*;
import java.util.List;

// Represents a writer that writes JSON representation of workroom to file
// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of list of player characters to file
    public void write(List<PlayerCharacter> characters) {
        JSONArray jsonArray = new JSONArray();

        for (PlayerCharacter pc : characters) {
            jsonArray.put(pc.toJson());
        }

        saveToFile(jsonArray.toString(TAB)); 
    }

    
    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
        EventLog.getInstance().logEvent(new Event("Saved all characters"));
    }
}