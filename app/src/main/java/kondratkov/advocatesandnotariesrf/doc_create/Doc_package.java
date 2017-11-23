package kondratkov.advocatesandnotariesrf.doc_create;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
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

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.api_classes.PostConsultation;
import kondratkov.advocatesandnotariesrf.my_info.My_frag_orders_cons;

public class Doc_package extends FragmentActivity {

    private DocPackPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    public int code;
    public IN in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_package);

        in = new IN();
        in.set_activity(this);
        in.set_context(this);

        mPagerAdapter = new DocPackPagerAdapter(getSupportFragmentManager());

        PagerTitleStrip pagerTitleStrip =(PagerTitleStrip)findViewById(R.id.pager_title_strip);
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        pagerTitleStrip.setTextColor(getResources().getColor(R.color.color_white));
        pagerTitleStrip.setBackgroundResource(R.color.color_menubar_l);

        findViewById(R.id.doc_pack_but_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doc_package.this.finish();
            }
        });

        new UrlConnectionTask().execute();
        //new AsyncTaskMess().execute();

    }

    public class ZoomOutPageTransformer implements android.support.v4.view.ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            String s1 = in.get_token_type()+" "+in.get_token();
            String s2 = "http://"+in.get_url()+"/Consultations/GetConsultations";
            //RequestBody formBody = RequestBody.create(JSON, json_signup);

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/DocumentPackets/GetDocumentPackets")
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
            if(result!=null && code>=200 && code<300){
                in.set_json_doc_pack(result);
                mViewPager = (ViewPager) findViewById(R.id.pager_doc);
                mViewPager.setAdapter(mPagerAdapter);
                mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            }

            super.onPostExecute(result);
        }
    }

}
