package solids;

import model.Solid;
import model.Vertex;

import java.awt.*;

public class Octahedron extends Solid {
    public Octahedron(int x, int y, int z, int delka, Color color) {
        super();
        setColor(color);
        getVertexList().add(new Vertex(x, y - delka * 2, z)); //top

        getVertexList().add(new Vertex(x + delka, y, z));
        getVertexList().add(new Vertex(x, y, z + delka));
        getVertexList().add(new Vertex(x - delka, y, z));
        getVertexList().add(new Vertex(x, y, z - delka));

        getVertexList().add(new Vertex(x, y + delka * 2, z)); //bot


        for (int i = 1; i <= 4; i++) { //cykly pro zlepseni citelnosti
            getIndices().add(0);
            getIndices().add(i);
        }
        for (int i = 1; i <= 3; i++) {
            getIndices().add(i);
            getIndices().add(i + 1);
        }
        getIndices().add(4);
        getIndices().add(1);

        for (int i = 1; i <= 4; i++) {
            getIndices().add(5);
            getIndices().add(i);
        }
    }

}
