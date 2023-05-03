package org.dataLoader.service;

public interface ConnectionService<T, U> {
    void getData(T item, U receiver );
}
