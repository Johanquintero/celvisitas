package com.celar.celvisitas.Tools;

import android.hardware.camera2.CaptureRequest;
import android.os.Environment;
import android.os.Handler;

import com.celar.celvisitas.Activities.DashboardActivity;
import com.celar.celvisitas.Activities.LoginActivity;
import com.celar.celvisitas.models.User;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


/**
 * Created by Johan Garcia on 15/10/2021.
 */

public class AppConfig {

    public static final String YOUTUBE_APIKEY = "";
    public static final String REST_API = "http://10.10.10.70:8000/api/";


    public static final String REST_FORMULARIO = "https://181.48.90.198/celcovid/api/rest/formulario_post_unificado.php";

    public static com.celar.celvisitas.Tools.SessionManager session;
    public static String APPNAME = "Celvisitas";
    public static String USERNAME = "";
    public static String token = "";
    public static JSONObject userObject = new JSONObject();
    public static User user = new User();
    public static DashboardActivity dashboard = null;
    public static LoginActivity login = null;
    //    public static List<Curso> cursos = new ArrayList<>();
//    public static ArrayList<String> lista_cursos = new ArrayList<>();

    public static JSONObject json = null;
    public static Timer syncTimer = null;
    public static String nombre_foto = "";
    public static String ruta_foto = "";
    public static final Handler syncHandler = new Handler();
    public static Object Bearer;

    public static String getRuta() { return dashboard.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() ; }
    public static String cid = null;
    public static String uid = null;
    public static int notificacion_id = -1;

    public static boolean loggin = false;

}