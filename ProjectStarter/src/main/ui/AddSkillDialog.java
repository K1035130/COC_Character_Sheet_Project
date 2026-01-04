package ui;

import model.Skill;
import javax.swing.*;
import java.awt.*;

public class AddSkillDialog extends JDialog {
    private JTextField nameField;
    private JTextField valueField;
    private Skill result = null;

    // MODIFY：this
    // EFFECT: Creat dialog for add new skill
    public AddSkillDialog(JFrame parent) {
        super(parent, "Add skill", true);
        setSize(360, 220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildTipPanel(), BorderLayout.CENTER);
        add(buildConfrimBtPanel(), BorderLayout.SOUTH);
    }

    // MODIFY：this
    // EFFECT: Creat Panel for user to give new skill's Info
    private JPanel buildInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 25));

        inputPanel.add(new JLabel("Skill Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Skill value:"));
        valueField = new JTextField("0");
        inputPanel.add(valueField);
        return inputPanel;
    }

    // MODIFY：this
    // EFFECT: Creat Panel for give user some tips
    private JPanel buildTipPanel() {
        JPanel tipPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tipPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel tip1 = new JLabel("(With base value: 5)");
        JLabel tip2 = new JLabel("(Clicking 'Confirm', new skill will be updated immediately)");

        tipPanel.add(tip1);
        tipPanel.add(tip2);
        return tipPanel;
    }

    // MODIFY：this
    // EFFECT: Creat Panel for confrimation
    private JPanel buildConfrimBtPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton confirm = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");

        btnPanel.add(confirm);
        btnPanel.add(cancel);
        confirm.addActionListener(e -> onConfirm());
        cancel.addActionListener(e -> dispose());

        return btnPanel;
    }

    // MODIFY：this
    // EFFECT: Upon the user clicking 'Confirm', a new skill is created
    private void onConfirm() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the skill name...");
            return;
        }

        int addValue;
        try {
            addValue = Integer.parseInt(valueField.getText().trim());
            if (addValue < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "The input value is invalid...");
            return;
        }

        result = new Skill(name, Math.min(95, addValue)); 
        dispose(); 
    }

   
    public Skill getResult() {
        return result;
    }
}