package anol.converter;

import java.util.HashMap;

import static anol.converter.TagNames.Tags.*;

public class TagNames extends HashMap<String, TagNames.Tags> {

    public enum Tags {
        DESIGN, SHEET, PART, SOLID, SHAPE, CIRCLE, RECTANGLE, ROTATE,
        HEIGHT, WIDTH, THICK, RADIUS, FUNCTION, NAME, PAGE_SIZE,
        REPEAT_X, REPEAT_Y, DELTA_X, DELTA_Y, USER_DEFINED
    }

    public TagNames(String language) throws Exception {
        switch (language) {
            case "en":
                put("design", DESIGN);
                put("sheet", SHEET);
                put("part", PART);
                put("solid", SOLID);
                put("shape", SHAPE);
                put("circle", CIRCLE);
                put("rectangle", RECTANGLE);
                put("rotate", ROTATE);
                put("h", HEIGHT);
                put("w", WIDTH);
                put("t", THICK);
                put("r", RADIUS);
                put("f", FUNCTION);
                put("name", NAME);
                put("size", PAGE_SIZE);
                put("nx", REPEAT_X);
                put("ny", REPEAT_Y);
                put("dx", DELTA_X);
                put("dy", DELTA_Y);
                break;
            case "no":
                put("design", DESIGN);
                put("ark", SHEET);
                put("del", PART);
                put("emne", SOLID);
                put("komp", SHAPE);
                put("sirk", CIRCLE);
                put("rekt", RECTANGLE);
                put("v", ROTATE);
                put("h", HEIGHT);
                put("t", THICK);
                put("b", WIDTH);
                put("r", RADIUS);
                put("funk", FUNCTION);
                put("navn", NAME);
                put("type", PAGE_SIZE);
                put("nx", REPEAT_X);
                put("ny", REPEAT_Y);
                put("dx", DELTA_X);
                put("dy", DELTA_Y);
                break;
            default:
                throw new Exception("Language \"" + language + "\" is not supported.");
        }
    }

    public Tags getTagValue(String tagName) throws Exception {
        if (containsKey(tagName)) return super.get(tagName);
        else return USER_DEFINED;
    }

    public String getTagName(Tags tag) throws Exception {
        for (Entry<String, Tags> entry : entrySet()) {
            if (tag == entry.getValue()) {
                return entry.getKey();
            }
        }
        throw new Exception("Illegal tag \"" + tag + "\".");
    }

}
