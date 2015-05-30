package com.tised.admin_program.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class RootLayoutController implements Initializable{

    final static Logger logger = LogManager.getLogger(RootLayoutController.class);

	@FXML
	Button startAddingInfo, addAlternative, addCriteria;
	
	@FXML
	Pane addPanelCriterias;

	@FXML
	ListView criteriasList, alternativesList;

	@FXML
	TextField problem;

	private Scene scene;
	private ProblemInfoWorker problemWorker;
	private AnchorPane root;

	public void initialize(java.net.URL location,
            java.util.ResourceBundle resources){

		addPanelCriterias.setDisable(true);
		problemWorker = new ProblemInfoWorker(criteriasList, alternativesList);
	}
	
	@FXML
	public void startFillData(){
		
		logger.debug("clicked start fill data");
		addPanelCriterias.setDisable(false);
		startAddingInfo.setDisable(true);

		problemWorker.initProblemInput();
	}
	
	@FXML
	public void addCriteriaClicked(){

        logger.debug("clicked add criteria");

		TextField newCriteria = new TextField();
		newCriteria.setPromptText("Введите критерий. . .");

		criteriasList.getItems().add(newCriteria);
	}

	@FXML
	public void addAlternativeClicked(){

		logger.debug("clicked add alternative");

		TextField newAlternative = new TextField();
		newAlternative.setPromptText("Введите альтернативу. . .");

		alternativesList.getItems().add(newAlternative);
	}

	@FXML
	public void sendProblemClicked(){

		logger.debug("clicked send problem");

		try {
			problemWorker.packDataToJsonAndSend(problem.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void addExpertClicked(){

		logger.debug("clicked add expert");
	}

}
