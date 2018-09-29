package main.io.github.tavisco.rvglbutler.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.io.github.tavisco.rvglbutler.utils.Configs;

/**
 * Created by Tavisco on 29/04/18.
 */
public class LevelItem extends BaseItem {
    
	public LevelItem(File levelFolder) {
		this.setItemFolderName(levelFolder.getName());
		//this.setName(levelFolder.getName());
		
		String configFilePath = levelFolder.getAbsolutePath() + File.separator + getItemFolderName() + ".inf";
		
		File configFile = new File(configFilePath);

		try {
			List<String> lines = Files.readAllLines(configFile.toPath());
			for (String line : lines) {
				if (line.length() <= 4)
					continue;

				String name = line.substring(0, 4).toUpperCase();

				if (name.equals("NAME")) {
					//Now I use REGEX to extract the name of the item
					Pattern p = Pattern.compile("\'(.*?)\'");
					Matcher m = p.matcher(line);
					if (m.find()) {
						this.setName(m.group(0).replace("\'", ""));
						break;
					}
				}
			}
//			} else {
//				this.setName(param.getName());
//			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public String getImagePath() {
    	Configs configs = Configs.getInstance();
        return configs.getRvglPath() + File.separator + "gfx" + File.separator + super.itemFolderName + ".bmp";
    }
}