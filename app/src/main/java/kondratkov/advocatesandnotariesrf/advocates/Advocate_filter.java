package kondratkov.advocatesandnotariesrf.advocates;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;

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
import java.util.Date;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.New_sidebar;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.Bup;
import kondratkov.advocatesandnotariesrf.account.City;
import kondratkov.advocatesandnotariesrf.account.JudicialAreas;
import kondratkov.advocatesandnotariesrf.account.Region;
import kondratkov.advocatesandnotariesrf.doc_create.create_document;
import kondratkov.advocatesandnotariesrf.dop_dialog.Dialog_region;
import kondratkov.advocatesandnotariesrf.dop_dialog.Dialog_sud_ter;
import kondratkov.advocatesandnotariesrf.maps.LocationListenerGPServices;
import kondratkov.advocatesandnotariesrf.start_help.Start_activity_no_login;

public class Advocate_filter extends Activity implements Dialog_region.i_dialog_region, Dialog_sud_ter.i_dialog_sudTer {

    TextView tvf1, tvf2, tvf3, tvf4, tvf5, tvf6, tv_but_f;
    Button bt_filter;
    public TextView[] tv_mas = null;
    public String[] s_mas = new String[]{"", "", "", "", "", ""};
    public String[] s_mas_text = new String[]{"", "", "", "", "", ""};
    public boolean but_yes = false;
    public boolean answers_next = false;
    IN in;
    Point point;
    int view_height;
    int v_bup = 0;
    int v_office = 0;
    LocationManager locationManager;
    public LocationRequest mLocationRequest;

    public boolean[] bool_sort = new boolean[]{false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false};

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

    public Bup Filter_Bup;
    public String Filter_specialization = "";
    public String Filter_city = "";
    public int Filter_SudTer = 0;
    public double Filter_lan = 0;
    public double Filter_lon = 0;

    @BindView(R.id.frameLayout_advocateFilter_1)FrameLayout frameLayout_advocateFilter_1;
    @BindView(R.id.frameLayout_advocateFilter_2)FrameLayout frameLayout_advocateFilter_2;
    @BindView(R.id.frameLayout_advocateFilter_3)FrameLayout frameLayout_advocateFilter_3;
    @BindView(R.id.frameLayout_advocateFilter_4)FrameLayout frameLayout_advocateFilter_4;
    @BindView(R.id.frameLayout_advocateFilter_5)FrameLayout frameLayout_advocateFilter_5;
    @BindView(R.id.frameLayout_advocateFilter_6)FrameLayout frameLayout_advocateFilter_6;

