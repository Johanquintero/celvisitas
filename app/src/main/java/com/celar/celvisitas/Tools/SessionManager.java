package com.celar.celvisitas.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Julio Garcés on 11/05/2017.
 */
public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String KEY_ID          = "id";
    private static final String KEY_USERNAME    = "username";
    private static final String KEY_PASSWORD    = "password";
    private static final String KEY_USERNAME_SAVED    = "username_saved";
    private static final String KEY_PASSWORD_SAVED    = "password_saved";
    private static final String KEY_NOMBRE      = "nombre";
    private static final String KEY_APELLIDOS   = "apellidos";
    private static final String KEY_PERFILID    = "perfil_id";
    private static final String KEY_REGID       = "reg_id";
    private static final String KEY_ACCEPTA_POLITICAS = "acepta_politicas";
    private static final String KEY_INICIA_HUELLA = "usa_huella";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(AppConfig.APPNAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setAceptaPolitica(boolean actepta){
        editor.putBoolean(KEY_ACCEPTA_POLITICAS,actepta);
        editor.commit();
    }

    public void setUsebiometric(boolean useDiometric){
        editor.putBoolean(KEY_INICIA_HUELLA,useDiometric);
        editor.commit();
    }

    public void setUser(JSONObject u, boolean rememberLogin) {
        try {
            AppConfig.Bearer = u.get("token").toString();
            if (rememberLogin) {
                editor.putString(KEY_USERNAME, u.get("usuario").toString());
                editor.putString(KEY_PASSWORD, u.get("clave").toString());
                editor.putString(KEY_ID, u.get("id").toString());
                editor.putString(KEY_NOMBRE, u.get("nombre").toString());
                editor.putString(KEY_APELLIDOS, u.get("apellidos").toString());
                editor.putString(KEY_PERFILID, u.get("perfil_id").toString());
                editor.commit();
            }
        } catch (Exception e){
            Log.e("ERROR", e.getMessage());
        }

        AppConfig.user.setUser(u);
    }
    
    public void setUserSavedToBiometrics(String username, String password) {
        try {
            editor.putString(KEY_USERNAME_SAVED, username);
            editor.putString(KEY_PASSWORD_SAVED, password);
            editor.commit();
        } catch (Exception e){
            Log.e("ERROR", e.getMessage());
        }
    }

    public void destroySession() {
        editor.putString(KEY_USERNAME, "");
        editor.putString(KEY_PASSWORD, "");
        editor.putString(KEY_ID, "");
        editor.putString(KEY_NOMBRE, "");
        editor.putString(KEY_PERFILID, "");
        editor.putString(KEY_APELLIDOS, "");
        editor.putString(KEY_REGID, "");

        AppConfig.Bearer = "";

        editor.commit();

        AppConfig.user.clearData();
    }

    public boolean isLoggedIn() {
        return !(pref.getString(KEY_USERNAME, "").equals("") || pref.getString(KEY_PASSWORD, "").equals(""));
    }

    public boolean getAceptaPolitica(){
        return pref.getBoolean(KEY_ACCEPTA_POLITICAS,false);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }

    public String getMD5Password() {
        return pref.getString(KEY_PASSWORD, "");
    }

    public String getNombre() {
        return pref.getString(KEY_NOMBRE, "");
    }

    public String getApellidos() {
        return pref.getString(KEY_APELLIDOS, "");
    }

    public String getPerfilid() {
        return pref.getString(KEY_PERFILID, "");
    }

    public String getID() {
        return pref.getString(KEY_ID, "");
    }

    public  boolean getBiometric(){return  pref.getBoolean(KEY_INICIA_HUELLA,false);}

    public String getUsernameSaved() {
        return pref.getString(KEY_USERNAME_SAVED, "");
    }

    public String getPasswordSaved() {
        return pref.getString(KEY_PASSWORD_SAVED, "");
    }
}
