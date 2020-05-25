////////////////////////////////
// CREATED BY JOSEPH KAJUCH
// NOT FOR REUSE
////////////////////////////////
package application;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

/*
 * THIS GROUPING OF OBJECTS WILL ALLOW FURTHER MANIPULATION OF SEPARATE 
 * MODELED PARTS ON A MODEL -- NOT YET FULLY IMPLEMENTED.
 */
public class OBJGroup {
	public OBJGroup(ArrayList<OBJModel> parts) {
		this.parts = new OBJModel[parts.size()]; 
		for (int i = 0; i < parts.size(); i++) {
			this.parts[i] = parts.get(i);
		}
	}
	
	OBJModel[] parts; // ALL PARTS OF THE OBJECT FILE PARSED
	
	public void drawAll(boolean polys, boolean texts, boolean norms, GraphicsContext gc) {
		for (int i = 0; i < parts.length; i++) {
			parts[i].draw(polys, texts, norms, gc);
		}
	}

	public void rotateAll3D(double pitch, double roll, double yaw) {
		for (int i = 0; i < parts.length; i++) {
			parts[i].rotate3D(pitch, roll, yaw);
		}
	}
	
	public void scaleAll(double deltaY) {
		for (int i = 0; i < parts.length; i++) {
			parts[i].scale(deltaY);
		}
	}
	
}
