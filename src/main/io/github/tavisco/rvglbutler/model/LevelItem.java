package main.io.github.tavisco.rvglbutler.model;

import java.io.File;

import main.io.github.tavisco.rvglbutler.utils.Configs;

/**
 * Created by Tavisco on 29/04/18.
 */
public class LevelItem extends BaseItem {
    @Override
    public String getImagePath() {
    	Configs configs = Configs.getInstance();
        return configs.getRvglPath() + File.separator + "gfx" + File.separator + super.itemFolderName + ".bmp";
    }
}