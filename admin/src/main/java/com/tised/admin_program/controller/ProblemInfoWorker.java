package com.tised.admin_program.controller;

import com.tised.admin_program.support.Addresses;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by tised_000 on 30.05.2015.
 */
public class ProblemInfoWorker {

    ObservableList criteriasArray = FXCollections.observableArrayList();
    ObservableList alternativesArray = FXCollections.observableArrayList();

    ListView alternatives, criterias;

    public ProblemInfoWorker(ListView c, ListView a){

        this.criterias = c;
        this.alternatives = a;
    }

    public void initProblemInput(){

        TextField startCriteria = new TextField();
        startCriteria.setPromptText("Введите критерий. . .");
        criteriasArray.add(startCriteria);

        TextField startAlternative= new TextField();
        startAlternative.setPromptText("Введите альтернативу. . .");
        alternativesArray.add(startAlternative);

        criterias.setItems(criteriasArray);
        alternatives.setItems(alternativesArray);
    }

    public void packDataToJsonAndSend(String problem) throws IOException {

        JSONArray jsonAlternatives = new JSONArray();
        JSONArray jsonCriterias = new JSONArray();
        JSONObject jsonProblem = new JSONObject();
        JSONArray dataToSend = new JSONArray();

        jsonProblem.put("problem", problem);

        for (Object txt : alternativesArray){

            jsonAlternatives.add(((TextField)txt).getText());
        }

        for (Object txt : criteriasArray){

            jsonCriterias.add(((TextField)txt).getText());
        }

        dataToSend.add(jsonProblem);
        dataToSend.add(jsonCriterias);
        dataToSend.add(jsonAlternatives);

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(Addresses.saveProblem);

        MultipartEntity entity = new MultipartEntity();

        try {
            entity.addPart("problem", new StringBody(jsonProblem.toString()));
            entity.addPart("criterias", new StringBody(jsonCriterias.toString()));
            entity.addPart("alternatives", new StringBody(jsonAlternatives.toString()));
            entity.addPart("id_user",new StringBody("1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        post.setEntity(entity);

        HttpResponse response = client.execute(post);

        HttpEntity resEntity = response.getEntity();

        if (resEntity != null) {
            System.out.println("server answer: " + EntityUtils.toString(resEntity));
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
    }

    public void clearData(){

        alternatives.getItems().clear();
        criterias.getItems().clear();
    }


}
