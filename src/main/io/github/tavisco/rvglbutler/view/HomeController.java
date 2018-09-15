package main.io.github.tavisco.rvglbutler.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import main.io.github.tavisco.rvglbutler.Main;

public class HomeController {
	@FXML
	private Button btnLaunch;
	@FXML
	private AnchorPane paneRvIo;
	@FXML
	private AnchorPane paneCars;
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
            FXMLLoader loaderIO = new FXMLLoader();
            loaderIO.setLocation(Main.class.getResource("view/tabs/RvIoTab.fxml"));
            paneRvIo.getChildren().setAll((AnchorPane) loaderIO.load());
            
            FXMLLoader loaderCars = new FXMLLoader();
            loaderCars.setLocation(Main.class.getResource("view/tabs/CarsTab.fxml"));
            paneCars.getChildren().setAll((AnchorPane) loaderCars.load());
            
            FXMLLoader loaderLevels = new FXMLLoader();
        	loaderLevels.setLocation(Main.class.getResource("view/tabs/LevelsTab.fxml"));
            paneLevels.getChildren().setAll((AnchorPane) loaderLevels.load());
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
