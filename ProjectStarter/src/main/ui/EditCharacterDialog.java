package ui;

import model.*;

import javax.swing.*;

import exceptions.InvalidGenderException;

import java.awt.*;
import java.util.*;

public class EditCharacterDialog extends JDialog {
    private PlayerCharacter character;
    private Map<Attribute, JTextField> attrFields = new EnumMap<>(Attribute.class);
    private JTextField nameField;
    private JTextField ageField;
    private JTextField occField;
    private JTextField genderField;
    private DefaultListModel<Skill> skillListModel = new DefaultListModel<>();
    private JList<Skill> skillList;
    private JFrame basJFrame;

    // MODIFY： this
    // EFFECT: creating interface for editing chosen character
    public EditCharacterDialog(JFrame parent, PlayerCharacter pc) {
        super(parent, "Character Editor: " + pc.getName(), true);
        this.character = pc;
        this.basJFrame = parent;

        setSize(735, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(buildBasicInfoPanel(pc), BorderLayout.NORTH);
        add(buildAttInfoPanel(pc), BorderLayout.WEST);
        add(buildSkillInfoPanel(pc), BorderLayout.EAST);
        add(buildConfirmBtPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // MODIFY： this
    // EFFECT: creating a panel showing basic info of chosen character
    public JPanel buildBasicInfoPanel(PlayerCharacter pc) {
        JPanel basePanel = new JPanel(new GridLayout(2, 4, 20, 10));
        basePanel.setBorder(BorderFactory.createTitledBorder("Basic Information"));

        basePanel.add(new JLabel("Name: "));
        nameField = new JTextField(pc.getName());
        basePanel.add(nameField, BorderLayout.WEST);

        basePanel.add(new JLabel("Age: "));
        ageField = new JTextField(String.valueOf(pc.getAge()));
        basePanel.add(ageField);

        basePanel.add(new JLabel("Gender (male/female/other): "));
        genderField = new JTextField(pc.getGender());
        basePanel.add(genderField);

        basePanel.add(new JLabel("Occupation: "));
        occField = new JTextField(pc.getOccupation());
        basePanel.add(occField);
        return basePanel;
    }

    // MODIFY： this
    // EFFECT: creating a panel showing Attribute info of chosen character
    public JPanel buildAttInfoPanel(PlayerCharacter pc) {
        JPanel attrPanel = new JPanel(new GridLayout(6, 4, 10, 30));
        attrPanel.setBorder(BorderFactory.createTitledBorder("Attribute information"));

        for (Attribute a : Attribute.values()) {
            JLabel label = new JLabel(a.name() + " (Original : " + pc.getAttribute(a.name()) + ")");
            JTextField field = new JTextField(String.valueOf(pc.getAttribute(a.name())), 5);

            attrFields.put(a, field);
            attrPanel.add(label);
            attrPanel.add(field);
        }
        return attrPanel;
    }

    // MODIFY： this
    // EFFECT: creating a panel showing skill info of chosen character
    public JPanel buildSkillInfoPanel(PlayerCharacter pc) {
        JPanel skillPanel = new JPanel(new BorderLayout());
        skillPanel.setBorder(BorderFactory.createTitledBorder("Skill Editor (Base value: 5)"));
        skillList = new JList<>(skillListModel);
        skillList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Skill s : pc.getSkills()) {
            skillListModel.addElement(s);
        }

        JPanel skillButtonPanel = new JPanel(new FlowLayout());
        JButton addSkillBtn = new JButton("Add");
        JButton editSkillBtn = new JButton("Edit");
        JButton delSkillBtn = new JButton("Delete");

        skillButtonPanel.add(addSkillBtn);
        skillButtonPanel.add(editSkillBtn);
        skillButtonPanel.add(delSkillBtn);

        skillPanel.add(new JScrollPane(skillList), BorderLayout.CENTER);
        skillPanel.add(skillButtonPanel, BorderLayout.SOUTH);

        addSkillBtn.addActionListener(e -> addSkill());
        editSkillBtn.addActionListener(e -> editSkill());
        delSkillBtn.addActionListener(e -> deleteSkill());

        return skillPanel;
    }

    // MODIFY： this
    // EFFECT: creating a panel for confrim editing
    public JPanel buildConfirmBtPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton confirmBtn = new JButton(" Confirm ");
        JButton cancelBtn = new JButton(" Cancel ");
        JLabel tip1 = new JLabel("Tip: All values will be automatically adjusted to comply with the specified range.");
        btnPanel.add(tip1);
        btnPanel.add(confirmBtn);
        btnPanel.add(cancelBtn);

        confirmBtn.addActionListener(e -> applyChanges());
        cancelBtn.addActionListener(e -> dispose());
        return btnPanel;
    }

    // MODIFY： this
    // EFFECT: delete the chosen skill
    private void deleteSkill() {
        Skill selected = skillList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Confirm delete the Skill " + selected.getName() + "?\n"
                            + "Once you confrim, the skills will update directly.",
                    "Confirm delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                skillListModel.removeElement(selected);
                character.deleteSkill(selected.getName());
            }
        }
    }

    // MODIFY： this
    // EFFECT: edit the chosen skill
    private void editSkill() {
        Skill selected = skillList.getSelectedValue();
        if (selected == null) {
            return;
        }
        String valStr = JOptionPane.showInputDialog(this,
                "Modify skill value (you will get 5 base value)\n"
                        + "Once you confrim, the skill will update directly.");
        try {
            int newAdd = Integer.parseInt(valStr);
            if (newAdd >= 0) {
                selected.setAddValue(Math.min(newAdd, 95));
                skillList.repaint();
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            notValidInputMessage("skill value");
        }
    }

    // MODIFY： this
    // EFFECT: add a new skill
    private void addSkill() {
        AddSkillDialog dialog = new AddSkillDialog(basJFrame);
        dialog.setVisible(true);

        Skill newSkill = dialog.getResult();
        if (newSkill == null) {
            return;
        }

        if (!character.checkHaveSkill(newSkill.getName())) {
            skillListModel.addElement(newSkill);
            character.addSkill(newSkill.getName(), newSkill.getAddValue());
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Your skill is duplicate, please use edit",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // EFFECT: check if user give a valid name and Occupation
    private boolean checkValidNameOcc(PlayerCharacter newC) {
        boolean result = !((newC.getName().isBlank()) || (newC.getOccupation().isBlank()));
        return result;
    }

    // MODIFY： this
    // EFFECT: apply all the changes made
    private void applyChanges() {
        try {
            setBasicInfo();
        } catch (NumberFormatException e) {
            notValidInputMessage("Age");
            return;
        } catch (InvalidGenderException e) {
            notValidInputMessage("Gender");
            return;
        }

        try {
            setAttInfo();
        } catch (NumberFormatException e) {
            notValidInputMessage("Attribute");
            return;
        }

        if (checkValidNameOcc(character)) {
            JOptionPane.showMessageDialog(this, "Successful; HP, MP, SAN have been recalculated",
                    "successful", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            notValidInputMessage("Name or Occupation");
            return;
        }
    }

    // MODIFY： this
    // EFFECT: set all the attibute changes
    private void setAttInfo() throws NumberFormatException {
        for (Attribute a : Attribute.values()) {

            int val = Integer.parseInt(attrFields.get(a).getText());
            val = Math.max(0, Math.min(99, val));
            character.setAttribute(a.name(), val);
        }
    }

    // MODIFY： this
    // EFFECT: set all the basic info changes
    private void setBasicInfo() throws NumberFormatException, InvalidGenderException {
        character.setName(nameField.getText());
        character.setOccupation(occField.getText());

        if (checkGender(genderField.getText())) {
            character.setGender(genderField.getText());
        } else {
            throw new InvalidGenderException();
        }
        character.setGender(genderField.getText());
        int newAge = Integer.parseInt(ageField.getText());
        if (newAge >= 0) {
            character.setAge(newAge);
        } else {
            character.setAge(0);
        }
    }

    // EFFECT: check if the given gender is valid or not
    private Boolean checkGender(String s) {
        String[] log = { "male", "female", "other" };
        for (String g : log) {
            if (s.equalsIgnoreCase(g)) {
                return true;
            }
        }
        return false;
    }

    // EFFCET: show the invalid input message
    private void notValidInputMessage(String s) {
        JOptionPane.showMessageDialog(
                this,
                "Your " + s + " input is invalid",
                "Error",
                JOptionPane.WARNING_MESSAGE);
    }
}
