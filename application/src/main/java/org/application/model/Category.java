package org.application.model;

public enum Category {
    COMPUTER("computer"),
    GRAPHICS_CARD("graphics card"),
    MOUSE("mouse"),
    KEYBOARD("keyboard"),
    MOTHER_BOARD("mother board"),
    MONITOR("monitor"),
    MOUSE_PAD("mouse pad"),
    SMARTWATCH("smartwatch");

    private final String name;

    Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
