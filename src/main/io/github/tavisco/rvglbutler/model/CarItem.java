package main.io.github.tavisco.rvglbutler.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import de.olfillasodikno.openvolt.lib.structures.parameters.RVCarParameters;
//import de.olfillasodikno.openvolt.lib.structures.parameters.RVParameters;
import main.io.github.tavisco.rvglbutler.utils.Configs;

/**
 * Created by Tavisco on 29/04/18.
 */
public class CarItem extends BaseItem {

	public CarItem(File carFolder) {
		this.setItemFolderName(carFolder.getName());
		
		Configs configs = Configs.getInstance();
    	
		String configFilePath = configs.getRvglPath() + File.separator + "cars" + File.separator + getItemFolderName() + File.separator + "parameters.txt";
		
		File configFile = new File(configFilePath);
		
		try {
//			RVCarParameters param = new RVCarParameters(RVParameters.fromFile(configFile).getRoot());
//			param.decode();
//
//			if (param.getName() == null) {
				List<String> lines = Files.readAllLines(configFile.toPath());
				for (String line : lines) {
					if (line.length() <= 4)
						continue;

					String name = line.substring(0, 4).toUpperCase();

					if (name.equals("NAME")) {
						//Now I use REGEX to extract the name of the item
				        Pattern p = Pattern.compile("\"(.*?)\"");
				        Matcher m = p.matcher(line);
				        if (m.find()) {
				            this.setName(m.group(0).replace("\"", ""));
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
    public String getImagePath()
    {
    	Configs configs = Configs.getInstance();
    	
    	String imagePath = configs.getRvglPath() + File.separator + "cars" + File.separator + getItemFolderName() + File.separator + "carbox.bmp";
    	String carboxNotFoundPath = configs.getRvglPath() + File.separator + "gfx" + File.separator + "markar.bmp";
    	
        File image = new File(imagePath);
        if (image.exists()){
            return image.getPath();
        } else {
        	String path = File.separator + "resources" + File.separator + "carboxes" + File.separator + "mistery.png";
        	System.out.println(">>>>>>>>>>>>>> " + path);
            return getClass().getResource(path).getPath();
        }
    }
}