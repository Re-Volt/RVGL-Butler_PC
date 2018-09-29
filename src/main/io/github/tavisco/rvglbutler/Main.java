package main.io.github.tavisco.rvglbutler;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.io.github.tavisco.rvglbutler.utils.Configs;
import main.io.github.tavisco.rvglbutler.utils.Constants;
import main.io.github.tavisco.rvglbutler.view.HomeController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	

	@Override
	public void start(Stage primaryStage) {
		loadConfigs();
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("RVGL Butler");

		
		initRootLayout();
		showHomeOverview();
	}

	private void loadConfigs() {
		File confFile = new File("./butler.yaml");
		
		if (confFile.exists() && confFile.isFile()) {
	        Configs configs = Configs.getInstance();
	        if (!configs.isConfigsLoaded()) {
				System.out.println("WOW! Error while loading configs!");
			}
		} else {
			// No config file found!
			
			// Get the RVGL Path
			DirectoryChooser directoryChooser = new DirectoryChooser();
			File selectedDirectory = directoryChooser.showDialog(getPrimaryStage());

			if (selectedDirectory != null) {
				Configs configs = Configs.getInstance();
				configs.setRvglPath(selectedDirectory.getAbsolutePath());
				System.out.println("Setting RVGL path to: " + selectedDirectory.getAbsolutePath());
				
				// Get the RVGL executable
				for (File rvglExecutableFile : selectedDirectory.listFiles()) {
					for (String rvglExecutableName : Constants.RVGL_EXECUTABLES) {
						if (rvglExecutableName.equals(rvglExecutableFile.getName())) {
							configs.setRvglFound(true);
							configs.setRvglExecutable(rvglExecutableName);
						}
					}
				}
				
				if (configs.isRvglFound()) {
					System.out.println("Setting RVGL executable to: " + configs.getRvglExecutable());
					System.out.println("Saving configs...");
					// Create an ObjectMapper mapper for YAML
					ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

					// Write object as YAML file
					try {
						mapper.writeValue(confFile, configs);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println("Configs saved to : " + confFile.getAbsolutePath());
				}
				
				
			} else {
				// No Directory selected
			}
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
		BasicConfigurator.configure();
	    PropertyConfigurator
	        .configure("src/main/resources/log4j.properties");
		launch(args);
	}
}
