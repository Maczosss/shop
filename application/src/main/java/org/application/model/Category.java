package org.application.model;

public enum Category implements NamedEnum {
    COMPUTER("computer"),
    GRAPHICS_CARD("graphics card"),
    MOUSE("mouse"),
    KEYBOARD("keyboard"),
    MOTHER_BOARD("mother board"),
    MONITOR("monitor"),
    MOUSE_PAD("mouse pad"),
    SMARTWATCH("smartwatch"),
    EMPTY("empty");

    private final String name;

    Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public static Category getCategory(String name){
        var tempEnum = NamedEnum.getEnumByName(Category.class, name);
        return tempEnum==null?Category.EMPTY:tempEnum;
    }
}
