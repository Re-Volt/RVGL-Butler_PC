package io.github.tavisco.rvglbutler.view;

import java.io.IOException;

import io.github.tavisco.rvglbutler.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class HomeController {
	@FXML
	private Button btnLaunch;
	@FXML
	private AnchorPane paneLevels;
	
	// Reference to the main application.
    private Main mainApp;
    
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public HomeController() {

    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	try {
        	FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/tabs/LevelsTab.fxml"));
			//paneLevels = (AnchorPane) loader.load();
            paneLevels.getChildren().setAll((AnchorPane) loader.load());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    @FXML
    private void onBtnLaunchClicked() {
    	System.out.println("Launching RVGL...");
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

    }
}
