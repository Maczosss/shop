package org.application.model;

public interface NamedEnum {
    String getName();

    static <T extends Enum<T> & NamedEnum> T getEnumByName(Class<T> enumType, String name) {
        for (T value : enumType.getEnumConstants()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }
}

