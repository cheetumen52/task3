package control;

import transforms.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PointEditor {
    public static ArrayList<Point3D> getPoints() {
        JPanel panel = new JPanel(new GridLayout(5, 5));
        JTextField text1 = givejTextField();
        JTextField text2 = givejTextField();
        JTextField text3 = givejTextField();
        JTextField text4 = givejTextField();
        JTextField text5 = givejTextField();
        JTextField text6 = givejTextField();
        JTextField text7 = givejTextField();
        JTextField text8 = givejTextField();
        JTextField text9 = givejTextField();
        JTextField text10 = givejTextField();
        JTextField text11 = givejTextField();
        JTextField text12 = givejTextField();
        panel.add(new Label("Pořadí souřadnic"));
        panel.add(new Label("X"));
        panel.add(new Label("Y"));
        panel.add(new Label("Z"));
        panel.add(new Label("1."));
        panel.add(text1);
        panel.add(text2);
        panel.add(text3);
        panel.add(new Label("2."));
        panel.add(text4);
        panel.add(text5);
        panel.add(text6);
        panel.add(new Label("3."));
        panel.add(text7);
        panel.add(text8);
        panel.add(text9);
        panel.add(new Label("4."));
        panel.add(text10);
        panel.add(text11);
        panel.add(text12);
        JOptionPane.showMessageDialog(null, panel);

        ArrayList<JTextField> texts = new ArrayList<>(Arrays.asList(text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12));
        ArrayList<Point3D> points = new ArrayList<>();
        try {
            for (JTextField t : texts) {
                Double.parseDouble(t.getText());
            }
            for (int i = 0; i < texts.size(); i += 3) {
                points.add(new Point3D(Double.parseDouble(texts.get(i).getText()), Double.parseDouble(texts.get(i + 1).getText()), Double.parseDouble(texts.get(i + 2).getText())));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Vyskytla se chyba při převodu polí do čísel. Vyplň všechny textové pole číslicemi a opakuj akci", "Výjimka při převodu", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return points;
    }

    private static JTextField givejTextField() {
        return new JTextField(5);
    }
}
