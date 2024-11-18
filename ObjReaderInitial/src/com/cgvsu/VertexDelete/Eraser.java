package com.cgvsu.VertexDelete;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Eraser {
    public static Model vertexDelete(Model model, List<Integer> index, boolean new_file, boolean hanging_peaks) {

        Model rezultModel;
        if (new_file) rezultModel = model.clone();
        else rezultModel = model;

        Collections.sort(index);
        Collections.reverse(index);

        ArrayList<Integer> deletetextureVertices = new ArrayList<>();
        ArrayList<Integer> deletenormals = new ArrayList<>();


        for (int i = 0; i < model.polygons.size(); i++) {
            Polygon polygon = model.polygons.get(i);
            for (int q = 0; q < index.size(); q++) {
                for (int j = 0; j < polygon.getVertexIndices().size(); j++) {
                    if (polygon.getVertexIndices().get(j).equals(index.get(q))) {
                        model.vertices.remove(index.get(q));
                        deletenormals.add(polygon.getNormalIndices().get(j));
                        deletetextureVertices.add(polygon.getTextureVertexIndices().get(j));

                        polygon.getVertexIndices().remove(j);
                        polygon.getNormalIndices().remove(j);
                        polygon.getTextureVertexIndices().remove(j);

                    }


                }
                model.vertices.remove((Integer) index.get(q));

            }
        }

    }
}
