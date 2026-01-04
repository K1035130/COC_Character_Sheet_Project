package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.*;
import persistence.*;

public class CharacterLibrary {
    private static final String JSON_PATH = "./data/characterRecord.json";

    private Scanner scanner = new Scanner(System.in);
    private JsonReader reader = new JsonReader(JSON_PATH);
    private JsonWriter writer = new JsonWriter(JSON_PATH);
    private List<PlayerCharacter> library = new ArrayList<>(); // temp storage

    // EFFECT: start the App
    public void start() {
        boolean running = true;
        System.out.println("\n=== COC PlayerCharacter sheet ===");
        loadData();
        while (running) {
            showMenu();
            String choice = scanner.nextLine();
            running = startChoice(choice);
        }
        saveData();
        System.out.println("Application stoped");
    }

    // MODIFY: this
    // EFFECTS: Ask if the last saved data should be loaded or not
    private void loadData() {
        System.out.print("\nDo you want load your data from last time? "
                + "(yes/no, others input will be consider as no): ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("yes")) {
            try {
                library = reader.read();
                System.out.println("Load " + library.size() + " character(s).");
            } catch (IOException e) {
                System.out.println("Record invalid, no data loaded.");
            }
        } else {
            System.out.println(" ");
        }
    }

    // MODIFY: ./data/characterRecord.json
    // EFFECTS: Ask if current data should be saved or not
    private void saveData() {
        System.out.print("Do you want save your data? (yes/no, others input will be consider as no): ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("yes")) {
            try {
                writer.open();
                writer.write(library);
                writer.close();
                System.out.println("The data has been successfully saved.");
            } catch (IOException e) {
                System.out.println("Invalid save, application stop\n" + e.getMessage());
            }
        }
    }

    // MODIFY: this
    // EFFECT: Choose which action user want to take.
    private boolean startChoice(String choice) {
        switch (choice) {
            case "1":
                addCharacter();
                return true;
            case "2":
                viewCharacters();
                return true;
            case "3":
                editCharacter();
                return true;
            case "4":
                deleteCharacter();
                return true;
            case "0":
                System.out.println("");
                return false;
            default:
                System.out.println("Invalid input, try again");
                return true;
        }
    }

    // EFFECT: show the main menu and choices
    private void showMenu() {
        System.out.println("\n=== COC PlayerCharacter sheet ===");
        System.out.println("1. Build a new PlayerCharacter sheet");
        System.out.println("2. View current PlayerCharacter sheet library");
        System.out.println("3. Edit PlayerCharacter sheet");
        System.out.println("4. Delete PlayerCharacter sheet");
        System.out.println("0. Quit the application");
        System.out.print("Your choice: ");
    }

    // MODIFY: this
    // EFFECT: add one new character and add it in the library
    private void addCharacter() {
        System.out.print("Please enter the character's name: ");
        String name = scanner.nextLine();
        while (checkSameName(name)) {
            System.out.println("There is another charater already called this name.");
            System.out.println("Please give a new name: ");
            name = scanner.nextLine();
        }

        System.out.print("Please enter the character's gender (male, female or other): ");
        String gender = scanner.nextLine();

        System.out.print("Please enter the character's occupation: ");
        String occupation = scanner.nextLine();

        System.out.print("Please enter the character's age (Fractional parts will be dropped): ");
        int age = getAnIntFromUser(scanner.nextLine());

        PlayerCharacter c = new PlayerCharacter(name, occupation, age, gender);
        library.add(c);
        System.out.println("The PlayerCharacter had been created!");
    }

    // EFFECT: Show all the characters and its info in library
    private void viewCharacters() {
        if (library.isEmpty()) {
            System.out.println("No Character is current list.\n ");
            return;
        }
        System.out.println("\nCurrent Character List:");
        for (int i = 0; i < library.size(); i++) {
            System.out.println("Character " + (i + 1) + ":");
            viewCharacter(library.get(i));
        }
    }

    // EFFECT: Show all the skills of the given character
    private void viewSkills(PlayerCharacter c) {
        for (int i = 0; i < c.getSkills().size(); i++) {
            Skill s = c.getSkills().get(i);
            System.out.print(
                    (i + 1) + "." + s.getName() + ": " + s.getTotalValue() + " ");
        }
    }

