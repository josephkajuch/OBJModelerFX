////////////////////////////////
// CREATED BY JOSEPH KAJUCH
// NOT FOR REUSE
////////////////////////////////
package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/*
 * MODEL CLASS - BACK FACE DETECTION MAKES THESE MODELS WORK BEST WITH SOLID
 * MODEL OBJECTS! REMOVE BACK FACE DETECTION TO ALLOW FULL MODEL RENDERING 
 * IN THE DRAWLINES METHOD
 */
public class OBJModel {
	public double[][] v; // vertices
	public int[][][] f; // facets! (faces)
	public double[][] n; // normals
	public double[][] t; // textures
	final int dimensions = 3;
	String name;

	public OBJModel(double[][] vertices, int[][][] facets, double[][] normals,
			double[][] textures, String name) {
		v = vertices;
		f = facets;
		n = normals;
		t = textures;
		this.name = name;
	}

	// HANDLES ALL DRAWING
	protected void draw(boolean polys, boolean texts, boolean norms,
			GraphicsContext gc) {
		if (texts)
			drawTextures(gc);
		if (norms)
			drawNormals(gc);
		drawLines(gc, polys);
	};

	// ONLY WORKS FOR SOLIDS! BACK FACE DETECTION FOR SCREEN OPTIMIZATION
	private boolean isBackFace(double[] pA, double[] pB, double[] pC) {
		// BASIC ALGORITHM FOUND ONLINE - STACKOVERFLOW
		double ax = pA[0] - pA[1];
		double ay = pB[0] - pB[1];
		double bx = pA[0] - pA[2];
		double by = pB[0] - pB[2];
		double cz = ax * by - ay * bx;
		return cz < 0;
	};

	private void drawLines(GraphicsContext gc, boolean polys) {
		gc.setStroke(Color.LAWNGREEN);
		gc.setFill(Color.RED);
		// For loop iterates thru all polys/faces
		for (int i = 0; i < f.length; i++) {

			// FOR CUBE
			{
				if (i == 2) {
					gc.setFill(Color.GREEN);
				} else if (i == 4) {
					gc.setFill(Color.BLUE);
				} else if (i == 6) {
					gc.setFill(Color.YELLOW);
				} else if (i == 8) {
					gc.setFill(Color.PURPLE);
				} else if (i == 10) {
					gc.setFill(Color.ORANGE);
				}
			} // FOR CUBE

			int[] indV = new int[f[i][0].length];

			for (int j = 0; j < indV.length; j++)
				indV[j] = f[i][j][0]; // this V facet index

			double[] vx = new double[indV.length];
			double[] vy = new double[indV.length];
			double[] vz = new double[indV.length];

			for (int j = 0; j < vx.length; j++) {
				vx[j] = v[indV[j]][0];
				vy[j] = v[indV[j]][1];
				vz[j] = v[indV[j]][2];
			}

			// LINES -- A CHECK FOR THE DIRECTION OF THE NORMAL VECTOR
			// SHOULD BE DONE TO TELL ORIENTATION OF PLANE/DRAW PRIORITY

			// REMOVE THE isBackFace CALL TO STOP BACK FACE DETECTION
			if (isBackFace(vx, vy, vz) == false) {
				for (int j = 0; j <= vx.length - 1; j++) {
					if (j == vx.length - 1) {
						// OPTIMIZES - CHECKS IF ON FRAME
						if (onFrameOptimCheck(vx[j], vy[j], vx[0], vy[0]))
							gc.strokeLine(vx[j], vy[j], vx[0], vy[0]);
						break;
					}

					// OPTIMIZES - CHECKS IF ON FRAME
					if (onFrameOptimCheck(vx[j], vy[j], vx[j + 1], vy[j + 1]))
						gc.strokeLine(vx[j], vy[j], vx[j + 1], vy[j + 1]);
				}
				
				// POLYGON -- FOR CUBE
				if (polys)
					gc.fillPolygon(vx, vy, vx.length);
			}
		}
	}

	// Finds if any line has a point off the screen and doesn't draw it if so.
	// NOT THE BEST WAY TO OPTIMIZE!!!
	private boolean onFrameOptimCheck(double vx1, double vy1, double vx2,
			double vy2) {
		// If within frame bounds
		if ((vx1 > 0 && vx1 < Main.APP_WIDTH && vy1 > 0
				&& vy1 < Main.APP_HEIGHT)
				|| (vx2 > 0 && vx2 < Main.APP_WIDTH && vy2 > 0
						&& vy2 < Main.APP_HEIGHT)) {

			return true;
		}
		return false;
	}

