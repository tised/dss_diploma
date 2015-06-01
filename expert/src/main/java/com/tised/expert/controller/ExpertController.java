package com.tised.expert.controller;

import com.tised.expert.model.DataContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by tised_000 on 31.05.2015.
 */
public class ExpertController implements Initializable {

    final static Logger logger = LogManager.getLogger(ExpertController.class);

    String[] marksStringArray = {"Равная важность",
            "Среднее",
            "Умеренное превосходство",
            "Среднее",
            "Существенное или сильное превосходство",
            "Среднее",
            "Значительное превосходство",
            "Среднее",
            "Очень большое превосходство"};

    @FXML
    Button restartButton, startMAIProcessing, nextOp;

    @FXML
    ListView criteriaListView, alternativeListView, marks;

    @FXML
    Label problem, rightOp, leftOp, consistency;

    @FXML
    GridPane workTable;

    DataContainer dataContainer;

    ObservableList criteriasArray = FXCollections.observableArrayList();
    ObservableList alternativesArray = FXCollections.observableArrayList();
    ObservableList marksArray = FXCollections.observableArrayList();

    DataProcessing maiProcess;
    private boolean swap;

    public void initialize(java.net.URL location,
                           java.util.ResourceBundle resources) {

        logger.trace("Controller initialized");
        dataContainer = new DataContainer();
        maiProcess = new DataProcessing(dataContainer, workTable, leftOp, rightOp, consistency);

        ServerGetter getter = new ServerGetter();
        try {
            getter.getProblem(dataContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        initStartData();
    }

    public void restartButtonClick(){

        logger.debug("clicked restart button");
    }

    private void initStartData(){

        criteriasArray.addAll(dataContainer.getCriterias());
        alternativesArray.addAll(dataContainer.getAlternatives());
        marksArray.addAll(marksStringArray);

        criteriaListView.setItems(criteriasArray);
        alternativeListView.setItems(alternativesArray);
        marks.setItems(marksArray);

        problem.setText(problem.getText() + dataContainer.getProblem());
    }

    public void swapButtonClicked(){

        logger.trace("swap clicked");
        String tmp = "";
        tmp = leftOp.getText();
        leftOp.setText(rightOp.getText());
        rightOp.setText(tmp);
        if (!swap)
            swap = true;
        else
            swap = false;
    }

    public void startMAIProcessingClicked(){

        logger.trace("MAI processing started");
        marks.getSelectionModel().select(0);
        maiProcess.startProcess(dataContainer.getMnemonicCriterias(),"criteria");
    }

    public void nextOpClicked(){

        if (maiProcess.updateTable(marks.getSelectionModel().getSelectedIndex()+1,swap))   {

          //  JOptionPane.showMessageDialog(null, "Вся таблица заполнена!");
            nextOp.setDisable(true);
        }
        swap = false;

    }

}
