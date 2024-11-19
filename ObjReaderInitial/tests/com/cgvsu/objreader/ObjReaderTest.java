package com.cgvsu.objreader;

import com.cgvsu.VertexDelete.Eraser;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ModelTest {

    private Model model;
    private Polygon polygon;

    @Before
    public void setUp() {
        // Создаем модель для теста
        model = new Model();
        polygon = new Polygon();

        // Инициализация с фиктивными данными (например, индексы вершин, нормалей и текстур)
        model.vertices.add(new Vector3f(0.0f, 0.0f, 0.0f));  // Вершина 0
        model.vertices.add(new Vector3f(1.0f, 0.0f, 0.0f));  // Вершина 1
        model.vertices.add(new Vector3f(0.0f, 1.0f, 0.0f));  // Вершина 2
        model.vertices.add(new Vector3f(1.0f, 1.0f, 0.0f));  // Вершина 3

        model.textureVertices.add(new Vector2f(0.0f, 0.0f));  // Текстурная вершина 0
        model.textureVertices.add(new Vector2f(0.0f, 1.0f));  // Текстурная вершина 1
        model.textureVertices.add(new Vector2f(1.0f, 0.0f));  // Текстурная вершина 2
        model.textureVertices.add(new Vector2f(1.0f, 1.0f));  // Текстурная вершина 3

        model.normals.add(new Vector3f(0.0f, 0.0f, 1.0f));  // Нормаль 0
        model.normals.add(new Vector3f(0.0f, 1.0f, 0.0f));  // Нормаль 1
        model.normals.add(new Vector3f(1.0f, 0.0f, 0.0f));  // Нормаль 2
        model.normals.add(new Vector3f(0.0f, 0.0f, -1.0f)); // Нормаль 3

        // Добавление полигона, который использует эти вершины
        polygon.setVertexIndices((ArrayList<Integer>) List.of(0,1,2));
        polygon.setTextureVertexIndices((ArrayList<Integer>) List.of(0,1,2));
        polygon.setNormalIndices((ArrayList<Integer>) List.of(0,1,2));


        model.polygons.add(polygon);
    }

    @Test
    public void testClone_ModelCloning() {
        // Клонируем модель
        Model clonedModel = model.clone();

        // Проверяем, что клонированная модель не равна исходной, но содержимое одинаково
        assertNotSame("Cloned model should not be the same instance as the original model.", model, clonedModel);
        assertEquals("Cloned model should have the same number of vertices.", model.vertices.size(), clonedModel.vertices.size());
        assertEquals("Cloned model should have the same number of polygons.", model.polygons.size(), clonedModel.polygons.size());
    }

    @Test
    public void testVertexDelete_RemovesVertices() {
        // Удаляем вершину с индексом 1
        List<Integer> verticesToDelete = Arrays.asList(1);
        Model updatedModel = Eraser.vertexDelete(model, verticesToDelete, true, false);

        // Проверяем, что вершина с индексом 1 была удалена
        assertEquals("Vertex with index 1 should be removed.", 3, updatedModel.vertices.size());
        assertFalse("Vertex with index 1 should be removed.", updatedModel.vertices.contains(new Vector3f(1.0f, 0.0f, 0.0f)));
    }

    @Test
    public void testPolygonVertexReferences_AfterVertexDeletion() {
        // Удаляем вершину с индексом 1
        List<Integer> verticesToDelete = Arrays.asList(1);
        Model updatedModel = Eraser.vertexDelete(model, verticesToDelete, true, false);

        // Проверяем, что полигон обновился
        Polygon updatedPolygon = updatedModel.polygons.get(0);
        assertEquals("The polygon should have 2 vertices after deletion.", 2, updatedPolygon.getVertexIndices().size());
        assertFalse("The polygon should not contain vertex 1.", updatedPolygon.getVertexIndices().contains(1));
    }

    @Test
    public void testVertexDelete_DeletesTextureVertices() {
        // Удаляем текстурную вершину с индексом 1
        List<Integer> textureVerticesToDelete = Arrays.asList(1);
        Model updatedModel = Eraser.vertexDelete(model, textureVerticesToDelete, true, false);

        // Проверяем, что текстурная вершина с индексом 1 была удалена
        assertEquals("Texture vertex with index 1 should be removed.", 3, updatedModel.textureVertices.size());
        assertFalse("Texture vertex with index 1 should be removed.", updatedModel.textureVertices.contains(new Vector2f(0.0f, 1.0f)));
    }

    @Test
    public void testVertexDelete_DeletesNormals() {
        // Удаляем нормаль с индексом 1
        List<Integer> normalsToDelete = Arrays.asList(1);
        Model updatedModel = Eraser.vertexDelete(model, normalsToDelete, true, false);

        // Проверяем, что нормаль с индексом 1 была удалена
        assertEquals("Normal with index 1 should be removed.", 3, updatedModel.normals.size());
        assertFalse("Normal with index 1 should be removed.", updatedModel.normals.contains(new Vector3f(0.0f, 1.0f, 0.0f)));
    }

    @Test
    public void testPolygonNormalReferences_AfterNormalDeletion() {
        // Удаляем нормаль с индексом 1
        List<Integer> normalsToDelete = Arrays.asList(1);
        Model updatedModel = Eraser.vertexDelete(model, normalsToDelete, true, false);

        // Проверяем, что полигон обновился
        Polygon updatedPolygon = updatedModel.polygons.get(0);
        assertFalse("The polygon should not contain normal 1.", updatedPolygon.getNormalIndices().contains(1));
    }
}
