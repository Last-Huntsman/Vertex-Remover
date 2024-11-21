package com.cgvsu.objreader;

import com.cgvsu.VertexDelete.Eraser;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.Assert.*;

public class ObjReaderTest {
   static Path fileName = Path.of("C:\\Users\\zuzuk\\IdeaProjects\\Task3\\ObjReaderInitial\\tests\\SimpleModelsForReaderTests\\caracal_cube.obj");
    static String fileContent;
    static  Model model;
    static {
        try {
            fileContent = Files.readString(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        model = ObjReader.read(fileContent);
    }

    @Test
    public void testDeleteSingleVertex() throws IOException {
        // Загружаем модель из OBJ-файла


        // Удаляем вершину
        List<Integer> verticesToDelete = List.of(0);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, true);

        // Выводим результат в OBJ-формате
        modifiedModel.exportToOBJ();

        // Проверяем количество оставшихся вершин
        assertEquals("Вершина не была удалена!", model.vertices.size() - 1, modifiedModel.vertices.size());
    }

    @Test
    public void testDeleteMultipleVertices() throws IOException {


        // Удаляем несколько вершин
        List<Integer> verticesToDelete = Arrays.asList(0, 1, 2);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, true);

        // Выводим результат в OBJ-формате
        modifiedModel.exportToOBJ();

        // Проверяем количество оставшихся вершин
        assertEquals("Несколько вершин не были удалены!", model.vertices.size() - 3, modifiedModel.vertices.size());
    }

    @Test
    public void testDeleteAndCheckPolygons() throws IOException {

        // Удаляем вершину
        List<Integer> verticesToDelete = Collections.singletonList(0);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, false);

        // Проверяем, что полигоны с удаленными вершинами тоже были удалены
        int initialPolygonCount = model.polygons.size();
        int modifiedPolygonCount = modifiedModel.polygons.size();

