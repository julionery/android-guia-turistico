package com.example.julionery.guia.Data;

import android.content.Context;

import com.example.julionery.guia.Models.User;
import com.example.julionery.guia.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebTaskLogin extends WebTaskBase{

    private static String SERVICE_URL = "login";
    private String email;
    private String senha;

    public WebTaskLogin(Context context, String email, String senha){
        super(context, SERVICE_URL);
        this.email = email;
        this.senha = senha;
    }

    @Override
    public void handleResponse(String response) {
        User user = new User();
        try {
            JSONObject responseAsJSON = new JSONObject(response);
            String name = responseAsJSON.getString("name");
            user.setName(name);
            String token = responseAsJSON.getString("token");
            user.setToken(token);
            String email = responseAsJSON.getString("email");
            user.setEmail(email);
            String subtitulo = responseAsJSON.getString("subtitulo");
            user.setSubtitulo(subtitulo);
            String photoUrl = responseAsJSON.getString("photoURL");
            user.setPhotoURL(photoUrl);
            EventBus.getDefault().post(user);

        } catch (JSONException e) {
            if(!isSilent()){
                EventBus.getDefault().post(new Error(getContext().getString(R.string.label_error_invalid_response)));
            }
        }
    }

    private User readUser(JSONObject userAsJSON)  throws  JSONException{
        User user = new User();
        user.setName(userAsJSON.getString("name"));
        user.setToken(userAsJSON.getString("token"));
        user.setEmail(userAsJSON.getString("email"));
        user.setSubtitulo(userAsJSON.getString("subtitulo"));
        user.setPhotoURL(userAsJSON.getString("photoURL"));
        return user;
    }

    @Override
    public String getRequestBody(){
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("email", email);
        requestMap.put("senha", senha);

        JSONObject json = new JSONObject(requestMap);
        String jsonString = json.toString();

        return  jsonString;
    }


}