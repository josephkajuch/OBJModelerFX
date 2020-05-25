////////////////////////////////
// CREATED BY JOSEPH KAJUCH
// NOT FOR REUSE
////////////////////////////////
package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/*
 * CLASS IS UNUSED - WAS ORIGINALLY USED FOR BASIC MODELING BEFORE OBJPARSER
 * WAS FULLY IMPLEMENTED
 */
public class Cube {
	public double[][] v; // vertices
	// public double[] x, y;
	final int numVertices = 8;
	final static double CS = 400; // cube size
	final int dimensions = 3;
	protected double[] currCubeSideX = new double[4];
	protected double[] currCubeSideY = new double[4];
	int[] sidePriority;

	// x,y,z start positinos
	public Cube(double x, double y, double z) {
		v = new double[numVertices][dimensions];
		v[0][0] = x;
		v[0][1] = y;
		v[0][2] = z;

		v[1][0] = x + CS;
		v[1][1] = y;
		v[1][2] = z;

		v[2][0] = x;
		v[2][1] = y;
		v[2][2] = z + CS;

		v[3][0] = x + CS;
		v[3][1] = y;
		v[3][2] = z + CS;

		v[4][0] = x;
		v[4][1] = y + CS;
		v[4][2] = z;

		v[5][0] = x + CS;
		v[5][1] = y + CS;
		v[5][2] = z;

		v[6][0] = x;
		v[6][1] = y + CS;
		v[6][2] = z + CS;

		v[7][0] = x + CS;
		v[7][1] = y + CS;
		v[7][2] = z + CS;
	}

	public void draw(GraphicsContext gc) {
		updateSidePriority();
		fillSides(gc);
		drawLines(gc);
		drawPoints(gc);

	}

	private void updateSidePriority() {

	}

	private void drawLines(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(3);
		// A
		gc.strokeLine(v[0][0], v[0][1], v[1][0], v[1][1]);
		gc.strokeLine(v[0][0], v[0][1], v[2][0], v[2][1]);
		gc.strokeLine(v[1][0], v[1][1], v[3][0], v[3][1]);
		gc.strokeLine(v[2][0], v[2][1], v[3][0], v[3][1]);

		// B
		gc.strokeLine(v[4][0], v[4][1], v[5][0], v[5][1]);
		gc.strokeLine(v[4][0], v[4][1], v[6][0], v[6][1]);
		gc.strokeLine(v[5][0], v[5][1], v[7][0], v[7][1]);
		gc.strokeLine(v[6][0], v[6][1], v[7][0], v[7][1]);

		// A --> B
		gc.strokeLine(v[0][0], v[0][1], v[4][0], v[4][1]);
		gc.strokeLine(v[1][0], v[1][1], v[5][0], v[5][1]);
		gc.strokeLine(v[2][0], v[2][1], v[6][0], v[6][1]);
		gc.strokeLine(v[3][0], v[3][1], v[7][0], v[7][1]);
	}

	private void drawPoints(GraphicsContext gc) {
		// COLOR OF POINTS
		gc.setFill(Color.BLUE);
		for (int i = 0; i < v.length; i++)
			gc.fillOval(v[i][0] - 5.0, v[i][1] - 5.0, 10.0, 10.0);
	}


	public void makeSide(int side, GraphicsContext gc) {
		switch (side) {
		// TOP
		case 0:
			gc.setFill(Color.RED);
			currCubeSideX[0] = v[0][0];
			currCubeSideX[1] = v[2][0];
			currCubeSideX[2] = v[3][0];
			currCubeSideX[3] = v[1][0];

			currCubeSideY[0] = v[0][1];
			currCubeSideY[1] = v[2][1];
			currCubeSideY[2] = v[3][1];
			currCubeSideY[3] = v[1][1];
			break;
		// LEFT
		case 1:
			gc.setFill(Color.GREEN);
			currCubeSideX[0] = v[0][0];
			currCubeSideX[1] = v[2][0];
			currCubeSideX[2] = v[6][0];
			currCubeSideX[3] = v[4][0];

			currCubeSideY[0] = v[0][1];
			currCubeSideY[1] = v[2][1];
			currCubeSideY[2] = v[6][1];
			currCubeSideY[3] = v[4][1];
			break;
		// RIGHT
		case 2:
			gc.setFill(Color.BLUE);
			currCubeSideX[0] = v[1][0];
			currCubeSideX[1] = v[3][0];
			currCubeSideX[2] = v[7][0];
			currCubeSideX[3] = v[5][0];

			currCubeSideY[0] = v[1][1];
			currCubeSideY[1] = v[3][1];
			currCubeSideY[2] = v[7][1];
			currCubeSideY[3] = v[5][1];
			break;

		// BOTTOM
		case 3:
			gc.setFill(Color.RED);
			currCubeSideX[0] = v[4][0];
			currCubeSideX[1] = v[6][0];
			currCubeSideX[2] = v[7][0];
			currCubeSideX[3] = v[5][0];

			currCubeSideY[0] = v[4][1];
			currCubeSideY[1] = v[6][1];
			currCubeSideY[2] = v[7][1];
			currCubeSideY[3] = v[5][1];
			break;
		// FRONT
		case 4:
			gc.setFill(Color.PINK);
			currCubeSideX[0] = v[0][0];
			currCubeSideX[1] = v[1][0];
			currCubeSideX[2] = v[5][0];
			currCubeSideX[3] = v[4][0];

			currCubeSideY[0] = v[0][1];
			currCubeSideY[1] = v[1][1];
			currCubeSideY[2] = v[5][1];
			currCubeSideY[3] = v[4][1];
			break;

		// BACK
		case 5:
			gc.setFill(Color.ORANGE);
			currCubeSideX[0] = v[2][0];
			currCubeSideX[1] = v[3][0];
			currCubeSideX[2] = v[7][0];
			currCubeSideX[3] = v[6][0];

			currCubeSideY[0] = v[2][1];
			currCubeSideY[1] = v[3][1];
			currCubeSideY[2] = v[7][1];
			currCubeSideY[3] = v[6][1];
			break;
		}
	}

	private void fillSides(GraphicsContext graphic) {
		for (int j = 0; j < 6; j++) {
			makeSide(j, graphic);
			graphic.fillPolygon(currCubeSideX, currCubeSideY, 4);
		}
	}

}
