package com.cgvsu.VertexDelete;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

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

        for (int i = 0; i < model.polygons.size(); i++) {
            Polygon polygon = model.polygons.get(i);
            for (int q = 0; q < index.size(); q++) {
                for (int j = 0; j < polygon.getVertexIndices().size(); j++) {
                    if (polygon.getVertexIndices().get(j).equals(index.get(q))) {
                        deletetextureVertices.add(polygon.getVertexIndices().get(j));
                        deletenormals.add(polygon.getNormalIndices().get(j));
                        deletetextureVertices.add(polygon.getTextureVertexIndices().get(j));

                        polygon.getVertexIndices().remove(j);
                        polygon.getNormalIndices().remove(j);
                        polygon.getTextureVertexIndices().remove(j);
                    }
                }


            }
        }

        return model;
    }
}
