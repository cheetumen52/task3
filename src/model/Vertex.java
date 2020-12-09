package model;

import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.Optional;

public class Vertex {
    final private Point3D position;

    public Vertex(double x, double y, double z) {
        position = new Point3D(x, y, z);
    }

    public Vertex(Point3D p) {
        position = p;
    }

    public Vertex transform(Mat4 model) {
        return new Vertex(position.mul(model));
    }

    public Point3D getPosition() {
        return position;
    }

    public Optional<Vec3D> dehomog() {
        return position.dehomog();
    }

    public boolean isInView() {
        return (-position.getW() <= position.getX() && position.getY() <= position.getW() && 0 <= position.getZ() && 0 <= position.getW());
    }
}
