package kondratkov.advocatesandnotariesrf.consultation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import kondratkov.advocatesandnotariesrf.account.JuristSpecializationSector;
import kondratkov.advocatesandnotariesrf.api_classes.ClientQuestion;
import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;
import kondratkov.advocatesandnotariesrf.api_classes.PostConsultation;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Consult_phone_time extends Activity {

    IN in;
    String datetime;
    Date date_post;

    FrameLayout frame_cons_time_phone;

    TextView tv_text_phone, textView_time, textView_date;
    public Spinner spinner_phone;
    private long docTip =1 ;
    int tip_mes = 0;
    String tip_s_mess= "-";
    String JSON_POST = "";
    int code;

    public String []strTip = null;

    Calendar dateAndTime=Calendar.getInstance();
    public JuristSpecializationSector juristSpecializationSector;
    public ClientQuestion[] msArrayclientQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_phone_time);

        in = new IN();

        frame_cons_time_phone = (FrameLayout) findViewById(R.id.frame_cons_time_phone);

        tv_text_phone = (TextView)findViewById(R.id.editText_phone);


        findViewById(R.id.but_zak_cons_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in.set_text(String.valueOf(tv_text_phone.getText()));
                openDialog();
            }
        });

        findViewById(R.id.phone_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // in.set_text(String.valueOf(tv_text_phone.getText()));
                setTime(frame_cons_time_phone);
            }
        });

        findViewById(R.id.phone_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // in.set_text(String.valueOf(tv_text_phone.getText()));
                setDate(frame_cons_time_phone);
            }
        });

        spinner_phone = (Spinner)findViewById(R.id.spinner_phone);
        strTip = getResources().getStringArray(R.array.ArrayTipAddQuest);

        findViewById(R.id.my_phone_but_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Consult_phone_time.this.finish();
            }
        });

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.ArrayTipAddQuest, R.layout.my_quest_item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_phone.setAdapter(adapter);
        docTip = spinner_phone.getSelectedItemId();
        spinner_phone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                juristSpecializationSector = new JuristSpecializationSector();
                juristSpecializationSector.Id = (int) id;
                juristSpecializationSector.SectorName = String.valueOf(getResources().getStringArray(R.array.ArrayTipAddQuest)[(int) id]);

                in.set_doc_tip(strTip[pos]);
                tip_mes = pos;
                tip_s_mess = strTip[pos];

            }
        });


        textView_time = (TextView)findViewById(R.id.textView_time);
        textView_date = (TextView)findViewById(R.id.textView_date);
        setInitialDateTime();


    }

    // установка начальных даты и времени
    private void setInitialDateTime() {
        textView_time.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
        textView_date.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
        /*textView_time.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
        textView_date.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));*/
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(Consult_phone_time.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };


    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(Consult_phone_time.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };


    @TargetApi(Build.VERSION_CODES.M)
    private void openDialog() {
        final Dialog dialog = new Dialog(Consult_phone_time.this);
        dialog.setTitle(R.string.title_text_doc);
        dialog.setContentView(R.layout.consult_phone_time_dialog);

        TextView tv_time    = (TextView) dialog.findViewById(R.id.cons_phone_tv_time);
        TextView tv_tip   = (TextView) dialog.findViewById(R.id.cons_phone_tv_tip);
        TextView tv_text   = (TextView) dialog.findViewById(R.id.cons_phone_tv_text);


        String s ="12:11 21.12.2012";
        s = textView_time.getText()+" "+textView_date.getText();
        int hour = 0;
        datetime = textView_time.getText().toString();
        /*if(textView_time.getText().length()>4){
            String sss = textView_time.getText().toString();
            if(Integer.parseInt(String.valueOf(sss.charAt(1)))>2){
                datetime = s+" PM";
            }else {
                datetime = s+" AM";
            }
        }else{
            datetime = s+" AM";
        }
        s = String.valueOf(timePicker.getHour())+":";
        if(timePicker.getMinute()<10) {
            s += "0"+ String.valueOf(timePicker.getMinute())+ " ";
        }else {
            s += String.valueOf(timePicker.getMinute())+ " ";
        }
        s +=String.valueOf(datePicker.getDayOfMonth())+"."+String.valueOf(datePicker.getMonth()+1)+"."+String.valueOf(datePicker.getYear());

        datetime = String.valueOf(datePicker.getDayOfMonth())+"/"+String.valueOf(datePicker.getMonth()+1)+"/"+String.valueOf(datePicker.getYear())+" ";
        if(timePicker.getHour()>12){
            datetime += String.valueOf(24-timePicker.getHour())+":"+String.valueOf(timePicker.getMinute())+ " PM";
        }else{
            datetime += String.valueOf(timePicker.getHour())+":"+String.valueOf(timePicker.getMinute())+" AM";
        }
*/
        tv_time.setText(datetime);
        tv_text.setText(tv_text_phone.getText());
        tv_tip.setText(tip_s_mess);

        Button btnAdd = (Button) dialog.getWindow().findViewById(
                R.id.cons_phone_but_okB);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson_post = new Gson();

                PostConsultation postConsultation = new PostConsultation();
                postConsultation.SpecializationSector = juristSpecializationSector;
                postConsultation.ContactMethod = PostConsultation.preferredContactMethod.Phone;
                postConsultation.ConsultationQuestion = in.get_text();

                //SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
                String dateInString = "Friday, Jun 7, 2013 12:10:56 PM";

                //Date date = dateAndTime.getTime();
                //System.out.println(date);
                //dateInString = formatter.format(date.toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm:ss");

                dateInString = sdf.format(dateAndTime.getTime());

                postConsultation.ClientPreferredConsultationDate = dateInString;

                JSON_POST =  gson_post.toJson(postConsultation);

                new UrlConnectionTask().execute(JSON_POST);
                dialog.dismiss();
            }
        });

        Button btnDismiss = (Button) dialog.getWindow().findViewById(
                R.id.cons_phone_btn_cancelB);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/Consultations/PostConsultation")
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
            String s = "dd";
            if (code>=200 && code<300 &&result!=null) {
                Toast.makeText(Consult_phone_time.this,
                        "Запрос на консультацию по телефону отправлен!",
                        Toast.LENGTH_LONG).show();
                Consult_phone_time.this.finish();

            }else{
                Toast.makeText(Consult_phone_time.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

    class AsyncTaskExample1 extends AsyncTask<Void, Integer, String> {
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
                jsonObject.put("dt", datetime);
                jsonObject.put("desc", in.get_text());
                jsonObject.put("spec", 1);
                jsonObject.put("connect", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json = String.valueOf(jsonObject);
            Log.d("qwertty", "jghghX "+ json);
            return ServerSendData.sendRegData(url, json);
        }

        protected void onPostExecute(String result) {
            Log.d("qwertty", "XXX "+ result);
            if(result!=null) {
                Toast.makeText(Consult_phone_time.this,
                        "Запрос на консультацию по телефону отправлен!",
                        Toast.LENGTH_LONG).show();
                Consult_phone_time.this.finish();

            }else{
                Toast.makeText(Consult_phone_time.this,
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
