package com.celar.celvisitas.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.celar.celvisitas.Activities.LoginActivity;
import com.celar.celvisitas.Tools.AppConfig;

import org.json.JSONObject;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String KEY_ID          = "id";
    private static final String KEY_EMAIL    = "email";
    private static final String KEY_PASSWORD    = "password";
    private static final String KEY_NOMBRE      = "nombre";
    private static final String KEY_CASA  = "";
    private static final String KEY_RESIDENCIA  = "";
    private static final String IS_LOGIN       = "IsLoggedIn";
    private static final String KEY_USER_SESSION  = "";



    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(AppConfig.APPNAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createloginSession(String email,String password,String nombre){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_NOMBRE,nombre);
        editor.commit();
    }

    public void savedTokenUser(String tokenUser){
        editor.putString(KEY_USER_SESSION,tokenUser);
        editor.commit();
    }


    public void createInfoProfile(String casa,String residencia){
        editor.putString(KEY_CASA,casa);
        editor.putString(KEY_RESIDENCIA,residencia);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN,false);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent intent = new Intent(_context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }
    }
    public HashMap<String,String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        user.put(KEY_EMAIL,pref.getString(KEY_EMAIL,null));
        user.put(KEY_PASSWORD,pref.getString(KEY_PASSWORD,null));
        user.put(KEY_ID,pref.getString(KEY_ID,null));
        user.put(KEY_NOMBRE,pref.getString(KEY_NOMBRE,null));
        user.put(KEY_USER_SESSION,pref.getString(KEY_USER_SESSION,null));
        user.put(KEY_CASA,pref.getString(KEY_CASA,null));
        user.put(KEY_RESIDENCIA,pref.getString(KEY_RESIDENCIA,null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        AppConfig.Bearer = "";
        AppConfig.user.clearData();

        _context.startActivity(intent);
    }



    public String getKeyUserSession() {
        return pref.getString(KEY_USER_SESSION, "");
    }

    public String getMD5Password() {
        return pref.getString(KEY_PASSWORD, "");
    }

    public String getNombre() {
        return pref.getString(KEY_NOMBRE, "");
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "");
    }
    public String getCasa() {
        return pref.getString(KEY_CASA, "");
    }
    public String getResidencia() {
        return pref.getString(KEY_RESIDENCIA, "");
    }

    public String getID() {
        return pref.getString(KEY_ID, "");
    }

}
