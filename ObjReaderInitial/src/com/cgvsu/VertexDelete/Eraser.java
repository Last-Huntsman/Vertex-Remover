package com.cgvsu.VertexDelete;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.*;

public class Eraser {

    private static Set<Integer> deletetextureVertices = new HashSet<Integer>();
    private static Set<Integer> deletenormals = new HashSet<Integer>();


    public static Model vertexDelete(Model model, List<Integer> index, boolean new_file, boolean hanging_peaks) {

        ArrayList<Polygon> polygons = new ArrayList<>();
        Collections.sort(index);
        Collections.reverse(index);

        for (int i = 0; i < model.polygons.size(); i++) {
            Polygon polygon = model.polygons.get(i);
            Polygon polygonrez = new Polygon();

            Set<Integer> textureVertexIndices = new HashSet<Integer>();
            Set<Integer> normalIndices = new HashSet<Integer>();
            Set<Integer> vertexIndices = new HashSet<Integer>();

            for (int j = 0; j < polygon.getVertexIndices().size(); j++) {
                if (index.contains(polygon.getVertexIndices().get(j))) {
                    deletenormals.add(polygon.getNormalIndices().get(j));
                    deletetextureVertices.add(polygon.getTextureVertexIndices().get(j));
                } else {
                    vertexIndices.add(polygon.getVertexIndices().get(j));
                    textureVertexIndices.add(polygon.getTextureVertexIndices().get(j));
                    normalIndices.add(polygon.getNormalIndices().get(j));
                }
            }
            polygonrez.setNormalIndices(new ArrayList<>(normalIndices));
            polygonrez.setTextureVertexIndices(new ArrayList<>(textureVertexIndices));
            polygonrez.setVertexIndices(new ArrayList<>(vertexIndices));

            if (polygonrez.getNormalIndices().size() > 2) polygons.add(polygonrez);

        }
        model.polygons=polygons;

        if (new_file) return vertexDeletenewF(model, polygons, index, hanging_peaks);
        else return vertexDeleteoldF(model, index, hanging_peaks);
    }

    private static Model vertexDeletenewF(Model model, ArrayList<Polygon> polygons, List<Integer> index, boolean hanging_peaks) {
        Model modelrez = new Model();
        modelrez.polygons=polygons;

        for (int i = 0; i < model.vertices.size(); i++) {
            if (!index.contains(i)) {
                modelrez.vertices.add(model.vertices.get(i).clone());
            }
        }
        for (int i = 0; i < model.textureVertices.size(); i++) {
            if (!deletetextureVertices.contains(i)) {
                modelrez.textureVertices.add(model.textureVertices.get(i).clone());
            }
        }

        for (int i = 0; i < model.normals.size(); i++) {
            for (int j = 0; j < modelrez.polygons.size(); j++) {
                Polygon polygon = modelrez.polygons.get(j);
                for (int k = 0; k < polygon.getNormalIndices().size(); k++) {
                    if (polygon.getNormalIndices().get(k).equals(i)) {
                        modelrez.normals.add(model.normals.get(i).clone());
                    }
                }

            }
        }


        return modelrez;
    }

    private static Model vertexDeleteoldF(Model model, List<Integer> index, boolean hanging_peaks) {

        List<Integer> textureVerticesList = new ArrayList<>(deletetextureVertices);
        List<Integer> normalsList = new ArrayList<>(deletenormals);


        for (int i = index.size() - 1; i >= 0; i--) {
            model.vertices.remove((int) index.get(i));
        }
        for (int i = textureVerticesList.size() - 1; i >= 0; i--) {
            model.textureVertices.remove((int) textureVerticesList.get(i));
        }


        for (Integer integer : normalsList) {
            boolean f = true;
            pol:
            for (int i = 0; i < model.polygons.size(); i++) {
                Polygon polygon = model.polygons.get(i);
                for (int k = 0; k < polygon.getNormalIndices().size(); k++) {
                    if (integer.equals(polygon.getNormalIndices().get(k))) {
                        f = false;
                        break pol;
                    }
                }
            }
            if (f) model.normals.remove((int) integer);
        }

        return model;
    }
}
