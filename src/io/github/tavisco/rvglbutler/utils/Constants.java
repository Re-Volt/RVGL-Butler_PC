package io.github.tavisco.rvglbutler.utils;

import java.io.File;

public class Constants {
	private static Constants instance;
	private boolean rvglPathSet = false;
	
	private Constants() {
		
	}
	
	public static synchronized Constants getInstance() {
		if (instance == null) {
			instance = new Constants();
		}
		return instance;
	}
	
	//Paths
    private String rvglPath = "";
    private String PATH_RVGL_BUTLER = "";
    private final String PATH_RVGL_BUTLER_UNZIP = PATH_RVGL_BUTLER + File.separator + "unzipped";
    private final String VERSIONS_FOLDER_NAME = "versions";

	//Links
    public static final String RVGL_LAST_VERSION_LINK = "https://distribute.re-volt.io/releases/rvgl_version.txt";
    public static final String RVGL_ANDROID_APK_LINK = "https://forum.re-volt.io/viewtopic.php?f=8&t=76";
    public static final String RVIO_AVAIABLE_PACKAGES_LINK = "http://distribute.re-volt.io/packages.txt";
    public static final String RVIO_ASSETS_LINK = "https://distribute.re-volt.io/assets/";
    public static final String RVIO_DOWNLOAD_PACKS_LINK = "https://distribute.re-volt.io/packs/";

    //Files
    public static final String RVGL_CURRENT_VERSION_TXT = "rvgl_version.txt";
    public static final String CAR_PARAMETER_FILE_NAME = "parameters.txt";
    private final String LEVEL_PLACEHOLDER_IMAGE = rvglPath + File.separator + "gfx" + File.separator + "acclaim.bmp";

    //Misc
    public static final String TAG = ">>>";
    
    public String getRvglPath() {
		if (rvglPathSet) {
			return rvglPath;			
		}
		
		return null;
	}
    
    public void setRvglPath(String rvglPath) {
		if (rvglPath != null) {
			this.rvglPath = rvglPath;
			rvglPathSet = true;
		}
	}

}
