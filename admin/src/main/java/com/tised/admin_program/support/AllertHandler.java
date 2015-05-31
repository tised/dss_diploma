package com.tised.admin_program.support;

import com.tised.admin_program.controller.ServerSetter;
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

    public static void showLogin(){

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

    public static void showAddExpert(){

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Добавить эксперта");
        ServerSetter serverSetter = new ServerSetter();
// Set the button types.
        ButtonType loginButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Имя");
        TextField surName = new TextField();
        surName.setPromptText("Фамилия");
        TextField email = new TextField();
        email.setPromptText("эл. почта");

        grid.add(new Label("Имя:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(surName, 1, 1);
        grid.add(new Label("эл. почта:"), 0, 2);
        grid.add(email, 1, 2);

// Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                String res = serverSetter.addExpertToServer(username.getText(), surName.getText(), email.getText());
                return new Pair<>("result", res);
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(resultUpload -> {

            while(!resultUpload.getValue().equals("OK")) {

                dialog.showAndWait();
            }

            logger.debug(resultUpload.getKey() + " === " + resultUpload.getValue());
        });
    }

    private static String toMd5(String value){

        String salt = "onvoghetilop";

        return DigestUtils.md5Hex(value + salt);
    }
}
