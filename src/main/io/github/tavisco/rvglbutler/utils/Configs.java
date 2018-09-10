package main.io.github.tavisco.rvglbutler.utils;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Configs {
	private static Configs instance;

	private Configs() {
	}

	public static synchronized Configs getInstance() {
		if (instance == null) {
			instance = new Configs();
			instance.loadConfigs();
		}
		return instance;
	}

	private void loadConfigs() {
		File confFile = new File("./butler.yaml");

		if (confFile.exists() && confFile.isFile()) {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			try {
				instance = mapper.readValue(confFile, Configs.class);
				System.out.println("Configs loaded!");
				instance.configsLoaded = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// Paths
	private String rvglPath = "";
	private String rvglExecutable = "";
	private boolean rvglFound = false;
	@JsonIgnore
	private boolean configsLoaded = false;

	public String getRvglExecutable() {
		return rvglExecutable;
	}

	public void setRvglExecutable(String rvglExecutable) {
		this.rvglExecutable = rvglExecutable;
	}

	public boolean isRvglFound() {
		return rvglFound;
	}

	public void setRvglFound(boolean rvglFound) {
		this.rvglFound = rvglFound;
	}

	public String getRvglPath() {
		return rvglPath;
	}

	public void setRvglPath(String rvglPath) {
		this.rvglPath = rvglPath;
	}

	public boolean isConfigsLoaded() {
		return configsLoaded;
	}

	public void setConfigsLoaded(boolean configsLoaded) {
		this.configsLoaded = configsLoaded;
	}
	
	

}
