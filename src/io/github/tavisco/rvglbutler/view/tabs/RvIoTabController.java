package io.github.tavisco.rvglbutler.view.tabs;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import io.github.tavisco.rvglbutler.model.IOPackageItem;
import io.github.tavisco.rvglbutler.utils.Constants;
import io.netty.util.concurrent.Future;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				loadRvIoPackages();
				return true;
			}
		};
		
		/*task.setOnSucceeded(e -> {
			System.out.println("All I/O packs loaded!");
		});*/
		new Thread(task).start();
		
	}

	private void bindTableColumns() {
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		iOVersionColumn.setCellValueFactory(cellData -> cellData.getValue().getRemoteVersionProperty());
		localVersionColumn.setCellValueFactory(cellData -> cellData.getValue().getLocalVersionProperty());
	}

	private void loadRvIoPackages() {
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
				IOPackageItem ioPack = new IOPackageItem(rvioPack, ioPacks);
				//ioPacks.add(ioPack);
			}
		}
	}

}
