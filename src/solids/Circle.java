package solids;

import model.Solid;
import model.Vertex;

import java.awt.*;

public class Circle extends Solid {
    public Circle(int x, Color color) {
        super();
        setColor(color);
        for (int i = 0; i < 36; i++)
            getVertexList().add(new Vertex(x, Math.cos(Math.PI / 16 * i), Math.sin(Math.PI / 16 * i)));
        for (int i = 0; i < 35; i++) {
            getIndices().add(i);
            getIndices().add(i + 1);
        }
    }
}
