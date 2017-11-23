package kondratkov.advocatesandnotariesrf;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import kondratkov.advocatesandnotariesrf.api_classes.NewPushMessage;
import kondratkov.advocatesandnotariesrf.input.SignUP;
import kondratkov.advocatesandnotariesrf.my_info.My_orders;
import kondratkov.advocatesandnotariesrf.my_info.My_perep_orders_mess;
import kondratkov.advocatesandnotariesrf.my_info.My_questions;

public class Service_mess extends Service {
    private static final String DEBUG_TAG = "qwerty";
    IN in;
    public double lat=0, lon=0;
    public String URL;
    public String json_start;
    public JSONObject jService;
    private static final int NOTIFY_ID = 101;
    public int N = 0;
    public int code;
    public SharedPreferences sPref;
    public Service_mess() {
    }

    public static final long NOTIFY_INTERVAL = 60 * 1000; // 60 seconds

    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        in = new IN();
        sPref = PreferenceManager.getDefaultSharedPreferences(this);


        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,
                NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    JSONObject json_st = new JSONObject();
                    try {
                        json_st.put("lng", 0);
                        json_st.put("lad", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    json_start = String.valueOf(json_st);
                    //XY_set();
                    new UrlConnectionTask().execute(json_start);
                }
            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "[dd/MM/yyyy - HH:mm:ss]", Locale.getDefault());

