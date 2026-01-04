package ui;

import persistence.*;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class AskLoadDialog extends JFrame {
    private static final String JSON_PATH = "./data/characterRecord.json";

    // MODIFY: This
    // EFFECT: Create a dialog ask the user if they wish to load data
    public AskLoadDialog() {
        super("=== COC PlayerCharacter sheet ===");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(giveMainPanel(), BorderLayout.CENTER);

        setVisible(true);

    }

    // MODIFY: This
    // EFFECT: Create a panel ask the user if they wish to load data
    private JPanel giveMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        JLabel questionLabel = new JLabel("Do you want to load the last saved character data?", SwingConstants.CENTER);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(questionLabel);
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(giveYNBottonPanel());

        return mainPanel;
    }

    // MODIFY: This
    // EFFECT: Create buttons let user choose
    private JPanel giveYNBottonPanel() {

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        Dimension buttonSize = new Dimension(100, 40);
        yesButton.setPreferredSize(buttonSize);
        noButton.setPreferredSize(buttonSize);

        buttonPanel.add(yesButton);
        buttonPanel.add(Box.createHorizontalStrut(100));
        buttonPanel.add(noButton);

        yesButton.addActionListener(e -> choiceYes());
        noButton.addActionListener(e -> choiceNo());

        return buttonPanel;
    }

    // MODIFY: This
    // EFFECT: After user clicks "yes", data will be loaded
    private void choiceYes() {
        JsonReader reader = new JsonReader(JSON_PATH);
        List<PlayerCharacter> loadedC = null;
        try {
            loadedC = reader.read();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this, "Loading fail", "Loading successfully", JOptionPane.ERROR_MESSAGE);
            loadedC = null;
        }

        int count = (loadedC == null) ? 0 : loadedC.size();
        JOptionPane.showMessageDialog(
                this, count + " characters loaded", "Loading successfully", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        new CharacterLibrarySwingUI(loadedC);
    }

    // EFFECT: After user clicks "no", nothing will be done
    private void choiceNo() {
        dispose();
        new CharacterLibrarySwingUI(null);
    }
}
