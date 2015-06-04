package com.tised.expert;

import com.tised.expert.controller.ExpertController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by tised_000 on 31.05.2015.
 */
public class Main extends Application {

    AnchorPane rootLayout;
    Stage primaryStage;

    final static Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Expert");
        this.primaryStage.setResizable(false);
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        initRootLayout();
    }

    public void initRootLayout() {

        try {

            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            String fxmlFile = "/view/rootLayout.fxml";
            rootLayout = (AnchorPane) loader.load(getClass().getResourceAsStream(fxmlFile));
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
           // scene.getStylesheets().add("http://www.jpedal.org/simon/dynamiccss/webcss.css");
            primaryStage.setScene(scene);
            primaryStage.show();
            ExpertController controller = (ExpertController) loader.getController();
            controller.setScene(scene);
            controller.init();

            logger.trace("Application started");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}
