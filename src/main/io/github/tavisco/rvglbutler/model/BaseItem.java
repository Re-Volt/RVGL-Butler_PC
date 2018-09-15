package main.io.github.tavisco.rvglbutler.model;

import java.io.File;

import main.io.github.tavisco.rvglbutler.model.enums.ItemType;


/**
 * Created by Tavisco on 29/04/18.
 */
public abstract class BaseItem {
    protected String name;
    protected ItemType type;
    protected String itemFolderName;

    public String getName() {
        return name;
    }

    public void setName(String itemName) {
        this.name = itemName;
    }

    public abstract String getImagePath();

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType itemType) {
        this.type = itemType;
    }

    public String getItemFolderName() {
        return itemFolderName;
    }

    public void setItemFolderName(String itemPath) {
        this.itemFolderName = itemPath;
    }
    
    public void deleteItem() {
    	System.out.println("Deleting " + this.getName());
    }

    /*public String getFullPath() {
        return basePath + File.separator + type.getTypePath() + File.separator + itemFolderName;
    }*/

}