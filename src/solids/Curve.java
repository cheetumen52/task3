package solids;

import model.Solid;
import model.Vertex;
import transforms.Cubic;
import transforms.Point3D;

import java.awt.*;
import java.util.ArrayList;

public class Curve extends Solid {
    Point3D a;
    Point3D b;
    Point3D c;
    Point3D d;
    private Cubic cubic;
    private int type = 0; //defaultni hodnoty
    private double accuracy = 0.02; //defaultni hodnoty


    public Curve(Point3D a, Point3D b, Point3D c, Point3D d, Color color) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        setColor(color);
        fillVertexes();
    }

    public void fillVertexes() {
        switch (type) {
            case 0:
                cubic = new Cubic(Cubic.BEZIER,
                        a, b, c, d);
                break;
            case 1:
                cubic = new Cubic(Cubic.COONS,
                        a, b, c, d);
                break;
            case 2:
                cubic = new Cubic(Cubic.FERGUSON,
                        a, b, c, d);
                break;
        }
        for (double i = 0; i < 1; i += accuracy) {
            getVertexList().add(new Vertex(cubic.compute(i)));
        }
        for (int i = 0; i < getVertexList().size() - 1; i++) {
            getIndices().add(i);
            getIndices().add(i + 1);
        }
    }

    @Override
    public void refresh() { //smazani seznamu, novy vypocet bodu
        getVertexList().clear();
        getIndices().clear();
        fillVertexes();
    }

    @Override
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    } //uprava presnosti

    @Override
    public void setType(int type) {
        this.type = type;
    } //typ krivky

    @Override
    public void setCustomCoords(ArrayList<Point3D> list) { //vlastni souradnice krivky
        a = list.get(0);
        b = list.get(1);
        c = list.get(2);
        d = list.get(3);
    }
}
