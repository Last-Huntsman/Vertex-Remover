package com.cgvsu.math;

// Это заготовка для собственной библиотеки для работы с линейной алгеброй
public class Vector2f implements Cloneable {
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public Vector2f clone() throws CloneNotSupportedException {
        return (Vector2f) super.clone();
    }
    float x, y;
}
