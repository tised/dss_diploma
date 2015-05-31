/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tised.admin_program.model;

import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONObject;

import java.util.ArrayList;


/**
 *
 * @author tised_000
 */
public class DataContainer {
    

    private JSONArray problemsFromServer;

    public DataContainer() {

        problemsFromServer = new JSONArray();
    }

    public JSONArray getProblemsFromServer() {

        return problemsFromServer;
    }

    public void setProblemsFromServer(JSONArray problemsFromServer) {

        this.problemsFromServer = problemsFromServer;
    }

    
}
