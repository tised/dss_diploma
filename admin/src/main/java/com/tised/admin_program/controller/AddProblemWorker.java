package com.tised.admin_program.controller;

import com.tised.admin_program.CustomControls.CustomTextField;
import com.tised.admin_program.model.DataContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.codehaus.groovy.grails.web.json.JSONArray;
import java.io.IOException;

/**
 * Created by tised_000 on 30.05.2015.
 */
public class AddProblemWorker {

    ObservableList criteriasArray = FXCollections.observableArrayList();
    ObservableList alternativesArray = FXCollections.observableArrayList();
    DataContainer dataContainer;
    ListView alternatives, criterias;
    Scene scene;
    Label progressLabel;

    public AddProblemWorker(DataContainer dataContainer, Scene scene){

        this.scene = scene;
        this.criterias = (ListView) scene.lookup("#criteriasList");
        this.alternatives = (ListView) scene.lookup("#alternativesList");
        this.dataContainer = dataContainer;
        this.progressLabel = (Label) scene.lookup("#progressLabel");
    }

    public void initProblemInput(){

        addToProgress("Начнинаем добавление новой альтернативы. . .");
        CustomTextField startCriteria = new CustomTextField();
        startCriteria.setPromptText("Введите критерий. . .");
        criteriasArray.add(startCriteria);

        TextField startAlternative= new TextField();
        startAlternative.setPromptText("Введите альтернативу. . .");
        alternativesArray.add(startAlternative);

        criterias.setItems(criteriasArray);
        alternatives.setItems(alternativesArray);
    }

    public void packDataToJsonAndSend(String problem) throws IOException {

        addToProgress("Пакуем данные в JSON для отправления. . .");
        JSONArray jsonAlternatives = new JSONArray();
        JSONArray jsonCriterias = new JSONArray();
        JSONArray dataToSend = new JSONArray();

        for (Object txt : alternativesArray){

            jsonAlternatives.add(((TextField)txt).getText());
        }

        for (Object txt : criteriasArray){

            jsonCriterias.add(((TextField)txt).getText());
        }

        dataToSend.add(problem);
        dataToSend.add(jsonCriterias);
        dataToSend.add(jsonAlternatives);

        ServerSetter setter = new ServerSetter(scene);
        addToProgress("Отправляем новую проблему на сервер. . .");
        setter.addProblemToServer(dataToSend);
    }

    public void clearData(){

        alternatives.getItems().clear();
        criterias.getItems().clear();
    }

    private void addToProgress(String text){

        progressLabel.setText(progressLabel.getText() + "\n"+text);
    }

}
