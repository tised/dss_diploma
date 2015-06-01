package com.tised.admin_program.controller;

import com.tised.admin_program.support.Addresses;
import com.tised.admin_program.model.DataContainer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * Created by tised_000 on 31.05.2015.
 */
public class ServerGetter {

    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(ServerGetter.class);
    DataContainer data;

    public ServerGetter(DataContainer data){

        this.data = data;
    }

    public ServerGetter(){}

    public void downloadProblems(){

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Addresses.getAllProblems);

            MultipartEntity entity = new MultipartEntity();

            entity.addPart("tag", new StringBody("as"));
            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String answer = EntityUtils.toString(resEntity);
                System.out.println("get all problems answer: " + answer);
                data.setProblemsFromServer(new JSONArray(answer));
            }
            if (resEntity != null) {
                resEntity.consumeContent();
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
        } catch (IOException ex) {
            logger.error(ex);
        } catch (JSONException ex) {
            logger.error(ex);
        }
    }

    public JSONArray downloadCurProblem(String id){

        JSONArray problems = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Addresses.getCurProblem);

            MultipartEntity entity = new MultipartEntity();

            entity.addPart("id_problem", new StringBody(id));
            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String answer = EntityUtils.toString(resEntity);
                logger.debug("get cur problem answer: " + answer);

                problems = new JSONArray(answer);
            }
            if (resEntity != null) {
                resEntity.consumeContent();
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
        } catch (IOException ex) {
            logger.error(ex);
        } catch (JSONException ex) {
            logger.error(ex);
        }

        return problems;
    }

    public int getResultFromProblem(String id){

        int resAl = 0;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Addresses.getResultAlternative);

            MultipartEntity entity = new MultipartEntity();

            entity.addPart("id_problem", new StringBody(id));
            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String answer = EntityUtils.toString(resEntity);
                logger.debug("get result alternative answer: " + answer);
                resAl = Integer.valueOf(answer);

            }
            if (resEntity != null) {
                resEntity.consumeContent();
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
        } catch (IOException ex) {
            logger.error(ex);
        } catch (JSONException ex) {
            logger.error(ex);
        }

        return resAl;
    }
}
