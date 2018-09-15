package main.io.github.tavisco.rvglbutler.utils.rvgl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import main.io.github.tavisco.rvglbutler.model.CarItem;
import main.io.github.tavisco.rvglbutler.utils.Configs;

public class FindCars {

    public static List<CarItem> getAllCars() {
    	
    	List<String> dontShowCars = new ArrayList<>();
    	dontShowCars.add("misc");


    	List<CarItem> cars = new ArrayList<CarItem>();
    	
    	Configs configs = Configs.getInstance();
    	
        String carsPath = configs.getRvglPath() + File.separator + "cars";
        File carsFolder = new File(carsPath);
        File[] allCarsFolders = carsFolder.listFiles();

        if (!carsFolder.isDirectory() || !carsFolder.canRead() || allCarsFolders.length == 0) {
            // TODO: Error while listing cars
            // The game is installed?
            return null;
        }

        boolean skip;
        for (File carFolder : allCarsFolders) {
        	skip = false;
			for (String dontShow : dontShowCars) {
				if (dontShow.equals(carFolder.getName()))
					skip = true;
			}

			if (skip)
				continue;
        	
            CarItem car = new CarItem(carFolder);
            cars.add(car);

        }

        return cars;
    }
}