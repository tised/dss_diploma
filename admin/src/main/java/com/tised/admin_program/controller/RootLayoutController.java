package com.tised.admin_program.controller;

import com.tised.admin_program.model.DataContainer;
import com.tised.admin_program.support.AllertHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public class RootLayoutController implements Initializable{

    final static Logger logger = LogManager.getLogger(RootLayoutController.class);
	DataContainer dataContainer;

	@FXML
	Button startAddingInfo, addAlternative, addCriteria, getResultButton;
	
	@FXML
	Pane addInfoProblemPanel;

	@FXML
	ListView criteriasList, alternativesList;

	@FXML
	TextField problem;

	@FXML
	TabPane tabs;

	@FXML
	Label resultAlternative;

	@FXML
	Tab addProblemTab, solveProblemTab;

	@FXML
	GridPane problemsFromServer, currentProblemFromServer;

	private AddProblemWorker problemWorker;
	private SolveProblemWorker solveWorker;

	public void initialize(java.net.URL location,
            java.util.ResourceBundle resources){

		dataContainer = new DataContainer();
		addInfoProblemPanel.setDisable(true);

		problemWorker = new AddProblemWorker(criteriasList, alternativesList, dataContainer);
		solveWorker = new SolveProblemWorker(dataContainer, problemsFromServer, currentProblemFromServer, resultAlternative);

		tabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> arg0,
								Tab arg1, Tab arg2) {

				if(arg2 == solveProblemTab) {
					logger.debug("solve problem tab chosed");

					solveWorker.init();
				}

				if(arg2 == addProblemTab) {
					logger.debug("add problem tab chosed");


				}
			}
		});

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

	@FXML
	public void getResultButtonClick(){

		ServerGetter getter = new ServerGetter();

		resultAlternative.setText("Результат: " + String.valueOf(getter.getResultFromProblem(resultAlternative.getId())));
	}

}
