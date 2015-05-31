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

    public ServerGetter(){

    }

    private void downloadProblems(){

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
                System.out.println("server answer: " + answer);
                data.setProblemsFromServer(new JSONArray(answer));
            }
            if (resEntity != null) {
                resEntity.consumeContent();
            }
        } catch (UnsupportedEncodingException ex) {
            logger.debug(ex);
        } catch (IOException ex) {
            logger.debug(ex);
        } catch (JSONException ex) {
            logger.debug(ex);
        }
    }
}
