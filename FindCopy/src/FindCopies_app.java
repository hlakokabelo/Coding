import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class FindCopies_app extends StackPane {
	BorderPane bp = new BorderPane();
	DirectoryChooser dc = new DirectoryChooser();
	TextArea txtAStatus = new TextArea();
	/**
	 * A list of files
	 */
	ArrayList<File> filesToCheck = new ArrayList<>();
	ArrayList<File> foldersSelected = new ArrayList<>();
	Button btnClear = new Button("Clear everything");
	// signals whether the files to be checked are mp4/mp3
	private boolean checkForMovies = false;
	/**
	 * Signals that only folders are to be added in {@link #filesToCheck}
	 */
	private boolean addFolders = false;

	public FindCopies_app() {

		mainUI();
		menuItems();
		this.getChildren().add(bp);
	}

	private void menuItems() {
		MenuBar menuBar = new MenuBar();

		Menu menu = new Menu("_Choose File Type");
		MenuItem miMovie = new MenuItem("_Movies");
		MenuItem miSongs = new MenuItem("_Songs \t #");
		MenuItem miFolders = new MenuItem("_Folders");

		menu.getItems().addAll(miFolders, miMovie, miSongs);

		for (MenuItem item : menu.getItems()) {
			item.setOnAction(e -> {
				// Rename every menuItem to their default name
				for (MenuItem i : menu.getItems()) {
					i.setText(i.getText().replaceAll("#", "").trim());
				}
				menuItemAction(e);
			});
		}
		menuBar.getMenus().add(menu);
		bp.setTop(menuBar);
	}

	/**
	 * A generic setOnAction for menuItems
	 * 
	 * @param e used to trace the source of the event , i.e the menutItem that was clicked/selected
	 */
	void menuItemAction(ActionEvent e) {
		btnClear.fire();

		MenuItem item = (MenuItem) e.getSource();

		String name = item.getText().replaceAll("#", "");
		String newName = name.trim() + "\t#";

		name = name.toLowerCase();

		// set default
		addFolders = false;
		checkForMovies = false;

		// which was clicked???
		if (name.contains("movies")) {
			checkForMovies = true;
			item.setText(newName);
		} else if (name.contains("folders")) {
			addFolders = true;
			item.setText(newName);
		} else if (name.contains("songs")) {
			item.setText(newName);
		}

		if (!foldersSelected.isEmpty())
			for (File file : foldersSelected) {
				addToList(file.listFiles());
				String status = (addFolders ? " folder(s)" : " file(s)") + " added\n";
				txtAStatus.appendText(filesToCheck.size() + status);
			}

		System.err.println(item.getText());
	}

	/**
	 * initializes and displays UI
	 */
	private void mainUI() {
		Button btnAddFol = new Button("Add folder");
		btnAddFol.setMinHeight(40);
		btnAddFol.setMinWidth(140);

		Button btnFindCopy = new Button("Find Copy");
		btnFindCopy.setMinHeight(40);
		btnFindCopy.setMinWidth(140);

		txtAStatus.setEditable(false);
		txtAStatus.appendText("~~Copies~~\n\n");

		TextArea txtAFiles = new TextArea();
		txtAFiles.setEditable(false);
		txtAFiles.appendText("~~Selected files~~\n\n");

		btnClear.setMinHeight(40);
		btnClear.setMinWidth(140);

		txtAFiles.setPrefWidth(140);
		btnAddFol.setOnAction(e -> {
			File file = dc.showDialog(null);
			if (file != null) {
				dc.setInitialDirectory(file.getParentFile());

				foldersSelected.add(file);

				addToList(file.listFiles());

				// display size
				System.err.println(filesToCheck.size());
				String status = (addFolders ? " folder(s)" : " file(s)") + " added\t";
				txtAStatus.appendText(filesToCheck.size() + status);

				// display folder name
				txtAFiles.appendText(file.getName() + "\n");
			} else {
				System.err.println("none selected");
			}
		});

		btnFindCopy.setOnAction(e -> {
			if (!filesToCheck.isEmpty())
				FindCopy();

			txtAStatus.appendText("~~~~~~~done checking~~~~~~~\n");
		});

		btnClear.setOnAction(e -> {
			txtAStatus.setText("~~Copies~~\n\n");
			txtAFiles.setText("~~Selected files~~\n\n");
			filesToCheck.clear();
		});

		VBox vb = new VBox(10, btnAddFol, btnFindCopy, txtAFiles, btnClear);

		bp.setLeft(vb);
		bp.setCenter(txtAStatus);

	}

	private void addToListHelper(File[] listFiles) {

		if (listFiles == null || listFiles.length < 0)
			return;

		for (File file : listFiles) {
			if (addFolders && file.isDirectory()) {
				filesToCheck.add(file);
			} else if (checkForMovies)
				addMovies(file);
			else
				addSongs(file);

			/*******************************************
			 * Code for recursing folders
			 *******************************************/

			// recursion conditions
			if (!file.isDirectory())
				continue;

			// makes sure we run 17 threads at a time

			if ((filesToCheck.size() % 17) != 0) {
				addToListHelper(file.listFiles());
				continue;
			}

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					addToListHelper(file.listFiles());
				}
			});
			thread.start();
		}

	}

	/**
	 * Adds files to a list
	 * 
	 * @param listFiles
	 */
	private void addToList(File[] listFiles) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				addToListHelper(listFiles);
			}
		});
		thread.run();
		while (thread.isAlive()) {
			// waits until it completes running
			// cause by run method
		}
		System.err.println("done");
	}

	/**
	 * checks if the given file is a video file, by checking its file extension
	 * 
	 * @param file said file
	 * @return true if its a video else false
	 */

	public boolean isVideo(File file) {

		String filename = file.getName();
		String videoFiles[] = { ".mp4", ".mkv", ".avi", ".m4p", ".mov", ".webm", ".mov" };

		for (String videoFile : videoFiles)
			if (filename.endsWith(videoFile.toLowerCase()))
				return true;

		return false;
	}

	private void addMovies(File file) {

		if (file.isDirectory())
			return;
		// add to list
		if (file.getName().contains("[") || file.getName().contains("(") || isVideo(file))
			filesToCheck.add(file);
	}

	private void addSongs(File file) {

		if (file.isDirectory())
			return;
		// add to list
		if (file.getName().endsWith(".mp3"))
			filesToCheck.add(file);
	}

	/**
	 * used to find copies of a file, by comparing it to a list of files
	 * 
	 * @param file said file
	 */
	private void FindCopyHelper(File file) {
		ArrayList<File> copiesFound = new ArrayList<>();

		copiesFound.add(file); // add main file
		findCopies(file, copiesFound);

		// were any copies found?
		String strCopiesFound = "";
		if (copiesFound.size() > 1) {
			boolean addSeperator = false;

			for (File copy : copiesFound) {
				String c = copy.getAbsolutePath();
				if (!strCopiesFound.contains(c)) {
					strCopiesFound += (c) + "\n";
					addSeperator = true;
				}
			}
			// index 0 , sits our main file
			// deleteCopies(copiesFound, 1);

			if (addSeperator) {
				strCopiesFound += ("~~~~~~~~~~~~~~~~~####~~~~~~~~~~~~~~~~~\n");
				addSeperator = false;
			}
		}
		if (!strCopiesFound.equals(""))
			txtAStatus.appendText(strCopiesFound);

	}

	/**
	 * used to find copies of a file, by comparing it to a list of files
	 * 
	 * @param file said file
	 */
	private void FindCopy() {
		int threads = 0;
		Thread thread = new Thread(new Runnable() {
			public void run() {

				for (File file : filesToCheck) {
					Thread thread2 = new Thread(new Runnable() {
						public void run() {
							FindCopyHelper(file);
						}
					});
					
					if (threads > 10) {
						thread2.start();
					} else {
						thread2.start();
					}
				}
			}
		});
		thread.run();
		while (thread.isAlive()) {
			// waits until it completes running
			// cause by run method
		}

	}

	@SuppressWarnings("unused")
	private void deleteCopies(ArrayList<File> copies) {
		deleteCopies(copies, 0);
	}

	private void deleteCopies(ArrayList<File> copies, int index) {
		for (int i = index; i < copies.size(); i++) {
			File file = copies.get(i);

			if (file.isDirectory())
				FileHandler.deletFol(file);
			else
				file.delete();
		}
	}

	/***
	 * used to find copies of a file, by comparing it to a list of files
	 * {@link FindCopies_app#filesToCheck} and stores found files in an array-list
	 * 
	 * @param file        said file
	 * @param storeCopies found copies are stored here
	 * 
	 */
	private void findCopies(File file, ArrayList<File> storeCopies) {
		if (filesToCheck.isEmpty())
			return;

		for (File f2 : filesToCheck) {
			if (!f2.exists())
				continue;

			String regEx = "\\([\\d]{1,3}\\)";

			String name1 = f2.getName().replaceAll(regEx, "");
			String name2 = file.getName().replaceAll(regEx, "");

			if (name1.equals(name2) && !f2.getAbsolutePath().equals(file.getAbsolutePath())) {
				if (checkForMovies)
					storeCopies.add(f2);
				else if (isOk(f2))
					storeCopies.add(f2);

			}
		}
	}

	private boolean isOk(File f) {
		String name = f.getName().toLowerCase();

		String toCheck[] = { "track", "01 intro","subs","subtitles" };
		

		for (String word : toCheck) {
			if (name.contains(word.toLowerCase()))
				return false;
		}
		return true;
	}

//	private void writeToFile(ArrayList<File> copiesFound) {
//		File file = new File("files/copies.txt");
//		file.mkdirs();
//		try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, true))) {
//			for (File f : copiesFound) {
//				pw.println(f.getAbsolutePath(
//						));
//				pw.flush();
//			}
//			pw.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
}