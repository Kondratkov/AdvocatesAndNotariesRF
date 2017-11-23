package kondratkov.advocatesandnotariesrf.maps;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class Map_coor extends Activity {
    IN in;
    private GoogleMap mapMark;
    public LatLng mark;
    public Marker marker;

    public FrameLayout asked_frameProg;
    public ProgressBar asked_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_coor);
        in = new IN();

        findViewById(R.id.map_coor_but_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map_coor.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        in = new IN();
        asked_frameProg = (FrameLayout) findViewById(R.id.map_coor_frameProg);
        asked_progressBar = (ProgressBar) findViewById(R.id.map_coor_progressBar);

        asked_frameProg.setBackgroundResource(R.color.frameOn);
        asked_frameProg.setClickable(true);
        asked_progressBar.setVisibility(ProgressBar.VISIBLE);

        if (in.get_longitude() != 0 && in.get_longitude() != 0) {
            start1();
        } else if (in.get_text().equals("")) {
            start2();
        } else {
            new FileReadTask().execute();
        }
    }

    public void stopProgressBar() {
        asked_frameProg.setBackgroundResource(R.color.frameOff);
        asked_frameProg.setClickable(false);
        asked_progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public void start1() {
        LatLng latLng = new LatLng(in.get_latitude(), in.get_longitude());
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment1 = (MapFragment) fragmentManager
                .findFragmentById(R.id.map_coor_fragment);
        mapMark = mapFragment1.getMap();
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
        mapMark.setMyLocationEnabled(true);
        // выбираем тип карты
        mapMark.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mapMark.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        BitmapDescriptor bitmapDescriptor = null;
        if(in.jut_ili_not) {
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.x_marker_jur);
        }
        else{
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.x_marker_not);
        }
        stopProgressBar();
        Marker marker = mapMark.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .title(in.get_fio_jur()));
        dropPinEffect(marker);
    }
    public void start2(){
        this.finish();
    }
    public void start3(){
        this.finish();
    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

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
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    public void start_iz_coor(String s){

        JSONObject jsonObject= null;
        try {
            jsonObject= new JSONObject(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(jsonObject.getString("status").equals("OK")){
                Log.d("qwerty", "j!@!@!@!@!@!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!jj llffff");
            }

            Log.d("qwerty", " -jsonArray- " + String.valueOf(jsonObject.getJSONArray("results").length()));
            Log.d("qwerty", " -geometry- " + String.valueOf(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry")));
            //Log.d("qwerty", " -bounds northeast- "+String.valueOf(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("bounds").getJSONObject("northeast")));
            in.set_latitude(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            in.set_longitude(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

            LatLng latLng = new LatLng(in.get_latitude(), in.get_longitude());
            FragmentManager fragmentManager = getFragmentManager();
            MapFragment mapFragment1 = (MapFragment) fragmentManager
                    .findFragmentById(R.id.map_coor_fragment);
            mapMark = mapFragment1.getMap();
            // выбираем тип карты
            mapMark.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            mapMark.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            in.set_latitude(latLng.latitude);
            in.set_longitude(latLng.longitude);
            new AsyncTaskNotary().execute();
            BitmapDescriptor bitmapDescriptor = null;
            Log.d("qwerty", "для натариуса на карте труе "+ in.get_jut_ili_not());
            if(in.get_jut_ili_not() == true) {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.x_marker_jur);
            }
            else{
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.x_marker_not);
            }
            stopProgressBar();
            Marker marker = mapMark.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(bitmapDescriptor)
                    .title(in.get_fio_jur()));
            dropPinEffect(marker);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private class FileReadTask extends AsyncTask<Void, Void, Void> {

        String textResult;

        @Override
        protected Void doInBackground(Void... params) {

            URL textUrl;

            try {
                textUrl = new URL("http://maps.google.com/maps/api/geocode/json?address="+in.get_text().replaceAll(" ", "")+"&sensor=false&language=ru");

                BufferedReader bufferReader = new BufferedReader(
                        new InputStreamReader(textUrl.openStream()));

                String StringBuffer;
                String stringText = "";
                while ((StringBuffer = bufferReader.readLine()) != null) {
                    stringText += StringBuffer;
                }
                bufferReader.close();

                textResult = stringText;
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                textResult = e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                textResult = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("qwerty", "otvet " + textResult);
            if(textResult!=null){
                start_iz_coor(textResult);
            }
            else{
                finish();
            }

            super.onPostExecute(result);
        }
    }
    class AsyncTaskNotary extends AsyncTask<Void, Integer, String> {
        // фоновая работа
        @Override
        protected String doInBackground(Void... params) {
            String str_url = "http://"+in.get_url()+"/123.notget";
            String json = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", in.get_id_jur());//String.valueOf(in.get_id()));
                jsonObject.put("X", in.get_latitude());//adin.get_password_jur());
                jsonObject.put("Y", in.get_longitude());//adin.get_password_jur());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            json = String.valueOf(jsonObject);

            return ServerSendData.sendRegData(str_url, json);
        }
        // выполняется после doInBackground, имеет доступ к UI
        protected void onPostExecute(String result) {
        }
    }
    public static class ServerSendData {

        public static String sendRegData(String stringUrl, String json_user_str) {

            String result =  null;
            try {
                URL url = new URL(stringUrl);

                URLConnection connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;

                httpConnection.setDoOutput(true);
                httpConnection.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());

                out.write(json_user_str.getBytes());

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
            } catch (MalformedURLException e) {
            } catch (IOException e1) {
            }

            return result;
        }
    }
}
