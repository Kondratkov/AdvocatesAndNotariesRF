package kondratkov.advocatesandnotariesrf.advocates;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kondratkov.advocatesandnotariesrf.AddQuestion;
import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.Address;
import kondratkov.advocatesandnotariesrf.account.City;
import kondratkov.advocatesandnotariesrf.account.Region;
import kondratkov.advocatesandnotariesrf.api_classes.Complaint;
import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;
import kondratkov.advocatesandnotariesrf.input.LogIN;
import kondratkov.advocatesandnotariesrf.maps.Map_coor;

public class Advocate_profile extends Activity {

    public Boolean bool_jur_data = false;
    public LinearLayout lila_prof_jur_data;
    public String FIO="";
    public String JALOBA_FROM ="";
    public String JALOBA = "";
    public String j_1 ="";

    public View lm[] = new View[6];//-ff

    public String [] data_prof_v = null;//getResources().getStringArray(R.array.array_profile);
    public String [] data_profile = new String[5];

    String json_jur_profile_query ="{\"iduser\":\"32\",\"password\":\"12312\",\"id_jur\":\"12312\"}";

    public CharSequence date_year_today = DateFormat.format("yyyy", System.currentTimeMillis());
    public CharSequence dateJSON = null;
    public int int_year_today = 0;
    public int int_year_json = 0;

    public TextView tv_online, tv_fio, tv_rating, tv_phone, tv_email,tv_reply, tv_thanks, tv_adres, tv_stag, tv_kvalif, tv_site, tv_firm, tv_srv, tv_fsar, tv_job, tv_1, tv_2, tv_3;
    public LinearLayout lila_kvalif, lila_info;

    public ImageView image_profile_jur_icon;
    public RatingBar ratingBar;
    //public

    private ImageButton imageButtonJuristProfileEmail;

    public JSONObject jsonObjectJurProf = null;
    public IN in = null;

    private static final String DEBUG_TAG = "qwerty";
    public  String URL_START;

    public FrameLayout asked_frameProg;
    public ProgressBar asked_progressBar;

