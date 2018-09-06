package kondratkov.advocatesandnotariesrf.notary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import kondratkov.advocatesandnotariesrf.maps.Map_coor;

public class Notary_profile extends Activity {

    TextView not_tv_prof_1,   not_tv_prof_2,   not_tv_prof_3,
             not_tv_prof_4,   not_tv_prof_5,   not_tv_prof_6, not_tv_prof_7,
             not_tv_prof_8,   not_tv_prof_9,   not_tv_prof_10,
             not_tv_prof_11,  not_tv_prof_12,  not_tv_prof_13, not_tv_prof_14,
            not_tv_prof_fio, not_tv_prof_city;
    TextView [] tvP;
    IN in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notary_profile);

        in = new IN();

        if(in.getOnemay())
        {}
        else
        {
            Notary_profile.this.finish();
        }

        not_tv_prof_1  = (TextView)findViewById(R.id.not_tv_prof_1);
        not_tv_prof_2  = (TextView)findViewById(R.id.not_tv_prof_2);
        not_tv_prof_3  = (TextView)findViewById(R.id.not_tv_prof_3);
        not_tv_prof_4  = (TextView)findViewById(R.id.not_tv_prof_4);
        not_tv_prof_5  = (TextView)findViewById(R.id.not_tv_prof_5);
        not_tv_prof_6  = (TextView)findViewById(R.id.not_tv_prof_6);
        not_tv_prof_7  = (TextView)findViewById(R.id.not_tv_prof_7);
        not_tv_prof_8  = (TextView)findViewById(R.id.not_tv_prof_8);
        not_tv_prof_9  = (TextView)findViewById(R.id.not_tv_prof_9);
        not_tv_prof_10 = (TextView)findViewById(R.id.not_tv_prof_10);
        not_tv_prof_11 = (TextView)findViewById(R.id.not_tv_prof_11);
        not_tv_prof_12 = (TextView)findViewById(R.id.not_tv_prof_12);
        not_tv_prof_13 = (TextView)findViewById(R.id.not_tv_prof_13);
        not_tv_prof_14 = (TextView)findViewById(R.id.not_tv_prof_14);
        not_tv_prof_fio = (TextView)findViewById(R.id.not_tv_prof_fio);
        not_tv_prof_city = (TextView)findViewById(R.id.not_tv_prof_city);

        tvP = new TextView[]{not_tv_prof_fio, not_tv_prof_city, not_tv_prof_1,
                not_tv_prof_2,  not_tv_prof_3, not_tv_prof_4,    not_tv_prof_5, not_tv_prof_6,
                not_tv_prof_7,  not_tv_prof_8, not_tv_prof_9,    not_tv_prof_10,not_tv_prof_11,
                not_tv_prof_12, not_tv_prof_13, not_tv_prof_14};

        for(int i =0; i<tvP.length; i++){
            if(i==7){
                if(Boolean.parseBoolean(in.get_not_prof()[i])==true){
                    tvP[i].setText("Пн-Сб");
                }else {
                    tvP[i].setText("Пн-Пт");
                }
            }else if(i<16&&i>5&&i!=7){
                if(Boolean.parseBoolean(in.get_not_prof()[i])==true){
                    tvP[i].setText("да");
                }else{
                    tvP[i].setText("нет");
                }
            }
            else{
                tvP[i].setText(in.get_not_prof()[i]);
            }
        }

        not_tv_prof_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(not_tv_prof_4.getText())));
                startActivity(browserIntent);
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.jur_prof_but_close:
                Notary_profile.this.finish();
                break;
            case R.id.not_prof_but_maps:
                in.set_text(in.get_not_prof()[4]);
                //in.set_latitude(Double.parseDouble(in.get_not_prof()[17]));
                //in.set_longitude();
                in.set_id_jur(Integer.parseInt(in.get_not_prof()[16]));
                in.set_fio_jur(in.get_not_prof()[0]);
                in.set_jut_ili_not(false);

                double d_latitude = 0;
                double d_longitude = 0;

                try{
                    if(Double.parseDouble(in.get_not_prof()[17]) == 0 || Double.parseDouble(in.get_not_prof()[18]) == 0){
                    }else{
                        d_latitude = Double.parseDouble(in.get_not_prof()[17]);
                        d_longitude = Double.parseDouble(in.get_not_prof()[18]);
                    }
                }catch (Exception e){}

                Intent intent = new Intent(Notary_profile.this, Map_coor.class);
                intent.putExtra("THIS_LATITUDE", d_latitude);
                intent.putExtra("THIS_LONGITUDE", d_longitude);
                startActivity(intent);
                break;
            case R.id.imageButtonNotaryProfilPhone:
                if(not_tv_prof_1.getText().length()!=0){
                    try{
                        Intent intent12 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+not_tv_prof_1.getText()));
                        startActivity(intent12);
                    }catch (Exception e){}
                }
                break;
            case R.id.imageButtonNotaryProfileEmail:
                if(not_tv_prof_2.getText().length()!=0){
                    try{
                        Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                        intentEmail.setData(Uri.parse("mailto:"+not_tv_prof_2.getText()));
                        intentEmail.putExtra(Intent.EXTRA_SUBJECT, in.get_nik_user());
                        if (intentEmail.resolveActivity(getPackageManager()) != null) {
                            startActivity(intentEmail);
                        }
                    }catch (Exception e){}
                }
                break;
        }
    }

}
