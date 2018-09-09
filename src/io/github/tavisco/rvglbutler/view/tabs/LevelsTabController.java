package io.github.tavisco.rvglbutler.view.tabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import io.github.tavisco.rvglbutler.model.LevelItem;
import io.github.tavisco.rvglbutler.utils.rvgl.FindLevels;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LevelsTabController {
	@FXML
	private TextField txtSearch;
	@FXML
	private TilePane tileLevels;

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public LevelsTabController() {
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
		tileLevels.setPadding(new Insets(15, 15, 15, 15));
		tileLevels.setHgap(15);
		tileLevels.setVgap(15);

		// String levelPath = "/home/tavisco/.rvgl/levels";
		// String basePath = "/home/tavisco/.rvgl";
		// File levelFolder = new File(levelPath);

		/*for (File levelFile : levelFolder.listFiles()) {
			LevelItem level = new LevelItem();
			level.setName(levelFile.getName());
			level.setItemPath(levelFile.getName());
			level.setBasePath(basePath);

			File lvlImage = new File(level.getImagePath());*/
		
		List<LevelItem> allLevels = FindLevels.getAllLevels();

		for (LevelItem level : allLevels) {
			ImageView imgView;
			imgView = createImageView(new File(level.getImagePath()));

			if (imgView != null) {
				final ContextMenu contextMenu = new ContextMenu();
			    final MenuItem itemDelete = new MenuItem("Delete");

			    contextMenu.getItems().addAll(itemDelete);
			    contextMenu.setAutoHide(true);
			    
			    itemDelete.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        System.out.println("Deleting " + level.getName());
                    }
                });
			    
			    imgView.setOnContextMenuRequested(e -> 
			    contextMenu.show(imgView, e.getScreenX(), e.getScreenY()));
				
				imgView.setPreserveRatio(true);

				Label lblLevelName = new Label(level.getName());
				// lblLevelName.setPadding(new Insets(15, 0, 0, 0));
				VBox pane = new VBox();

				pane.getChildren().add(imgView);
				pane.getChildren().add(lblLevelName);
				//pane.setPrefHeight(imgView.getFitHeight() + 90);

				pane.setAlignment(Pos.BOTTOM_CENTER);
				tileLevels.getChildren().addAll(pane);
			}
		}
		
			
		//}
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