	private void drawNormals(GraphicsContext gc) {
		gc.setStroke(Color.LAWNGREEN);
		gc.setFill(Color.RED);
		
		for (int i = 0; i < f.length; i++) {
			int[] indN = null;
			indN = new int[f[i][1].length];

			// if (indN != null) {
			for (int j = 0; j < indN.length; j++) {
				indN[j] = f[i][j][2];
			}

			double[] vnx = new double[indN.length];
			double[] vny = new double[indN.length];

			for (int j = 0; j < vnx.length; j++) {
				if (indN[j] == -1) {
					vnx = null;
					vny = null;
					break;
				}
				vnx[j] = n[indN[j]][0];
				vny[j] = n[indN[j]][1];
			}

			// LINES NORM
			if (vnx != null) {
				for (int j = 0; j <= vnx.length - 1; j++) {
					if (j == vnx.length - 1) {
						// OPTIMIZES - CHECKS IF ON FRAME
						if ((vnx[j] > 0 && vnx[j] < Main.APP_WIDTH && vny[j] > 0
								&& vny[j] < Main.APP_HEIGHT)
								|| (vnx[0] > 0 && vnx[0] < Main.APP_WIDTH
										&& vny[0] > 0
										&& vny[0] < Main.APP_HEIGHT))
							gc.strokeLine(vnx[j], vny[j], vnx[0], vny[0]);
						break;
					}
					// OPTIMIZES - CHECKS IF ON FRAME
					if ((vnx[j] > 0 && vnx[j] < Main.APP_WIDTH && vny[j] > 0
							&& vny[j] < Main.APP_HEIGHT)
							|| (vnx[j + 1] > 0 && vnx[j + 1] < Main.APP_WIDTH
									&& vny[j + 1] > 0
									&& vny[j + 1] < Main.APP_HEIGHT))
						gc.strokeLine(vnx[j], vny[j], vnx[j + 1], vny[j + 1]);
				}
			}
		}
	}

	private void drawTextures(GraphicsContext gc) {
		gc.setStroke(Color.LAWNGREEN);
		gc.setFill(Color.RED);
		
		for (int i = 0; i < f.length; i++) {
			int[] indT = null;
			indT = new int[f[i][2].length];

			for (int j = 0; j < indT.length; j++) {
				indT[j] = f[i][j][1];
			}

			double[] vtx = new double[indT.length];
			double[] vty = new double[indT.length];

			for (int j = 0; j < vtx.length; j++) {
				if (indT[j] == -1) {
					vtx = null;
					vty = null;
					break;
				}
				vtx[j] = t[indT[j]][0];
				vty[j] = t[indT[j]][1];
			}

			// LINES TEXT
			if (vtx != null) {
				for (int j = 0; j <= vtx.length - 1; j++) {
					if (j == vtx.length - 1) {
						// OPTIMIZES - CHECKS IF ON FRAME
						if ((vtx[j] > 0 && vtx[j] < Main.APP_WIDTH && vty[j] > 0
								&& vty[j] < Main.APP_HEIGHT)
								|| (vtx[0] > 0 && vtx[0] < Main.APP_WIDTH
										&& vty[0] > 0
										&& vty[0] < Main.APP_HEIGHT))
							gc.strokeLine(vtx[j], vty[j], vtx[0], vty[0]);
						break;
					}
					// OPTIMIZES - CHECKS IF ON FRAME
					if ((vtx[j] > 0 && vtx[j] < Main.APP_WIDTH && vty[j] > 0
							&& vty[j] < Main.APP_HEIGHT)
							|| (vtx[j + 1] > 0 && vtx[j + 1] < Main.APP_WIDTH
									&& vty[j + 1] > 0
									&& vty[j + 1] < Main.APP_HEIGHT))
						gc.strokeLine(vtx[j], vty[j], vtx[j + 1], vty[j + 1]);
				}
			}
		}
	}

	private void fillPolys(GraphicsContext gc) {
		gc.setFill(Color.RED);
		for (int i = 0; i < f.length; i++) {
			int[] ind = new int[f[i].length];

			for (int j = 0; j < ind.length; j++) {
				ind[j] = f[i][j][0];
			}
			double[] vx = new double[ind.length];
			double[] vy = new double[ind.length];
			for (int j = 0; j < vx.length; j++) {
				vx[j] = v[ind[j]][0];
				vy[j] = v[ind[j]][1];
			}
			gc.fillPolygon(vx, vy, vx.length);
		}
	}

