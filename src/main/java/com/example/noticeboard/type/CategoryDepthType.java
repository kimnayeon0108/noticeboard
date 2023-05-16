package com.example.noticeboard.type;

public enum CategoryDepthType {

    FIRST(1, "대분류"), SECOND(2, "중분류"), THIRD(3, "소분류");

    int depth;
    String name;

    CategoryDepthType(int depth, String name) {
        this.depth = depth;
        this.name = name;
    }

    public static CategoryDepthType getFromDepth(int depth) {
        switch (depth) {
            case 1:
                return FIRST;
            case 2:
                return SECOND;
            case 3:
                return THIRD;
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
