package io.github.tavisco.rvglbutler.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import io.github.tavisco.rvglbutler.model.enums.UpdateStatus;
import io.github.tavisco.rvglbutler.utils.Configs;
import io.github.tavisco.rvglbutler.utils.Constants;

public class IOPackageItem {
	private final StringProperty name;
	private StringProperty localVersion = new SimpleStringProperty("");
	private StringProperty remoteVersion = new SimpleStringProperty("");
	private boolean remoteVersionChecked = false;
	private UpdateStatus updateStatus;

	private final String ERROR_STRING = "Error";


	
	public IOPackageItem(String name, ObservableList<IOPackageItem> ioPacks) {
		this.name = new SimpleStringProperty(name);
		determineRemoteVersion(ioPacks);
		determineLocalVersion();
	}

	private void determineRemoteVersion(ObservableList<IOPackageItem> ioPacks) {
		String rvioRequest = Constants.RVIO_ASSETS_LINK + getName() + ".txt";
		//System.out.println(rvioRequest);
		
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config().setConnectTimeout(1500);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		Request getRequest = Dsl.get(rvioRequest).build();

		ListenableFuture<Response> listenableFuture = client.executeRequest(getRequest);
		listenableFuture.addListener(() -> {
			Response response;
			try {
				response = listenableFuture.get();
				String remoteVersion = response.getResponseBody().substring(0, 7);
				//System.out.println(getName() + " - V: " + remoteVersion);
				setRemoteVersion(remoteVersion);
				ioPacks.add(IOPackageItem.this);
				//packList.refresh();
				//packList.notify();
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
		
		/*ListenableFuture<Response> whenResponse = client.prepareGet(rvioRequest).execute();
		try {
			Response response = whenResponse.get();
			String remoteVersion = response.getResponseBody().substring(0, 7);
			System.out.println(getName() + " - V: " + remoteVersion);
			setRemoteVersion(remoteVersion);
			
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				client.close();
				//finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		/*String remoteVersion;
		try {
			remoteVersion = getHTML(rvioRequest).substring(0, 7);
			//System.out.println(getName() + " - V: " + remoteVersion);
			setRemoteVersion(remoteVersion);
			
			//System.out.println("Done!");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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

	public String getName() {
		return name.get();
	}

	public boolean isRemoteVersionChecked() {
		return remoteVersionChecked;
	}

	public String getLocalVersion() {
		if (localVersion.get().equals(ERROR_STRING)) {
			return "---";
		}
		return localVersion.get();
	}

	public String getRemoteVersion() {
		if (isRemoteVersionChecked()) {
			return remoteVersion.get();
		} else {
			return "Checking...";
		}
	}

	public void setLocalVersion(String localVersion) {
		this.localVersion.set(localVersion);
	}

	public void setRemoteVersion(String remoteVersion) {
		this.remoteVersion.set(remoteVersion);
	}

	public String getDownloadLink() {
		return Constants.RVIO_DOWNLOAD_PACKS_LINK.concat(getName()).concat(".zip");
	}

	public void setRemoteVersionChecked(boolean remoteVersionChecked) {
		this.remoteVersionChecked = remoteVersionChecked;

		if (remoteVersionChecked) {
			determinePackageUpdateStatus();
		}

	}

	private void determinePackageUpdateStatus() {
		if (getLocalVersion().equals(ERROR_STRING)) {
			updateStatus = UpdateStatus.NOT_INSTALLED;
			return;
		}

		if (getLocalVersion().equals(getRemoteVersion())) {
			updateStatus = UpdateStatus.UPDATED;
		} else {
			updateStatus = UpdateStatus.UPDATE_AVAIABLE;
		}
	}

	public UpdateStatus getUpdateStatus() {
		return updateStatus;
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
	
	public static String getHTML(String urlToRead) throws Exception {
	      StringBuilder result = new StringBuilder();
	      URL url = new URL(urlToRead);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         result.append(line);
	      }
	      rd.close();
	      return result.toString();
	   }

}
