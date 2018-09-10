package main.io.github.tavisco.rvglbutler.view.tabs;

import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.io.github.tavisco.rvglbutler.model.IOPackageItem;
import main.io.github.tavisco.rvglbutler.utils.Constants;

public class RvIoTabController {

	@FXML
	private TableView<IOPackageItem> packageTable;
	@FXML
	private TableColumn<IOPackageItem, String> statusColumn;
	@FXML
	private TableColumn<IOPackageItem, String> nameColumn;
	@FXML
	private TableColumn<IOPackageItem, String> iOVersionColumn;
	@FXML
	private TableColumn<IOPackageItem, String> localVersionColumn;

	private ObservableList<IOPackageItem> ioPacks = FXCollections.observableArrayList();

	public RvIoTabController() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	private void initialize() {
		bindTableColumns();
		packageTable.setItems(ioPacks);
		loadRvIoPackages();
		setContextMenu();
	}
	
	private void setContextMenu() {
		MenuItem miInstall = new MenuItem("Install/Update");
		miInstall.setOnAction((ActionEvent event) -> {
			IOPackageItem item = packageTable.getSelectionModel().getSelectedItem();
		    System.out.println("Installing/Updating: " + item.getName());
		    item.download();
		});
		
		ContextMenu menu = new ContextMenu();
		menu.getItems().add(miInstall);
		packageTable.setContextMenu(menu);
		
	}

	private void loadRvIoPackages() {
		Task<Boolean> getRemoteVersionTask = new Task<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				getAllRvIoPackages();
				return true;
			}
		};
		
		new Thread(getRemoteVersionTask).start();
	}

	private void bindTableColumns() {
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUpdateStatus().getMessage()));
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		iOVersionColumn.setCellValueFactory(cellData -> cellData.getValue().getRemoteVersionProperty());
		localVersionColumn.setCellValueFactory(cellData -> cellData.getValue().getLocalVersionProperty());
		
	}

	private void getAllRvIoPackages() {
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config().setConnectTimeout(1500);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		ListenableFuture<Response> whenResponse = client.prepareGet(Constants.RVIO_AVAIABLE_PACKAGES_LINK).execute();
		try {
			Response response = whenResponse.get();
			fillRvIoPacksTable(response.getResponseBody());

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void fillRvIoPacksTable(String packs) {
		if (!packs.isEmpty()) {
			// Split on new lines
			String rvioPackages[] = packs.split("\\r?\\n");
			System.out.println("Found " + rvioPackages.length + " packs on RV/IO");
			for (String rvioPack : rvioPackages) {
				new IOPackageItem(rvioPack, ioPacks);
				//ioPacks.add(ioPack);
			}
		}
	}

}