	private void drawPoints(GraphicsContext gc) {
		// COLOR OF POINTS
		gc.setFill(Color.BLUE);
		for (int i = 0; i < v.length; i++)
			gc.fillOval(v[i][0] - 2.5, v[i][1] - 2.5, 5.0, 5.0);
	}

	// Gimbal Lock Occurs (when using on screen dragging for rotation)
	public void rotate3D(double pitch, double roll, double yaw) {
		double cosa = Math.cos(yaw);
		double sina = Math.sin(yaw);

		double cosb = Math.cos(pitch);
		double sinb = Math.sin(pitch);

		double cosc = Math.cos(roll);
		double sinc = Math.sin(roll);

		double Axx = cosa * cosb;
		double Axy = cosa * sinb * sinc - sina * cosc;
		double Axz = cosa * sinb * cosc + sina * sinc;

		double Ayx = sina * cosb;
		double Ayy = sina * sinb * sinc + cosa * cosc;
		double Ayz = sina * sinb * cosc - cosa * sinc;

		double Azx = -sinb;
		double Azy = cosb * sinc;
		double Azz = cosb * cosc;

		for (int i = 0; i < v.length; i++) {
			double px = v[i][0];
			double py = v[i][1];
			double pz = v[i][2];

			v[i][0] = Axx * (px - Main.centerX) + Axy * (py - Main.centerY)
					+ Axz * (pz - Main.centerZ) + Main.centerX;
			v[i][1] = Ayx * (px - Main.centerX) + Ayy * (py - Main.centerY)
					+ Ayz * (pz - Main.centerZ) + Main.centerY;
			v[i][2] = Azx * (px - Main.centerX) + Azy * (py - Main.centerY)
					+ Azz * (pz - Main.centerZ) + Main.centerZ;
		}

		for (int i = 0; i < n.length; i++) {
			double px = n[i][0];
			double py = n[i][1];
			double pz = n[i][2];

			n[i][0] = Axx * (px - Main.centerX) + Axy * (py - Main.centerY)
					+ Axz * (pz - Main.centerZ) + Main.centerX;
			n[i][1] = Ayx * (px - Main.centerX) + Ayy * (py - Main.centerY)
					+ Ayz * (pz - Main.centerZ) + Main.centerY;
			n[i][2] = Azx * (px - Main.centerX) + Azy * (py - Main.centerY)
					+ Azz * (pz - Main.centerZ) + Main.centerZ;
		}
	}

	public void scale(double deltaY) {
		if (deltaY < 0) {
			// scale up *
			double scale = -deltaY + 1;
			for (int i = 0; i < v.length; i++) {
				v[i][0] *= scale;
				v[i][1] *= scale;
				v[i][2] *= scale;
			}
		} else if (deltaY > 0) {
			// scale down /
			double scale = deltaY + 1;
			for (int i = 0; i < v.length; i++) {
				v[i][0] = (v[i][0] / scale);
				v[i][1] = (v[i][1] / scale);
				v[i][2] = (v[i][2] / scale);
			}
		}
	}

	private boolean backFaceRemoval(double[] vx, double[] vy, double[] vz) {
		// ALGORITHM FOUND ONLINE -- NOT USED
		
		double[] nx = new double[vx.length];
		double[] ny = new double[vy.length];
		double[] nz = new double[vz.length];

		// Calculate the normals of all points in this poly
		for (int j = 0; j < nx.length; j++) {
			// Normal<x,y,z> calculation
			double[] calc1 = new double[] { vx[2] - vx[1], vy[2] - vx[1],
					vz[2] - vz[1] };
			double[] calc2 = new double[] { vx[3] - vx[2], vy[3] - vx[2],
					vz[3] - vz[2] };

			// cross product of the 2 vectors == new x,y,z vector normal
			nx[j] = calc1[1] * calc2[2] - calc2[1] * calc1[2];
			ny[j] = calc2[0] * calc1[2] - calc1[0] * calc2[2];
			nz[j] = calc1[0] * calc2[1] - calc2[0] * calc1[1];
		}

		// Calculate Dot product N dot P
		double dotProduct = vx[0] * nx[0];

		// Test/plot whether surface is visible/not
		if (dotProduct >= 0)
			return true; // visible
		return false; // not visible

	}

}