    // MODIFY: this
    // EFFECT: Let user to choose which character and what they want edit.
    private void editCharacter() {
        System.out.println("The currently existing characters info is as follows:");
        viewCharacters();
        System.out.print("Please enter the name of the character to be edited: ");
        String name = scanner.nextLine();

        for (PlayerCharacter c : library) {
            if (c.getName().equalsIgnoreCase(name)) {
                Boolean running = true;
                while (running) {
                    System.out.println(" \nCurrent info about the Character you have selected: ");
                    viewCharacter(c);
                    editMeun();
                    String choice = scanner.nextLine();
                    running = editChoice(choice, c);
                }
                return;
            }
        }
        System.out.println("Character not found.");
    }

    // MODIFY: this
    // EFFECT: Choose which info user want to edit.
    private Boolean editChoice(String choice, PlayerCharacter c) {
        switch (choice) {
            case "1":
                editName(c);
                return true;
            case "2":
                editOccupation(c);
                return true;
            case "3":
                editAge(c);
                return true;
            case "4":
                editAttribute(c);
                return true;
            case "5":
                editSkills(c);
                return true;
            case "0":
                System.out.println("Finish editting");
                return false;
            default:
                System.out.println("Invalid input, try again: ");
                return true;
        }
    }

    // EFFECT: show the edit menu and choices
    private void editMeun() {
        System.out.println("Which info do you want to edit/update?");
        System.out.println("1. The Name");
        System.out.println("2. The Occupation");
        System.out.println("3. The Age");
        System.out.println("4. The Attributive");
        System.out.println("5. The Skills");
        System.out.println("0. Return");
        System.out.print("Your choice: ");
    }

    // MODIFY: This
    // EFFECT: Update character's name based on user request
    private void editName(PlayerCharacter c) {
        System.out.print("Please enter a new Name: ");
        String newName = scanner.nextLine();
        if (checkSameName(newName)) {
            System.out.println("Update fail.");
            System.out.println("The new name must not be the same as any character's name.");
            return;
        }
        c.setName(newName);
        System.out.println("Character info has been updated.");
    }

    // MODIFY: This
    // EFFECT: Update character's occupation based on user request
    private void editOccupation(PlayerCharacter c) {
        System.out.print("Please enter a new occupation: ");
        String newOccupation = scanner.nextLine();

        c.setOccupation(newOccupation);
        System.out.println("Character info has been updated.");
    }

    // MODIFY: This
    // EFFECT: Update character's age based on user request
    private void editAge(PlayerCharacter c) {
        System.out.print("Please enter a new age (Fractional parts will be dropped):  ");
        int newAge = getAnIntFromUser(scanner.nextLine());

        c.setAge(newAge);
        System.out.println("Character info has been updated.");
    }

    // MODIFY: This
    // EFFECT: Update character's Attribute based on user request
    private void editAttribute(PlayerCharacter c) {
        System.out.println("The current character attribute data is as follows:");

        Boolean running = true;
        while (running) {
            System.out.println(c.getAllAttributes());
            System.out.println("The derived attributes(HP,MP,SAN) will auto-caulculate, and can not be set directly.");
            System.out.print("Please chose one Attribute to edit: ");
            String newAttribute = getAttributeFromUser(scanner.nextLine());

            System.out.print("Please give a new value (Fractional parts will be dropped): ");
            int newValue = getAnIntFromUser(scanner.nextLine());
            c.setAttribute(newAttribute, Math.min(99, newValue));
            System.out.println("Character info has been updated.");

            System.out.println("Do you want edit more Attribute? (yes or no, other inputs will be consider as yes): ");
            if (scanner.nextLine().equalsIgnoreCase("no")) {
                running = false;
            }
        }
    }

