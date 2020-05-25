////////////////////////////////
// CREATED BY JOSEPH KAJUCH
// NOT FOR REUSE
////////////////////////////////
package application;

import javafx.animation.AnimationTimer;
import java.awt.event.KeyEvent;
import java.io.File;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import processing.javafx.PSurfaceFX;

/*
 * DRIVES THE JAVAFX APPLICATION FOR VIEWING 3D OBJ MODEL FILES
 * 
 * USE JVM ARGUMENTS:
 * --module-path "YOUR JAVAFX PATH HERE (BUILT USING 11.0.2)" 
 * --add-modules javafx.controls, javafx.fxml 
 * -Dprism.forceGPU=true 
 * -Dprism.verbose=true 
 * -Dprism.vsync=false
 */
public class Main extends Application {

	// APP CONSTANTS/PROPERTIES
	private static final String APP_TITLE = "FX3DVec3";
	public static int APP_WIDTH = 1250;
	public static int APP_HEIGHT = 800;
	public static int centerX = APP_WIDTH / 2;
	public static int centerY = APP_HEIGHT / 2;
	public static int centerZ = 0;
	Image icon;
	int state;
	public static boolean moveit = false; // Determines if rotation occurs
	boolean pause = false;
	boolean lines = true;
	boolean polys = false;
	boolean textures = false;
	boolean normals = false;
	boolean points = true;
	boolean info = true;
	
	// CALCULATION VARIABLES
	static double pitch, roll, yaw;
	boolean clicked = false;
	double[] firstClick = new double[3]; // stores first click location
	double[] currClick = new double[3]; // current click location
	double speedX = 0.0001;
	double speedY = -0.0001;
	double speedZ = -0.000001;
	double clickXLoc = 0; // Click locations stored in these 2 vars
	double clickYLoc = 0; 

	// FOR MODELS
	OBJGroup currModelGroup;
	OBJParser modeler;

	// IF CUBES IS USED -- UNUSED
	Cube[] cubes;

	private void initiate() {
		pitch = Math.PI / 150;
		roll = 0;
		yaw = Math.PI / 100;
		state = 1;
		modeler = new OBJParser();
	}
	
	private void drawAxes(GraphicsContext gc) {
		// ZAXIS PORTRUDES TOWARDS YOU!
		// YAXIS
		gc.setStroke(Color.BLUE);
		gc.strokeLine(0, centerY, APP_WIDTH, centerY);
		// YAXIS
		gc.setStroke(Color.RED);
		gc.strokeLine(centerX, 0, centerX, APP_HEIGHT);
	}

	private void drawInfo(GraphicsContext graphic) {
		graphic.setFill(Color.WHITE);
		graphic.setFont(new Font(25));
		graphic.fillText("3D OBJ MODEL VIEWER", APP_WIDTH / 100,
				APP_HEIGHT / 20);
		graphic.setFont(new Font(15));

		graphic.fillText("by: Joseph Kajuch", (APP_WIDTH / 100),
				(APP_HEIGHT / 12));

		graphic.setFont(Font.getDefault());
		graphic.setFill(Color.AQUA);
		graphic.fillText(("ORIGIN IS x: " + centerX + " y: " + centerY),
				(APP_WIDTH / 100), ((APP_HEIGHT / 10)) * 9);

	}

	private void paintState0(GraphicsContext graphic) {
		graphic.setFill(Color.RED);
		// graphic.fillRect(centerX/2, centerY/2, APP_WIDTH/2, APP_HEIGHT/2);
		graphic.fillRect(0, 0, APP_WIDTH / 2, APP_HEIGHT / 2);
	}

