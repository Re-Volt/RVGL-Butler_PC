package io.github.tavisco.rvglbutler.utils.rvgl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.tavisco.rvglbutler.model.LevelItem;
import io.github.tavisco.rvglbutler.model.enums.ItemType;
import io.github.tavisco.rvglbutler.utils.Configs;

public class FindLevels {
	public static List<LevelItem> getAllLevels() {

		List<String> dontShowTracks = new ArrayList<>();
		dontShowTracks.add("intro");
		dontShowTracks.add("frontend");
		dontShowTracks.add("stunts");

		List<LevelItem> levels = new ArrayList<>();

		Configs configs = Configs.getInstance();

		String path = configs.getRvglPath() + File.separator + ItemType.LEVEL.getTypePath();
		File directory = new File(path);
		File[] files = directory.listFiles();

		if (!directory.isDirectory() || !directory.canRead() || files.length == 0) {
			return null;
		}

		boolean skip;
		for (File levelFile : files) {
			skip = false;

			for (String dontShow : dontShowTracks) {
				if (dontShow.equals(levelFile.getName()))
					skip = true;
			}

			if (skip)
				continue;

			LevelItem level = new LevelItem();
			level.setItemFolderName(levelFile.getName());
			level.setName(levelFile.getName());
			levels.add(level);
		}

		return levels;
	}
}
