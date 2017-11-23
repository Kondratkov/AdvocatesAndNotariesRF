package kondratkov.advocatesandnotariesrf.consultation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Date;
import java.util.Locale;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.JuristSpecializationSector;
import kondratkov.advocatesandnotariesrf.api_classes.PostConsultation;

public class Consult_mess extends Activity {

    IN in;
    public int code;

    TextView tv_text_mess, tv_mess_comment;
    public Spinner spinner_phone;
    private long docTip =1 ;
    int tip_mes = 0;
    String tip_s_mess= "-";

    String JSON_POST ="";
    JuristSpecializationSector juristSpecializationSector;

    public String []strTip = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_mess);

        in = new IN();

        findViewById(R.id.but_zak_cons_mess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_text();
                openDialog();
            }
        });

        tv_text_mess = (TextView)findViewById(R.id.et_cons_mess_1);
        tv_mess_comment  = (TextView)findViewById(R.id.et_cons_mess_2) ;

        spinner_phone = (Spinner)findViewById(R.id.spinner_cons_mess);
        strTip = getResources().getStringArray(R.array.ArrayTipAddQuest);

        findViewById(R.id.cons_mess_1_but_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consult_mess.this.finish();
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
    }

    void save_text(){
        in.set_text(String.valueOf(tv_text_mess.getText()));
        in.set_date(String.valueOf(tv_mess_comment.getText()));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void openDialog() {
        final Dialog dialog = new Dialog(Consult_mess.this);
        dialog.setTitle(R.string.title_text_doc);
        dialog.setContentView(R.layout.consult_mess_dialog);

        TextView tv_them    = (TextView) dialog.findViewById(R.id.cons_mess_them);
        TextView tv_body   = (TextView) dialog.findViewById(R.id.cons_mess_quest);
        TextView tv_comment   = (TextView) dialog.findViewById(R.id.cons_mess_comment);

        tv_them.setText(tip_s_mess);
        tv_body.setText(tv_text_mess.getText());
        tv_comment.setText(tv_mess_comment.getText());

        Button btnAdd = (Button) dialog.getWindow().findViewById(
                R.id.cons_mess_but_okB);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson_post = new Gson();

                PostConsultation postConsultation = new PostConsultation();
                postConsultation.SpecializationSector = juristSpecializationSector;
                postConsultation.ContactMethod = PostConsultation.preferredContactMethod.Email;
                postConsultation.ConsultationQuestion = String.valueOf(tv_text_mess.getText());
                postConsultation.ClientNote = String.valueOf(tv_mess_comment.getText());
                //postConsultation.ClientPreferredConsultationDate = dateAndTime.getTime().toString();

                JSON_POST =  gson_post.toJson(postConsultation);
                new UrlConnectionTask().execute(JSON_POST);
                //new AsyncTaskExample().execute();
                dialog.dismiss();
            }
        });

        Button btnDismiss = (Button) dialog.getWindow().findViewById(
                R.id.cons_mess_btn_cancelB);

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
            String s= in.get_token_type()+" "+in.get_token();
            String s2 = params[0];
            String s3= "http://"+in.get_url()+"/Consultations/PostConsultation";
            String s4="213";

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
            if (code>=200 && code<300 && result!=null) {
                Toast.makeText(Consult_mess.this,
                        "Запрос на консультацию отправлен!",
                        Toast.LENGTH_LONG).show();
                Consult_mess.this.finish();

            }else{
                Toast.makeText(Consult_mess.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }
}