	private void paintState1(GraphicsContext graphic) {
		// BACKGROUND
		graphic.setLineDashes(0);
		graphic.setFill(Color.BLACK);
		graphic.fillRect(0, 0, APP_WIDTH, APP_HEIGHT);

		drawAxes(graphic);

		if (info)
			drawInfo(graphic);

		if (currModelGroup != null) {
			if (moveit)
				currModelGroup.rotateAll3D(pitch, roll, yaw);
			currModelGroup.drawAll(polys, textures, normals, graphic);
		}

		// ORIGIN
		graphic.setFill(Color.LIGHTGREEN);
		graphic.fillOval(centerX - 5, centerY - 5, 10.0, 10.0);
		
		if (clicked) {
			graphic.setFill(Color.CYAN);
			graphic.fillOval(clickXLoc - 2.5, clickYLoc - 2.5, 5.0, 5.0);
			graphic.setStroke(Color.WHITE);
			graphic.setLineDashes(10);
			graphic.strokeLine(clickXLoc, clickYLoc, currClick[0],
					currClick[1]);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		icon = new Image("icon.png");

		/************** SCENE/STAGE SETUP **************/

		BorderPane root = new BorderPane();
		Pane bottomBarPane = new Pane();
		Pane canvPane = new Pane();
		root.setCenter(canvPane);
		Scene controls = new Scene(bottomBarPane);
		Stage secondStage = new Stage();
		{
			secondStage.setScene(controls);
			secondStage.setResizable(false);
			secondStage.setX(APP_WIDTH - 25);
			secondStage.setY(APP_HEIGHT / 4);
			secondStage.getIcons().setAll(icon);
		}

		Scene theScene = new Scene(root);
		{
			primaryStage.setScene(theScene);
			primaryStage.setWidth(APP_WIDTH);
			primaryStage.setHeight(APP_HEIGHT);
			primaryStage.setResizable(true);
			primaryStage.setX(-10);
			primaryStage.setY(15);
			primaryStage.getIcons().setAll(icon);
		}

		/****************** UI ELEMENTS ******************/

		Canvas canvas = new Canvas(APP_WIDTH, APP_HEIGHT - 90);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		VBox bottomBar = new VBox();
		{
			bottomBar.setAlignment(Pos.TOP_CENTER);
			bottomBar.setPrefSize(APP_WIDTH / 4, 150 * 3);
		}

		TextField f1 = new TextField();
		{
			f1.setEditable(true);
			f1.setPrefSize(APP_WIDTH / 4, 50);
			f1.setPromptText("PITCH");
		}

		TextField f2 = new TextField();
		{
			f2.setEditable(true);
			f2.setPrefSize(APP_WIDTH / 4, 50);
			f2.setPromptText("ROLL");
		}

		TextField f3 = new TextField();
		{
			f3.setEditable(true);
			f3.setPrefSize(APP_WIDTH / 4, 50);
			f3.setPromptText("YAW");
		}

		TextArea a1 = new TextArea();
		{
			a1.setText("CURRENT VALUES:\nPITCH: " + pitch + "\nROLL: " + roll
					+ "\nYAW: " + yaw);
			a1.setEditable(false);
		}

		TextField f4 = new TextField();
		{
			f4.setEditable(true);
			f4.setPrefSize(APP_WIDTH / 8, 50);
			f4.setPromptText("Model File Name (No .obj)");
		}

		CheckBox showPolys = new CheckBox();
		showPolys.setOnAction(e -> {
			if (showPolys.isSelected()) {
				polys = true;
			} else
				polys = false;
		});

		Label polysLabel = new Label("Polygons (cube) ");
		polysLabel.setFont(new Font(12));

		CheckBox showTexts = new CheckBox();
		showTexts.setOnAction(e -> {
			if (showTexts.isSelected()) {
				textures = true;
			} else
				textures = false;
		});

		Label texturesLabel = new Label(" Textures (exp) ");
		texturesLabel.setFont(new Font(12));

		CheckBox showNorms = new CheckBox();
		showNorms.setOnAction(e -> {
			if (showNorms.isSelected()) {
				normals = true;
			} else
				normals = false;
		});

		Label normalsLabel = new Label(" Normals (exp) ");
		normalsLabel.setFont(new Font(12));

		HBox polysBox = new HBox();
		polysBox.getChildren().addAll(polysLabel, showPolys, texturesLabel,
				showTexts, normalsLabel, showNorms);

		TextField f5 = new TextField();
		{
			f5.setEditable(true);
			f5.setPrefSize(APP_WIDTH / 8, 50);
			f5.setPromptText("Model Scale");
		}

		HBox loadBox = new HBox();
		loadBox.getChildren().addAll(f4, f5);

		Button loadButton = new Button();
		{
			// loadButton.setStyle("-fx-focus-color: black;");
			loadButton.setPrefSize(APP_WIDTH / 7, 50);
			loadButton.setText("Load Model");
			loadButton.setAlignment(Pos.CENTER);
			loadButton.setOnAction(e -> {

				try {
					modeler.scale = -Double.parseDouble(f5.getText());
					currModelGroup = modeler.loadModel(
							"obj" + File.separator + f4.getText() + ".obj", gc);
				} catch (Exception e1) {
					System.err.println(
							"Couldn't load model (check filename/scale");

				}

			});
		}

		Button spacer = new Button();
		{
			spacer.setVisible(false);
			spacer.setPrefHeight(10);
		}

		Button spacer2 = new Button();
		{
			spacer2.setVisible(false);
			spacer2.setPrefHeight(10);
		}

		Button inputButton = new Button();
		{
			inputButton.setPrefSize(APP_WIDTH / 7, 50);
			inputButton.setText("Change Rotation");
			inputButton.setAlignment(Pos.CENTER);
			inputButton.setOnAction(e -> {

				try {
					pitch = Double.parseDouble(f1.getText());
					roll = Double.parseDouble(f2.getText());
					yaw = Double.parseDouble(f3.getText());
					a1.setText("CURRENT VALUES:\nPITCH: " + pitch + "\nROLL: "
							+ roll + "\nYAW: " + yaw);
					f1.clear();
					f2.clear();
					f3.clear();
				} catch (NumberFormatException e1) {
					// skip bad input
				}
			});
			f3.setOnKeyPressed(e1 -> {
				if (e1.getCode().getCode() == KeyEvent.VK_ENTER) {
					inputButton.fire();
				}
			});
		}

		bottomBar.getChildren().addAll(inputButton, spacer2, f1, f2, f3, spacer,
				a1, polysBox, loadBox, loadButton);
		bottomBarPane.getChildren().add(bottomBar);
		canvPane.getChildren().add(canvas);
		canvas.widthProperty().bind(canvPane.widthProperty());
		canvas.heightProperty().bind(canvPane.heightProperty());

		/**************** INPUT HANDLERS **********************/

		myMouseHandlers(root, a1);

		theScene.setOnKeyPressed(e1 -> {
			if (e1.getCode().getCode() == KeyEvent.VK_R) {
				initiate();
			} else if (e1.getCode().getCode() == KeyEvent.VK_SPACE) {
				pause = true;
			} else if (e1.getCode().getCode() == KeyEvent.VK_W) {
				moveit = true;
			} else if (e1.getCode().getCode() == KeyEvent.VK_S) {
				moveit = false;
			}
		});

		primaryStage.setOnCloseRequest(e1 -> {
			// primaryStage.close();
			secondStage.close();
		});

		theScene.setOnKeyReleased(e1 -> {
			if (e1.getCode().getCode() == KeyEvent.VK_SPACE)
				pause = false;
		});

		/************** ANIMATION TIMER *****************/

		initiate();

		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				// TODO: make thread of its own for parser so project doesn't
				// freeze while loading OBJ Files

				APP_WIDTH = (int) primaryStage.getWidth();
				APP_HEIGHT = (int) primaryStage.getHeight();

				if (pause == false) {
					centerX = APP_WIDTH / 2;
					centerY = APP_HEIGHT / 2;
					switch (state) {
					case 0:
						paintState0(gc);
						break;
					case 1:
						paintState1(gc);
						break;
					}
				}
			}
		}.start();

