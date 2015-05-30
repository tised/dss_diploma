package com.tised.admin_program.controller;


import com.tised.admin_program.support.AllertHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
	Pane addInfoProblemPanel;

	@FXML
	ListView criteriasList, alternativesList;

	@FXML
	TextField problem;

	private Scene scene;
	private ProblemInfoWorker problemWorker;
	private AnchorPane root;


	public void initialize(java.net.URL location,
            java.util.ResourceBundle resources){

		addInfoProblemPanel.setDisable(true);
		problemWorker = new ProblemInfoWorker(criteriasList, alternativesList);

		AllertHandler.showLogin();
	}
	
	@FXML
	public void startFillData(){
		
		logger.debug("clicked start fill data");
		addInfoProblemPanel.setDisable(false);
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

		problemWorker.clearData();

		startAddingInfo.setDisable(false);
		addInfoProblemPanel.setDisable(true);
		problem.setText("");
	}

	@FXML
	public void addExpertClicked(){

		AllertHandler.showAddExpert();
		logger.debug("clicked add expert");
	}

}
