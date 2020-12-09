package model;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Solid {
    private boolean active = false; //rozhodovani o editaci modelu
    private Mat4 model = new Mat4Identity();
    private List<Vertex> vertexList = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private Color color = new Color(0xff0000);

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public List<Integer> getIndices() {
        return indices;
    }


    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    } //model pro jednotlive solidy

    public boolean isActive() {
        return active;
    } //aktivni objekt editace

    public void setActive() { //aktivni objekt editace
        active = true;
    }

    public void resetActive() {
        active = false;
    } //aktivni objekt editace

    //Curve.class methods -> override
    public void setAccuracy(double accuracy) {
    }

    public void setType(int type) {
    }

    public void refresh() {
    }

    public void setCustomCoords(ArrayList<Point3D> list) {
    }
}
