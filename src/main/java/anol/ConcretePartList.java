package anol;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConcretePartList {

    List<ConcretePart> partList;

    public ConcretePartList() {
        this.partList = new ArrayList<ConcretePart>();
    }

    public Rectangle2D getBounds() {
        Rectangle2D bounds = null;
        Iterator<ConcretePart> iterator = partList.listIterator();
        while (iterator.hasNext()) {
            ConcretePart part = iterator.next();
            bounds = part.getBounds();
        }
        return bounds;
    }

    public String getBoundingBox() {
        // TODO: This one give bounding box of all parts on all sheets
        String boundingBox = "";
        Iterator<ConcretePart> iterator = partList.listIterator();
        while (iterator.hasNext()) {
            ConcretePart part = iterator.next();
            boundingBox = part.getBoundingBox();
        }
        return boundingBox;
    }

    public Iterator<ConcretePart> getIterator() {
        return partList.listIterator();
    }

    public void addPart(ConcretePart concretePart) {
        partList.add(concretePart);
    }
}
