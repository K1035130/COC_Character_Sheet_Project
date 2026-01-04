package ui;

import model.*;

import javax.swing.*;
import java.awt.*;

public class RadarPanel extends JPanel {

    private Attribute[] attrs = {
            Attribute.STR, Attribute.DEX, Attribute.CON, Attribute.APP,
            Attribute.POW, Attribute.SIZ, Attribute.INT, Attribute.EDU
    };

    private PlayerCharacter pc;
    private int centerX = 250;
    private int centerY = 160;
    private int radius = 120;
    private double angleStep = 2 * Math.PI / attrs.length;

    public RadarPanel(PlayerCharacter pc) {
        this.pc = pc;
    }

    // EFFECT: Create a radar chart based chosen Character
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawPolygon(g2);
        drawAttName(g2);
        drawDriAtt(g2);
    }

    // EFFECT: Show the HP, MP, SAN below the redar chart
    private void drawDriAtt(Graphics2D g2) {

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String hpStr = "HP:" + pc.getAttribute("HP");
        String mpStr = "MP:" + pc.getAttribute("MP");
        String sanStr = "SAN:" + pc.getAttribute("SAN");
        g2.drawString(hpStr + "   " + mpStr + "   " + sanStr, centerX - radius + 20, centerY + radius + 65);
    }

    // EFFECT: Show the Attributes name on the redar chart
    private void drawAttName(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        for (int i = 0; i < attrs.length; i++) {
            int x = centerX + (int) ((radius + 20) * Math.sin(i * angleStep));
            int y = centerY - (int) ((radius + 20) * Math.cos(i * angleStep));
            g2.setColor(Color.BLACK);
            g2.drawString(attrs[i].name() + ":" + pc.getAttribute(attrs[i].name()), x - 25, y + 10);
        }
    }

    // EFFECT: Draw the radar grid line on the redar chart
    private void drawGrid(Graphics2D g2) {
        g2.setColor(Color.LIGHT_GRAY);
        for (int r = 40; r <= radius; r += 40) {
            Polygon grid = new Polygon();
            for (int i = 0; i < attrs.length; i++) {
                int x = centerX + (int) (r * Math.sin(i * angleStep));
                int y = centerY - (int) (r * Math.cos(i * angleStep));
                grid.addPoint(x, y);
            }
            g2.drawPolygon(grid);
        }
    }

    // EFFECT: Draw the radar polygon on the redar chart based chosen Character
    private void drawPolygon(Graphics2D g2) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < attrs.length; i++) {
            int val = pc.getAttribute(attrs[i].name());
            double scale = Math.min(val, 99) / 99.0;
            int x = centerX + (int) (radius * scale * Math.sin(i * angleStep));
            int y = centerY - (int) (radius * scale * Math.cos(i * angleStep));
            polygon.addPoint(x, y);
        }

        g2.setColor(new Color(0, 0, 255, 100));
        g2.fillPolygon(polygon);
        g2.drawPolygon(polygon);
    }
}