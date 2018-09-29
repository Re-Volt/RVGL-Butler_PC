package main.io.github.tavisco.rvglbutler.view.tabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import main.io.github.tavisco.rvglbutler.model.CarItem;
import main.io.github.tavisco.rvglbutler.utils.rvgl.FindCars;

public class CarsTabController {
	@FXML
	private TextField txtSearch;
	@FXML
	private TilePane tileCars;

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public CarsTabController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		loadLevels();
	}

	private void loadLevels() {
		tileCars.setPadding(new Insets(15, 15, 15, 15));
 		tileCars.setHgap(15);
		tileCars.setVgap(15);
		
		List<CarItem> allCars = FindCars.getAllCars();

		for (CarItem car : allCars) {
			ImageView imgView;
			
			if (car.getImagePath() == null)
				continue;
			
			imgView = createImageView(new File(car.getImagePath()));

			if (imgView != null) {
				final ContextMenu contextMenu = new ContextMenu();
			    final MenuItem itemDelete = new MenuItem("Delete");

			    contextMenu.getItems().addAll(itemDelete);
			    contextMenu.setAutoHide(true);
			    
			    itemDelete.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        car.deleteItem();
                    }
                });
			    
			    imgView.setOnContextMenuRequested(e -> 
			    contextMenu.show(imgView, e.getScreenX(), e.getScreenY()));
				
				imgView.setPreserveRatio(true);

				Label lblLevelName = new Label(car.getName());
				VBox vbox = new VBox();

				vbox.getChildren().add(imgView);
				vbox.getChildren().add(lblLevelName);

				vbox.setAlignment(Pos.BOTTOM_CENTER);
				tileCars.getChildren().add(vbox);
			}
		}
	}

	private ImageView createImageView(final File imageFile) {
		// DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
		// The last two arguments are: preserveRatio, and use smooth (slower)
		// resizing

		ImageView imageView = null;
		try {
			final Image image = new Image(new FileInputStream(imageFile), 150, 0, true, true);
			imageView = new ImageView(image);
			imageView.setFitWidth(150);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return imageView;
	}
}
