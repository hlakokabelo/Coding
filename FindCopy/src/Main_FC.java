import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main_FC extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FindCopies_app app = new FindCopies_app();

		Scene scene = new Scene(app);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Find Copies");
		primaryStage.show();

	}

}
