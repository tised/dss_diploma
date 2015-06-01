package com.tised.admin_program.controller;

import com.tised.admin_program.model.DataContainer;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONException;
import org.codehaus.groovy.grails.web.json.JSONObject;

/**
 * Created by tised_000 on 31.05.2015.
 */

public class SolveProblemWorker {

    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(SolveProblemWorker.class);
    private final Label result;

    private DataContainer dataContainer;
    private GridPane problemsFromServerPane, currentProblemPane;
    private ServerGetter getter;


    public SolveProblemWorker(DataContainer dataContainer, GridPane problemsFromSever, GridPane currentProblemFromServer, Label res){

        this.dataContainer = dataContainer;
        this.problemsFromServerPane = problemsFromSever;
        this.currentProblemPane = currentProblemFromServer;
        this.result = res;
    }

    public void init() {

        getter = new ServerGetter(dataContainer);
        getter.downloadProblems();

//        problemsFromServerPane.getChildren().clear();
//        problemsFromServerPane.setGridLinesVisible(true);
        currentProblemPane.getChildren().clear();
        currentProblemPane.setGridLinesVisible(true);

        showAllProblems();
    }

    private void showAllProblems(){

        int sizeOfArr=dataContainer.getProblemsFromServer().length();

        for (int i = 0; i < sizeOfArr; i ++){

            try {
                JSONObject row = dataContainer.getProblemsFromServer().getJSONObject(i);
                Label idProblem = new Label(row.get("id_problem").toString());
                problemsFromServerPane.add(idProblem, 0, i + 1);
                Label valOfExperts = new Label(row.get("val_of_experts").toString());
                problemsFromServerPane.add(valOfExperts, 1, i + 1);

                Button getCurrentProblemButton = new Button(">>>");
                getCurrentProblemButton.setId(row.get("id_problem").toString());

                getCurrentProblemButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        logger.debug("clicked show current problem with id === " + getCurrentProblemButton.getId());
                        currentProblemPane.getChildren().clear();
                        result.setId(getCurrentProblemButton.getId());
                        JSONArray curProblemArray = getter.downloadCurProblem(getCurrentProblemButton.getId());

                        int row = 0;

                        for (Object curVector : curProblemArray) {
                            String[] elements = getStringArray(((JSONObject)curVector).getJSONArray("res_vector"));

                            for (int j = 0; j < elements.length; j++) {
                                currentProblemPane.add(new Label(elements[j]), j, row);
                            }
                            row++;
                        }
                        currentProblemPane.setGridLinesVisible(true);
                    }
                });
                problemsFromServerPane.add(getCurrentProblemButton, 2, i + 1);

            } catch (JSONException ex) {
                logger.error(ex);
            }
        }
    }

    private  String[] getStringArray(JSONArray jsonArray){
        String[] stringArray = null;
        int length = jsonArray.length();
        if(jsonArray!=null){
            stringArray = new String[length];
            for(int i=0;i<length;i++){
                stringArray[i]= jsonArray.optString(i);
            }
        }
        return stringArray;
    }
}
