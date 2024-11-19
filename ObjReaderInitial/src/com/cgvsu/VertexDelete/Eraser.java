package com.cgvsu.VertexDelete;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class Eraser {
    public static Model vertexDelete(Model model, List<Integer> index, boolean new_file, boolean hanging_peaks) {

        Model rezultModel;
        if (new_file) rezultModel = model.clone();
        else rezultModel = model;

        Collections.sort(index);
        Collections.reverse(index);

        TreeSet<Integer> deletetextureVertices = new TreeSet<Integer>();
        TreeSet<Integer> deletenormals = new TreeSet<Integer>();
        TreeSet<Integer> deletevertices = new TreeSet<Integer>();

        for (int i = 0; i < rezultModel.polygons.size(); i++) {
            Polygon polygon = rezultModel.polygons.get(i);
            for (int q = 0; q < index.size(); q++) {
                for (int j = 0; j < polygon.getVertexIndices().size(); j++) {
                    if (polygon.getVertexIndices().get(j).equals(index.get(q))) {
                        deletetextureVertices.add(polygon.getVertexIndices().get(j));
                        deletenormals.add(polygon.getNormalIndices().get(j));
                        deletetextureVertices.add(polygon.getTextureVertexIndices().get(j));

                        polygon.getVertexIndices().remove((int) j);
                        polygon.getNormalIndices().remove((int) j);
                        polygon.getTextureVertexIndices().remove((int) j);
                    }
                }
            }
        }
        List<Integer> textureVerticesList = new ArrayList<>(deletetextureVertices);
        List<Integer> normalsList = new ArrayList<>(deletenormals);
        List<Integer> verticesList = new ArrayList<>(deletevertices);

        for (int i = verticesList.size() - 1; i > 0; i--) {
            rezultModel.vertices.remove((int) verticesList.get(i));
        }
        for (int i = textureVerticesList.size() - 1; i > 0; i--) {
            rezultModel.textureVertices.remove((int) textureVerticesList.get(i));
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


        return rezultModel;
    }
}
