package control;


import model.Scene;
import model.Solid;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.Raster;
import render.Renderer;
import solids.*;
import transforms.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller3D implements Controller {

    private final Panel panel;
    private double xBefore;
    private double yBefore;
    private LineRasterizer rasterizer;
    private Solid circle;
    private Solid tetrahedron;
    private Scene axis;
    private Solid axisX;
    private Solid axisY;
    private Solid axisZ;
    private Solid curve;
    private boolean lines;
    private Octahedron pyramid;
    private Solid cube;
    private Renderer renderer;
    private Camera camera;
    private String active; // aktivni editace
    private Scene scene; // seznam objektu
    private double yTrans, xTrans, yinc, xinc, zinc, zoom, zTrans; // promenne pro transformaci


    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners(panel);
        update();
    }

    public void initObjects(Raster raster) {
        scene = new Scene(); //scena pro objekty
        axis = new Scene(); //scena pro osy
        initShapes(); // inicializace vsech tvaru
        initValues(); // inicializace vsech promennych - metody -> prehlednejsi kod
        rasterizer = new LineRasterizerGraphics(raster);
        camera = new Camera().withPosition(new Vec3D(3, 6.04, -26.74)).addAzimuth(-1.55).addZenith(1.32); // pozice kamery aby sledovala aktivni cast sceny
        initRenderer(raster); // inicializace rendereru
        panel.requestFocus();
        panel.requestFocusInWindow();
        update();
    }

    private void initValues() {
        zTrans = 0;
        yTrans = 0;
        xTrans = 0;
        yinc = 0;
        xinc = 0;
        zinc = 0;
        zoom = 1;
        lines = true; //zobrazeni os X,Y,Z
        active = "Tetrahedron"; //aktivni prvek editace
    }

    private void initRenderer(Raster raster) {
        renderer = new Renderer(raster, rasterizer);
        renderer.setModel(new Mat4RotXYZ(0, 0, 0)); //nastaveni matic
        renderer.setView(camera.getViewMatrix());
        renderer.setProj(new Mat4PerspRH((float) Math.PI / 6, 1, 0, 100));
    }

    private void initShapes() {
        pyramid = new Octahedron(-5, 0, 5, 1, Color.BLUE);
        cube = new Cube(1, 1, 1, 3, Color.PINK);
        tetrahedron = new Tetrahedron(-2, 0, -2, 2, Color.WHITE);
        circle = new Circle(1, Color.WHITE);
        axisX = new Axis(6, Color.RED, 0);
        axisZ = new Axis(6, Color.YELLOW, 2);
        axisY = new Axis(6, Color.GREEN, 1);
        curve = new Curve(new Point3D(0, -8, 0), new Point3D(3, -10, 0), new Point3D(4, 0, 0), new Point3D(8, -8, 0), Color.red);
        tetrahedron.setActive(); //nastaveni jako aktivni objekt editace -> string vlevo nahore
        axis.addSolids(Arrays.asList(axisX, axisY, axisZ));
        scene.addSolids(Arrays.asList(circle, tetrahedron, curve, cube, pyramid)); //pridani do sceny
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) { //pohled kamery
                camera = camera.addAzimuth(Math.PI / 5 * (float) (e.getX() - xBefore) / (float) panel.getWidth());
                camera = camera.addZenith(Math.PI / 5 * (float) (e.getY() - yBefore) / (float) panel.getWidth());
                xBefore = e.getX();
                yBefore = e.getY();
                update();
            }

        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) { //pohled kamery - promenne
                    xBefore = e.getX();
                    yBefore = e.getY();
                }
            }
        });

        panel.addMouseWheelListener(e -> { //zoom objektu
            int skips = e.getWheelRotation();
            if (skips < 0) {
                zoom += 0.01;
            } else {
                zoom -= 0.01;
            }
            if (zoom < 0) return;
            update();
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_Z: // Editace krivky - nastaveni bodu - parametru
                        ArrayList<Point3D> coords;
                        if ((coords = PointEditor.getPoints()) != null) { // Vlastni editor
                            curve.setCustomCoords(coords);
                            curve.refresh();
                        }
                        break;
                    case KeyEvent.VK_T: //Volba presnosti krivky
                        String option2 = JOptionPane.showInputDialog("Napiš číslo v intervalu (0,0.30> : ");
                        try {
                            double o = Double.parseDouble(option2);
                            if (o > 0 && o <= 0.30) { //urceni intervalu
                                curve.setAccuracy(o); //nastaveni intervalu
                                curve.refresh(); //promazani listu a znovuvypocet bodu
                            }
                        } catch (Exception q) {
                            showDialogMessage("Nastala chyba při změně křivky!", "Přesnost křivky", JOptionPane.WARNING_MESSAGE);
                        }
                        break;

                    case KeyEvent.VK_K: // Zmena tvaru krivky - bezier, coons, ferguson
                        Object response3 = JOptionPane.showInputDialog(null, "Vyber tvar křivky", "Změna křivky", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Bezier", "Coons", "Ferguson"}, "Bezier");
                        if (response3 == null) {
                            showDialogMessage("Změna tělesa zrušena!", "Změna tělesa", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        switch (response3.toString()) {
                            case "Bezier":
                                curve.setType(0);
                                break;
                            case "Coons":
                                curve.setType(1);
                                break;
                            case "Ferguson":
                                curve.setType(2);
                                break;
                        }
                        curve.refresh(); //smazani listu, vypocet novych bodu
                        break;

                    case KeyEvent.VK_E: // Volba telesa k transformaci
                        Object response2 = JOptionPane.showInputDialog(null, "Které těleso mám editovat?", "Změna tělesa", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Tetrahedron", "Circle", "Cube", "Octahedron", "Curve"}, "Tetrahedron");
                        if (response2 == null) {
                            showDialogMessage("Změna tělesa zrušena!", "Změna tělesa", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        deactivateSelection(); // deaktivace vyberu vsech teles
                        zTrans = 0; // reinicializace pro novy objekt + obnoveni puvodni velikosti objektu - pro lepsi editaci
                        yTrans = 0;
                        xTrans = 0;
                        yinc = 0;
                        xinc = 0;
                        zinc = 0;
                        zoom = 1;
                        switch (response2.toString()) {
                            case "Tetrahedron":
                                tetrahedron.setActive(); // nastavi active na true -> upravim model pouze u aktivniho objektu
                                active = "Tetrahedron";
                                break;
                            case "Circle":
                                circle.setActive();
                                active = "Circle";
                                break;
                            case "Cube":
                                cube.setActive();
                                active = "Cube";
                                break;
                            case "Octahedron":
                                pyramid.setActive();
                                active = "Octahedron";
                                break;
                            case "Curve":
                                curve.setActive();
                                active = "Curve";
                                break;
                        }
                        break;


                    case KeyEvent.VK_B:
                        // vybírací menu - změna barev
                        Object response = JOptionPane.showInputDialog(null, "Kterou barvu mám změnit?", "Změna barvy", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Axis X", "Axis Y", "Axis Z", "Tetrahedron", "Circle", "Cube", "Curve"}, "Axis X");
                        if (response == null) {
                            showDialogMessage("Změna barev zrušena, nebudou aplikovány žádné změny!", "Změna barvy", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        switch (response.toString()) {
                            case "Axis X":
                                axisX.setColor(showColorDialog());
                                break;
                            case "Axis Y":
                                axisY.setColor(showColorDialog());
                                break;
                            case "Axis Z":
                                axisZ.setColor(showColorDialog());
                                break;
                            case "Tetrahedron":
                                tetrahedron.setColor(showColorDialog());
                                break;
                            case "Circle":
                                circle.setColor(showColorDialog());
                                break;
                            case "Cube":
                                cube.setColor(showColorDialog());
                                break;
                            case "Curve":
                                curve.setColor(showColorDialog());
                                break;
                        }
                        break;
                    case KeyEvent.VK_C: //vyresetovani sceny
                        hardClear();
                        break;
                    case KeyEvent.VK_NUMPAD9: //rotace Z+
                        zinc += 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD7: //rotace Z-
                        zinc -= 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD8: //rotace Y+
                        yinc += 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD5: //rotace Y-
                        yinc -= 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD4: //rotace x-
                        xinc -= 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD6: //rotace x+
                        xinc += 0.1;
                        break;
                    case KeyEvent.VK_RIGHT: //posunuti po X do minusu
                        xTrans -= 0.1;
                        break;
                    case KeyEvent.VK_LEFT: //posunuti po X do plusu
                        xTrans += 0.1;
                        break;
                    case KeyEvent.VK_UP: //posunuti po Y do plusu
                        yTrans += 0.1;
                        break;
                    case KeyEvent.VK_DOWN: //posunuti po Y do minusu
                        yTrans -= 0.1;
                        break;
                    case KeyEvent.VK_HOME: //posunuti po Z do plusu
                        zTrans += 0.1;
                        break;
                    case KeyEvent.VK_END: //posunuti po Z do minusu
                        zTrans -= 0.1;
                        break;

                    case KeyEvent.VK_O: // Pravouhla
                        renderer.setProj(new Mat4OrthoRH(20, 20, 0.1, 10));
                        break;
                    case KeyEvent.VK_P: // Perspektivni
                        renderer.setProj(new Mat4PerspRH((float) Math.PI / 6, 1, 0, 100));
                        break;

                    case KeyEvent.VK_SHIFT: // posun kamery nahoru
                        camera = camera.up(0.1);
                        break;
                    case KeyEvent.VK_CONTROL: //posun kamery dolu
                        camera = camera.down(0.1);
                        break;
                    case KeyEvent.VK_W: //posun kamery dopredu
                        camera = camera.forward(0.1);
                        break;
                    case KeyEvent.VK_S: //posun kamery dozadu
                        camera = camera.backward(0.1);
                        break;
                    case KeyEvent.VK_A: //posun kamery doleva
                        camera = camera.left(0.1);
                        break;
                    case KeyEvent.VK_D: //posun kamery doprava
                        camera = camera.right(0.1);
                        break;
                    case KeyEvent.VK_L: //vypnuti zobrazeni os
                        lines = !lines;
                        break;
                }
                update();
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void deactivateSelection() { //deaktivace active pro vsechny objekty sceny
        for (Solid s : scene.getSolids()) {
            s.resetActive();
        }
    }

    private Color showColorDialog() { //metoda pro zobrazeni nastaveni barvy
        return JColorChooser.showDialog(null, "Vyber barvu", Color.WHITE);
    }

    private void showDialogMessage(String message, String title, int kind) { //metoda pro zjednodusene vyvolani dialogu
        JOptionPane.showMessageDialog(null, message, title, kind);
    }

    private void update() {
        panel.clear();
        for (Solid s : scene.getSolids()) { //zmena modelu u aktivniho objektu - editace
            if (s.isActive()) {
                s.setModel(new Mat4RotXYZ(xinc, yinc, zinc).mul(new Mat4Transl(xTrans, yTrans, zTrans)).mul(new Mat4Scale(zoom, zoom, zoom)));
            }
        }
        renderer.render(scene); //render cele sceny s vlastnimi modely
        if (lines) { //zobrazeni os
            renderer.render(axis);
        }
        panel.drawString("Aktivní prvek - modelovací matice: " + active, 15, 15);
        renderer.setView(camera.getViewMatrix());
        panel.repaint();
    }

    private void hardClear() { // úplné smazání
        panel.clear();
        initObjects(panel.getRaster());
    }


}
