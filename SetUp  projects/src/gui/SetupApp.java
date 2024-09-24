package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fileHandler.Clean;
import fileHandler.ManageFile;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class SetupApp extends StackPane {
	BorderPane bp = new BorderPane();

	FileChooser fc = new FileChooser();
	TextArea txtArea = new TextArea();
	DirectoryChooser dc = new DirectoryChooser();
	File initialDir = new File("C:\\Users\\kB\\Documents\\ASSISTANT\\#Marking");

	public SetupApp() {
		mainUI();

		this.getChildren().add(bp);
	}

	private void mainUI() {
		Button btnUnzip = new Button("Unzip file(s)");
		btnUnzip.setMinHeight(40);
		btnUnzip.setMinWidth(140);

		Button btnFindzip = new Button("Find zips and unzip");
		btnFindzip.setMinHeight(40);
		btnFindzip.setMinWidth(140);

		Button btnClean = new Button("Select fol to clean");
		btnClean.setMinHeight(40);
		btnClean.setMinWidth(140);

		txtArea.setEditable(false);

		btnUnzip.setOnAction(e -> {
			unzipAction();
		});

		btnFindzip.setOnAction(e -> {
			Findzip();
		});

		btnClean.setOnAction(e -> {
			cleanAction();
		});

		VBox vb = new VBox(10, btnUnzip, btnClean, btnFindzip);

		bp.setLeft(vb);
		bp.setCenter(txtArea);

	}

	private void Findzip() {
		dc.setInitialDirectory(getInitialDir());
		ArrayList<File> zips = new ArrayList<>();
		File file = dc.showDialog(null);
		if (file != null) {
			Findzip(file, zips);

			File[] tempFile = toList(zips);
			ManageFile.unzipFile(tempFile);
			ManageFile.delZips(tempFile);
			ManageFile.delZips(tempFile);
			txtArea.appendText("done");
		}
	}

	private void Findzip(File dir, ArrayList<File> zips) {
		if (dir.isDirectory()) {
			for (File file : dir.listFiles()) {
				Findzip(file, zips);
			}
		} else if (dir.getName().contains(".zip")) {
			zips.add(dir);
		}
	}

	private void cleanAction() {
		dc.setInitialDirectory(getInitialDir());
		File file = dc.showDialog(null);
		if (file != null) {
			Clean cleaner = new Clean(file);
			txtArea.appendText(cleaner.Start());
		}

	}

	private File getInitialDir() {
		return (initialDir.exists() ? initialDir : null);
	}

	private void unzipAction() {

		fc.setInitialDirectory(getInitialDir());
		List<File> files = fc.showOpenMultipleDialog(null);
		if (files != null) {
			fc.setInitialDirectory(files.get(0).getParentFile());
			File[] tempFile = toList(files);

			ManageFile.unzipFile(tempFile);
			ManageFile.delZips(tempFile);
			ManageFile.delZips(tempFile);
			txtArea.appendText("done");
		}

	}

	private File[] toList(List<File> files) {
		File[] list = new File[files.size()];
		int c = 0;
		for (File file : files) {
			list[c] = file;
			c++;
		}
		return list;
	}

}
