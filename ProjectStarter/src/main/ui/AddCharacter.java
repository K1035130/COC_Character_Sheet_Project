package ui;

import java.util.List;
import model.*;

public class AddCharacter {
    private List<PlayerCharacter> loc;
    private PlayerCharacter newC;
    private CharacterLibrarySwingUI baseWindow;

    // MODIFY: this
    // EFFECT: Create a new blank character for add
    public AddCharacter(CharacterLibrarySwingUI characterLibrarySwingUI, List<PlayerCharacter> characters) {
        loc = characters;
        newC = new PlayerCharacter("", "", 0, "");
        baseWindow = characterLibrarySwingUI;
    }

    // MODIFY: this
    // EFFECT: Use the Character Editor window to enter information for the new
    // character and add it to list
    public void buildNewCharacter() {
        new EditCharacterDialog(baseWindow, newC);
        if (checkValidNameOccGen()) {
            loc.add(newC);
        }
    }

    // EFFECT: check if user give a valid name and Occupation and gender
    private boolean checkValidNameOccGen() {
        boolean result = !((newC.getName().isBlank())
                || (newC.getOccupation().isBlank()) 
                || (newC.getGender().isBlank()));
        return result;
    }

}
