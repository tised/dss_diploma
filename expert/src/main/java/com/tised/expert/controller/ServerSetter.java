package com.tised.expert.controller;

import com.tised.expert.support.Addresses;
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
 * Created by tised_000 on 30.05.2015.
 */
public class ServerSetter {

    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(ServerSetter.class);

    public ServerSetter(){}

    public String checkLogin(String email, String password){
        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Addresses.checkLogin);

            MultipartEntity entity = new MultipartEntity();

            entity.addPart("password", new StringBody(password));
            entity.addPart("email", new StringBody(email));
            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String answer = EntityUtils.toString(resEntity);
                logger.debug("server answer: " + answer);
                result = answer;
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

        return result;
    }
}
