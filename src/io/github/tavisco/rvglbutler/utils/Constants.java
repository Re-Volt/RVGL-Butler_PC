package io.github.tavisco.rvglbutler.utils;

import java.io.File;

public class Constants {
	private static Configs configs;
	
	public Constants() {
		configs = Configs.getInstance();
	}
	
	//Paths
	// public static final String PATH_RVGL_BUTLER_UNZIP = configs.getRvglPath() + File.separator + "unzipped";
	public static final String VERSIONS_FOLDER_NAME = "versions";

	//Links
    public static final String RVGL_LAST_VERSION_LINK = "https://distribute.re-volt.io/releases/rvgl_version.txt";
    public static final String RVGL_ANDROID_APK_LINK = "https://forum.re-volt.io/viewtopic.php?f=8&t=76";
    public static final String RVIO_AVAIABLE_PACKAGES_LINK = "http://distribute.re-volt.io/packages.txt";
    public static final String RVIO_ASSETS_LINK = "https://distribute.re-volt.io/assets/";
    public static final String RVIO_DOWNLOAD_PACKS_LINK = "https://distribute.re-volt.io/packs/";

    //Files
    public static final String RVGL_CURRENT_VERSION_TXT = "rvgl_version.txt";
    public static final String CAR_PARAMETER_FILE_NAME = "parameters.txt";
    // public static final String LEVEL_PLACEHOLDER_IMAGE = configs.getRvglPath() + File.separator + "gfx" + File.separator + "acclaim.bmp";
    public static final String[] RVGL_EXECUTABLES = {"rvgl.exe", "rvgl"};

    //Misc
    public static final String TAG = ">>>";

}
