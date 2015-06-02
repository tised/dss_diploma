package com.tised.expert.support;

import com.tised.expert.controller.ServerSetter;
import com.tised.expert.model.DataContainer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;

import java.util.Optional;

/**
 * Created by tised_000 on 30.05.2015.
 */
public class AllertHandler {

    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(ServerSetter.class);

    public static void showLogin(DataContainer data){

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("");
        dialog.setHeaderText("Look, a Custom Login Dialog");
        ServerSetter serverSetter = new ServerSetter();
// Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("cancel", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField login = new TextField();
        login.setText("lol@gmail.com");
        login.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setText("12345");
        grid.add(new Label("Username:"), 0, 0);
        grid.add(login, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        Node cancelButton = dialog.getDialogPane().lookupButton(cancelButtonType);
        loginButton.setDisable(true);
        cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.debug("exit pervent");
                Platform.exit();
                System.exit(0);
            }
        });
// Do some validation (using the Java 8 lambda syntax).
        login.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
        Platform.runLater(() -> login.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                logger.debug("login == " + login.getText() + " pass === " + toMd5(password.getText()));
                String res = serverSetter.checkLogin(login.getText(), toMd5(password.getText()));
                return new Pair<>("result", res);
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {

            if(!usernamePassword.getValue().equals("OK")){

                logger.debug(usernamePassword.getKey() + " === " + usernamePassword.getValue());
                dialog.showAndWait();
            }

        });
    }

    private static String toMd5(String value){

        String salt = "onvoghetilop";

        return DigestUtils.md5Hex(value + salt);
    }
}
