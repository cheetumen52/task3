package render;

import model.Scene;
import model.Solid;
import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.Raster;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Vec3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private Raster raster; //naplnit konstruktorem
    private LineRasterizer lineRasterizer;
    private Mat4 model = new Mat4Identity();
    private Mat4 view = new Mat4Identity();
    private Mat4 proj = new Mat4Identity();
    private Color color = new Color(0xff0000); // defaultni hodnota, kdyby nahodou chybela u solid

    public Renderer(Raster raster, LineRasterizer lineRasterizer) {
        this.raster = raster;
        this.lineRasterizer = lineRasterizer;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void render(Scene scene) {
        for (Solid solid : scene.getSolids()) {
            render(solid);
        }
    }

    public void render(Solid solid) {
        if (solid.getColor() != null) color = solid.getColor();
        Mat4 m = solid.getModel().mul(view).mul(proj);
        List<Vertex> tempVertices = new ArrayList<>();
        for (Vertex v : solid.getVertexList()) {
            tempVertices.add(v.transform(m));
        }

        //vytvorit hrany
        for (int i = 0; i < solid.getIndices().size() - 1; i += 2) {
            int indexA = solid.getIndices().get(i);
            int indexB = solid.getIndices().get(i + 1);

            Vertex a = tempVertices.get(indexA);
            Vertex b = tempVertices.get(indexB);
            renderEdge(a, b);
        }
    }


    private void renderEdge(Vertex av, Vertex bv) {
        if (!av.dehomog().isPresent() || !bv.dehomog().isPresent()) {
            return;
        }
        if (av.isInView() && bv.isInView()) {
            final Vec3D a = av.dehomog().get();
            final Vec3D b = bv.dehomog().get();
            int x1 = (int) ((a.getX() + 1) * (raster.getWidth() - 1) / 2);
            int x2 = (int) ((b.getX() + 1) * (raster.getWidth() - 1) / 2);
            int y1 = (int) ((1 - a.getY()) * (raster.getHeight() - 1) / 2);
            int y2 = (int) ((1 - b.getY()) * (raster.getHeight() - 1) / 2);
            lineRasterizer.setColor(color);
            lineRasterizer.rasterize(x1, y1, x2, y2);
        }
    }
}
