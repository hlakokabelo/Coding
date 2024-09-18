import java.io.File;

import gui.SetupApp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainSetup extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		SetupApp app = new SetupApp();

		Scene scene = new Scene(app);
		primaryStage.setScene(scene);
		primaryStage.setTitle("");
		primaryStage.show();

	}
}
