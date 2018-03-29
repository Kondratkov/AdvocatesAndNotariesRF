package kondratkov.advocatesandnotariesrf.notary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.New_sidebar;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.City;
import kondratkov.advocatesandnotariesrf.account.Region;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindNotaryByFilter;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.GetNotaryByAddressCoordinates;
import kondratkov.advocatesandnotariesrf.api_classes.Notary;
import kondratkov.advocatesandnotariesrf.dop_dialog.Dialog_region;
import kondratkov.advocatesandnotariesrf.maps.LocationListenerGPServices;
import kondratkov.advocatesandnotariesrf.start_help.Start_activity_no_login;

public class Notary_filter extends Activity implements Dialog_region.i_dialog_region {

    LinearLayout lila;
    TextView tvf1, tvf2, tvf3, tvf4, tvf5, tvf6, tvf7, tvf8;
    public CheckBox ch1, ch2, ch3, ch4, ch5;
    Button bt_filter, buttonNotaryFilterYesNo;
    public FrameLayout frameLayout_not_filter_Visio;
    public TextView[] tv_mas = null;
    public Button[] mButtonsFilter = null;
    public boolean but_yes = false;
    public boolean dop_fil = false;
    IN in;
    public boolean[] bool_sort;
    public int code;

    private LocationManager locationManager;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notary_filter);

        in = new IN();

        bool_sort = new boolean[]{false, false, false, false, false,
                false, false, false, false, false,
                false, false, false};


        lila = (LinearLayout) findViewById(R.id.lila_notary_fil);

        tvf1 = (TextView) findViewById(R.id.notary_filter_tv_1);
        tvf2 = (TextView) findViewById(R.id.notary_filter_tv_2);
        tvf3 = (TextView) findViewById(R.id.notary_filter_tv_3);
        tvf4 = (TextView) findViewById(R.id.notary_filter_tv_4);
        tvf5 = (TextView) findViewById(R.id.notary_filter_tv_5);
        tvf6 = (TextView) findViewById(R.id.notary_filter_tv_6);
        tvf7 = (TextView) findViewById(R.id.notary_filter_tv_7);
        tvf8 = (TextView) findViewById(R.id.notary_filter_tv_8);

        ch1 = (CheckBox) findViewById(R.id.cb_fil_1);
        ch2 = (CheckBox) findViewById(R.id.cb_fil_2);
        ch3 = (CheckBox) findViewById(R.id.cb_fil_3);
        ch4 = (CheckBox) findViewById(R.id.cb_fil_4);
        ch5 = (CheckBox) findViewById(R.id.cb_fil_5);

        Button b3 = (Button)findViewById(R.id.notary_filter_but_3);
        Button b4 = (Button)findViewById(R.id.notary_filter_but_4);
        Button b5 = (Button)findViewById(R.id.notary_filter_but_5);
        Button b6 = (Button)findViewById(R.id.notary_filter_but_6);
        Button b7 = (Button)findViewById(R.id.notary_filter_but_7);
        Button b8 = (Button)findViewById(R.id.notary_filter_but_8);

        mButtonsFilter = new Button[]{b3, b4, b5, b6, b7, b8};

        frameLayout_not_filter_Visio = (FrameLayout)findViewById(R.id.frameLayout_not_filter_Visio);

        buttonNotaryFilterYesNo = (Button)findViewById(R.id.buttonNotaryFilterYesNo);

        //tv_but_f = (TextView) findViewById(R.id.notary_filter_tv_dbt_filter);

        tv_mas = new TextView[]{tvf1, tvf2, tvf3, tvf4, tvf5, tvf6, tvf7, tvf8};
        bt_filter = (Button) findViewById(R.id.notary_filter_but_filter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bool_sort[8]= isChecked;
                on_position();
            }
        });
        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bool_sort[9]= isChecked;
                on_position();
            }
        });
        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bool_sort[10]= isChecked;
                on_position();
            }
        });
        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bool_sort[11]= isChecked;
                on_position();
            }
        });
        ch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bool_sort[12]= isChecked;
                on_position();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(in.getOnemay()){

        }else{
            Intent intent = new Intent(Notary_filter.this, Start_activity_no_login.class);
            startActivity(intent);
            Notary_filter.this.finish();
        }

        frameLayout_not_filter_Visio.setVisibility(View.GONE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListenerGPServices l = new LocationListenerGPServices();
        l.start_my(locationManager, getApplicationContext());
    }

    public void XY_set() {
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
        Double lat, lon;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            in.set_latitude(location.getLatitude());
            in.set_longitude(location.getLongitude());
            Toast.makeText(this, "" + lat.toString() + "-" + lon.toString(), Toast.LENGTH_SHORT).show();

        } catch (NullPointerException e) {
            Toast.makeText(this, "HELL-NO", Toast.LENGTH_SHORT).show();
            Log.e("HELL-NO", "n", e);
            xyi();
            e.printStackTrace();
        }
    }
    public void xyi(){
        LocationListenerGPServices l = new LocationListenerGPServices();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        l.start(lm, this );
    }
    //end coor

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.notary_filter_but_1://*-*-*-*-*-*-
                if(bool_sort[0]){
                    bool_sort[0]=false;
                    in.set_filter_tip(1);
                    if(bool_sort[1] = false){
                        for (int i = 0; i <bool_sort.length ; i++) {
                            bool_sort[i]=false;
                        }
                    }
                }else{
                    bool_sort[0]=true;
                    bool_sort[1]=false;
                    tv_mas[1].setText("Другой регион/населенный пункт");
                    in.set_id_city("");
                    in.set_filter_tip(1);
                }
                in.set_latitude(IN.latitude_my);
                in.set_longitude(IN.longitude_my);
                on_position();
                break;
            case R.id.notary_filter_but_2://*-*-*-*-*-*-
                Dialog_region dr = new Dialog_region(Notary_filter.this, in.get_url());
                dr.openDialogRegion();
                break;
            case R.id.notary_filter_but_3://*-*-*-*-*-*-
                if(bool_sort[2]){
                    bool_sort[2]=false;
                }else{
                    bool_sort[2]=true;
                }
                on_position();
                break;
            case R.id.notary_filter_but_4://*-*-*-*-*-*-
                if(bool_sort[3]){
                    bool_sort[3]=false;
                }else{
                    bool_sort[3]=true;
                }
                on_position();
                break;
            case R.id.notary_filter_but_5://*-*-*-*-*-*-
                if(bool_sort[4]){
                    bool_sort[4]=false;
                }else{
                    bool_sort[4]=true;
                }
                on_position();
                break;
            case R.id.notary_filter_but_6://*-*-*-*-*-*-
                if(bool_sort[5]){
                    bool_sort[5]=false;
                }else{
                    bool_sort[5]=true;
                }
                on_position();
                break;
            case R.id.notary_filter_but_7://*-*-*-*-*-*-
                if(bool_sort[6]){
                    bool_sort[6]=false;
                }else{
                    bool_sort[6]=true;
                }
                on_position();
                break;
            case R.id.notary_filter_but_8://*-*-*-*-*-*-
                if(bool_sort[7]){
                    bool_sort[7]=false;
                }else{
                    bool_sort[7]=true;
                }
                on_position();
                break;
            case R.id.notary_filter_but_9:
                if(dop_fil){
                    dop_fil=false;
                    lila.setLayoutParams(
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1), (float) 0));
                }else{
                    dop_fil=true;
                    lila.setLayoutParams(
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0));
                }
                break;
            /**
             * поиск всех нотариусов
             */
            case R.id.notary_filter_but_vse:
                in.set_filter_tip(0);
                Gson gson = new Gson();

                FindNotaryByFilter notary= new FindNotaryByFilter();
                notary.WorkInOffDays = bool_sort[2];
                notary.Equipage = bool_sort[3];
                notary.Appointments = bool_sort[4];
                notary.AppointmentsEmail = bool_sort[5];
                notary.WorkWithJur = bool_sort[6];
                notary.IsPleadtingHereditaryCases = bool_sort[7];
                notary.IsSitesCertification = bool_sort[8];
                notary.LockDocuments = bool_sort[9];
                notary.DepositsReception = bool_sort[10];
                notary.RequestsAndDuplicate = bool_sort[11];
                notary.Transactions = bool_sort[12];
                notary.Consultation = false;

                notary.SortingType = FindNotaryByFilter.sortingType.Name;
                String JSON = gson.toJson(notary);
                frameLayout_not_filter_Visio.setVisibility(View.VISIBLE);
                new UrlConnectionTaskFilter().execute(JSON);

                break;
            case R.id.notary_filter_but_filter:

                Gson gson2 = new Gson();

                GetNotaryByAddressCoordinates notaryByAddressCoordinates = new GetNotaryByAddressCoordinates();
                notaryByAddressCoordinates.WorkInOffDays = bool_sort[2];
                notaryByAddressCoordinates.Equipage = bool_sort[3];
                notaryByAddressCoordinates.Appointments = bool_sort[4];
                notaryByAddressCoordinates.AppointmentsEmail = bool_sort[5];
                notaryByAddressCoordinates.WorkWithJur = bool_sort[6];
                notaryByAddressCoordinates.IsPleadtingHereditaryCases = bool_sort[7];
                notaryByAddressCoordinates.IsSitesCertification = bool_sort[8];
                notaryByAddressCoordinates.LockDocuments = bool_sort[9];
                notaryByAddressCoordinates.DepositsReception = bool_sort[10];
                notaryByAddressCoordinates.RequestsAndDuplicate = bool_sort[11];
                notaryByAddressCoordinates.Transactions = bool_sort[12];
                notaryByAddressCoordinates.Consultation = false;
                notaryByAddressCoordinates.Latitude = in.get_latitude();
                notaryByAddressCoordinates.Longitude = in.get_longitude();

                notaryByAddressCoordinates.Radius = 55;
                notaryByAddressCoordinates.SortingType = GetNotaryByAddressCoordinates.sortingType.Name;
                String JSONByAddressCoordinates = gson2.toJson(notaryByAddressCoordinates);
                frameLayout_not_filter_Visio.setVisibility(View.VISIBLE);
                new UrlConnectionTaskFilter().execute(JSONByAddressCoordinates);

                break;
            case R.id.notary_filter_but_close:
                Notary_filter.this.finish();
                break;
            case R.id.notary_filter_but_menu:
                intent = new Intent(Notary_filter.this, New_sidebar.class);
                startActivity(intent);
                break;
        }
    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = Notary_filter.this.getResources().getDisplayMetrics();
        int px =Math.round(dp *(displayMetrics.xdpi /DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void on_position(){
        but_yes=false;
        for(int i=0; i<bool_sort.length; i++){
            if(bool_sort[i]){
                but_yes = true;
            }
        }
        if (but_yes) {
            buttonNotaryFilterYesNo.setVisibility(View.GONE);
            bt_filter.setBackgroundResource(R.color.black_g);//Color(Color.argb(255, 255, 255, 255));
            } else {
            buttonNotaryFilterYesNo.setVisibility(View.VISIBLE);
            bt_filter.setBackgroundResource(R.color.color_button_gray); //;Color(Color.argb(181, 56, 175, 56));//setTextColor(Color.argb(255, 255, 255, 255));
        }
        for(int i=0; i<8; i++){
            if (bool_sort[i]) {
                tv_mas[i].setBackgroundColor(Color.argb(181, 56, 175, 56));
            } else {
                tv_mas[i].setBackgroundColor(Color.argb(255, 255, 255, 255));
            }
        }

        if(bool_sort[0]==false && bool_sort[1]==false){
            for (int i = 0; i < bool_sort.length; i++) {
                bool_sort[i] = false;
            }
            for (int i = 2; i < 8; i++) {
                tv_mas[i].setBackgroundResource(R.color.common_google_signin_btn_text_light_default);
                mButtonsFilter[i-2].setVisibility(View.GONE);
            }
        }else {
            for (int i = 0; i <mButtonsFilter.length ; i++) {
                mButtonsFilter[i].setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void iv_onRegion(City city, Region region, boolean b) {
        if(b){
//            if(bool_sort[1]){
//                bool_sort[1]=false;
//                tv_mas[1].setText("Другой регион/населенный пункт");
//                in.set_id_city("");
//                in.set_filter_tip(1);
//            }else{
                bool_sort[1]=true;
                bool_sort[0]=false;
                in.set_filter_tip(2);
                tv_mas[1].setText(city.Name);
                in.set_latitude(city.Latitude);
                in.set_longitude(city.Longitude);
                in.set_city(city);
                Log.d("QWERTY", String.valueOf(in.get_latitude())+" "+String.valueOf(in.get_longitude()));
//            }

        }else{
            bool_sort[1]=false;
            tv_mas[1].setText("Другой регион/населенный пункт");
            in.set_id_city("");
            in.set_filter_tip(1);
        }
        on_position();
    }

    public void startListActivity(String result){
        Intent intent = new Intent(getApplicationContext(), Notary_list.class);
        Gson gson = new Gson();
        if (in.get_filter_tip() == 0) {

            IN.RESULT_LIST_NOTARY = gson.fromJson(result, Notary[].class);
            for (Notary notary:IN.RESULT_LIST_NOTARY ) {
                try {
                    notary.Distance = in.dDistance(
                            notary.Latitude,
                            notary.Longitude,
                            locationManager,
                            getApplicationContext()
                    );
                }catch (Exception e){
                    notary.Distance = 12312312312.2;
                }
            }
            intent.putExtra("SEARCH_NOTARY_ARRAY_BOOL", bool_sort);
            intent.putExtra("FILES_sort", 1);
            intent.putExtra("CITY_SEARCH", "");
            startActivity(intent);
        }else{

            IN.RESULT_LIST_NOTARY = gson.fromJson(result, Notary[].class);
            for (Notary notary:IN.RESULT_LIST_NOTARY ) {
                try {
                    notary.Distance = in.dDistance(
                            notary.Latitude,
                            notary.Longitude,
                            locationManager,
                            getApplicationContext()
                    );
                }catch (Exception e){
                    notary.Distance = 12312312312.2;
                }
            }
            intent.putExtra("FILES_sort", 2);
            intent.putExtra("SEARCH_NOTARY_ARRAY_BOOL", bool_sort);

            if(tv_mas[1].equals("Другой регион/населенный пункт")|| bool_sort[0]){
                intent.putExtra("CITY_SEARCH", "");
            }else {
                intent.putExtra("CITY_SEARCH", tv_mas[1].getText());
            }
            if( IN.RESULT_LIST_NOTARY.length==0){
                Toast.makeText(this, "Нет данных, по вашему запросу!", Toast.LENGTH_SHORT).show();
            }else{
                startActivity(intent);
            }

        }
        frameLayout_not_filter_Visio.setVisibility(View.GONE);
    }

    class UrlConnectionTaskFilter extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            Request request = null;
            if (in.get_filter_tip() == 0) {
                request = new Request.Builder()
                        .header("Authorization", in.get_token_type() + " " + in.get_token())
                        .url("http://" + in.get_url() + "/Notaries/GetNotaryByFilter")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
                        .build();
            }else {
                request = new Request.Builder()
                        .header("Authorization", in.get_token_type() + " " + in.get_token())
                        .url("http://" + in.get_url() + "/Notaries/GetNotaryByAddressCoordinates")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
                        .build();
            }

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();
                code = response.code();

            } catch (IOException e) {
                e.printStackTrace();
                frameLayout_not_filter_Visio.setVisibility(View.GONE);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null && code >= 200 && code < 300) {
                startListActivity(result);
            }else{
                frameLayout_not_filter_Visio.setVisibility(View.GONE);
            }
            super.onPostExecute(result);
        }
    }
}
