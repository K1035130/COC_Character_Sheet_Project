package ui;

import model.*;
import model.Event;
import persistence.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class CharacterLibrarySwingUI extends JFrame implements WindowListener {
    private static final String JSON_PATH = "./data/characterRecord.json";

    private JTable table;
    private DefaultTableModel tableModel;
    private List<PlayerCharacter> characters;

    // MODIFY: this
    // EFFECT: Create the initial interface
    public CharacterLibrarySwingUI(List<PlayerCharacter> loadedCharacters) {
        super("=== COC PlayerCharacter sheet ===");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        addWindowListener(this);

        if (loadedCharacters != null) {
            characters = loadedCharacters;
        } else {
            characters = new ArrayList<>();
        }

        initTable();
        initButtons();

        setVisible(true);

    }

    // MODIFY: this
    // EFFECT: Create the initial table for characters
    private void initTable() {
        String[] columns = { "Number", "Name", "Gender", "Age", "Occupation" };
        tableModel = new DefaultTableModel(columns, 0);

        table = new JTable(tableModel);

        Font tableFont = new Font("Arial", Font.PLAIN, 22);
        Font headerFont = new Font("Arial", Font.BOLD, 22);
        table.setFont(tableFont);
        table.setRowHeight(30);
        table.getTableHeader().setFont(headerFont);

        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        refreshTable();
    }

    // MODIFY: this
    // EFFECT: Create the initial Buttons
    private void initButtons() {

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 20, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton addButton = new JButton("Add Character");
        JButton viewButton = new JButton("View Character");
        JButton editButton = new JButton("Edit Character");
        JButton deleteButton = new JButton("Delete Character");
        JButton saveButton = new JButton("Save Character");
        JButton[] buttons = { addButton, viewButton, editButton, deleteButton, saveButton };

        setBottonFormat(buttons, buttonPanel);

        addButton.addActionListener(e -> addCharacter());
        viewButton.addActionListener(e -> viewCharacter());
        editButton.addActionListener(e -> editCharacter());
        deleteButton.addActionListener(e -> deleteCharacter());
        saveButton.addActionListener(e -> saveCharacters());

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // MODIFY: this
    // EFFECT: Change Buttons' format
    private void setBottonFormat(JButton[] jbs, JPanel buttonPanel) {
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);
        Dimension buttonSize = new Dimension(150, 45);

        for (JButton b : jbs) {
            b.setFont(buttonFont);
            b.setPreferredSize(buttonSize);
            b.setFocusPainted(false);
            buttonPanel.add(b);
        }
    }

    // MODIFY: ./data/characterRecord.json
    // EFFECTS: save characters' info as json
    private void saveCharacters() {
        JsonWriter writer = new JsonWriter(JSON_PATH);

        try {
            writer.open();
            writer.write(characters);
            writer.close();

            int count = characters.size();
            JOptionPane.showMessageDialog(
                    this, "Save " + count + " Characters.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Fail to save", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFY: this
    // EFFECT: delete the chosen character
    private void deleteCharacter() {
        int row = table.getSelectedRow();

        if (row == -1) {
            nullSelectMessage();
            return;
        }

        PlayerCharacter pc = characters.get(row);
        String name = pc.getName();

        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want delete \"" + name + "\"?", "Delete Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            characters.remove(row);
            refreshTable();
            JOptionPane.showMessageDialog(
                    this, "The Character \"" + name + " has been deleted...", "Deleted successfully",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // MODIFY: this
    // EFFECT: edit the info of chosen character
    private void editCharacter() {
        int row = table.getSelectedRow();

        if (row == -1) {
            nullSelectMessage();
            return;
        }

        PlayerCharacter pc = characters.get(row);
        new EditCharacterDialog(this, pc);
        refreshTable();
    }

    // MODIFY: this
    // EFFECT: view the info of chosen character
    private void viewCharacter() {
        int row = table.getSelectedRow();
        if (row == -1) {
            nullSelectMessage();
            return;
        }

        PlayerCharacter pc = characters.get(row);
        new ViewCharacterDialog(this, pc);
    }

    // MODIFY: this
    // EFFECT: add the new character
    private void addCharacter() {
        AddCharacter ac = new AddCharacter(this, characters);
        ac.buildNewCharacter();
        refreshTable();
    }

    // MODIFY: this
    // EFFECT: update the value in the table
    private void refreshTable() {
        tableModel.setRowCount(0);
        int index = 1;
        for (PlayerCharacter pc : characters) {
            tableModel.addRow(
                    new Object[] { index++, pc.getName(), pc.getGender(), pc.getAge(), pc.getOccupation() });
        }
    }

    // Effect: show message that no character been chosen
    private void nullSelectMessage() {
        JOptionPane.showMessageDialog(
                this,
                "Please select one Character.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
    }

    // EFFECT： Print all events when the window closes
    @Override
    public void windowClosing(WindowEvent e) {
        for (Event ev : EventLog.getInstance()) {
            System.out.println(ev.toString() + "\n");
        }
    }

    // Useless function
    @Override
    public void windowOpened(WindowEvent e) {
    }

    // Useless function
    @Override
    public void windowClosed(WindowEvent e) {
    }

    // Useless function
    @Override
    public void windowIconified(WindowEvent e) {
    }

    // Useless function
    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    // Useless function
    @Override
    public void windowActivated(WindowEvent e) {
    }

    // Useless function
    @Override
    public void windowDeactivated(WindowEvent e) {
    }

}