    public JuristAccounClass juristAccounClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advocate_profile);

        in = new IN();

        asked_frameProg = (FrameLayout)findViewById(R.id.profj_frameProg);
        asked_progressBar = (ProgressBar)findViewById(R.id.profj_progressBar);

        asked_frameProg.setBackgroundResource(R.color.frameOn);
        asked_frameProg.setClickable(true);
        asked_progressBar.setVisibility(ProgressBar.VISIBLE);

        new UrlConnectionTask().execute();
    }

    public void start_activity(){

        int_year_today = Integer.parseInt(String.valueOf(date_year_today));
        lila_prof_jur_data = (LinearLayout)findViewById(R.id.jur_prof_lila_data);

        tv_online = (TextView)findViewById(R.id.jur_prof_tv_online);
        tv_fio    = (TextView)findViewById(R.id.jur_prof_tv_fio);
        tv_rating = (TextView)findViewById(R.id.jur_prof_tv_rating );
        tv_phone  = (TextView)findViewById(R.id.jur_prof_tv_phone );
        tv_email  = (TextView)findViewById(R.id.jur_prof_tv_email );
        //tv_reply  = (TextView)findViewById(R.id.jur_prof_but_reply );
        //tv_thanks = (TextView)findViewById(R.id.jur_prof_but_thanks );
        tv_kvalif = (TextView)findViewById(R.id.jur_prof_tv_specializ);
        tv_adres  = (TextView)findViewById(R.id.jur_prof_tv_adres );
        tv_stag   = (TextView)findViewById(R.id.jur_prof_tv_stag );
        tv_site   = (TextView)findViewById(R.id.jur_prof_tv_site );
        tv_firm   = (TextView)findViewById(R.id.jur_prof_tv_firm );
        tv_srv    = (TextView)findViewById(R.id.jur_prof_tv_srv );
        tv_fsar   = (TextView)findViewById(R.id.jur_prof_tv_fsar );
        tv_job    = (TextView)findViewById(R.id.jur_prof_tv_job );
        tv_1      = (TextView)findViewById(R.id.jur_prof_tv_1);
        tv_2      = (TextView)findViewById(R.id.jur_prof_tv_2);
        tv_3      = (TextView)findViewById(R.id.jur_prof_tv_3);

        ratingBar = (RatingBar)findViewById(R.id.jur_prof_ratingbar);

        imageButtonJuristProfileEmail = (ImageButton)findViewById(R.id.imageButtonJuristProfileEmail);

        image_profile_jur_icon= (ImageView)findViewById(R.id.jur_prof_image_icon );

        data_prof_v = getResources().getStringArray(R.array.array_profile_jur);

            if(juristAccounClass.IsOnline==true){
                tv_online.setText("online");
                tv_online.setTextColor(getResources().getColor(R.color.black_g));
            }
            else{
                tv_online.setText("offline");
                tv_online.setTextColor(getResources().getColor(R.color.black_m));
            }
            tv_fio.setText(juristAccounClass.Surename + " " + juristAccounClass.Name + " " + juristAccounClass.Patronymic);
            FIO = juristAccounClass.Fio;
            in.set_fio_jur(FIO);

        try {
            tv_rating.setText(String.valueOf(juristAccounClass.Rating));

            tv_phone.setText(juristAccounClass.Phone);//jsonObjectJurProf.getString("phone"));
            tv_email.setText(juristAccounClass.Email);//jsonObjectJurProf.getString("email"));

            try{
                tv_adres.setText(juristAccounClass.Address.City + " " + juristAccounClass.Address.Street + " " + juristAccounClass.Address.StreetNumber + " " + juristAccounClass.Address.BuildingNumber);//jsonObjectJurProf.getString("adres"));
            }catch (Exception e){}
             //tv_firm.setText();//jsonObjectJurProf.getString("name_firm"));
            //tv_site.setText(juristAccounClass.);//jsonObjectJurProf.getString("site"));

        }catch (Exception e){}

        String sp="";
        try{
            for(int i=0; i<juristAccounClass.Specializations.length; i++){
                sp = sp+juristAccounClass.Specializations[i].SectorName+" ";
            }
        }catch (Exception e){}
        tv_kvalif.setText(sp);

        try{
            tv_firm.setText(juristAccounClass.JuristOrganisation.OrganisationName);
        }catch (Exception e){}
        try{
            tv_1.setText(juristAccounClass.JudicialArea.Name);
        }catch (Exception e){}
        try{
            tv_2.setText(juristAccounClass.NumberInApReestr);
        }catch (Exception e){}
        try{
            tv_3.setText("Ученая степень: "+ juristAccounClass.PhdInfo +"\nВладение иностранными языками: "+ juristAccounClass.LanguagesInfo +"\nСтатус адвоката присвоен: "+ juristAccounClass.JuristStatusAssignedBy);
        }catch (Exception e){}

            ratingBar.setRating((float) juristAccounClass.Rating);//jsonObjectJurProf.getDouble("rating"));
           /*
            ratingBar.setRating((float) jsonObjectJurProf.getDouble("rating"));
            tv_phone.setText(jsonObjectJurProf.getString("phone"));
            tv_email.setText(jsonObjectJurProf.getString("email"));
            tv_reply.setText(jsonObjectJurProf.getString("qt_reply"));
            tv_thanks.setText(jsonObjectJurProf.getString("qt_thanks"));
            tv_adres.setText(jsonObjectJurProf.getString("adres"));
            tv_firm.setText(jsonObjectJurProf.getString("name_firm"));
            tv_site.setText(jsonObjectJurProf.getString("site"));
*/
            //dateJSON = DateFormat.format("yyyy", Long.decode(jsonObjectJurProf.getString("stag")));
            //int_year_json = Integer.parseInt(String.valueOf(dateJSON));
            //int_year_json = int_year_today - int_year_json;
            //int t1 = int_year_json % 10;
            //int t2 = int_year_json % 100;
            //String stta = "";
            //if(t1 == 1 && t2 != 11) {stta =String.valueOf(int_year_json)+" год";}
            //else if(t1>1 && t1<5 && t2!=12 && t2!=13 && t2!=14) {stta =String.valueOf(int_year_json)+" года";}
            //else{ stta =String.valueOf(int_year_json)+" лет";}
            //tv_stag.setText(stta);

        //tv_stag.setText(juristAccounClass.ExperienceLevel.getDate());//jsonObjectJurProf.getString("stag"));

            //if(Boolean.parseBoolean(jsonObjectJurProf.getString("timout"))==true){tv_srv.setText("да");}//jsonObjectJurProf.getString("да")
            //else{tv_srv.setText("нет");}//(jsonObjectJurProf.getString("нет"));}
            try {
                tv_site.setText(juristAccounClass.Site);
            }catch (Exception e){}

            try {
                tv_firm.setText(juristAccounClass.JuristOrganisation.OrganisationName);
            }catch (Exception e){}


            try{
                tv_stag.setText(in.dateDisplayStag(juristAccounClass.ExperienceLevel));
            }catch (Exception e){}

            if(juristAccounClass.CanFastComing){tv_srv.setText("Да");}
            else{tv_srv.setText("Нет");}

            if(juristAccounClass.IsFsarMember==true){tv_fsar.setText("да");}//(jsonObjectJurProf.getString("да"));}
            else{tv_fsar.setText("нет");}//(jsonObjectJurProf.getString("нет"));}

            if(juristAccounClass.WorkInOffDays==true){tv_job.setText("ежедневно");}//(jsonObjectJurProf.getString("ежедневно"));}
            else{tv_job.setText("ПН -ПТ");}//(jsonObjectJurProf.getString("ПН -ПТ"));}

            if(juristAccounClass.Longitude >0 && juristAccounClass.Latitude!=0){
                in.set_latitude(juristAccounClass.Latitude);
                in.set_longitude(juristAccounClass.Longitude);
            }

            in.set_jut_ili_not(true);

        listZ();
        stopProgressBar();
    }

    public void onClickProfileJur(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.jur_prof_but_data:
                if(bool_jur_data == false){
                    for(int i=0; i<5; i++) {
                        LinearLayout l = new LinearLayout(this);
                        TextView t1 = new TextView(this);
                        TextView t2 = new TextView(this);

                        l.setPadding(0, 25, 0, 0);

                        t1.setText(data_prof_v[i]);
                        t1.setTextSize(16);
                        t1.setWidth(250);
                        t1.setPadding(0, 7, 0, 0);

                        t2.setText(data_profile[i]);
                        t2.setTextSize(18);
                        //t2.setBackgroundResource(R.color.on_line_false);
                        t2.setWidth(lila_prof_jur_data.getWidth() - 250);//match_parent
                        //Log.d("qwerty", "H " + String.valueOf(lila_prof_jur_data.getWidth()));
                        //Log.d("qwerty", "H2 "+String.valueOf(l.getLayoutParams().width));
                        t2.setGravity(Gravity.CENTER);
                        t2.setTextColor(getResources().getColor(R.color.black_m));

                        l.addView(t1);
                        l.addView(t2);
                        //l.setBackgroundResource(R.color);

                        lm[i] = l;
                        lila_prof_jur_data.addView(l);

                        bool_jur_data = true;
                    }
                }
                else{
                    for (int i = 0; i < 5; i++) {

                        lila_prof_jur_data.removeView(lm[i]);
                        bool_jur_data = false;
                    }
                }
                break;

            case R.id.but_jaloba_prof:
                dialog_jaloba();
                break;

            case R.id.imageButtonJuristProfileEmail:
                try{
                    Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                    intentEmail.setData(Uri.parse("mailto:"+tv_email.getText()));
                    intentEmail.putExtra(Intent.EXTRA_SUBJECT, in.get_nik_user());
                    if (intentEmail.resolveActivity(getPackageManager()) != null) {
                        startActivity(intentEmail);
                    }
                }catch (Exception e){}
                break;

            case R.id.imageButtonJuristProfilPhone:
                try{
                    Intent intent12 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tv_phone.getText()));
                    startActivity(intent12);
                }catch (Exception e){}
                break;

            case R.id.imageButtonJuristProfileSite:
                try{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(tv_site.getText())));
                    startActivity(browserIntent);
                }catch (Exception e){
                    try{
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+String.valueOf(tv_site.getText())));
                        startActivity(browserIntent);
                    }catch (Exception e1){
                        try{
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www."+String.valueOf(tv_site.getText())));
                            startActivity(browserIntent);
                        }catch (Exception e2){}
                    }
                }
                break;
            case R.id.jur_prof_but_reply:
                //intent = new Intent(Profile_Jur.this, list_qt_jur.class);
                //intent.putExtra("qw", "Ответы на вопросы в чате");
                //intent.putExtra("mess", 1);
                //int count = getIntent().getIntExtra("name", 0);
                startActivity(intent);
                break;

            case R.id.jur_prof_but_thanks:
                //intent = new Intent(Profile_Jur.this, list_qt_jur.class);
                //intent.putExtra("qw", "Благодарности");
                //intent.putExtra("mess", 2);
                //int count = getIntent().getIntExtra("name", 0);
                startActivity(intent);
                break;
            case R.id.jur_prof_but_maps:
                in.set_fio_jur(String.valueOf(tv_fio.getText()));
                in.set_jut_ili_not(true);
                intent = new Intent(Advocate_profile.this, Map_coor.class);
                startActivity(intent);
                break;

            case R.id.jur_prof_but_add:
                if(in.getOnemay()) {
                    in.set_add_quest_tip(2);
                    try {
                        in.set_fio_jur(String.valueOf(tv_fio.getText()));
                    }catch (Exception e){}
                        intent = new Intent(Advocate_profile.this, AddQuestion.class);
                    startActivity(intent);
                    Advocate_profile.this.finish();
                }else {
                    openDialog_no_reg();
                }
                break;
            case R.id.jur_prof_but_close:
                Advocate_profile.this.finish();
                break;
        }
    }

    public void dialog_jaloba(){
        final Dialog dialog = new Dialog(Advocate_profile.this);
        dialog.setTitle("");
        dialog.setContentView(R.layout.advocate_profile_dialog_jaloba);

        Button btnClose = (Button) dialog.getWindow().findViewById(
                R.id.jaloba_but_cancel);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Button btnYes = (Button) dialog.getWindow().findViewById(
                R.id.jaloba_but_yes);

        final Button btnYes2 = (Button) dialog.getWindow().findViewById(
                R.id.jaloba_but_yes2);

        final EditText et_adv = (EditText)dialog.getWindow().findViewById(R.id.jaloba_et_ad);
        et_adv.setText(FIO);
        j_1 =FIO;

        final CheckBox ch1 = (CheckBox)dialog.getWindow().findViewById(R.id.jaloba_but_checkBox1);
        final CheckBox ch2 = (CheckBox)dialog.getWindow().findViewById(R.id.jaloba_but_checkBox2);
        final CheckBox ch3 = (CheckBox)dialog.getWindow().findViewById(R.id.jaloba_but_checkBox3);
        final CheckBox ch4 = (CheckBox)dialog.getWindow().findViewById(R.id.jaloba_but_checkBox4);

        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch1.isChecked()){
                    ch2.setChecked(false);
                    ch3.setChecked(false);
                    ch4.setChecked(false);
                }
            }
        });
        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch2.isChecked()){
                    ch1.setChecked(false);
                    ch3.setChecked(false);
                    ch4.setChecked(false);
                }
            }
        });
        ch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch3.isChecked()){
                    ch1.setChecked(false);
                    ch2.setChecked(false);
                    ch4.setChecked(false);
                }
            }
        });
        ch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch4.isChecked()){
                    ch1.setChecked(false);
                    ch3.setChecked(false);
                    ch2.setChecked(false);
                }
            }
        });

        final EditText jaloba_et_text = (EditText)dialog.getWindow().findViewById(R.id.jaloba_et_text);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch1.isChecked()){
                    JALOBA_FROM = "Неправильный ответ";
                }else if(ch2.isChecked()){
                    JALOBA_FROM="Хамство";
                }else if(ch3.isChecked()){
                    JALOBA_FROM="Игнорирование вопроса";
                }else if(ch4.isChecked()){
                    JALOBA_FROM="Другое";
                }else{
                    JALOBA_FROM="Другое";
                }

                Calendar dateAndTime=Calendar.getInstance();
                String dateInString = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm:ss");

                dateInString = sdf.format(dateAndTime.getTime());
                Complaint complaint = new Complaint();

                complaint.AccountType = Complaint.AccountTypes.Jurist;
                complaint.JuristId = in.get_id_jur();
                complaint.AccountId = in.get_id_user();
                complaint.From = JALOBA_FROM;
                complaint.Message = JALOBA_FROM+": \n"+String.valueOf(jaloba_et_text.getText());
                complaint.Date = dateInString;

                Gson gsonJ = new Gson();
                JALOBA = gsonJ.toJson(complaint);

                new UrlConnectionTaskJaloba().execute(JALOBA);

                dialog.dismiss();
            }
        });
        btnYes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch1.isChecked()){
                    JALOBA_FROM = "Неправильный ответ";
                }else if(ch2.isChecked()){
                    JALOBA_FROM="Хамство";
                }else if(ch3.isChecked()){
                    JALOBA_FROM="Игнорирование вопроса";
                }else if(ch4.isChecked()){
                    JALOBA_FROM="Другое";
                }else{
                    JALOBA_FROM="Другое";
                }

                Calendar dateAndTime=Calendar.getInstance();
                String dateInString = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm:ss");

                dateInString = sdf.format(dateAndTime.getTime());
                Complaint complaint = new Complaint();

                complaint.AccountType = Complaint.AccountTypes.Jurist;
                complaint.JuristId = in.get_id_jur();
                complaint.AccountId = in.get_id_user();
                complaint.From = JALOBA_FROM;
                complaint.Message = JALOBA_FROM+": \n"+String.valueOf(jaloba_et_text.getText());
                complaint.Date = dateInString;

                try{
                    Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                    intentEmail.setData(Uri.parse("mailto:"+"2211107@mmka.info"));
                    intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Жалоба на адвоката "+ j_1);
                    intentEmail.putExtra(Intent.EXTRA_TEXT, JALOBA_FROM+": \n"+String.valueOf(jaloba_et_text.getText()));
                    if (intentEmail.resolveActivity(getPackageManager()) != null) {
                        startActivity(intentEmail);
                    }
                }catch (Exception e){}

                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void stopProgressBar(){
        asked_frameProg.setBackgroundResource(R.color.frameOff);
        asked_frameProg.setClickable(false);
        asked_progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public void listZ () {
        for (int i =0; i<5; i++){
            switch(i){
                case 0:
                    data_profile[0] = juristAccounClass.NumberInApReestr;
                    break;
                case 1:
                        /*dateJSON = DateFormat.format("yyyy", Long.decode(jsonObjectProf.getString("stag")));
                        int_year_json = Integer.parseInt(String.valueOf(dateJSON));
                        int_year_json = int_year_today - int_year_json;

                        int t1 = int_year_json % 10;
                        int t2 = int_year_json % 100;
                        if(t1 == 1 && t2 != 11) {data_profile[1] =String.valueOf(int_year_json)+" год";}
                        else if(t1>1 && t1<5 && t2!=12 && t2!=13 && t2!=14) {data_profile[1] =String.valueOf(int_year_json)+" года";}*/
                        //data_profile[1] = juristAccounClass.ExperienceLevel.getYear();//String.valueOf(int_year_json)+" лет";}

                        //data_profile[1] = String.valueOf(int_year_json);

                    break;
                case 2:

                    break;
                case 3:
                    if(juristAccounClass.IsFsarMember==true){
                        data_profile[3] = "да";
                    }
                    else{
                        data_profile[3] = "нет";
                    }

                    break;
                case 4:
                    if(juristAccounClass.WorkInOffDays==true){
                        data_profile[4] = "да";
                    }
                    else{
                        data_profile[4] = "нет";
                    }
                    break;
            }
        }
    }

    private void openDialog_no_reg() {
        final Dialog dialog_no = new Dialog(Advocate_profile.this);
        dialog_no.setTitle("");
        dialog_no.setContentView(R.layout.advocate_dialog_no_reg);

        Button dialog_no_reg_input = (Button) dialog_no.getWindow().findViewById(
                R.id.dialog_no_reg_input);

        Button dialog_no_reg_cancel = (Button) dialog_no.getWindow().findViewById(
                R.id.dialog_no_reg_cancel);

        dialog_no_reg_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent134 = new Intent(Advocate_profile.this, LogIN.class);//DocCreation.class);
                startActivity(intent134);
                dialog_no.dismiss();
            }
        });
        dialog_no_reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_no.dismiss();
            }
        });

        dialog_no.show();
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
                    .url("http://"+in.get_url()+"/JuristAccounts/GetJuristAccount/"+in.get_id_jur())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            juristAccounClass = gson.fromJson(result, JuristAccounClass.class);
            start_activity();
            super.onPostExecute(result);
        }
    }
    int code;
    class UrlConnectionTaskJaloba extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/Complaints/CreateComplaint")
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
            if(code>=200&&code<300&&result!=null){
                Toast.makeText(Advocate_profile.this,
                        "Ваша жалоба будет расмотренна нашими модератарами в ближайшее время!",
                        Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }
}

