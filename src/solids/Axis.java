package solids;

import model.Solid;
import model.Vertex;

import java.awt.*;

public class Axis extends Solid {
    //cervena osa x (0,0,0) do (10,0,0)
    //zelena osa y (0,0,0) do (0,10,0)
    // ....
    public Axis(int end, Color color, int type) {
        getVertexList().add(new Vertex(0, 0, 0));
        setColor(color);
        switch (type) { //typ osy - X, Y, Z - podle toho koncovy bod
            case 0:
                getVertexList().add(new Vertex(end, 0, 0));
                break;
            case 1:
                getVertexList().add(new Vertex(0, end, 0));
                break;
            case 2:
                getVertexList().add((new Vertex(0, 0, end)));
                break;
        }
        //pridani indexu
        getIndices().add(0);
        getIndices().add(1);
    }
}
