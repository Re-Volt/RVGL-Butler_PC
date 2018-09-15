package main.io.github.tavisco.rvglbutler.model;

import java.io.File;
import java.io.IOException;

import de.olfillasodikno.openvolt.lib.structures.parameters.RVParameters;
import de.olfillasodikno.openvolt.lib.utils.RVReader;
import main.io.github.tavisco.rvglbutler.utils.Configs;

/**
 * Created by Tavisco on 29/04/18.
 */
public class LevelItem extends BaseItem {
    
	public LevelItem(File levelFolder) {
		this.setItemFolderName(levelFolder.getName());
		this.setName(levelFolder.getName());
		
		/*String configFilePath = "/home/tavisco/.rvgl/cars/cougar/parameters.txt"; //levelFolder.getAbsolutePath() + File.separator + getItemFolderName() + ".inf";
		
		File configFile = new File(configFilePath);
		
		try {
			RVParameters params = RVReader.paramFromFile(configFile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	@Override
    public String getImagePath() {
    	Configs configs = Configs.getInstance();
        return configs.getRvglPath() + File.separator + "gfx" + File.separator + super.itemFolderName + ".bmp";
    }
}