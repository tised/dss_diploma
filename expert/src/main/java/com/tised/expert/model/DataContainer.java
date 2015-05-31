package com.tised.expert.model;

import java.util.ArrayList;

/**
 * Created by tised_000 on 31.05.2015.
 */
public class DataContainer {

    private String problem;
    private ArrayList<String> alternatives, criterias, mnemonicAlternatives, mnemonicCriterias;
    private float[][] calculatedCriterias;
    private double[][] calculatedPrioritys;

    public double[][] getCalculatedPrioritys() {
        return calculatedPrioritys;
    }

    public void setCalculatedPrioritys(double[][] calculatedPrioritys) {
        this.calculatedPrioritys = calculatedPrioritys;
    }

    public float[][] getCalculatedCriterias() {
        return calculatedCriterias;
    }

    public void setCalculatedCriterias(float[][] calculatedCriterias) {
        this.calculatedCriterias = calculatedCriterias;
    }

    public ArrayList<String> getMnemonicAlternatives() {
        return mnemonicAlternatives;
    }

    public void setMnemonicAlternatives(ArrayList<String> mnemonicAlternatives) {
        this.mnemonicAlternatives = mnemonicAlternatives;
    }

    public ArrayList<String> getMnemonicCriterias() {
        return mnemonicCriterias;
    }

    public void setMnemonicCriterias(ArrayList<String> mnemonicCriterias) {
        this.mnemonicCriterias = mnemonicCriterias;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public ArrayList<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(ArrayList<String> alternatives) {
        this.alternatives = alternatives;
    }

    public ArrayList<String> getCriterias() {
        return criterias;
    }

    public void setCriterias(ArrayList<String> criterias) {
        this.criterias = criterias;
    }


    public DataContainer(){

    }
    
}
