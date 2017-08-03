package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static Stage principalStage;
		
	public void start(Stage primaryStage) throws IOException {
		Main.principalStage = primaryStage;
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("vista/VentanaPrincipal.fxml"));
        Parent raiz = loader.load();
        
        Scene scene = new Scene(raiz);
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("Aeropuertos");
        primaryStage.show();
		
	}
			
	public static void main(String[] args) {
		launch(args);
	}
}
