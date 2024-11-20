package com.cgvsu.VertexDelete;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.*;

public class Eraser {

    private static Set<Integer> deletetextureVertices = new HashSet<Integer>();
    private static Set<Integer> deletenormals = new HashSet<Integer>();
    private static Set<Integer> deletevertices = new HashSet<Integer>();


    public static Model vertexDelete(Model model, List<Integer> index, boolean new_file, boolean hanging_peaks) {

        Model rezultModel;
        if (new_file) rezultModel = model.clone();
        else rezultModel = model;

        Collections.sort(index);
        Collections.reverse(index);

        Set<Integer> deletetextureVertices = new HashSet<Integer>();
        Set<Integer> deletenormals = new HashSet<Integer>();
        Set<Integer> deletevertices = new HashSet<Integer>();


        if (new_file) return vertexDeletenewF(model, index, hanging_peaks);

        else return vertexDeleteoldF(model, index, hanging_peaks);
    }

    public static Model vertexDeletenewF(Model model, List<Integer> index, boolean hanging_peaks) {
        Model modelrez = new Model();


        for (int i = 0; i < model.polygons.size(); i++) {
            Polygon polygon = model.polygons.get(i);
            Polygon polygonrez = new Polygon();

            ArrayList<Integer> vertexIndices =new ArrayList<>();
            ArrayList<Integer> textureVertexIndices =new ArrayList<>();
            ArrayList<Integer> normalIndices =new ArrayList<>();

            for (int j = 0; j < polygon.getVertexIndices().size(); j++) {
                if (index.contains(polygon.getVertexIndices().get(j))) {
                    deletevertices.add(polygon.getVertexIndices().get(j));
                    deletenormals.add(polygon.getNormalIndices().get(j));
                    deletetextureVertices.add(polygon.getTextureVertexIndices().get(j));

                    polygon.getVertexIndices().remove((int) j);
                    polygon.getNormalIndices().remove((int) j);
                    polygon.getTextureVertexIndices().remove((int) j);

                    if (polygon.getVertexIndices().size() < 3) {
                        vertexIndices.add(polygon.getVertexIndices().get(j));
                        textureVertexIndices.add(polygon.getTextureVertexIndices().get(j));
                        i--;
                    }
                } else {


                }
            }
        }

        for (int i = 0; i < model.vertices.size(); i++) {
            if (!deletevertices.contains(i)) {
                modelrez.vertices.add(model.vertices.get(i).clone());
            }
        }
        for (int i = 0; i < model.textureVertices.size(); i++) {
            if (!deletetextureVertices.contains(i)) {
                modelrez.textureVertices.add(model.textureVertices.get(i).clone());
            }
        }

        for (Integer integer : normalsList) {
            boolean f = true;
            pol:
            for (int i = 0; i < rezultModel.polygons.size(); i++) {
                Polygon polygon = rezultModel.polygons.get(i);
                for (int k = 0; k < polygon.getNormalIndices().size(); k++) {
                    if (integer.equals(polygon.getNormalIndices().get(k))) {
                        f = false;
                        break pol;
                    }
                }
            }
            if (f) rezultModel.normals.remove((int) integer);
        }


    }

    public static Model vertexDeleteoldF(Model model, List<Integer> index, boolean hanging_peaks) {

        for (int i = 0; i < model.polygons.size(); i++) {
            Polygon polygon = model.polygons.get(i);
            for (int q = 0; q < index.size(); q++) {
                for (int j = 0; j < polygon.getVertexIndices().size(); j++) {
                    if (polygon.getVertexIndices().get(j).equals(index.get(q))) {
                        deletevertices.add(polygon.getVertexIndices().get(j));
                        deletenormals.add(polygon.getNormalIndices().get(j));
                        deletetextureVertices.add(polygon.getTextureVertexIndices().get(j));

                        polygon.getVertexIndices().remove((int) j);
                        polygon.getNormalIndices().remove((int) j);
                        polygon.getTextureVertexIndices().remove((int) j);

                        if (polygon.getVertexIndices().size() < 3) {
                            model.polygons.remove(polygon);
                            i--;
                        }
                    }
                }
            }
        }

        List<Integer> textureVerticesList = new ArrayList<>(deletetextureVertices);
        List<Integer> normalsList = new ArrayList<>(deletenormals);
        List<Integer> verticesList = new ArrayList<>(deletevertices);


        for (int i = verticesList.size() - 1; i >= 0; i--) {
            model.vertices.remove((int) verticesList.get(i));
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