            return sdf.format(new Date());
        }
    }

    public void sendActionNotification(String sTicker, String sContentTitle, String sContentText, int NUM) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Намерение для запуска второй активности
        Intent intent = new Intent(this, MainActivity.class);
        int time = intent.getIntExtra("time", 1);
        PendingIntent pi = intent.getParcelableExtra("pendingIntent");

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);


        // Строим уведомление
        Notification builder = new Notification.Builder(this)
                .setTicker(sTicker)
                .setContentTitle(sContentTitle)
                .setContentText(
                        sContentText)
                .setSmallIcon(R.drawable.ricfas).setContentIntent(pIntent)
                .addAction(R.drawable.ain_ic_iconlawyer, "Открыть", pIntent)

                .build();

        builder.flags |= Notification.FLAG_AUTO_CANCEL;
        in.set_int_start_activity(NUM);
        N = N+1;
        notificationManager.notify(N, builder);
    }

    public void sendBigPictureStyleNotification(String sTicker, String sContentTitle, String sContentText, int NUM, int idt) {

        //Intent intentS = new Intent(Service_mess.this, MainActivity.class);
        //startActivity(intentS);

        Context context = getApplicationContext();
        Intent notificationIntent = null;
        switch (NUM){
            case 1:
                notificationIntent = new Intent(context, My_questions.class);
                break;
            case 2:
                notificationIntent = new Intent(context, AskAQuestion.class);
                in.set_idt(idt);
                in.set_place(0);
                break;
            case 3:
                notificationIntent = new Intent(context, My_orders.class);
                break;
            case 4:
                in.set_idt(idt);
                notificationIntent = new Intent(context, My_perep_orders_mess.class);
                break;
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ricfas)
                // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ricfas))
                //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker(sTicker)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle(sContentTitle)
                //.setContentText(res.getString(R.string.notifytext))
                .setContentText(sContentText); // Текст уведомления

        // Notificationnotification = builder.getNotification(); // до API 16
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        N = N+1;
        notificationManager.notify(N, notification);
    }



    public void url_starting1(String s){
try {
    Gson gson = new Gson();
    NewPushMessage[] newPushMessage = null;
    newPushMessage = gson.fromJson(s, NewPushMessage[].class);
    if (newPushMessage.length == 0) {

    }
    else
    {
        try {
            ArrayList<NewPushMessage> newPushMessages1 = new ArrayList<NewPushMessage>();
            ArrayList<NewPushMessage> newPushMessages2 = new ArrayList<NewPushMessage>();//null;
            ArrayList<NewPushMessage> newPushMessages3 = new ArrayList<NewPushMessage>();//null;
            ArrayList<NewPushMessage> newPushMessages4 = new ArrayList<NewPushMessage>();//null;
            ArrayList<NewPushMessage> newPushMessages5 = new ArrayList<NewPushMessage>();//null;

            for (int i = 0; i < newPushMessage.length; i++) {
                switch (newPushMessage[i].EnumPushTypes()) {
                    case 1:
                        if(newPushMessage[i].Date.equals(in.get_data_servise())){}
                        else newPushMessages1.add(newPushMessage[i]);

                        break;
                    case 2:
                        if(newPushMessage[i].Date.equals(in.get_data_servise())){}
                        else newPushMessages2.add(newPushMessage[i]);
                        break;
                    case 3:
                        newPushMessages3.add(newPushMessage[i]);
                        break;
                    case 4:
                        newPushMessages4.add(newPushMessage[i]);
                        break;
                    case 5:
                        if(newPushMessage[i].Date.equals(in.get_data_servise())){}
                        else newPushMessages5.add(newPushMessage[i]);
                        break;
                }
            }

            if (newPushMessages1.size() == 1) {
                if (sPref.getBoolean("pref_setting_push_1", true) == false) {
                } else {
                    if(newPushMessage[0].AccountId == in.get_id_user()){
                    sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ",
                            "У вас новое сообщение", 2,
                            newPushMessage[0].ServiceId);}
                }

            } else if (newPushMessages1.size() > 1) {
                sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ",
                        "У вас новые сообщения", 1,
                        1);//sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
            } else if (newPushMessages2.size() == 1) {
                if (sPref.getBoolean("pref_setting_push_3", true) == false) {
                } else {
                    sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ", "Новые сообщения в консультации", 4, newPushMessage[0].ServiceId);//sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
                }
            } else if (newPushMessages2.size() > 1) {
                if (sPref.getBoolean("pref_setting_push_3", true) == false) {
                } else {
                    sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ", "Новые сообщения в консультации", 4, newPushMessage[0].ServiceId);//sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
                }
            } else if (newPushMessages3.size() == 1) {
                //sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ", "изменение статуса заказа докуметов", 0, 1);//sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
            } else if (newPushMessages3.size() > 1) {
                // sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ", "изменение статуса заказа докуметов", 0, 1);//sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
            } else if (newPushMessages4.size() == 1) {
                // sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ", "У вас новое сообщение в заказе документов", 0, 1);//sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
            } else if (newPushMessages4.size() > 1) {
                //  sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ", "У вас новое сообщение в заказе документов", 0, 1);//sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
            }


            if (newPushMessages5.size() == 1) {
                if (sPref.getBoolean("pref_setting_push_2", true) == false) {
                } else {
                    sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение о ",
                            "Пакет документов отправлен на почту", 3, 1);
                }
                //sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
            } else if (newPushMessages5.size() > 1) {
                if (sPref.getBoolean("pref_setting_push_2", true) == false) {
                } else {
                    sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение о ",
                            "Пакет документов отправлен на почту", 3,
                            1);
                }
                //sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
            } else {

            }
        } catch (Exception e) {
        }
    }
        /*try {
            jService = new JSONObject(s);

            if(jService.getString("status").equals("OK")){

                if(jService.getJSONArray("array").length()>0){
                    for (int i =0; i<jService.getJSONArray("array").length(); i++) {

                        if (jService.getJSONArray("array").getJSONObject(i).getInt("count") == 1) {
                            if(sPref.getBoolean("pref_setting_push_1", true)==false){
                                Log.d("qwerty123", "PREF_SETTING NEW 1 NO ");
                            }
                            else{
                                Log.d("qwerty123", "PREF_SETTING NEW 1 YES ");
                                sendBigPictureStyleNotification("Вам задали вопрос!", "Вопрос от " + jService.getJSONArray("array").getJSONObject(i).getString("nik"), jService.getJSONArray("array").getJSONObject(i).getString("text"), 1, 0);
                            }
                            //sendActionNotification(
                        } else {
                            if(sPref.getBoolean("pref_setting_push_2", true)==false){
                                Log.d("qwerty123", "PREF_SETTING NEW 2 NO ");
                            }
                            else {
                                Log.d("qwerty123", "PREF_SETTING NEW 2 YES ");
                                sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от " + jService.getJSONArray("array").getJSONObject(i).getString("nik"),
                                        jService.getJSONArray("array").getJSONObject(i).getString("text"), 2,
                                        jService.getJSONArray("array").getJSONObject(i).getInt("idt"));
                            }
                        }
                    }
                }

            }
            else if(jService.getString("status").equals("NONE")){
            }
            else {
            }

        } catch (JSONException e) {
        }*/
}catch (Exception e){}
    }

    @Override
    public void onStart(Intent intent, int startid) {

    }

    @Override
    public void onDestroy() {

    }

    public void XY_set1() {
        //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        android.location.Location location = locationManager.getLastKnownLocation(bestProvider);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            in.set_latitude(location.getLatitude());
            in.set_longitude(location.getLongitude());
            //Toast.makeText(this, "" + lat.toString() + "-" + lon.toString(), Toast.LENGTH_SHORT).show();

        } catch (NullPointerException e) {
            Toast.makeText(this, "HELL-NO", Toast.LENGTH_SHORT).show();
            Log.e("HELL-NO", "n", e);
            e.printStackTrace();

        }
    }

    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);
            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/Account/GetNewPushes")
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();
                code = response.code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //Gson gson = new Gson();
            if(result!=null && 200<=code && code<300){
               String ss = result;
                if(ss.length()!=0) {
                    url_starting1(result);
                }
            }
            super.onPostExecute(result);
        }
    }
}
        //Gson gson = new Gson();
        //NewPushMessage[] newPushMessage =null;
       // newPushMessage = gson.fromJson(s, NewPushMessage[].class);

      /*  sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ",
                "213", 2,
                1);
        ArrayList<NewPushMessage>newPushMessages1 = null;
        ArrayList<NewPushMessage>newPushMessages2 = null;
        ArrayList<NewPushMessage>newPushMessages3 = null;
        ArrayList<NewPushMessage>newPushMessages4 = null;
        ArrayList<NewPushMessage>newPushMessages5 = null;

        for(int i =0; i<newPushMessage.length; i++){
            switch (newPushMessage[i].EnumPushTypes()){
                case 1:
                    newPushMessages1.add(newPushMessage[i]);
                    break;
                case 2:
                    newPushMessages2.add(newPushMessage[i]);
                    break;
                case 3:
                    newPushMessages3.add(newPushMessage[i]);
                    break;
                case 4:
                    newPushMessages4.add(newPushMessage[i]);
                    break;
                case 5:
                    newPushMessages5.add(newPushMessage[i]);
                    break;
            }
        }

        if(newPushMessages1.size()==1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }else
        if(newPushMessages1.size()>1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }else
        if(newPushMessages2.size()==1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }else
        if(newPushMessages2.size()>1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }else
        if(newPushMessages3.size()==1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }else
        if(newPushMessages3.size()>1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }else
        if(newPushMessages4.size()==1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }else
        if(newPushMessages4.size()>1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }
        if(newPushMessages5.size()==1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }else
        if(newPushMessages5.size()>1){
            sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
        }else{

        }*/
