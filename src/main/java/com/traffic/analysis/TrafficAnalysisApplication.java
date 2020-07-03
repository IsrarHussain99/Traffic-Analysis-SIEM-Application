package com.traffic.analysis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TrafficAnalysisApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// start spring container and get spring context
		ConfigurableApplicationContext context = SpringApplication.run(TrafficAnalysisApplication.class);

		// now create the FXML loader
		FXMLLoader loader = new FXMLLoader(TrafficAnalysisApplication.class.getResource("display_data.fxml"));

		// ask spring to instantiate our FXML controller classes, rather than JavaFX itself
		loader.setControllerFactory(context::getBean);

		// now load the FXML file
		Parent parent = loader.load();

		// create scene with the loaded Parent node
		Scene scene = new Scene(parent);

		// set the stage's scene
		stage.setScene(scene);

		// show the stage
		stage.show();
	}
}
