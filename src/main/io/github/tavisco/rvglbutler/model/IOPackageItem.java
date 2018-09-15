package main.io.github.tavisco.rvglbutler.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import main.io.github.tavisco.rvglbutler.model.enums.UpdateStatus;
import main.io.github.tavisco.rvglbutler.utils.Configs;
import main.io.github.tavisco.rvglbutler.utils.Constants;
import main.io.github.tavisco.rvglbutler.utils.ZipUtils;

public class IOPackageItem {
	private final StringProperty name;
	private StringProperty localVersion = new SimpleStringProperty("");
	private StringProperty remoteVersion = new SimpleStringProperty("");
	private ObjectProperty<UpdateStatus> updateStatus = new SimpleObjectProperty<UpdateStatus>(UpdateStatus.ERROR);

	private final String ERROR_STRING = "- - -";

	public IOPackageItem(String name, ObservableList<IOPackageItem> ioPacks) {
		this.name = new SimpleStringProperty(name);
		determineLocalVersion();
		determineRemoteVersion(ioPacks);
	}

	private void determineRemoteVersion(ObservableList<IOPackageItem> ioPacks) {
		String rvioRequest = Constants.RVIO_ASSETS_LINK + getName() + ".txt";

		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config().setConnectTimeout(1500);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		Request getRequest = Dsl.get(rvioRequest).build();

		ListenableFuture<Response> listenableFuture = client.executeRequest(getRequest);
		listenableFuture.addListener(() -> {
			Response response;
			try {
				response = listenableFuture.get();
				String remoteVersion = response.getResponseBody().substring(0, 7);
				setRemoteVersion(remoteVersion);
				ioPacks.add(IOPackageItem.this);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					client.close();
					finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, Executors.newCachedThreadPool());
	}

	private void determineLocalVersion() {
		Configs configs = Configs.getInstance();

		File localVersionFile = new File(configs.getRvglPath().concat(File.separator)
				.concat(Constants.VERSIONS_FOLDER_NAME).concat(File.separator).concat(getName()).concat(".txt"));
		if (localVersionFile.isFile() && localVersionFile.canRead()) {
			setLocalVersion(readLocalVersion(localVersionFile));
		} else {
			setLocalVersion(ERROR_STRING);
		}
	}

	private String readLocalVersion(File localVersionFile) {
		try (Scanner sc = new Scanner(localVersionFile)) {
			return sc.next();
		} catch (FileNotFoundException e) {
			return ERROR_STRING;
		}
	}

	public boolean download() {
		long completeFileSize = -1;
		try {

			URL url = new URL(getDownloadLink());
			HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
			completeFileSize = httpConnection.getContentLength();
		} catch (Exception e) {
			// TODO: handle exception
		}

		String contentText = "";

		if (completeFileSize != -1) {
			contentText = String.format("You will download aprox. %s MB. Continue?", completeFileSize / 1000000);
		} else {
			contentText = "Please confirm your action.";
		}

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Download confirmation");
		alert.setHeaderText(String.format("Do you want to download %s?", getName()));
		alert.setContentText(contentText);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.YES) {
			doDownload();
			return true;
		}

		return false;
	}

	private void doDownload() {
		Runnable updatethread = new Runnable() {
			public void run() {
				try {
					Configs configs = Configs.getInstance();
					String savePathStr = configs.getRvglPath() + File.separator + "butler";

					File savePathFolder = new File(savePathStr);

					if (!savePathFolder.exists()) {
						savePathFolder.mkdir();
					}

					String saveFile = savePathStr + File.separator + getName() + ".zip";

					URL url = new URL(getDownloadLink());
					HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
					long completeFileSize = httpConnection.getContentLength();

					java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
					java.io.FileOutputStream fos = new java.io.FileOutputStream(saveFile);
					java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
					byte[] data = new byte[1024];
					long downloadedFileSize = 0;
					int x = 0;
					while ((x = in.read(data, 0, 1024)) >= 0) {
						downloadedFileSize += x;

						// calculate progress
						final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize))
								* 100d);

						// TODO: update progress

						System.out.println(">>> " + currentProgress + "%");

						bout.write(data, 0, x);
					}
					bout.close();
					in.close();

					ZipUtils.unzip(saveFile, configs.getRvglPath());

					String versionPathStr = configs.getRvglPath() + File.separator + Constants.VERSIONS_FOLDER_NAME;

					File versionPathFolder = new File(versionPathStr);

					if (!versionPathFolder.exists()) {
						versionPathFolder.mkdir();
					}

					PrintWriter writer = new PrintWriter(
							configs.getRvglPath().concat(File.separator).concat(Constants.VERSIONS_FOLDER_NAME)
									.concat(File.separator).concat(getName()).concat(".txt"),
							"UTF-8");
					writer.print(getRemoteVersion());
					writer.close();

				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}
			}
		};
		new Thread(updatethread).start();
	}

	public String getName() {
		return name.get();
	}

	public String getLocalVersion() {
			return localVersion.get();
	}

	public String getRemoteVersion() {
		return remoteVersion.get();
	}

	public void setLocalVersion(String localVersion) {
		this.localVersion.set(localVersion);
	}

	public void setRemoteVersion(String remoteVersion) {
		this.remoteVersion.set(remoteVersion);
		determinePackageUpdateStatus();
	}

	public String getDownloadLink() {
		return Constants.RVIO_DOWNLOAD_PACKS_LINK.concat(getName()).concat(".zip");
	}

	private void determinePackageUpdateStatus() {
		if (getLocalVersion().equals(ERROR_STRING)) {
			updateStatus.set(UpdateStatus.NOT_INSTALLED);
			return;
		}

		if (getLocalVersion().equals(getRemoteVersion())) {
			updateStatus.set(UpdateStatus.UPDATED);
			System.out.println("Installed: " + this.getName());
		} else {
			updateStatus.set(UpdateStatus.UPDATE_AVAIABLE);
		}
	}

	public UpdateStatus getUpdateStatus() {
		return updateStatus.get();
	}
	
	@SuppressWarnings("unused")
	private void setUpdateStatus(UpdateStatus status) {
		updateStatus.set(status);
	}

	public StringProperty getNameProperty() {
		return name;
	}

	public StringProperty getLocalVersionProperty() {
		return localVersion;
	}

	public StringProperty getRemoteVersionProperty() {
		return remoteVersion;
	}
	
	public ObjectProperty<UpdateStatus> getUpdateStatusProperty(){
		return updateStatus;
	}

}