		primaryStage.setTitle(APP_TITLE);
		primaryStage.show();
		secondStage.setTitle("Controls");
		secondStage.show();
	}

	private void myMouseHandlers(BorderPane root, TextArea a1) {
		root.setOnScroll(e -> {

			System.out.println(e.getDeltaY());
			if (currModelGroup != null)
				currModelGroup.scaleAll(e.getDeltaY());

		});

		root.setOnMousePressed(e -> {
			if (clicked == false) {
				clickXLoc = e.getX();
				clickYLoc = e.getY();
			}

			pitch = 0;
			roll = 0;
			yaw = 0;
			currClick[0] = e.getX();
			currClick[1] = e.getY();
			currClick[2] = e.getZ();
			firstClick[0] = e.getX();
			firstClick[1] = e.getY();
			firstClick[2] = e.getZ();
			clicked = true;

		});

		root.setOnMouseDragged(e -> {
			moveit = true;
			currClick[0] = e.getX();
			currClick[1] = e.getY();
			currClick[2] = e.getZ();

			pitch += ((firstClick[0] - currClick[0])) * speedX;
			roll += ((firstClick[1] - currClick[1])) * speedY;

			// GET Z DOESNT GET ANYTHING. JUST ZERO!
			yaw += ((firstClick[2] - currClick[2])) * speedZ;
			firstClick[0] = currClick[0];
			firstClick[1] = currClick[1];
			firstClick[2] = currClick[2];

			a1.setText("CURRENT VALUES:\nPITCH: " + pitch + "\nROLL: " + roll
					+ "\nYAW: " + yaw);
		});

		root.setOnMouseDragOver(e -> {
			moveit = false;
		});

		root.setOnMouseReleased(e -> {

			clicked = false;
			moveit = false;
		});
	}

	// A FEW BASIC SHAPES CONSTRUCTED FROM CUBES -- ALL UNUSED

	@SuppressWarnings("unused")
	private void cube9() {
		cubes = new Cube[10];

		double inc = -Cube.CS;
		for (int i = 0; i < cubes.length / 2; i++)
			cubes[i] = new Cube(centerX + (inc += Cube.CS), centerY, centerZ);

		inc = -Cube.CS;
		for (int i = 4; i < cubes.length - 2; i++)
			cubes[i] = new Cube(centerX + (inc += Cube.CS),
					centerY + 2 * Cube.CS, centerZ);

		cubes[8] = new Cube(centerX, centerY + (Cube.CS), centerZ);
		cubes[9] = new Cube(centerX + 3 * Cube.CS, centerY + (Cube.CS),
				centerZ);
	}

	@SuppressWarnings("unused")
	private void cross8() {
		// cross
		double inc = -Cube.CS;
		cubes = new Cube[8];
		for (int i = 0; i < cubes.length / 2; i++)
			cubes[i] = new Cube(centerX + (inc += Cube.CS), centerY, centerZ);
		inc = -2 * Cube.CS;
		for (int i = 3; i < cubes.length; i++)
			cubes[i] = new Cube(centerX + Cube.CS, centerY + (inc += Cube.CS),
					centerZ);
	}

	@SuppressWarnings("unused")
	private void cube1() {
		cubes = new Cube[1];
		cubes[0] = new Cube(centerX - Cube.CS / 2, centerY - Cube.CS / 2,
				centerZ - Cube.CS / 2);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
