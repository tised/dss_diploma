package com.tised.admin_program.controller;

import com.tised.admin_program.model.DataContainer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONException;
import org.codehaus.groovy.grails.web.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tised_000 on 31.05.2015.
 */

public class SolveProblemWorker {

    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(SolveProblemWorker.class);
    private final Label result;
    private DataContainer dataContainer;
    private GridPane problemsFromServerPane;//, currentProblemPane;
    private ServerGetter getter;
    private Scene scene;
    private TableView tableWithResults;

    public SolveProblemWorker(Scene scene, DataContainer dataContainer, GridPane problemsFromSever, Label res){

        this.dataContainer = dataContainer;
        this.problemsFromServerPane = problemsFromSever;
        //this.currentProblemPane = currentProblemFromServer;
        this.result = res;
        this.scene = scene;
        tableWithResults = (TableView) scene.lookup("#tableWithResults");


    }

    public void init() {

        getter = new ServerGetter(dataContainer);
        getter.downloadProblems();

        problemsFromServerPane.getChildren().subList(2,problemsFromServerPane.getChildren().size()).clear();
        tableWithResults.getColumns().clear();

        showAllProblems();
    }

    private void showAllProblems(){

        int sizeOfArr=dataContainer.getProblemsFromServer().length();

        for (int i = 0; i < sizeOfArr; i ++){

            try {
                JSONObject row = dataContainer.getProblemsFromServer().getJSONObject(i);
                Label idProblem = new Label(row.get("id_problem").toString());
                idProblem.setAlignment(Pos.CENTER);
                idProblem.setMaxWidth(Double.MAX_VALUE);
                idProblem.setPrefWidth(100);
                idProblem.setStyle("-fx-border-color: black;");
                problemsFromServerPane.add(idProblem, 0, i + 1);

                Label valOfExperts = new Label(row.get("val_of_experts").toString());
                valOfExperts.setAlignment(Pos.CENTER);
                valOfExperts.setMaxWidth(Double.MAX_VALUE);
                valOfExperts.setPrefWidth(100);
                valOfExperts.setStyle("-fx-border-color: black;");
                problemsFromServerPane.add(valOfExperts, 1, i + 1);

                Button getCurrentProblemButton = new Button(">>>");
                getCurrentProblemButton.setId(row.get("id_problem").toString());
                getCurrentProblemButton.setAlignment(Pos.CENTER);

                getCurrentProblemButton.setOnMouseClicked(event -> {

                    tableWithResults.getColumns().clear();

                    logger.debug("clicked show current problem with id === " + getCurrentProblemButton.getId());

                    result.setId(getCurrentProblemButton.getId());

                    JSONArray curProblemArray = getter.downloadCurProblem(getCurrentProblemButton.getId());

                    int row1 = 0;
                    logger.debug("len of array = " + curProblemArray.length() + " len of sub == " + ((JSONObject)curProblemArray.get(0)).getJSONArray("res_vector").length());
                    String[][] resultItemsArray = new String[curProblemArray.length()][((JSONObject)curProblemArray.get(0)).getJSONArray("res_vector").length()];

                    for (Object curVector : curProblemArray) {
                        String[] elements = getStringArray(((JSONObject)curVector).getJSONArray("res_vector"));

                        resultItemsArray[row1] = elements;
                        row1++;
                    }

                    ObservableList<String[]> data = FXCollections.observableArrayList();

                    data.clear();
                    data.addAll(Arrays.asList(resultItemsArray));

                    logger.debug("Array for table === "  + data);

                    for (int i1 = 0; i1 < resultItemsArray[0].length; i1++) {
                        TableColumn tc = new TableColumn();
                        final int colNo = i1;
                        tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                            @Override
                            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                                return new SimpleStringProperty((p.getValue()[colNo]));
                            }
                        });

                        tc.setPrefWidth(90);

                        tableWithResults.getColumns().add(tc);
                    }

                    tableWithResults.setItems(data);
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
