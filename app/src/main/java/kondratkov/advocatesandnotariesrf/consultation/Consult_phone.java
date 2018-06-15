package kondratkov.advocatesandnotariesrf.consultation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

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

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;

public class Consult_phone extends Activity {

    IN in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_phone);

        in = new IN();

        findViewById(R.id.phone_cons_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consult_phone.this.finish();
            }
        });

        findViewById(R.id.but_cons_phone_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_call_phone();
                Consult_phone.this.finish();
            }
        });

        findViewById(R.id.but_cons_phone_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Consult_phone.this, Consult_phone_time.class));
            }
        });
    }

    void on_call_phone(){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:88002501107"));
        startActivity(intent);
    }

    class AsyncTaskExample extends AsyncTask<Void, Integer, String> {
        // фоновая работа
        String url = "";
        String json = "";
        @Override
        protected String doInBackground(Void... params) {
            url = "http://"+in.get_url()+"/123.consadd";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("idu", in.get_id_user());
                jsonObject.put("password", in.get_password());
                jsonObject.put("dt", "");
                jsonObject.put("desc", 1);
                jsonObject.put("addinfo", 1);
                jsonObject.put("spec", 1);
                jsonObject.put("connect", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json = String.valueOf(jsonObject);

            return ServerSendData.sendRegData(url, json);
        }

        protected void onPostExecute(String result) {
            if(result!=null) {
                Toast.makeText(Consult_phone.this,
                        "Запрос на консультацию по телефону отправлен!",
                        Toast.LENGTH_LONG).show();
                Consult_phone.this.finish();

            }else{
                Toast.makeText(Consult_phone.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    public static class ServerSendData {

        public static String sendRegData(String Url, String json) {

            String result = null;
            try {
                URL url = new URL(Url);

                URLConnection connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;

                httpConnection.setDoOutput(true);
                httpConnection.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());
                // writeStream(out);
                out.write(json.getBytes());
                out.flush();
                out.close();

                InputStream in = new BufferedInputStream(httpConnection.getInputStream());
                int responseCode = 0;

                responseCode = httpConnection.getResponseCode();
                //Log.d(DEBUG_TAG, "z4  " + String.valueOf(in));

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpConnection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    result = r.readLine();

                } else {
                }
            } catch (MalformedURLException e) {
            } catch (IOException e1) {
            }
            return result;
        }
    }


}