    FrameLayout [] mFrameLayouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advocate_filter);

        ButterKnife.bind(this);

        tvf1 = (TextView) findViewById(R.id.advocates_filter_tv_1);
        tvf2 = (TextView) findViewById(R.id.advocates_filter_tv_2);
        tvf3 = (TextView) findViewById(R.id.advocates_filter_tv_3);
        tvf4 = (TextView) findViewById(R.id.advocates_filter_tv_4);
        tvf5 = (TextView) findViewById(R.id.advocates_filter_tv_5);
        tvf6 = (TextView) findViewById(R.id.advocates_filter_tv_6);
        tv_but_f = (TextView) findViewById(R.id.advocates_filter_tv_dbt_filter);
        tv_mas = new TextView[]{tvf1, tvf2, tvf3, tvf4, tvf5, tvf6};
        bt_filter = (Button) findViewById(R.id.advocates_filter_but_filter);

        mFrameLayouts = new FrameLayout[]{
                frameLayout_advocateFilter_1, frameLayout_advocateFilter_2,
                frameLayout_advocateFilter_3, frameLayout_advocateFilter_4,
                frameLayout_advocateFilter_5, frameLayout_advocateFilter_6};

        in = new IN();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (in.getOnemay()) {

        } else {
            Intent intent = new Intent(Advocate_filter.this, Start_activity_no_login.class);
            startActivity(intent);
            Advocate_filter.this.finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        LinearLayout button = (LinearLayout) findViewById(R.id.advocates_filter_lila_1);
        LinearLayout lila_2 = (LinearLayout) findViewById(R.id.advocates_filter_lila_2);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);
        view_height = lila_2.getHeight();

        // Initialize the Point with x, and y positions
        point = new Point();
        point.x = location[0];
        point.y = location[1];
    }

    public void start_play() {
        int bw = 0;
        for (int i = 0; i < s_mas.length; i++) {
            if (s_mas[i].equals("")) {
                mFrameLayouts[i].setBackgroundColor(Color.argb(255, 255, 255, 255));
                if(i==1)tv_mas[i].setText("Выбрать город");
                if(i==4)tv_mas[i].setText("Судебная территория");
                bw++;
            } else {
                but_yes = true;
                tv_mas[i].setText(s_mas[i]);
                mFrameLayouts[i].setBackgroundColor(Color.argb(181, 56, 175, 56));
            }
        }
        if(bw == s_mas.length)but_yes = false;

        if (but_yes) {
            tv_but_f.setClickable(false);
            bt_filter.setClickable(true);
            bt_filter.setBackgroundColor(Color.argb(181, 56, 175, 56));
        } else {
            tv_but_f.setClickable(true);
            bt_filter.setClickable(false);
            bt_filter.setBackgroundColor(Color.argb(255, 255, 255, 255));
        }
    }

    //start coor
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
        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
            case R.id.advocates_filter_but_1:
                s_mas[0] = "Текущее местоположение";
                s_mas[1] = "";
                in.set_id_city("");

                XY_set();
                in.set_filter_tip(2);
                Filter_city = "";
                Filter_Bup = new Bup();
                Filter_lan = in.get_latitude();
                Filter_lon = in.get_longitude();
                start_play();
                break;
            case R.id.advocates_filter_but_2:
                Filter_Bup = new Bup();
                Dialog_region dr = new Dialog_region(Advocate_filter.this, in.get_url());
                dr.openDialogRegion();
                //Log.d("qwerty", "CITY YYYYY ="+dr.City_id);
                //openDialogRegion();//new AsyncTaskDialog().execute();
                break;
            case R.id.advocates_filter_but_3:
                dialog_start_spec(this, point);
                break;
            case R.id.advocates_filter_but_4:
                dialog_start_bup();
                break;
            case R.id.advocates_filter_but_5:
                Dialog_sud_ter ds = new Dialog_sud_ter(Advocate_filter.this, in.get_url());
                ds.openDialogSudTer();
                //new AsyncTaskExample().execute();
                //dialog_start_3();
                break;
            case R.id.advocates_filter_but_6:
                if(answers_next){
                    answers_next = false;
                    s_mas[5] = "";
                    start_play();
                }else {
                    XY_set();
                    answers_next = true;
                    s_mas[5] = getResources().getString(R.string.advocate_filter_6);
                    start_play();
                }
                //dialog_start_office();
                break;
            case R.id.advocates_filter_but_close:
                Advocate_filter.this.finish();
                break;
            case R.id.advocates_filter_but_menu:
                intent = new Intent(Advocate_filter.this, New_sidebar.class);
                startActivity(intent);
                break;
            case R.id.advocates_filter_but_vse:
                in.set_filter_tip(0);
                intent = new Intent(Advocate_filter.this, Advocates_list.class);
                Gson gson1 = new Gson();
                intent = new Intent(Advocate_filter.this, Advocates_list.class);
                Bup bup_now = new Bup();
                intent.putExtra("BUP", gson1.toJson(bup_now));
                intent.putExtra("SPECIALIZATION", "");
                intent.putExtra("CITY", "");
                intent.putExtra("SUDTER", 0);
                intent.putExtra("LANT", 0);
                intent.putExtra("LONG", 0);
                intent.putExtra("ANSWERNEXT", false);
                startActivity(intent);
                break;
            case R.id.advocates_filter_but_filter:
                if(in.get_filter_tip()==0){in.set_filter_tip(1);}
                Gson gson = new Gson();
                intent = new Intent(Advocate_filter.this, Advocates_list.class);
                intent.putExtra("BUP", gson.toJson(Filter_Bup));
                intent.putExtra("SPECIALIZATION", Filter_specialization);
                intent.putExtra("CITY", Filter_city);
                intent.putExtra("RADIUS", 50);
                intent.putExtra("SUDTER", Filter_SudTer);
                intent.putExtra("LANT", Filter_lan);
                intent.putExtra("LONG", Filter_lon);
                intent.putExtra("ANSWERNEXT", answers_next);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void iv_onRegion(City city, Region region, boolean b) {
        Log.d("qwertyTRY YES", "TRY YES _123");
        if(b){
            s_mas[0] = "";
            s_mas[1] = city.Name;
            Filter_lan = city.Latitude;
            Filter_lon =city.Longitude;
            in.set_filter_tip(1);
            Filter_city = city.Name;
        }else{
           // s_mas[0] = "Текущее местоположение";
            s_mas[0] = "";
            s_mas[1] = "";
            in.set_id_city("");

            //XY_set();
           // in.set_filter_tip(2);
        }

        start_play();
    }

    public boolean ff = true;

    public void dialog_start_spec(final Activity context, Point p) {
        LinearLayout button = (LinearLayout) findViewById(R.id.advocates_filter_lila_1);
        int popupWidth = button.getWidth();
        //int popupHeight = 270;

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.advocate_dialog_filter_spec, null);


        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(layout,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        //popup.setHeight(popupHeight);
        popup.setFocusable(true);
        popup.showAsDropDown(button);

        // Some offset to align the popup a bit to the right, and a bit down,
        // relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = view_height - 5;

        final LinearLayout lila_spec = (LinearLayout) layout.findViewById(R.id.filter_spec_lila_spec);
        CheckBox filter_check_spc_1 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_1);
        CheckBox filter_check_spc_2 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_2);
        CheckBox filter_check_spc_3 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_3);
        CheckBox filter_check_spc_4 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_4);
        CheckBox filter_check_spc_5 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_5);
        CheckBox filter_check_spc_6 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_6);
        CheckBox filter_check_spc_7 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_7);
        CheckBox filter_check_spc_8 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_8);
        CheckBox filter_check_spc_9 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_9);
        CheckBox filter_check_spc_10 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_10);
        CheckBox filter_check_spc_11 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_11);
        CheckBox filter_check_spc_12 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_12);
        CheckBox filter_check_spc_13 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_13);
        CheckBox filter_check_spc_14 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_14);
        CheckBox filter_check_spc_15 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_15);
        CheckBox filter_check_spc_16 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_16);
        CheckBox filter_check_spc_17 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_17);
        CheckBox filter_check_spc_18 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_18);
        CheckBox filter_check_spc_19 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_19);
        CheckBox filter_check_spc_20 = (CheckBox) layout.findViewById(R.id.filter_spec_check_spc_20);
        final CheckBox[] check_spc = new CheckBox[]{filter_check_spc_1, filter_check_spc_2, filter_check_spc_3, filter_check_spc_4,
                filter_check_spc_5, filter_check_spc_6, filter_check_spc_7, filter_check_spc_8, filter_check_spc_9,
                filter_check_spc_10, filter_check_spc_11, filter_check_spc_12, filter_check_spc_13, filter_check_spc_14,
                filter_check_spc_15, filter_check_spc_16, filter_check_spc_17, filter_check_spc_18, filter_check_spc_19,
                filter_check_spc_20};

        Filter_specialization = "";
        for (int i = 0; i < check_spc.length; i++) {
            check_spc[i].setChecked(bool_sort[i]);
        }

        for (int i = 0; i < check_spc.length; i++) {
            check_spc[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (int j = 0; j < check_spc.length; j++) {

                        check_spc[j].setChecked(false);

                        buttonView.setChecked(isChecked);
                    }
                }
            });
        }


        // Clear the default translucent background

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(button, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
        //popupWindow.showAsDropDown(butOpenPopup, 200, -15);

        // Getting a reference to Close button, and close the popup when
        // clicked.

        Button close = (Button) layout.findViewById(R.id.filter_spec_but_close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ff = true;
                popup.dismiss();
            }
        });

        Button add_filter = (Button) layout.findViewById(R.id.filter_spec_but_start_sort);
        add_filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 0; i < check_spc.length; i++) {
                    s_mas_text[2] = "";
                    s_mas[2] = "";
                    if (check_spc[i].isChecked()) {
                        s_mas[2] = "Специализации";
                        Filter_specialization = getResources().getStringArray(R.array.ArraySpecialization)[i];
                        s_mas[3] = "";
                        Filter_Bup = new Bup();
                        i = check_spc.length;
                        // s_mas_text[2] = check_spc[i].getText().toString();
                    }
                }
                ff = true;
                start_play();
                popup.dismiss();
            }
        });
    }

    public void dialog_start_bup() {
        final Dialog dialog = new Dialog(Advocate_filter.this);
        dialog.setTitle("Бесплатная юридическая помощь");
        dialog.setContentView(R.layout.advocate_dialog_filter_bup);

        Button btnClose = (Button) dialog.getWindow().findViewById(
                R.id.but_bup_cancel);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Button btnYes = (Button) dialog.getWindow().findViewById(
                R.id.but_bup_yes);

        final CheckBox ch1 = (CheckBox) dialog.getWindow().findViewById(R.id.bup_checkBox);
        final CheckBox ch2 = (CheckBox) dialog.getWindow().findViewById(R.id.bup_checkBox2);
        final CheckBox ch3 = (CheckBox) dialog.getWindow().findViewById(R.id.bup_checkBox3);

        switch (v_bup) {
            case 1:
                ch1.setChecked(true);
                break;
            case 2:
                ch2.setChecked(true);
                break;
            case 3:
                ch3.setChecked(true);
        }

        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch1.isChecked()) {
                    ch2.setChecked(false);
                }
            }
        });

        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch2.isChecked()) {
                    ch1.setChecked(false);
                }
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Bup = new Bup();
                Filter_Bup.Upk51 = ch1.isChecked();
                Filter_Bup.Gk50 = ch2.isChecked();
                Filter_Bup.Fz324 = ch3.isChecked();

                if (ch1.isChecked()) {
                    v_bup = 1;
                    s_mas[3] = "Бесплатная юридическая помощь";
                } else if (ch2.isChecked()) {
                    v_bup = 2;
                    s_mas[3] = "Бесплатная юридическая помощь";
                } else if(ch3.isChecked()) {
                    v_bup = 3;
                    s_mas[3] = "Бесплатная юридическая помощь";
                }else
                {
                    v_bup = 0;
                    s_mas[3] = "";
                }
                dialog.dismiss();
                s_mas[2] = "";
                start_play();
            }
        });
        dialog.show();
    }

