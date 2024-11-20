package com.cgvsu.model;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;

public class Model implements Cloneable {

    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();
    public ArrayList<Polygon> polygons = new ArrayList<>();

    public Model() {
    }

    // Метод clone
    @Override
    public Model clone() {
        Model clonedModel = new Model();

        // Копирование вершин
        clonedModel.vertices = new ArrayList<>();
        for (Vector3f vertex : this.vertices) {
            clonedModel.vertices.add(vertex.clone());
        }

        // Копирование текстурных вершин
        clonedModel.textureVertices = new ArrayList<>();
        for (Vector2f textureVertex : this.textureVertices) {
            clonedModel.textureVertices.add(textureVertex.clone());
        }

        // Копирование нормалей
        clonedModel.normals = new ArrayList<>();
        for (Vector3f normal : this.normals) {
            clonedModel.normals.add(normal.clone());
        }

        // Копирование полигонов
        clonedModel.polygons = new ArrayList<>();
        for (Polygon polygon : this.polygons) {
            clonedModel.polygons.add(polygon.clone());
        }

        return clonedModel;
    }
}
