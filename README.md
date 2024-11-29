Пример
java

Копировать код
// Import the required classes
import com.cgvsu.model.Model;
import com.cgvsu.VertexDelete.Eraser;

// Create or load a 3D model
Model myModel = loadModel("example.obj");

// Define vertices to delete
List<Integer> verticesToDelete = Arrays.asList(0, 3, 7);

// Perform vertex deletion
Model updatedModel = Eraser.vertexDelete(myModel, verticesToDelete, true, false, false, false);

// Save or use the updated model
saveModel(updatedModel, "updated_example.obj");
