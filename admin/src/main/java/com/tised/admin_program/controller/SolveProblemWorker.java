package com.tised.admin_program.controller;

import com.tised.admin_program.model.DataContainer;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.codehaus.groovy.grails.web.json.JSONException;
import org.codehaus.groovy.grails.web.json.JSONObject;

/**
 * Created by tised_000 on 31.05.2015.
 */
public class SolveProblemWorker {

    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(SolveProblemWorker.class);

    private DataContainer dataContainer;
    private GridPane problemsFromServer;
    ServerGetter getter;
    public SolveProblemWorker(DataContainer dataContainer, GridPane problemsFromSever){

        this.dataContainer = dataContainer;
        this.problemsFromServer = problemsFromSever;
    }

    public void init() {

        getter = new ServerGetter(dataContainer);
        getter.downloadProblems();

        showAllProblems();
    }

    private void showAllProblems(){

        int sizeOfArr=dataContainer.getProblemsFromServer().length();

        for (int i = 0; i < sizeOfArr; i ++){

            try {
                JSONObject row = dataContainer.getProblemsFromServer().getJSONObject(i);
                Label idProblem = new Label(row.get("id_problem").toString());
                problemsFromServer.add(idProblem, 0, i+1);
                Label valOfExperts = new Label(row.get("val_of_experts").toString());
                problemsFromServer.add(valOfExperts, 1, i+1);

                Button getCurrentProblemButton = new Button(">>>");
                getCurrentProblemButton.setId(row.get("id_problem").toString());
                getCurrentProblemButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        logger.debug("clicked show current problem with id === " + getCurrentProblemButton.getId());

                        dataContainer.setProblemsFromServer(getter.downloadCurProblem(getCurrentProblemButton.getId()));

                    }
                });
                problemsFromServer.add(getCurrentProblemButton, 2, i+1);

            } catch (JSONException ex) {
                logger.error(ex);
            }
        }

    }
}
