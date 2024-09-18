package gui;

import java.io.File;
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

	DirectoryChooser dc = new DirectoryChooser();

	public SetupApp() {
		mainUI();

		this.getChildren().add(bp);
	}

	private void mainUI() {
		Button btnUnzip = new Button("Unzip file(s)");
		btnUnzip.setMinHeight(40);
		btnUnzip.setMinWidth(140);

		Button btnClean = new Button("Select fol to clean");
		btnClean.setMinHeight(40);
		btnClean.setMinWidth(140);

		TextArea txtArea = new TextArea();
		txtArea.setEditable(false);

		btnUnzip.setOnAction(e -> {
			txtArea.appendText(unzipAction() + "\n");
		});

		btnClean.setOnAction(e -> {
			txtArea.appendText(cleanAction() + "\n");
		});

		VBox vb = new VBox(10, btnUnzip, btnClean);

		bp.setLeft(vb);
		bp.setCenter(txtArea);

	}

	private String cleanAction() {

		File file = dc.showDialog(null);
		if (file != null) {
			new Clean(file);
		}
		return " ";
	}

	private String unzipAction() {
		List<File> files = fc.showOpenMultipleDialog(null);
		if (files != null) {
			fc.setInitialDirectory(files.get(0).getParentFile());
			File[] tempFile = toList(files);

			ManageFile.unzipFile(tempFile);
			ManageFile.delZips(tempFile);
			ManageFile.delZips(tempFile);
			return "Done";
		}
		return " ";
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
