import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class SearchApp extends StackPane {
	BorderPane bp = new BorderPane();

	TextArea txtArea = new TextArea();

	public SearchApp() {
		mainUI();

		this.getChildren().add(bp);
	}

	private void mainUI() {
		Button btnSearch = new Button("Select fol to search");
		btnSearch.setMinHeight(40);
		btnSearch.setMinWidth(140);
		TextField txtFSearch = new TextField();

		txtArea.setEditable(false);

		DirectoryChooser dc = new DirectoryChooser();
		btnSearch.setOnAction(e -> {
			String text = txtFSearch.getText();
			if (!text.equals("")) {
				File fol = dc.showDialog(null);
				if (fol != null) {
					File[] files = fol.listFiles();
					searchFile(text, files);
					txtArea.appendText("\n\n\t\t\t\tDONE----");
				}
			}
		});

		VBox vb = new VBox(10, new HBox(5, new Label("Text to search"), txtFSearch), btnSearch);

		bp.setLeft(vb);
		bp.setCenter(txtArea);

	}

	private void searchFile(String text, File[] files) {
		if (files != null)
			for (File file : files) {
				String filename = file.getName();
				if (filename.contains(text)) {
					txtArea.appendText(file.getParent()+"-\t-"+filename+"\n");
					System.out.println(file.getParent()+"-\t-"+filename+"\n");
				}

				if (file.isAbsolute()) {
					
					class threadClass implements Runnable {
						@Override
						public void run() {
							searchFile(text, file.listFiles());
						}
					}
						
					
					
					
					Thread thread = new Thread(new threadClass());
					thread.run();
				
				}
			}

	}
}
