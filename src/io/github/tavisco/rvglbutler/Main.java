package io.github.tavisco.rvglbutler;

import java.io.File;
import java.io.IOException;

import io.github.tavisco.rvglbutler.utils.Constants;
import io.github.tavisco.rvglbutler.view.HomeController;
import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("RVGL Butler");

		loadConfigs();
		initRootLayout();
		showHomeOverview();
	}

	private void loadConfigs() {
		
		// Get the RVGL Path
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(getPrimaryStage());

		if (selectedDirectory != null) {
			Constants consts = Constants.getInstance();
			consts.setRvglPath(selectedDirectory.getAbsolutePath());
			System.out.println("Setting RVGL path to: " + selectedDirectory.getAbsolutePath());
		} else {
			// No Directory selected
		}
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the home overview inside the root layout.
	 */
	public void showHomeOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Home.fxml"));
			AnchorPane home = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(home);

			// Give the controller access to the main app.
			HomeController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
