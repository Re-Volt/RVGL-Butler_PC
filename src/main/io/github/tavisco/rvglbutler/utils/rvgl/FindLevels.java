package main.io.github.tavisco.rvglbutler.utils.rvgl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import main.io.github.tavisco.rvglbutler.model.LevelItem;
import main.io.github.tavisco.rvglbutler.model.enums.ItemType;
import main.io.github.tavisco.rvglbutler.utils.Configs;

public class FindLevels {
	public static List<LevelItem> getAllLevels() {

		List<String> dontShowTracks = new ArrayList<>();
		dontShowTracks.add("intro");
		dontShowTracks.add("frontend");
		dontShowTracks.add("stunts");

		List<LevelItem> levels = new ArrayList<>();

		Configs configs = Configs.getInstance();

		String levelPath = configs.getRvglPath() + File.separator + ItemType.LEVEL.getTypePath();
		File levelFolder = new File(levelPath);
		File[] allLevelsFolders = levelFolder.listFiles();

		if (!levelFolder.isDirectory() || !levelFolder.canRead() || allLevelsFolders.length == 0) {
			return null;
		}

		boolean skip;
		for (File singleLevelFolder : allLevelsFolders) {
			skip = false;

			for (String dontShow : dontShowTracks) {
				if (dontShow.equals(singleLevelFolder.getName()))
					skip = true;
			}

			if (skip)
				continue;
			
			LevelItem level = new LevelItem(singleLevelFolder);
			levels.add(level);
		}

		return levels;
	}
}
