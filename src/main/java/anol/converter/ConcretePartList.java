package anol.converter;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class ConcretePartList extends ArrayList<ConcretePart> {

    public Rectangle2D getBounds() {
        boolean first = true;
        Rectangle2D bounds = null;
        for (ConcretePart part : this) {
            if (first) {
                first = false;
                bounds = part.getBounds();
            } else {
                bounds.add(part.getBounds());
            }
        }
        return bounds;
    }
}
