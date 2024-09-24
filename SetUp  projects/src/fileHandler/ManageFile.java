package fileHandler;

import java.io.File;
import java.io.IOException;

public class ManageFile {
	
	

	public static void delMarked(File parentFol, File compareTo) {
		for (File childF : parentFol.listFiles()) {
			for (File f : compareTo.listFiles()) {
				if (f.getName().equals(childF.getName())) {
					deleteFol(parentFol);
					return;
				}
			}
		}
	}

	private static void deleteFol(File file) {
		if(file.isDirectory()) {
			for (File f  : file.listFiles()) {
				deleteFol(f);
			}
			file.delete();
		}else {
			file.delete();
		}
	}

	public static void delZips(File[] file) {
		for (File f : file) {
			if (f.getName().contains(".zip")) {
				f.delete();
			}
		}
	}
    
	public static void relocateFiles(File file) {
		for (File f : file.listFiles()) {
			if(f.isDirectory()){
				File fol = f.listFiles()[0];
				if(f.getName().equals(fol.getName())){
					for (File fol1 : fol.listFiles()) {
						moveFile(f, fol1);
					}
					fol.deleteOnExit();
					fol.delete();
				}
			}
		}
	}
	public static void moveFile(File newLocation,File file){
		if(file.isDirectory()){
			for (File f : file.listFiles()) {
				File newLoc = new File(newLocation,file.getName());
				newLoc.mkdirs();
				moveFile(newLoc, f);
				file.deleteOnExit();
				file.delete();
            }
		}else{
				File newFile = new File(newLocation,file.getName());
				file.renameTo(newFile);
			}
		
	}
	
	public static void unzipFile(File[] file) {
		for (File f : file) {
			if (f.isDirectory()) {
				unzipFile(f.listFiles());
			} else if (f.getName().contains(".zip")) {
				try {
					UnzipF.unzip(f.getAbsolutePath());

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					f.deleteOnExit();
				}
				f.deleteOnExit();
			}

		}
        relocateFiles(file[0].getParentFile());
	}
}
