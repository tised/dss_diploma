package com.tised.expert.controller;

import com.tised.expert.model.DataContainer;
import com.tised.expert.support.Addresses;
import groovy.json.internal.ArrayUtils;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.groovy.grails.web.json.JSONArray;

import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by tised_000 on 01.06.2015.
 */
public class ResultPriorityFinder {

    final static Logger logger = LogManager.getLogger(ResultPriorityFinder.class);

    GridPane workTable;
    int subR, subC, sizeOfArr;
    DataContainer data;
    Label result, progressLabel;
    String dir = "..\\expert\\", nameOfFile = dir + "C.txt";
    Scene scene;

    public ResultPriorityFinder(DataContainer d, Scene s){

        this.scene = s;
        this.workTable = (GridPane) scene.lookup("#resultTable");
        this.data = d;
        this.result = (Label) scene.lookup("#result");
        this.progressLabel = (Label) scene.lookup("#progressLabel");
    }


    public void produceResultPriority(){

        addToProgress("Начинаем поиск итоговой альтернативы. . .");
        subR = 0; subC = 1;

        workTable.getChildren().clear();
        addToProgress("заполняем табилцу. . .");
        ColumnConstraints cc = new ColumnConstraints();
        cc.setFillWidth(true);
        cc.setHgrow(Priority.ALWAYS);

        for (int i = 0; i < data.getCriterias().size() + 1; i++) {

            workTable.getColumnConstraints().add(cc);
        }

        RowConstraints rc = new RowConstraints();
        rc.setFillHeight(true);
        rc.setVgrow(Priority.ALWAYS);

        for (int i = 0; i < data.getAlternatives().size() + 1; i++) {

            workTable.getRowConstraints().add(rc);
        }

        workTable.setGridLinesVisible(true);

        logger.debug("grid r == "  + workTable.getRowConstraints().size() + " c == " + workTable.getColumnConstraints().size());

        workTable.add(new Label("Результат: "), workTable.getColumnConstraints().size() - 1, 0);
        workTable.add(new Label("Альтернативы: "), 0, 0);

        for (int i = 1; i < workTable.getRowConstraints().size() - 1; i++){

            workTable.add(new Label(data.getAlternatives().get(i - 1)), 0, i);
        }

        for (int i = 1; i < workTable.getColumnConstraints().size()-1; i++){

            workTable.add(new Label(String.valueOf( precision( data.getCalculatedPrioritys()[i - 1][0]) ) ), i, 0);
        }

        for (int i = 1; i < data.getCriterias().size()+1; i++){

            int j = 1;
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(new File(dir + "A"+ i +".txt"));
                BufferedReader br = new BufferedReader(fileReader);
                String line = null;
                // if no more lines the readLine() returns null
                while ((line = br.readLine()) != null) {
                    // reading lines until the end of the file
                    workTable.add(new Label(line), i, j);
                    j++;
                }  } catch (FileNotFoundException ex) {
                logger.error( ex);
            } catch (IOException ex) {
                logger.error(ex);
            } finally {
                try {
                    fileReader.close();
                } catch (IOException ex) {
                    logger.error( ex);
                }
            }

        }

        addToProgress("Производим расчет итоговой альтернативы. . . ");
        calculateResultMAI();

        for (int i = 0; i<workTable.getChildren().size(); i++){

            GridPane.setHalignment(workTable.getChildren().get(i), HPos.CENTER);
        }
    }

    private void calculateResultMAI(){

        float res[] = new float[data.getAlternatives().size()];

        for (int i = 1; i < workTable.getRowConstraints().size()-1; i ++){
            float tmp = 0;
            for(int j = 1; j < workTable.getColumnConstraints().size()-1; j++){

                tmp += Float.valueOf(((Label)getNodeFromGridPane(workTable,j, 0)).getText())*Float.valueOf(((Label)getNodeFromGridPane(workTable,j, i)).getText());
            }

            workTable.add(new Label(String.valueOf(precision(tmp))), workTable.getColumnConstraints().size()-1, i);
            res[i-1] = precision(tmp);
        }

        float betterPriority = 0;
        int index = 0;
        for (int i =0; i < res.length; i ++){

            if(res[i]>betterPriority) {
                betterPriority = res[i];
                index = i;
            }
        }

            for(int j = 1; j < workTable.getColumnConstraints().size()-1; j++){

                getNodeFromGridPane(workTable, j, index+1).setStyle("-fx-border-color: green;");
            }


        addToProgress("Итоговая альтернатива найдена! ");
        result.setText(result.getText() + " " + precision(betterPriority));
        addToProgress("Отправляем результат на сервер. . . ");
        try {

            sendToServer(res);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    private void sendToServer(float res[]) throws UnsupportedEncodingException, IOException{


        JSONArray mJSONArray = new JSONArray();
        for (int i = 0; i < res.length; i++){
            mJSONArray.add(res[i]);
        }
        logger.debug("arr to send == " + Arrays.toString(res));
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(Addresses.setExpertResult);

        MultipartEntity entity = new MultipartEntity();

        entity.addPart("alternative_priority", new StringBody(mJSONArray.toString()));
        entity.addPart("id_expert", new StringBody(String.valueOf(data.getIdExpert())));
        entity.addPart("id_problem", new StringBody(String.valueOf(data.getProblemID())));
        post.setEntity(entity);

        HttpResponse response = client.execute(post);

        HttpEntity resEntity = response.getEntity();

        if (resEntity != null) {
            addToProgress("Результаты успешно сохранены!");
            String answer = EntityUtils.toString(resEntity);
            System.out.println("server answer: " + answer);

        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {

            if (GridPane.getColumnIndex(node) != null
                    && GridPane.getColumnIndex(node) == col
                    && GridPane.getRowIndex(node) != null
                    && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private static Float precision(Float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private static Double precision(Double d) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    private void addToProgress(String progress){

        progressLabel.setText(progressLabel.getText() + "\n" + progress);
    }
}
