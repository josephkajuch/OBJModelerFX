# OBJModelerFX
Basic OBJ File Viewer Using JavaFX

This program can parse the basic components of an OBJ File such as the Vertices (as well as normal and texture vertices) and display them in a 3D space. The 3D object can be rotated in space once loaded, either through mouse interaction, or with inputting specific pitch, roll and yaw values in the control window and pressing the W key, allowing the rotation to be continuously applied to the object.

Back Face Removal is implemented in the OBJModel class for performance enhancement, but can be removed for when trying to view models that aren't solid.

NOTE: WHEN LOADING AN OBJ FILE, YOU MUST: 
* Place the OBJ File into the obj folder in the project
* Type the name of the OBJ file into the program WITHOUT the .obj extension
* Input a scale, as each object file is of different size and may sometimes not be visible with a small size
* Wait for loading to finish after hitting the load button before pressing any other GUI elements, as OBJ loading freezes the GUI temporarily until the object is fully loaded


# JVM Arguments
Be sure to add these to the project's JVM:
--module-path "YOUR PATH TO JAVAFX HERE (Version 11.0.2)" --add-modules javafx.controls,javafx.fxml -Dprism.forceGPU=true -Dprism.verbose=true -Dprism.vsync=false