    // MODIFY: this
    // EFFECT: let use choose edit/add a skill or delete a skill
    private void editSkills(PlayerCharacter c) {
        System.out.println("\nThe current character skills data is as follows:");
        viewSkills(c);
        System.out.println("\nIf you want delete skills enter 1, edit/add skills enter 2");
        int choice = getAnIntFromUser(scanner.nextLine());
        switch (choice) {
            case 1:
                deleteSkills(c);
                break;
            case 2:
                editAddSkills(c);
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    // MODIFY: This
    // EFFECT: Edit/add character's skills based on user request
    private void editAddSkills(PlayerCharacter c) {
        Boolean running = true;
        while (running) {
            viewSkills(c);
            System.out.println("\nYou will have 5 base value for a skill");
            System.out.print("Please choose/add a new skill: ");
            String newSkill = scanner.nextLine();
            System.out.print("Please give a new addValue (Fractional parts will be dropped): ");
            int newValue = getAnIntFromUser(scanner.nextLine());
            c.addSkill(newSkill, Math.min(99, newValue));
            System.out.println("Character info has been updated.");

            System.out.println("Do you want add/edit more skills? (yes or no, other inputs will be consider as yes): ");
            if (scanner.nextLine().equalsIgnoreCase("no")) {
                running = false;
            }
        }
    }

    // MODIFIES: this
    // EFFECT: delete skill(s) based on user request
    private void deleteSkills(PlayerCharacter c) {
        Boolean running = true;
        while (running) {
            viewSkills(c);
            System.out.print("\nPlease choose the skill you want delete: ");
            String deleteSkill = scanner.nextLine();
            c.deleteSkill(deleteSkill);
            System.out.println("Character info has been updated.");
            System.out.println("Do you want delete more skills? (yes or no, other inputs will be consider as yes): ");
            if (scanner.nextLine().equalsIgnoreCase("no")) {
                running = false;
            }
        }
    }

    // MODIFY: this
    // EFFECT: Delete one character based on user's request
    private void deleteCharacter() {
        boolean running = true;
        while (running) {
            System.out.println(" \nThe currently existing characters info is as follows:");
            viewCharacters();
            System.out.println(
                    "If you don't want delete or want to quit, enter 0.");
            System.out.println(
                    "Please choose one Character for delete (you should give the number of the character): ");
            int deleteOne = getAnIntFromUser(scanner.nextLine());
            if (deleteOne > library.size() || deleteOne == 0) {
                System.out.println("Quit.");
                return;
            }
            library.remove(deleteOne - 1);
            System.out.println("\nLibary updated");
            System.out.println(
                    "Do you want delete more characters? (yes or no, other inputs will be consider as yes): ");
            if (scanner.nextLine().equalsIgnoreCase("no")) {
                running = false;
            }
        }
    }

    // EFFECT：Make sure that User's input is a positive Integer
    private int getAnIntFromUser(String input) {
        while (true) {
            if (input.matches("^\\d+(\\.\\d+)?$")) {
                return (int) Double.parseDouble(input);
            } else {
                System.out.println("Invalid, should be a non-negative number,try again: ");
                input = scanner.nextLine();
            }
        }
    }

    // EFFECT: Check if the user enter one of the Attributes or not
    private Boolean checkAttributeFromUser(String input) {
        for (Attribute attr : Attribute.values()) {
            if (attr.name().equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

    // EFFECT: Make sure that User's input is a Attribute
    private String getAttributeFromUser(String input) {
        while (true) {
            if (checkAttributeFromUser(input)) {
                return input.toUpperCase();
            } else {
                System.out.println(" \nYour input should be one of 9 Attribute, try again: ");
                input = scanner.nextLine();
            }
        }
    }

    // EFFECT: Check if there is a character in library called given name or not
    private Boolean checkSameName(String name) {
        for (PlayerCharacter c : library) {
            if (name.equalsIgnoreCase(c.getName())) {
                return true;
            }
        }
        return false;
    }

    // EFFECT: Show given character's info
    private void viewCharacter(PlayerCharacter c) {
        System.out.println("Name: " + c.getName());
        System.out.println("Age: " + c.getAge());
        System.out.println("Occupation: " + c.getOccupation());
        System.out.println("Attributes: \n" + c.getAllAttributes());
        System.out.println("Skills: ");
        viewSkills(c);
        System.out.println("");
    }
}