//
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void iv_onRegion(JudicialAreas judicialAreas, Region region, boolean b) {
        if(b){
            s_mas[4] = judicialAreas.Name;
            Filter_SudTer = judicialAreas.Id;
        }else{
            s_mas[4] = "";
            Filter_SudTer = 0;
        }

        //in.set_id_sud_ter(sud_ter_id);
        start_play();
    }
}



//   public void dialog_start_office() {
//        final Dialog dialog = new Dialog(Advocate_filter.this);
//        dialog.setTitle("Выбор");
//        dialog.setContentView(R.layout.advocate_dialog_filter_office);
//
//        Button btnClose = (Button) dialog.getWindow().findViewById(
//                R.id.but_office_cancel);
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        final Button btnYes = (Button) dialog.getWindow().findViewById(
//                R.id.but_office_yes);
//
//        final CheckBox ch1 = (CheckBox) dialog.getWindow().findViewById(R.id.office_checkBox);
//        final CheckBox ch2 = (CheckBox) dialog.getWindow().findViewById(R.id.office_checkBox2);
//        final CheckBox ch3 = (CheckBox) dialog.getWindow().findViewById(R.id.office_checkBox3);
//
//        switch (v_office) {
//            case 1:
//                ch1.setChecked(true);
//                break;
//            case 2:
//                ch2.setChecked(true);
//                break;
//            case 3:
//                ch3.setChecked(true);
//                break;
//        }
//
//        ch1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ch1.isChecked()) {
//                    ch2.setChecked(false);
//                    ch3.setChecked(false);
//                }
//            }
//        });
//
//        ch2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ch2.isChecked()) {
//                    ch1.setChecked(false);
//                    ch3.setChecked(false);
//                }
//            }
//        });
//
//        ch3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ch3.isChecked()) {
//                    ch1.setChecked(false);
//                    ch2.setChecked(false);
//                }
//            }
//        });
//
//        btnYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ch1.isChecked()) {
//                    v_office = 1;
//                    s_mas[5] = "Офис";
//                } else if (ch2.isChecked()) {
//                    v_office = 2;
//                    s_mas[5] = "Коллегия(бюро)";
//                } else if (ch3.isChecked()) {
//                    v_office = 3;
//                    s_mas[5] = "Выезд";
//                } else {
//                    v_office = 0;
//                    s_mas[5] = "";
//                }
//                dialog.dismiss();
//                start_play();s_mas[5] = "Офис";
//            }
//        });
//        dialog.show();
//    }