        assertTrue("Полигоны не были корректно обработаны!", modifiedPolygonCount < initialPolygonCount);
    }

    @Test
    public void testHangingIndices() throws IOException {


        // Удаляем вершину и проверяем "висячие" нормали
        List<Integer> verticesToDelete = Collections.singletonList(0);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, true, true, true);

        // Убедимся, что висячие нормали остались
        assertEquals("Нормали должны были остаться!", model.normals.size(), modifiedModel.normals.size());
    }
    @Test
    public void testDeleteAllVertices() throws IOException {
        // Удаляем все вершины
        List<Integer> verticesToDelete = new ArrayList<>();
        for (int i = 0; i < model.vertices.size(); i++) {
            verticesToDelete.add(i);
        }
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, true);

        // Проверяем, что не осталось вершин
        assertTrue("Все вершины должны быть удалены!", modifiedModel.vertices.isEmpty());

        // Проверяем, что не осталось полигонов
        assertTrue("Все полигоны должны быть удалены!", modifiedModel.polygons.isEmpty());
    }

    @Test
    public void testDeleteWithHangingPolygons() throws IOException {
        // Удаляем вершину, оставляя "висячие" полигоны
        List<Integer> verticesToDelete = Collections.singletonList(0);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, true);

        // Проверяем, что полигоны остались
        assertEquals("Полигоны не должны были быть удалены!", model.polygons.size(), modifiedModel.polygons.size());
    }

    @Test
    public void testDeleteVertexWithTextureIndices() throws IOException {
        // Удаляем вершину, также проверяя текстурные индексы
        List<Integer> verticesToDelete = Collections.singletonList(0);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, true, true);

        // Проверяем, что текстурные координаты корректно обработаны
        assertTrue("Некоторые текстурные координаты не были удалены!",
                modifiedModel.textureVertices.size() <= model.textureVertices.size());
    }

    @Test
    public void testDeleteWithPreservingNormalsAndTextures() throws IOException {
        // Удаляем вершину, оставляя нормали и текстурные индексы
        List<Integer> verticesToDelete = Collections.singletonList(0);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, true, true, true);

        // Проверяем, что нормали и текстурные координаты остались неизменными
        assertEquals("Все нормали должны были остаться!", model.normals.size(), modifiedModel.normals.size());
        assertEquals("Все текстурные координаты должны были остаться!", model.textureVertices.size(), modifiedModel.textureVertices.size());
    }

    @Test
    public void testDeleteMiddleVertex() throws IOException {
        // Удаляем одну из "средних" вершин
        int middleVertexIndex = model.vertices.size() / 2;
        List<Integer> verticesToDelete = Collections.singletonList(middleVertexIndex);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, false);

        // Проверяем, что модель содержит на одну вершину меньше
        assertEquals("Средняя вершина не была удалена!", model.vertices.size() - 1, modifiedModel.vertices.size());

        // Проверяем, что соответствующие полигоны были удалены
        assertTrue("Полигоны не были корректно обработаны!",
                modifiedModel.polygons.size() < model.polygons.size());
    }

    @Test
    public void testDeleteVertexWithCloneMode() throws IOException {
        // Удаляем вершину в режиме создания новой модели
        List<Integer> verticesToDelete = Collections.singletonList(0);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, true);

        // Проверяем, что исходная модель не изменилась
        assertEquals("Оригинальная модель не должна была измениться!", model.vertices.size(), ObjReader.read(fileContent).vertices.size());
        assertNotEquals("Измененная модель должна отличаться от оригинальной!", model.vertices.size(), modifiedModel.vertices.size());
    }

    @Test
    public void testDeleteNonExistingVertex() throws IOException {
        // Пытаемся удалить несуществующую вершину
        List<Integer> verticesToDelete = Collections.singletonList(model.vertices.size() + 1);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, true);

        // Проверяем, что модель осталась неизменной
        assertEquals("Модель не должна была измениться!", model.vertices.size(), modifiedModel.vertices.size());
        assertEquals("Количество полигонов не должно было измениться!", model.polygons.size(), modifiedModel.polygons.size());
    }
    @Test
    public void testDeleteVertexWithNewFileTrue() throws IOException {
        // Удаляем вершину с параметром newFile = true
        List<Integer> verticesToDelete = Collections.singletonList(0);
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, true);

        // Проверяем, что исходная модель не изменилась
        assertEquals("Оригинальная модель не должна была измениться!", model.vertices.size(), ObjReader.read(fileContent).vertices.size());
        assertNotEquals("Модифицированная модель должна отличаться от оригинальной!", model.vertices.size(), modifiedModel.vertices.size());

        // Убедимся, что модифицированная модель корректна
        assertEquals("Количество вершин должно уменьшиться на 1!", model.vertices.size() - 1, modifiedModel.vertices.size());
    }



    @Test
    public void testDeleteAllVerticesWithNewFileTrue() throws IOException {
        // Удаляем все вершины с newFile = true
        List<Integer> verticesToDelete = new ArrayList<>();
        for (int i = 0; i < model.vertices.size(); i++) {
            verticesToDelete.add(i);
        }
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, true);

        // Убедимся, что исходная модель осталась неизменной
        assertFalse("Оригинальная модель не должна быть пустой!", ObjReader.read(fileContent).vertices.isEmpty());

        // Проверяем, что модифицированная модель полностью очищена
        assertTrue("Модифицированная модель должна быть пустой!", modifiedModel.vertices.isEmpty());
        assertTrue("Все полигоны должны быть удалены в новой модели!", modifiedModel.polygons.isEmpty());
    }

    @Test
    public void testDeleteAllVerticesWithNewFileFalse() throws IOException {
        // Удаляем все вершины с newFile = false
        List<Integer> verticesToDelete = new ArrayList<>();
        for (int i = 0; i < model.vertices.size(); i++) {
            verticesToDelete.add(i);
        }
        Model modifiedModel = Eraser.vertexDelete(model, verticesToDelete, true, false, false, false);

        // Проверяем, что оригинальная модель была изменена
        assertTrue("Все вершины должны быть удалены!", modifiedModel.vertices.isEmpty());
        assertTrue("Все полигоны должны быть удалены!", modifiedModel.polygons.isEmpty());
    }

}
