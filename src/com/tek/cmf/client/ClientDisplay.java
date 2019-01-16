package com.tek.cmf.client;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ClientDisplay extends Application {

	public static int width;
	public static int height;
	public static Image frame;
	
	private static final int MIN_WIDTH = 500;
	
	public static void run(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage window) throws Exception {
		if(width < MIN_WIDTH) {
			double factor = (double)MIN_WIDTH / (double)width;
			width = MIN_WIDTH;
			height = (int)(factor * (double)height);
		}
		
		window.setTitle("Client Display");
		Canvas canvas = new Canvas(width, height);
		
		new AnimationTimer() {
			public void handle(long now) {
				GraphicsContext gc = canvas.getGraphicsContext2D();
				if(frame != null) {
					gc.drawImage(frame, 0, 0, width, height);
				}
			};
		}.start();
		
		StackPane root = new StackPane();
		root.getChildren().add(canvas);
		window.setScene(new Scene(root, width, height));
		window.setResizable(false);
		window.show();
	}

}
