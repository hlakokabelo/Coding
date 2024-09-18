import java.io.File;

public class FileHandler {

	public static void moveContents(File dest, File source) {
		if (source.listFiles().length > 0)
			// moving contents
			for (File file : source.listFiles()) {
				moveFile(dest, file);
			}

		source.delete();
	}

	public static void moveFile(File dest, File file) {

		if (file.isDirectory()) {
			File newDest = new File(dest, file.getName());
			newDest.mkdirs();

			moveContents(newDest, file);
		} else {
			File newFile = new File(dest, file.getName());
			boolean success = file.renameTo(newFile);
			System.err.println(file.getAbsolutePath() + "\t~~~\t" + success);
		}

	}

	/**
	 * Deletes a folder and its contents
	 * 
	 * @param fol
	 * @return true if folder was deleted else false
	 */
	public static boolean deletFol(File fol) {
		if (fol.isDirectory() && fol.listFiles().length > 0)
			for (File file : fol.listFiles()) {
				if (file.isDirectory())
					deletFol(file);
				file.delete();
			}
		return fol.delete();
	}

	public static void isCopy(File file) {

		String regEx = "\\([\\d]{1,3}\\)";
		String text = "#kb#"; // to show which files were noted

		if (!file.exists() || file.isDirectory())
			return;

		// is F2 a copy ? i.e it has (1)
		String name = file.getName().replaceAll(regEx, text);
		if (name.contains(text)) {
			name = name.replaceAll(text, "");

			File newFileName = new File(file.getParentFile(), name);
			boolean result = file.renameTo(newFileName);

			if (result)
				return;
			if (result == false && newFileName.exists() && file.length() == newFileName.length()) {
				// file.delete();
			} else {
				newFileName = new File(file.getParentFile(),
						file.getName().replaceAll("\\(", "").replaceAll("\\)", ""));
				result = file.renameTo(newFileName);

				System.out.println(file.getAbsolutePath());
			}
		}

	}

}
