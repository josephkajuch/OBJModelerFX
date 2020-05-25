////////////////////////////////
// CREATED BY JOSEPH KAJUCH
// NOT FOR REUSE
////////////////////////////////
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class OBJParser {

	File file;
	Scanner scnr; // FOR PARSING CAPABILITIES
	public double scale = -20; // NEGATIVE SCALE PUTS OBJECT RIGHT SIDE UP

	@SuppressWarnings("unused")
	private void loadingScreen(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.fillRect(Main.centerX / 2, Main.centerY / 2, Main.centerX,
				Main.centerY);
	}

	public OBJGroup loadModel(String fileName, GraphicsContext gc) {
		file = new File(fileName);

		try {
			double[][] vertices;
			int[][][] facets;
			double[][] normals;
			double[][] textures;
			ArrayList<double[]> vert = new ArrayList<>();
			ArrayList<double[]> text = new ArrayList<>();
			ArrayList<double[]> norm = new ArrayList<>();
			ArrayList<int[][]> fac = new ArrayList<>();

			scnr = new Scanner(file);
			String name = "default";
			String line;
			String lineHead;
			int k = 0;
			ArrayList<OBJModel> group = new ArrayList<>();
			// ADD G AND S CASE IMPLEMENTATIONS - TODO

			// NOTE: ALL LINES LOADED ARE PRINTED TO CONSOLE DURING LOADING,
			// AND LOADING FREEZES THE FRONT END APPLICATION WHILE THE LOADING
			// OCCURS ON THE BACKEND
			do {
				if (scnr.hasNext() == false)
					break;
				lineHead = scnr.next().trim();
				k++;
				switch (lineHead.charAt(0)) {
				case 'g':
					System.out.print("Line " + k + " " + lineHead + " ");
					System.out.println(scnr.nextLine());
					break;
				// UNIMPLEMENTED WORK:
//					System.out.print("Line " + k + " " + lineHead + " ");
//					line = scnr.nextLine().trim();
//					System.out.println(line);
//					
//					vertices = new double[vert.size()][3];
//					for (int i = 0; i < vertices.length; i++)
//						vertices[i] = vert.get(i);
//
//					vert =  new ArrayList<>();
//					textures = new double[text.size()][3];
//					for (int i = 0; i < textures.length; i++)
//						textures[i] = text.get(i);
//
//					normals = new double[norm.size()][3];
//					for (int i = 0; i < normals.length; i++)
//						normals[i] = norm.get(i);
//
//					norm = new ArrayList<>();
//					// EACH LINE IS A FACE
//					// EACH FACE HAS A 2D ARRAY WHERE THE 0 ARRAY IS V, 1 IS T, 2 IS N
//					facets = new int[fac.size()][][];
//					for (int i = 0; i < facets.length; i++)
//						facets[i] = fac.get(i);
//
//					fac =  new ArrayList<>();
//					
//					OBJModel model = new OBJModel(vertices, facets, normals, textures,
//							name);
//					group.add(model);
//					
//					break;
				case 'v':
					System.out.print("Line " + k + " " + lineHead + " ");
					line = scnr.nextLine().trim();
					System.out.println(line);
					String[] vertexS = line.split(" +");
					double[] vertexD = new double[3];
					vertexD[0] = (Double.parseDouble(vertexS[0]) * scale)
							+ Main.centerX;
					if (vertexS.length > 1) {
						try {
							vertexD[1] = (Double.parseDouble(vertexS[1])
									* scale) + Main.centerY;
						} catch (Exception e) {
							vertexD[1] = -1;
						}
					}
					if (vertexS.length > 2)
						vertexD[2] = (Double.parseDouble(vertexS[2]) * scale)
								+ Main.centerZ;
					if (lineHead.contentEquals("vn")) {
						norm.add(vertexD);
					} else if (lineHead.contentEquals("vt")) {
						text.add(vertexD);
					} else {
						vert.add(vertexD);
					}
					break;
				case 'f':

					// FACETS - Subtract 1 from each index
					line = scnr.nextLine().trim();
					System.out.println(line);

					System.out
							.println("Line " + k + " " + lineHead + " " + line);
					String[] vertexSt = line.split(" +");

					int[][] vertexD2 = new int[vertexSt.length][];

					if (line.contains("/") == false) {
						for (int i = 0; i < vertexD2.length; i++) {
							vertexD2[i] = new int[] {
									Integer.parseInt(vertexSt[i]) - 1, -1, -1 };
						}
						fac.add(vertexD2);

					} else {
						for (int i = 0; i < vertexSt.length; i++) {
							String[] facetItem = vertexSt[i].split("/");
							int[] vertexA = new int[3];
							
							if (facetItem[0] != null) {
								vertexA[0] = Integer.parseInt(facetItem[0]) - 1;
							}
							if (facetItem[1].isBlank() == false) {
								vertexA[1] = Integer.parseInt(facetItem[1]) - 1;
							} else {
								vertexA[1] = -1;
							}
							if (facetItem.length > 2
									&& facetItem[2].isBlank() == false) {
								vertexA[2] = Integer.parseInt(facetItem[2]) - 1;
							} else {
								vertexA[2] = -1;
							}
							vertexD2[i] = vertexA;
						}
						fac.add(vertexD2);
					}
					break;
				case ' ':
				case '\n':
				case '#':
				case 's':
				default:
					// SKIPS UNRECOGNIZED LINES
					System.out.print("Line " + k + " " + lineHead + " ");
					System.out.println(scnr.nextLine());
					break;
				}

			} while (scnr.hasNextLine());

			// SET UP OBJECT BY PUTTING INTO ARRAYS FOR BETTER MEM SPEED
			vertices = new double[vert.size()][3];
			for (int i = 0; i < vertices.length; i++)
				vertices[i] = vert.get(i);

			textures = new double[text.size()][3];
			for (int i = 0; i < textures.length; i++)
				textures[i] = text.get(i);

			normals = new double[norm.size()][3];
			for (int i = 0; i < normals.length; i++)
				normals[i] = norm.get(i);

			// EACH LINE IS A FACE
			// EACH FACE HAS A 2D ARRAY WHERE THE 0 ARRAY IS V, 1 IS T, 2 IS N
			facets = new int[fac.size()][][];
			for (int i = 0; i < facets.length; i++)
				facets[i] = fac.get(i);

			OBJModel model = new OBJModel(vertices, facets, normals, textures,
					name);
			group.add(model);
			scnr.close();
			return new OBJGroup(group);
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found");
		} catch (Exception e) {
			System.err.println("Error Parsing");
			e.printStackTrace();
			System.exit(1); // TO AVOID NULLPOINTER
		}
		return null;
	}

	public void centerModel(OBJModel model) {
		// TODO: Center model based on where origin is of Main
	}
}
