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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.New_sidebar;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.City;
import kondratkov.advocatesandnotariesrf.account.Region;
import kondratkov.advocatesandnotariesrf.dop_dialog.Dialog_region;
import kondratkov.advocatesandnotariesrf.maps.LocationListenerGPServices;
import kondratkov.advocatesandnotariesrf.start_help.Start_activity_no_login;

public class Notary_filter extends Activity implements Dialog_region.i_dialog_region {

    LinearLayout lila;
    TextView tvf1, tvf2, tvf3, tvf4, tvf5, tvf6, tvf7, tvf8, tv_but_f;
    public CheckBox ch1, ch2, ch3, ch4, ch5;
    Button bt_filter;
    public TextView[] tv_mas = null;
    public String[] s_mas = new String[]{"", "", "", "", "", "", "", ""};
    public String[] s_mas_text = new String[]{"", "", "", "", "", "", "", ""};
    public boolean but_yes = false;
    public boolean dop_fil = false;
    IN in;
    Point point;
    int view_height;
    int v_bup = 0;
    int v_office = 0;
    public boolean[] bool_sort = new boolean[]{false, false, false, false, false,
            false, false, false, false, false,
            false, false, false};

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private TextView mLatitudeTextView, mLongitudeTextView;

    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 1; // в метрах
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // в мс

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

        tv_but_f = (TextView) findViewById(R.id.notary_filter_tv_dbt_filter);
        tv_mas = new TextView[]{tvf1, tvf2, tvf3, tvf4, tvf5, tvf6, tvf7, tvf8};
        bt_filter = (Button) findViewById(R.id.notary_filter_but_filter);

        bool_sort = in.get_sort_notary();

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
                }else{
                    bool_sort[0]=true;
                    bool_sort[1]=false;
                    XY_set();
                    tv_mas[1].setText("Другой регион/населенный пункт");
                    in.set_id_city("");
                    in.set_filter_tip(1);
                }
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
            case R.id.notary_filter_but_vse:
                in.set_filter_tip(0);
                intent = new Intent(Notary_filter.this, Notary_list.class);
                intent.putExtra("FILES_sort", 1);
                startActivity(intent);
                break;
            case R.id.notary_filter_but_filter:
                intent = new Intent(Notary_filter.this, Notary_list.class);
                intent.putExtra("FILES_sort", 2);
                startActivity(intent);
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
            tv_but_f.setClickable(false);
            bt_filter.setClickable(true);
            bt_filter.setBackgroundColor(Color.argb(181, 56, 175, 56));//setTextColor(Color.argb(255, 255, 255, 255));
        } else {
            tv_but_f.setClickable(true);
            bt_filter.setClickable(false);
            bt_filter.setBackgroundColor(Color.argb(255, 255, 255, 255));
        }
        for(int i=0; i<8; i++){
            if (bool_sort[i]) {
                tv_mas[i].setBackgroundColor(Color.argb(181, 56, 175, 56));
            } else {
                tv_mas[i].setBackgroundColor(Color.argb(255, 255, 255, 255));
            }
        }
    }

    @Override
    public void iv_onRegion(City city, Region region, boolean b) {
        if(b){
            if(bool_sort[1]){
                bool_sort[1]=false;
                tv_mas[1].setText("Другой регион/населенный пункт");
                in.set_id_city("");
                in.set_filter_tip(1);
            }else{
                bool_sort[1]=true;
                bool_sort[0]=false;
                in.set_filter_tip(2);
                tv_mas[1].setText(city.Name);
                in.set_latitude(city.Latitude);
                in.set_longitude(city.Longitude);
                in.set_city(city);
                Log.d("QWERTY", String.valueOf(in.get_latitude())+" "+String.valueOf(in.get_longitude()));
            }

        }else{
            bool_sort[1]=false;
            tv_mas[1].setText("Другой регион/населенный пункт");
            in.set_id_city("");
            in.set_filter_tip(1);
        }
        on_position();
    }
}
