package solids;

import model.Solid;
import model.Vertex;

import java.awt.*;

public class Tetrahedron extends Solid {
    public Tetrahedron(int x, int y, int z, int delka, Color color) {
        super();
        setColor(color);
        getVertexList().add(new Vertex(x, y, z));
        getVertexList().add(new Vertex(x + delka, y, z));
        getVertexList().add(new Vertex(x, y + delka, z));
        getVertexList().add(new Vertex(x, y, z + delka));

        for (int i = 1; i <= 3; i++) { //cykly pro zlepseni citelnosti
            getIndices().add(0);
            getIndices().add(i);
        }
        getIndices().add(2);
        getIndices().add(1);
        for (int i = 1; i <= 2; i++) {
            getIndices().add(3);
            getIndices().add(i);
        }
    }
}
