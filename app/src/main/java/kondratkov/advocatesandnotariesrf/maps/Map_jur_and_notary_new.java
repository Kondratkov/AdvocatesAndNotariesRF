package kondratkov.advocatesandnotariesrf.maps;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.Bup;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindByCoordinatesFilter;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindJuristFilter;
import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;

/**
 * Created by Kondratkov on 14.12.2016.
 */

public class Map_jur_and_notary_new extends Activity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String DEBUG_TAG = "qwerty";
    String json_maps =
            "{\"array\":[" +
                    "{\"tip_place\":\"0\",\"id\":\"13\",\"surname\":\"Иванов\",\"name\":\"Иван1\",\"patronymic\":\"Иванович\",\"Y\":\"39.2994\",\"X\":\"51.7113\",\"adres\":\"Воронеж ул Какая-то д такой то\"}," +
                    "{\"tip_place\":\"0\",\"id\":\"13\",\"surname\":\"Иванов\",\"name\":\"Иван2\",\"patronymic\":\"Иванович\",\"Y\":\"39.2894\",\"X\":\"51.7123\",\"adres\":\"Воронеж ул Какая-то д такой то\"}," +
                    "{\"tip_place\":\"0\",\"id\":\"13\",\"surname\":\"Иванов\",\"name\":\"Иван3\",\"patronymic\":\"Иванович\",\"Y\":\"39.2694\",\"X\":\"51.7133\",\"adres\":\"Воронеж ул Какая-то д такой то\"}," +
                    "{\"tip_place\":\"0\",\"id\":\"13\",\"surname\":\"Иванов\",\"name\":\"Иван11\",\"patronymic\":\"Иванович\",\"Y\":\"39.2694\",\"X\":\"51.7883\",\"adres\":\"Воронеж ул Какая-то д такой то\"}]}";

    public String json_s = "";
    public ListView listViewMap;
    public JSONArray jsonArrayMap = null;
    public JSONObject jsonObjectMap = null;
    public List<JSONObject> listMap = null;

    public EditText et_dist;
    public SeekBar map_seekBar;

    public IN in = new IN();
    private GoogleMap map1;
    public LatLng mark_jur;

    public LinearLayout lila_map_filter;
    public Boolean boolFilter = true;

    public Marker marker;

    public JuristAccounClass[] mcArrayJuristAccoun;

    public int Search_id_city;
    public int Search_id_JuristAreas;
    public String Search_specialization;
    public Bup Search_bup;
    public FindJuristFilter.sortingType Search_sortingType1;
    public FindByCoordinatesFilter.sortingType Search_sortingType2;
    public String Search_surename;
    public boolean Search_IsOnline;
    public boolean Search_WorkInOffDays;
    public boolean Search_CanFastComing;

    public boolean sort_bool_fuck[];
    public boolean sortir = true;

    //--S----L--O--C----------------------------------------------------
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 1000; // в метрах
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 360000; // в мс
    //--F----L--O--C----------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_jur_and_notary);
    }

    @Override
    protected void onStart() {
        super.onStart();
        in.setChoice_of_menus(7);

        //--S----L--O--C----------------------------------------------------
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //mLocationListener = new MyLocationListener();
        //--F----L--O--C----------------------------------------------------

        lila_map_filter = (LinearLayout) findViewById(R.id.lila_map_filter);
        et_dist = (EditText) findViewById(R.id.et_dist);
        map_seekBar = (SeekBar) findViewById(R.id.map_seekBar);
        map_seekBar.setOnSeekBarChangeListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment1 = (MapFragment) fragmentManager
                .findFragmentById(R.id.fragment_map);
        map1 = mapFragment1.getMap();
        // выбираем тип карты
        map1.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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
        map1.setMyLocationEnabled(true);

        //mark_jur = new LatLng(51.7183, 39.2694);
        //map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));

        //setDataMap(json_maps);

        map1.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(DEBUG_TAG, "onMapClick: " + latLng.latitude + "," + latLng.longitude);

            }
        });

        map1.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d(DEBUG_TAG, "onMapLongClick: " + latLng.latitude + "," + latLng.longitude);
            }
        });

        map1.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition camera) {
                Log.d(DEBUG_TAG, "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);

                double l_x = in.get_latitude()-camera.target.latitude;
                double l_y = in.get_longitude()-camera.target.longitude;
                Log.d("qwerty", "nado li popo "+ l_x+" "+ l_y);
                if(Math.abs(l_x)>0.02||Math.abs(l_y)>0.02){
                    in.set_latitude(camera.target.latitude);
                    in.set_longitude(camera.target.longitude);
                    in.set_text(String.valueOf(et_dist.getText()));

                    //mark_jur = new LatLng(in.get_latitude(), in.get_longitude());
                    //marker = map1.addMarker(new MarkerOptions().title("dfdfdf").position(mark_jur));


                    new AsyncTaskMaps().execute();
                }
                //mark_jur = new LatLng(in.get_latitude(), in.get_longitude());
                //map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));
            }
        });
    }

    public void start_activity(String s){
        json_maps = s;
        JSONObject j = null;
        try {
            j = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        add_mark_maps(getMark(j));
        //setDataMap(json_maps);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // TODO Auto-generated method stub
        et_dist.setText(String.valueOf(seekBar.getProgress()*250));
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public void onClickMap(View v) {
        switch (v.getId()) {
            case R.id.but_map_filter:
                if (boolFilter == true) {
                    lila_map_filter.setLayoutParams(
                            new LinearLayout.LayoutParams(dpToPx(270), dpToPx(400), (float) 0));
                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.trans_map_filter_on);
                    lila_map_filter.startAnimation(anim);

                    boolFilter = false;
                } else {
                    Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.trans_map_filter_off);
                    lila_map_filter.startAnimation(anim1);

                    lila_map_filter.setLayoutParams(
                            new LinearLayout.LayoutParams(dpToPx(1), dpToPx(160), (float) 0));

                    in.set_text(String.valueOf(et_dist.getText()));
                    new AsyncTaskMaps().execute();
                    boolFilter = true;
                }/**/
                break;



            case R.id.but_map_close:
                Map_jur_and_notary_new.this.finish();
                break;
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public List<JSONObject> getMark(JSONObject jsonObjectMess) {

        List markList = new ArrayList<>();
        try {
            jsonArrayMap = jsonObjectMess.getJSONArray("array");

            for (int i = 0; i <jsonArrayMap.length(); i++) {
                JSONObject arrayElement = jsonArrayMap.getJSONObject(i);
                markList.add(arrayElement);
            }

        } catch (JSONException e) {
            // TODO Auto-generatedcatchblock
            e.printStackTrace();
        }
        return markList;
    }

    public void add_mark_maps(List<JSONObject> list){
        Log.d("qwerty", "LIST "+list.size());
        map1.clear();
        mark_jur = new LatLng(in.get_latitude(), in.get_longitude());
        CircleOptions circleOptions = new CircleOptions()
                .center(mark_jur)
                .radius(Integer.parseInt(String.valueOf(et_dist.getText())))
                .fillColor(getResources().getColor(R.color.maps_color))
                .strokeColor(getResources().getColor(R.color.maps_color2))
                .strokeWidth(5);
        map1.addCircle(circleOptions);

        for(int i = 0 ; i<list.size(); i++){
            try {
                Double dX = list.get(i).getDouble("X");//Double.parseDouble(list.get(i).getString("X").replaceAll(",", "")) / 10000;
                Double dY = list.get(i).getDouble("Y");//Double.parseDouble(list.get(i).getString("Y").replaceAll(",", "")) / 10000;
                Log.d("qwerty", "LL "+i+" "+dX+" "+dY );

                mark_jur = new LatLng(dX, dY);
                //map1.setInfoWindowAdapter(new MyInfoWindowAdapter());

                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.x_marker_jur);
                marker = map1.addMarker(new MarkerOptions().title(list.get(i).getString("name")).position(mark_jur).icon(bitmapDescriptor));
                //marker = map1.addMarker(new MarkerOptions().title(list.get(i).getString("name")).icon(bitmapDescriptor).snippet(list.get(i).getString("adres"))
                //     .snippet(list.get(i).getString("adres")));//+ " сайт: "+list.get(i).getString("site")).position(mark_jur));

                //marker = map1.addMarker(new MarkerOptions().title("dfdfdf"));
                dropPinEffect(marker);

                //map1.setOnMapLongClickListener(Map_jur_and_notary.this);


                //map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));
                //map1.addMarker(new MarkerOptions().position(mark_jur).title("адрес").snippet(list.get(i).getString("adres")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        map1.setMyLocationEnabled(true);
        map1.setOnMapClickListener(this);
    }


    public void setDataMap(String s) {
        try {
            jsonObjectMap = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonArrayMap = jsonObjectMap.getJSONArray("array");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArrayMap.length(); i++) {
            try {
                jsonArrayMap.getJSONObject(i);

                mark_jur = new LatLng(Double.parseDouble(jsonArrayMap.getJSONObject(i).getString("X")),
                        Double.parseDouble(jsonArrayMap.getJSONObject(i).getString("Y")));

                //map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));
                map1.addMarker(new MarkerOptions().position(mark_jur)
                        .title("адрес").snippet(jsonArrayMap.getJSONObject(i).getString("adres")));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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
        map1.setMyLocationEnabled(true);
        map1.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    class AsyncTaskMaps extends AsyncTask<Void, Integer, String> {
        // фоновая работа
        String url_s = "";
        @Override
        protected String doInBackground(Void... params) {
            JSONObject json_st = new JSONObject();
            try {
                json_st.put("idu", in.get_id_user());//String.valueOf(in.get_id()));
                json_st.put("password", in.get_password());
                json_st.put("tip_who", "0");//String.valueOf(adin.get_mess_id()));
                json_st.put("latitude", String.valueOf(in.get_latitude()));//String.valueOf(adin.get_mess_id()));
                json_st.put("longitude", String.valueOf(in.get_longitude()));//String.valueOf(adin.get_mess_id()));
                json_st.put("dist", in.get_text());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_s = String.valueOf(json_st);
            Log.d("qwerty", "START json_s "+ json_s);
            url_s ="http://"+in.get_url()+"/123.geolocjur";
            return ServerSendData.sendRegData(url_s, json_s);
        }

        // выполняется после doInBackground, имеет доступ к UI
        protected void onPostExecute(String result) {
            if(result != null){
                start_activity(result);
                Log.d("qwerty", "START "+ result);
            }
            else{
                Log.d("qwerty", "START  "+ "NULL");
            }

        }
    }
    public static class ServerSendData {

        public static IN iny = new IN();
        public static String sendRegData(String stringUrl, String json_str) {

            String result =  null;
            try {

                URL url = new URL(stringUrl);
                URLConnection connection = url.openConnection();

                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setDoOutput(true);

                httpConnection.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());

                out.write(json_str.getBytes());

                out.flush();
                out.close();

                InputStream in = new BufferedInputStream(httpConnection.getInputStream());

                int responseCode = 0;
                responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpConnection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));

                    result = r.readLine();

                } else {
                }
            } catch (MalformedURLException e) { Log.d(DEBUG_TAG, "z8");
            } catch (IOException e1) { Log.d(DEBUG_TAG, "z9");
            }

            return result;
        }
    }

    protected  void createLocationRequest() {
        LocationRequest mLocationRequest =  new  LocationRequest ();
        mLocationRequest . setInterval ( 10000 );
        mLocationRequest . setFastestInterval(5000);
        mLocationRequest . setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mContentView;

        MyInfoWindowAdapter() {
            mContentView = getLayoutInflater().inflate(
                    R.layout.maps_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView tv_title = (TextView) mContentView.findViewById(R.id.title);
            tv_title.setText(marker.getTitle());

            TextView tv_snippet = (TextView) mContentView.findViewById(R.id.snippet);
            tv_snippet.setText(marker.getSnippet());

            return mContentView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }
    }
    @Override
    public void onMapLongClick(LatLng point) {
        // TODO Auto-generated method stub
        Marker newMarker = map1.addMarker(new MarkerOptions()
                .position(point)
                .snippet(point.toString()));

        newMarker.setTitle(newMarker.getId());
    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 300;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 1 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window

                }
            }
        });
    }
}