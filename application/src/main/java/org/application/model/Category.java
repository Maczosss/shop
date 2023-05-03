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
    public static Category getCategory(String categoryName){
        for (Category category : Category.values()) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category name: " + categoryName);
    }
}
