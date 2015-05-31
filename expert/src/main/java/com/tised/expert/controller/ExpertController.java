package com.tised.expert.controller;

import com.tised.expert.model.DataContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by tised_000 on 31.05.2015.
 */
public class ExpertController implements Initializable {

    final static Logger logger = LogManager.getLogger(ExpertController.class);

    @FXML
    Button restartButton, startMAIProcessing;

    @FXML
    ListView criteriaListView, alternativeListView;

    @FXML
    Label problem;

    DataContainer dataContainer;

    ObservableList criteriasArray = FXCollections.observableArrayList();
    ObservableList alternativesArray = FXCollections.observableArrayList();

    public void initialize(java.net.URL location,
                           java.util.ResourceBundle resources) {

        logger.trace("Controller initialized");
        dataContainer = new DataContainer();

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

        criteriaListView.setItems(criteriasArray);
        alternativeListView.setItems(alternativesArray);

        problem.setText(problem.getText() + dataContainer.getProblem());
    }

    public void startMAIProcessingClicked(){

        logger.trace("MAI processing started");


    }

}
