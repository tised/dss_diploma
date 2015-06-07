package com.tised.admin_program.controller;

import com.tised.admin_program.model.DataContainer;
import com.tised.admin_program.support.AllertHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
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
	Label resultAlternative, progressLabel, solveProgressLabel;

	@FXML
	Tab addProblemTab, solveProblemTab;

	@FXML
	GridPane problemsFromServer;

	@FXML
	TableView tableWithResults;

	private AddProblemWorker problemWorker;
	private SolveProblemWorker solveWorker;
	private Scene scene;

	public void initialize(java.net.URL location,
            java.util.ResourceBundle resources){

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

		addToProgress("Добавлен новый критерий");

		TextField newCriteria = new TextField();
		newCriteria.setPromptText("Введите критерий. . .");

		criteriasList.getItems().add(newCriteria);
	}

	@FXML
	public void addAlternativeClicked(){

		logger.debug("clicked add alternative");
		addToProgress("Добавлена новая альтернатива");
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
		addToProgressSolveProblem("рассчитываем итоговую альтернативу для проблемы с номером " + resultAlternative.getId());
		ServerGetter getter = new ServerGetter();
		int resID = getter.getResultFromProblem(resultAlternative.getId());

		((TableColumn)tableWithResults.getColumns().get(resID)).setStyle("-fx-border-color: green;");
		addToProgressSolveProblem("лучшая альтерантива по итогу коллективного решения " + resID);
		resultAlternative.setText("Результат: " + String.valueOf(resID));
	}
	
	public void customInit(){

		dataContainer = new DataContainer();
		addInfoProblemPanel.setDisable(true);

		problemWorker = new AddProblemWorker(dataContainer, scene);
		solveWorker = new SolveProblemWorker(scene, dataContainer);

		Image image = new Image(this.getClass().getClassLoader().getResource("dss_image.png").toExternalForm());
		ImageView iv = (ImageView) scene.lookup("#programImage");
		iv.setImage(image);
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

	}

	public void setScene(Scene scene){

		this.scene = scene;
	}

	private void addToProgress(String text){

		progressLabel.setText(progressLabel.getText() + "\n"+text);
	}

	private void addToProgressSolveProblem(String text){

		solveProgressLabel.setText(solveProgressLabel.getText() + "\n"+text);
	}

}
