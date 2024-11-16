package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;

public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("/views/Main.fxml"));
			Scene scene = new Scene(root);
			
//			Scale scale = new Scale();
//	        root.getTransforms().add(scale);
//	        
//	        scale.xProperty().bind(scene.widthProperty().divide(800)); 
//	        scale.yProperty().bind(scene.heightProperty().divide(600));
			
	        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resource/Motoverse Logo.png")));
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// This comment should create a git conflit 
	//Ahmed ali comments
	public static void main(String[] args) {
		launch(args);
	}
}
