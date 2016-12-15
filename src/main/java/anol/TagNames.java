package anol;

import java.util.HashMap;
import java.util.Map;

import static anol.TagNames.Tags.*;

public class TagNames {

    public enum Tags {
        DESIGN, SHEET, PART, SOLID, SHAPE, CIRCLE, RECTANGLE, ROTATE,
        HEIGHT, WIDTH, RADIUS, FUNCTION, NAME, PAGE_SIZE,
        REPEAT_X, REPEAT_Y, DELTA_X, DELTA_Y, USER_DEFINED}

    Map<String, Tags> dictionary = new HashMap<>();

    public TagNames(String language) throws Exception {
        switch (language) {
            case "en":
                dictionary.put("design", DESIGN);
                dictionary.put("sheet", SHEET);
                dictionary.put("part", PART);
                dictionary.put("solid", SOLID);
                dictionary.put("shape", SHAPE);
                dictionary.put("circle", CIRCLE);
                dictionary.put("rectangle", RECTANGLE);
                dictionary.put("rotate", ROTATE);
                dictionary.put("h", HEIGHT);
                dictionary.put("w", WIDTH);
                dictionary.put("r", RADIUS);
                dictionary.put("f", FUNCTION);
                dictionary.put("name", NAME);
                dictionary.put("size", PAGE_SIZE);
                dictionary.put("nx", REPEAT_X);
                dictionary.put("ny", REPEAT_Y);
                dictionary.put("dx", DELTA_X);
                dictionary.put("dy", DELTA_Y);
                break;
            case "no":
                dictionary.put("design", DESIGN);
                dictionary.put("ark", SHEET);
                dictionary.put("del", PART);
                dictionary.put("emne", SOLID);
                dictionary.put("komp", SHAPE);
                dictionary.put("sirk", CIRCLE);
                dictionary.put("rekt", RECTANGLE);
                dictionary.put("v", ROTATE);
                dictionary.put("h", HEIGHT);
                dictionary.put("b", WIDTH);
                dictionary.put("r", RADIUS);
                dictionary.put("funk", FUNCTION);
                dictionary.put("navn", NAME);
                dictionary.put("type", PAGE_SIZE);
                dictionary.put("nx", REPEAT_X);
                dictionary.put("ny", REPEAT_Y);
                dictionary.put("dx", DELTA_X);
                dictionary.put("dy", DELTA_Y);
                break;
            default:
                throw new Exception("Language \"" + language + "\" is not supported.");
        }
    }

    public Tags get(String tagName) throws Exception {
        if (dictionary.containsKey(tagName)) {
            return dictionary.get(tagName);
        } else {
            return USER_DEFINED;
        }
    }

    public String get(Tags tag) throws Exception {
        for (Map.Entry<String, Tags> entry : dictionary.entrySet()) {
            if (tag == entry.getValue()) {
                return entry.getKey();
            }
        }
        throw new Exception("Illegal tag \"" + tag + "\".");
    }

}
