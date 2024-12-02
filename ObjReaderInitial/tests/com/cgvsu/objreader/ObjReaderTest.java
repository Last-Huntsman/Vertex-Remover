package com.cgvsu.objreader;

import com.cgvsu.VertexDelete.Eraser;
import com.cgvsu.model.Model;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.Assert.*;

public class ObjReaderTest {

    private static Model model;
    private static String fileContent;

    @BeforeClass
    public static void setup() throws IOException {
        Path fileName = Path.of("C:\\Users\\zuzuk\\IdeaProjects\\Task3\\ObjReaderInitial\\tests\\SimpleModelsForReaderTests\\caracal_cube.obj");
        fileContent = Files.readString(fileName);
        model = ObjReader.read(fileContent);
    }

    private Model deleteVertices(List<Integer> verticesToDelete, boolean newFile) {
        return Eraser.vertexDelete(model, verticesToDelete, true, false, false, newFile);
    }

    @Test
    public void testDeleteSingleVertex() {
        Model modifiedModel = deleteVertices(List.of(0), true);
        assertEquals("Вершина не была удалена!", model.vertices.size() - 1, modifiedModel.vertices.size());
    }

    @Test
    public void testDeleteMultipleVertices() {
        Model modifiedModel = deleteVertices(Arrays.asList(0, 1, 2), true);
        assertEquals("Несколько вершин не были удалены!", model.vertices.size() - 3, modifiedModel.vertices.size());
    }

    @Test
    public void testDeleteAndCheckPolygons() {
        Model modifiedModel = deleteVertices(List.of(1,2), true);
        assertTrue("Полигоны не были корректно обработаны!", modifiedModel.polygons.size() < model.polygons.size());
    }

    @Test
    public void testHangingIndices() {
        Model modifiedModel = Eraser.vertexDelete(model, List.of(0), true, true, true, true);
        assertEquals("Нормали должны были остаться!", model.normals.size(), modifiedModel.normals.size());
    }

    @Test
    public void testDeleteAllVertices() {
        List<Integer> verticesToDelete = new ArrayList<>();
        for (int i = 0; i < model.vertices.size(); i++) {
            verticesToDelete.add(i);
        }
        Model modifiedModel = deleteVertices(verticesToDelete, true);
        assertTrue("Все вершины должны быть удалены!", modifiedModel.vertices.isEmpty());
        assertTrue("Все полигоны должны быть удалены!", modifiedModel.polygons.isEmpty());
    }

    @Test
    public void testDeleteVertexWithTextureIndices() {
        Model modifiedModel = Eraser.vertexDelete(model, List.of(0), true, false, true, true);
        assertTrue("Некоторые текстурные координаты не были удалены!",
                modifiedModel.textureVertices.size() <= model.textureVertices.size());
    }

    @Test
    public void testDeleteMiddleVertex() {
        int middleVertexIndex = model.vertices.size() / 2;
        Model modifiedModel = deleteVertices(List.of(middleVertexIndex), true);
        assertEquals("Средняя вершина не была удалена!", model.vertices.size() - 1, modifiedModel.vertices.size());

    }

    @Test
    public void testDeleteVertexWithCloneMode() {
        Model modifiedModel = deleteVertices(List.of(0), true);
        assertEquals("Оригинальная модель не должна была измениться!", model.vertices.size(), ObjReader.read(fileContent).vertices.size());
        assertNotEquals("Измененная модель должна отличаться от оригинальной!", model.vertices.size(), modifiedModel.vertices.size());
    }

    @Test
    public void testDeleteNonExistingVertex() {
        Model modifiedModel = deleteVertices(List.of(model.vertices.size() + 1), true);
        assertEquals("Модель не должна была измениться!", model.vertices.size(), modifiedModel.vertices.size());
        assertEquals("Количество полигонов не должно было измениться!", model.polygons.size(), modifiedModel.polygons.size());
    }
}
