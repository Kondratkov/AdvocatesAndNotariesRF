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
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import kondratkov.advocatesandnotariesrf.api_classes.ClientQuestion;
import kondratkov.advocatesandnotariesrf.api_classes.NewPushMessage;
import kondratkov.advocatesandnotariesrf.api_classes.Order;
import kondratkov.advocatesandnotariesrf.input.SignUP;
import kondratkov.advocatesandnotariesrf.my_info.My_orders;
import kondratkov.advocatesandnotariesrf.my_info.My_perep_orders_mess;
import kondratkov.advocatesandnotariesrf.my_info.My_questions;

public class Service_mess extends Service {
    private static final String DEBUG_TAG = "qwerty";
    IN in;
    public double lat = 0, lon = 0;
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
                    new UrlConnectionTaskOrders().execute();
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
        N = N + 1;
        notificationManager.notify(N, builder);
    }

    public void sendBigPictureStyleNotification(String sTicker, String sContentTitle, String sContentText, int NUM, int idt) {

        //Intent intentS = new Intent(Service_mess.this, MainActivity.class);
        //startActivity(intentS);

        Context context = getApplicationContext();
        Intent notificationIntent = null;
        switch (NUM) {
            case 1:
                notificationIntent = new Intent(context, My_questions.class);
                break;
            case 2:
                in.set_idt(idt);
                in.set_place(0);
                new UrlConnectionTask1().execute();
                notificationIntent = new Intent(context, AskAQuestion.class);
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
        N = N + 1;
        notificationManager.notify(N, notification);
    }

    class UrlConnectionTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
            //RequestBody formBody = RequestBody.create(JSON, json_signup);
            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type() + " " + in.get_token())
                    .url("http://" + in.get_url() + "/ClientQuestions/GetClientQuestion/" + in.get_idt())
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
            Log.d("qwerty", result);
            Gson gson = new Gson();
            if (result != null && 200 <= code && code < 300) {

                ClientQuestion clientQuestion = gson.fromJson(result, ClientQuestion.class);
                start_client_quest(clientQuestion);

            }
            super.onPostExecute(result);
        }
    }

    public void start_client_quest(ClientQuestion clientQuestion) {
        if (clientQuestion.JuristId == 0) {
            in.set_Tiptip(false);
        } else {
            in.set_Tiptip(true);
        }
    }

    public void url_starting1(String s) {
        try {
            Gson gson = new Gson();
            NewPushMessage[] newPushMessage = null;
            newPushMessage = gson.fromJson(s, NewPushMessage[].class);
            if (newPushMessage.length == 0) {

            } else {
                try {
                    ArrayList<NewPushMessage> newPushMessages1 = new ArrayList<NewPushMessage>();
                    ArrayList<NewPushMessage> newPushMessages2 = new ArrayList<NewPushMessage>();//null;
                    ArrayList<NewPushMessage> newPushMessages3 = new ArrayList<NewPushMessage>();//null;
                    ArrayList<NewPushMessage> newPushMessages4 = new ArrayList<NewPushMessage>();//null;
                    ArrayList<NewPushMessage> newPushMessages5 = new ArrayList<NewPushMessage>();//null;

                    for (int i = 0; i < newPushMessage.length; i++) {
                        switch (newPushMessage[i].EnumPushTypes()) {
                            case 1:
                                if (newPushMessage[i].Date.equals(in.get_data_servise())) {
                                } else newPushMessages1.add(newPushMessage[i]);
                                SharedPreferences.Editor ed = sPref.edit();
                                ed.putInt("pref_new_message", sPref.getInt("pref_new_message", 0) + 1);
                                //ed.putInt("pref_new_consult", 0);
                                ed.commit();
                                break;
                            case 2:
                                if (newPushMessage[i].Date.equals(in.get_data_servise())) {
                                } else newPushMessages2.add(newPushMessage[i]);
                                SharedPreferences.Editor ed2 = sPref.edit();
                                ed2.putInt("pref_new_message", sPref.getInt("pref_new_message", 0) + 1);
                                ed2.commit();
                                break;
                            case 3:
                                newPushMessages3.add(newPushMessage[i]);
                                SharedPreferences.Editor ed3 = sPref.edit();
                                ed3.putInt("pref_new_consul", sPref.getInt("pref_new_consult", 0) + 1);
                                ed3.commit();
                                break;
                            case 4:
                                newPushMessages4.add(newPushMessage[i]);
                                SharedPreferences.Editor ed4 = sPref.edit();
                                ed4.putInt("pref_new_consul", sPref.getInt("pref_new_consult", 0) + 1);
                                ed4.commit();
                                break;
                            case 5:
                                if (newPushMessage[i].Date.equals(in.get_data_servise())) {
                                } else newPushMessages5.add(newPushMessage[i]);
                                SharedPreferences.Editor ed5 = sPref.edit();
                                ed5.putInt("pref_new_consul", sPref.getInt("pref_new_consult", 0) + 1);
                                ed5.commit();
                                break;
                        }
                    }

                    if (newPushMessages1.size() > 0) {
                        if (sPref.getBoolean("pref_setting_push_1", true) == false) {
                        } else {
                            if (newPushMessages1.get(0).OwnerId != in.get_id_user()) {
                                String name = "";
                                try {
                                    name = newPushMessage[0].OwnerName;
                                } catch (Exception e) {
                                }
                                sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ",
                                        "Сообщение от " + name, 2,
                                        newPushMessage[0].ServiceId);
                            }
                        }

//            } else if (newPushMessages1.size() > 1) {
//                sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение от ",
//                        "У вас есть новые сообщения", 1,
//                        1);//sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
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
                                    "Новое сообщение.(пакет документов)", 3, 1);
                        }
                        //sendBigPictureStyleNotification("Вам задали вопрос!", "","", 1, 0);
                    } else if (newPushMessages5.size() > 1) {
                        if (sPref.getBoolean("pref_setting_push_2", true) == false) {
                        } else {
                            sendBigPictureStyleNotification("У вас новое сообщение!", "Сообщение о ",
                                    "Новое сообщение.(пакет документов)", 3,
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
        } catch (Exception e) {
        }
    }

    @Override
    public void onStart(Intent intent, int startid) {

    }

    @Override
    public void onDestroy() {

    }

    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);
            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type() + " " + in.get_token())
                    .url("http://" + in.get_url() + "/Account/GetNewPushes")
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
            if (result != null && 200 <= code && code < 300) {
                String ss = result;
                if (ss.length() != 0) {
                    url_starting1(result);
                }
            }
            super.onPostExecute(result);
        }
    }

    class UrlConnectionTaskOrders extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);

            String s = in.get_token_type() + " " + in.get_token();
            String s1 = "http://" + in.get_url() + "/Orders/GetClientOrders";

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type() + " " + in.get_token())
                    .url("http://" + in.get_url() + "/Orders/GetClientOrders")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();
                String ddd = "";

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            List<Order> arrayPaySearch = new ArrayList<Order>();
            try {
                Gson gson = new Gson();
                in.msArrayOrders = gson.fromJson(result, Order[].class);

                for (int i = 0; i < in.msArrayOrders.length; i++) {
                    if (in.msArrayOrders[i].State == Order.OrderState.WaitingForPayment) {
                        arrayPaySearch.add(in.msArrayOrders[i]);
                    }
                }
                in.msArrayOrders = new Order[arrayPaySearch.size()];
                for (int i = 0; i < in.msArrayOrders.length; i++) {
                    in.msArrayOrders[i] = arrayPaySearch.get(i);
                }

                if (in.msArrayOrders.length == 0) {
                    in.msArrayOrders = null;
                }

//                for (int i = 0; i < msArrayOrders.length; i++) {
//                    if(msArrayOrders[i].State == Order.OrderState.WaitingForPayment){
//                        arrayPaySearch.add(msArrayOrders[i]);
//                    }
//                }
//                msArrayOrders = new Order[arrayPaySearch.size()];
//                for (int i = 0; i <msArrayOrders.length ; i++) {
//                    msArrayOrders[i] = arrayPaySearch.get(i);
//                }
//
//                AddList();
            } catch (Exception e) {
                in.msArrayOrders = null;
            }

            //start_activity();
            super.onPostExecute(result);
        }
    }
}
