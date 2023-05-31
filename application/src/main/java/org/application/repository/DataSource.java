package org.application.repository;

import org.application.model.NamedEnum;

public enum DataSource implements NamedEnum {
    DATABASE("database"),
    JSON_FILE("json file"),
    TXT_FILE("text file"),
    EMPTY("empty");

    private final String name;

    DataSource(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static DataSource getSource(String name){
        var tempEnum = NamedEnum.getEnumByName(DataSource.class, name);
        return tempEnum==null?DataSource.EMPTY:tempEnum;
    }
}
