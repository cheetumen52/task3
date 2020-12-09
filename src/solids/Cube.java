package solids;

import model.Solid;
import model.Vertex;

import java.awt.*;

public class Cube extends Solid {
    public Cube(int x, int y, int z, int delka, Color color) { //pocatek a delka stran
        super();
        setColor(color);
        getVertexList().add(new Vertex(x, y, z));
        getVertexList().add(new Vertex(x + delka, y, z));
        getVertexList().add(new Vertex(x, y + delka, z));
        getVertexList().add(new Vertex(x, y, z + delka));
        getVertexList().add(new Vertex(x + delka, y, z + delka));
        getVertexList().add(new Vertex(x, y + delka, z + delka));
        getVertexList().add(new Vertex(x + delka, y + delka, z));
        getVertexList().add(new Vertex(x + delka, y + delka, z + delka));

        getIndices().add(0);
        getIndices().add(1);
        getIndices().add(0);
        getIndices().add(2);
        getIndices().add(0);
        getIndices().add(3);
        getIndices().add(1);
        getIndices().add(4);
        getIndices().add(1);
        getIndices().add(6);
        getIndices().add(4);
        getIndices().add(3);
        getIndices().add(4);
        getIndices().add(7);
        getIndices().add(7);
        getIndices().add(6);
        getIndices().add(7);
        getIndices().add(5);
        getIndices().add(5);
        getIndices().add(2);
        getIndices().add(5);
        getIndices().add(3);
        getIndices().add(2);
        getIndices().add(6);


    }
}
