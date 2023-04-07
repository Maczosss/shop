package org.example.service;

public interface ConnectionService<T, U> {
    void getData(T item, U receiver );
}
