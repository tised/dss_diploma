package com.tised.expert.controller;

import com.tised.expert.model.DataContainer;
import com.tised.expert.support.Addresses;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tised_000 on 31.05.2015.
 */
public class ServerGetter {

    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(ServerGetter.class);

    public void getProblem(DataContainer data) throws IOException {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(Addresses.getProblem);


        MultipartEntity entity = new MultipartEntity();
        post.setEntity(entity);

        HttpResponse response = client.execute(post);

        HttpEntity resEntity = response.getEntity();

        if (resEntity != null) {
            String result = EntityUtils.toString(resEntity);
            logger.debug("server answer: " + result);

            try {
                JSONArray recievedProblem = new JSONArray(result);

                data.setProblem(recievedProblem.getString(0));

                JSONArray arr = new JSONArray(recievedProblem.get(1).toString());
                ArrayList<String> list = new ArrayList<String>();
                ArrayList<String> mList = new ArrayList<String>();
                for(int i = 0; i < arr.length(); i++){
                    list.add(arr.get(i).toString()+"(A"+i+")");
                    mList.add("A"+i);
                }
                data.setAlternatives(list);
                data.setMnemonicAlternatives(mList);

                arr = recievedProblem.getJSONArray(2);//new org.json.JSONArray(recievedProblem.get(2).toString());

                list = new ArrayList<String>();
                mList = new ArrayList<String>();
                for(int i = 0; i < arr.length(); i++){
                    list.add(arr.get(i).toString()+"(C"+i+")");
                    mList.add("C"+i);
                }

                data.setCriterias(list);
                data.setMnemonicCriterias(mList);

            } catch (JSONException ex) {
                logger.error(ex);
            }
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
    }
}
