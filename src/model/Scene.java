package model;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Solid> solids = new ArrayList<>();

    public List<Solid> getSolids() {
        return solids;
    }

    public void addSolids(List<Solid> list) {
        solids.addAll(list);
    }


}
