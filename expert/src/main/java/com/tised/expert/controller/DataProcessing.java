package com.tised.expert.controller;

import com.tised.expert.model.DataContainer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by tised_000 on 31.05.2015.
 */
public class DataProcessing {

    final static Logger logger = LogManager.getLogger(DataProcessing.class);

    GridPane workTable;
    Label leftOp, rightOp, consistency, subCrit;
    int sizeOfArr, valOfAlternatives = 0;
    DataContainer data;
    int subR = 0, subC = 1;
    String typeOf;
    String dir = "..\\expert\\", nameOfFile = dir + "C.txt";
    Scene scene;

    public DataProcessing(DataContainer data, Scene s){

        this.data = data;
        this.scene = s;
        this.workTable = (GridPane) scene.lookup("#workTable");
        this.leftOp = (Label) scene.lookup("#leftOp");
        this.rightOp = (Label) scene.lookup("#rightOp");
        this.consistency = (Label) scene.lookup("#consistency");
        this.subCrit = (Label) scene.lookup("#subCrit");
    }

    public Boolean startProcess(ArrayList<String> arr, String t) {

        this.typeOf = t;

        workTable.getChildren().clear();


        if (valOfAlternatives == data.getMnemonicCriterias().size())
            return false;

        subR = 0; subC = 1;

        sizeOfArr=arr.size()+1;

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100/sizeOfArr);
        cc.setFillWidth(true);
        cc.setHgrow(Priority.ALWAYS);

        for (int i = 0; i < sizeOfArr; i++) {

            workTable.getColumnConstraints().add(cc);
        }

        RowConstraints rc = new RowConstraints();
        rc.setFillHeight(true);
        rc.setVgrow(Priority.ALWAYS);

        for (int i = 0; i < sizeOfArr; i++) {

            workTable.getRowConstraints().add(rc);
        }

        workTable.setGridLinesVisible(true);

        for (int i = 0 ; i < sizeOfArr-1; i++){

            Text l1 = new Text(arr.get(i));
            workTable.add(l1, i+1, 0);

            Text l2 = new Text(arr.get(i));
            workTable.add(l2, 0, i+1);
        }

        for (int i = 1; i < sizeOfArr; i++)
            for(int j = 1; j < sizeOfArr; j++)
                if (i == j) {
                    Text l1 = new Text("1");
                    workTable.add(l1, i, j);
                }

        if(typeOf.equals("criteria")){

            leftOp.setText(data.getMnemonicCriterias().get(subR));
            rightOp.setText(data.getMnemonicCriterias().get(subC));
        }

        else
        {

            leftOp.setText(data.getMnemonicAlternatives().get(subR));
            rightOp.setText(data.getMnemonicAlternatives().get(subC));

            subCrit.setText(data.getMnemonicCriterias().get(valOfAlternatives));
            valOfAlternatives++;
            nameOfFile = dir + "A" + valOfAlternatives + ".txt";
        }

        return true;
    }

    public boolean updateTable(int val, boolean swap) {
        logger.debug("cur r === " + subR + "cur c === " + subC);

        //если номер строки и столбца не одинаковы, присваиваем имена
        //лейблам, иначе - увеличиваем номер столбца
        if (subC != subR){

            if(swap){
                workTable.add(new Text(String.valueOf(val)), subR + 1, subC + 1);
                workTable.add(new Text(String.valueOf(precision((float) 1 / val))), subC + 1, subR + 1);
            }
            else{
                workTable.add(new Text(String.valueOf(val)), subC + 1, subR + 1);
                workTable.add(new Text(String.valueOf(precision((float) 1 / val))), subR + 1, subC + 1);
            }
            subC++;

            //если вышли за границы массива, обновляем номер строки и столбца
            if (subC>sizeOfArr-2){
                subR++;
                subC = subR+1;
            }

            //проверяем вся ли таблица заполнена
            if (subR == sizeOfArr-2){
                //logger.debug(" before calc cur r === " + subR + "cur c === " + subC);
                calculateResult(sizeOfArr,sizeOfArr);
                return true;
            }

            //обновляем лейблы
            if(typeOf.equals("criteria")){
                leftOp.setText(data.getMnemonicCriterias().get(subR));
                rightOp.setText(data.getMnemonicCriterias().get(subC));
            }
            else
            {
                leftOp.setText(data.getMnemonicAlternatives().get(subR));
                rightOp.setText(data.getMnemonicAlternatives().get(subC));
            }

        }
        else
            subC++;

        return false;
    }

    private void calculateResult(int r, int c){

       // int r = workTable.getRowConstraints().size(), c = workTable.getColumnConstraints().size();
        consistency.setText("Consistency: ");
        logger.debug("r=="+r + " c== " +c);
        float[][] subCalc = new float[r][c];
        double[][] subCon = new double[r][1];

        double subVal = 1;
        float sumCon = 0;

        for (int i = 1; i < r; i++){
            logger.debug(i + " row");
            for (int j = 1; j < c; j++){

                subCalc[i-1][j-1] = Float.valueOf(((Text) getNodeFromGridPane(workTable, j, i)).getText());
                logger.debug(subCalc[i-1][j-1] + " ");
            }
        }

        data.setCalculatedCriterias(subCalc);

        for (int i = 0; i < c-1; i++){
            for (int j = 0; j < r-1; j++){

                subVal *= subCalc[i][j];
            }

            subVal = (float) Math.pow(subVal, (double)1/(c-1));
            subCon[i][0] = (float) subVal;
            sumCon += subCon[i][0];
            logger.debug(sumCon);
        }
        int l = 0;

        //приоритеты
        logger.debug("Приоритеты: ");

        for (l = 0; l < c-1; l++){
            subCon[l][0] /= sumCon;
            subCon[l][0] = roundToDecimals(subCon[l][0],2);
            logger.debug(subCon[l][0]);
        }

        try {
            saveToFile(nameOfFile, subCon, l);
        } catch (UnsupportedEncodingException ex) {
            logger.error( ex);
        } catch (IOException ex) {
            logger.error(ex);
        }

        sumCon = 0;
        double alpha = 0;
        for (int i = 0; i < c-1; i++){
            for (int j = 0; j<c-1; j++){

                sumCon += subCalc[j][i];
            }
            alpha += sumCon*subCon[i][0];
            sumCon = 0;
        }
        float consistencyIndex = 0;

        consistencyIndex = (float) ((alpha - (r-1)) / ((r-1) - 1));
        logger.debug("Consistency === " + consistencyIndex);
        consistency.setText(consistency.getText() + (precision(consistencyIndex)/getConstConsistency(r-1))*100 + "%");
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

    private void saveToFile(String f, double[][] array, int len) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        File file = new File(f);
        if (f.contains("C"))
            data.setCalculatedPrioritys(array);
        logger.debug("file === " + f);

        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        PrintWriter writer = new PrintWriter(f, "UTF-8");
        for (int i = 0; i < len; i++){
            writer.println(array[i][0]);
        }
        writer.close();
    }

    private static Float precision(Float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private float getConstConsistency(int v){

        switch(v){
            case 3: return (float) 0.58;
            case 4: return (float) 0.9;
            case 5: return (float) 1.12;
            case 6: return (float) 1.24;
            case 7: return (float) 1.32;
            case 8: return (float) 1.41;
            case 9: return (float) 1.45;
            case 10: return (float) 1.49;
        }

        return (float) 0.58;
    }

    public static double roundToDecimals(double d, int c){

        int temp = (int)(d * Math.pow(10 , c));
        return ((double)temp)/Math.pow(10 , c);
    }
}
