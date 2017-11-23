package kondratkov.advocatesandnotariesrf.my_info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
import java.util.List;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.New_sidebar;
import kondratkov.advocatesandnotariesrf.Pay_br_yandex;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.api_classes.Order;

public class My_orders extends AppCompatActivity implements View.OnTouchListener {

    private MyAdapter mAdapter;
    private ViewPager mPager;
    private LinearLayout linearLayout, linearLayout2;

    public TextView tv_1, tv_2, tv_3;
    public ImageView iv_1, iv_2, iv_3;
    public IN in;


    Button button;

    public ListView lv;
    public List<JSONObject> listJSON  = null;
    public JSONArray jsonArrayList = null;

    public Order[] msArrayOrders;
    public  int idc_p =0;

    public boolean b1;

    String json_otv_orders =
            "{\"array\":[" +
                    "{\"num_zak\":\"Н-002322\",\"theme_zak\":\" Кейс документов 1\",\"tip_zak\":\"Пакет докуметов\",\"date_zak\":\"1.03.2015\",\"pay_sum_zak\":\"250\"}," +
                    "{\"num_zak\":\"Н-002312\",\"theme_zak\":\" Гавно вопрос\",\"tip_zak\":\"Заказ документов\",\"date_zak\":\"12.08.2015\",\"pay_sum_zak\":\"150\"}," +
                    "{\"num_zak\":\"Н-000312\",\"theme_zak\":\" 12345\",\"tip_zak\":\"Консультация по телефону\",\"date_zak\":\"10.01.2013\",\"pay_sum_zak\":\"350\"}," +
                    "{\"num_zak\":\"Н-000214\",\"theme_zak\":\" Кейс документов 2\",\"tip_zak\":\"Пакет докуметов\",\"date_zak\":\"10.08.2015\",\"pay_sum_zak\":\"250\" }]}}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_orders);



        tv_1 = (TextView)findViewById(R.id.my_orders_tv_1);
        tv_2 = (TextView)findViewById(R.id.my_orders_tv_2);
        tv_3 = (TextView)findViewById(R.id.my_orders_tv_3);

        iv_1 = (ImageView)findViewById(R.id.my_orders_iv_1);
        iv_2 = (ImageView)findViewById(R.id.my_orders_iv_2);
        iv_3 = (ImageView)findViewById(R.id.my_orders_iv_3);

        in = new IN();

        mAdapter = new MyAdapter(getSupportFragmentManager());

        //linearLayout = (LinearLayout)findViewById(R.id.lila_pay);
        linearLayout2 = (LinearLayout)findViewById(R.id.lila_pay_pager);

        mPager = (ViewPager) findViewById(R.id.pager_orders);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0); // выводим one экран

        mPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View v, float pos) {
                final float invt = Math.abs(Math.abs(pos) - 1);
                v.setAlpha(invt);
                int alpha = (int)(pos*215);
                if(mPager.getCurrentItem()==0) {

                    tv_3.setTextColor(Color.argb(40, 255, 255, 255));
                    iv_3.setBackgroundColor(Color.argb(40, 254, 179, 42));

                    tv_2.setTextColor(Color.argb(40, 255, 255, 255));
                    iv_2.setBackgroundColor(Color.argb(40, 254, 179, 42));

                    tv_1.setTextColor(Color.argb(255 , 255, 255, 255));
                    iv_1.setBackgroundColor(Color.argb(255 , 254, 179, 42));
                }

                else if(mPager.getCurrentItem()==1){

                    tv_1.setTextColor(Color.argb(40, 255, 255, 255));
                    iv_1.setBackgroundColor(Color.argb(40, 254, 179, 42));

                    tv_3.setTextColor(Color.argb(40, 255, 255, 255));
                    iv_3.setBackgroundColor(Color.argb(40, 254, 179, 42));

                    tv_2.setTextColor(Color.argb(255, 255, 255, 255));
                    iv_2.setBackgroundColor(Color.argb(255, 254, 179, 42));
                }

                else if(mPager.getCurrentItem()==2){

                    tv_1.setTextColor(Color.argb(40, 255, 255, 255));
                    iv_1.setBackgroundColor(Color.argb(40, 254, 179, 42));

                    tv_2.setTextColor(Color.argb(40, 255, 255, 255));
                    iv_2.setBackgroundColor(Color.argb(40, 254, 179, 42));

                    tv_3.setTextColor(Color.argb(255, 255, 255, 255));
                    iv_3.setBackgroundColor(Color.argb(255, 254, 179, 42));
                }
            }
        });

        findViewById(R.id.but_my_orders_vse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent1 = new Intent(My_orders.this, Pay_br_yandex.class);
                //startActivity(intent1);
                //PayActivity.startPhone(My_orders.this);
            }
        });


//        button = (Button)findViewById(R.id.button4);
//        button.setOnTouchListener(this);
//
//        linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)500));

    }

    @Override
    public void onStart(){
        super.onStart();

        in.setChoice_of_menus(2);
        in.set_context(My_orders.this);
        in.set_activity(My_orders.this);

        if(in.getOnemay())
        {}
        else
        {
            My_orders.this.finish();
        }
        lv = (ListView)findViewById(R.id.lv_orders_pay);
        new UrlConnectionTask().execute();
        //new AsyncTaskMess().execute();
    }

    public void AddList(){

       /*Order [] orders_yes = new Order[msArrayOrders.length];
        for(int i =0; i< msArrayOrders.length; i++){
            if(msArrayOrders[i].State== Order.OrderState.WaitingForPayment){
                orders_yes[i] = msArrayOrders[i];
            }
        }*/
        MyAdapterList mam = new MyAdapterList(in.get_activity(), msArrayOrders);
        lv.setAdapter(mam);
    }

    public void onClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.my_orders_but_cancel:
                My_orders.this.finish();
                break;

            case R.id.my_orders_but_menu:
                intent = new Intent(My_orders.this, New_sidebar.class);
                startActivity(intent);
                break;

            case R.id.my_orders_tv_1:
                mPager.setCurrentItem(0);
                break;

            case R.id.my_orders_tv_2:
                mPager.setCurrentItem(1);
                break;

            case R.id.my_orders_tv_3:
                mPager.setCurrentItem(3);
                break;

        }
    }


    private int mY;

    public int sum = 400;

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = My_orders.this.getResources().getDisplayMetrics();
        int px =Math.round(dp *(displayMetrics.xdpi /DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = My_orders.this.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int yY = 0;
    public int yNew =0;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            //ACTION_DOWN срабатывает при прикосновении к экрану,
            //здесь определяется начальное стартовое положение объекта:
            case MotionEvent.ACTION_DOWN:
                yY =(int)event.getY();
                mY = Y;// - lParams.topMargin;
                break;

            //ACTION_MOVE обрабатывает случившиеся в процессе прикосновения изменения, здесь
            //содержится информация о последней точке, где находится объект после окончания действия прикосновения ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(yY > event.getY()){
                    sum = (int)(sum - pxToDp((int) (yY-event.getY()))*3);
                    yY = (int) event.getY();
                    Log.d("qwerty", "> =  "+ sum);
                }else{
                    sum = (int)(sum + pxToDp((int) (event.getY()-yY))*3);
                    yY = (int) event.getY();
                    Log.d("qwerty", "< =  "+ sum);
                }

                linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)sum));
                //processMovement((int)(yY));
                break;
        }
        return true;
    }
    private void processMovement(float _y) {
        // Todo: Обработка движения., (float) 0.07
        Log.d("qwerty", "yNew = "+yNew);
        Log.d("qwerty", "_y =  "+ _y);
        Log.d("qwerty", "yY =  "+ yY);
        //yNew = yY -_y;
        Log.d("qwerty", "sum = "+sum);
        if(_y<0 && sum>50){
            sum = sum + pxToDp((int) (yY -_y)*-1/100);
        }else{
            sum = sum - pxToDp((int) (yY -_y)*-1/100);
        }
        linearLayout2.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)sum));
//            sum = sum-pxToDp((int) (yY -_y));
//
//        }else if(yNew>yY-_y && sum<800){
//            sum = sum+pxToDp((int) (yY -_y));
//            linearLayout2.setLayoutParams(
//                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)sum));
//        }else{
//
//        }

    }

    class MyAdapterList extends ArrayAdapter {

        private Order[] orders;
        private Activity context;

        public String[] arrayTheme = getResources().getStringArray(R.array.ArraySpecialization);

        public MyAdapterList(Activity context, Order[] orders) {
            super(context, R.layout.item_pay_orders, orders);
            this.context = context;
            this.orders = orders;
        }

        public void start_profile_not(int i){
            //Intent intent = new Intent(Notary_list.this, Notary_profile.class);
            //startActivity(intent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.item_pay_orders, parent, false);
            TextView tv_tip;
            TextView tv_sum;
            TextView tv_them;
            TextView tv_num;
            TextView tv_date;
            ImageView imageViewType;
            Button but_pay;
            final int idKey;

            tv_tip = (TextView) rowView.findViewById(R.id.item_pay_tv_tip);
            tv_tip.setTextSize(in.get_font_1());
            tv_sum = (TextView) rowView.findViewById(R.id.item_pay_tv_pay);
            tv_sum.setTextSize(in.get_font_1());
            tv_them = (TextView) rowView.findViewById(R.id.item_pay_tv_num_them);
            tv_them.setTextSize(in.get_font_2());
            tv_num = (TextView) rowView.findViewById(R.id.item_pay_tv_num_zak);
            tv_num.setTextSize(in.get_font_2());
            tv_date  = (TextView) rowView.findViewById(R.id.item_pay_tv_date);

            imageViewType =(ImageView)rowView.findViewById(R.id.imageViewItemPay);

            //tv_tip.setText(orders[position].ServiceType.toString());

            //tv_tip.setText(list.get(position).getString("theme_zak"));
            //tv_sum.setText(list.get(position).getString("pay_sum_zak")+" \u20BD");
            tv_sum.setText("");
            //tv_them.setText(orders[position].list.get(position).getString("theme_zak"));
            if(orders[position].ServiceType.name().equals("DocumentPacketOrder")){
                imageViewType.setImageResource(R.drawable.ain_ic_docforyout);
                tv_tip.setText("Пакет документов");
            }else if(orders[position].ServiceType.name().equals("DocumentOrder")){
                imageViewType.setImageResource(R.drawable.ain_ic_docrequest);
                tv_tip.setText("Составление документов");
            }else{
                imageViewType.setImageResource(R.drawable.ain_ic_support);
                tv_tip.setText("Платная консультация ");
            }
            tv_num.setText(orders[position].OrderNumber);
            //tv_date.setText(list.get(position).getString("date_zak"));

            but_pay = (Button)rowView.findViewById(R.id.item_pay_but);
            but_pay.setText(String.valueOf(orders[position].PaymentAmount)+" \u20BD" +"\nОплатить");
            idc_p = orders[position].Id;
            idKey = orders[position].Id;

            but_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int iio= idKey;
                    in.set_idc(idKey);
                    Intent intent1 = new Intent(My_orders.this, Pay_br_yandex.class);
                    startActivity(intent1);
                    //PayActivity.startPhone(My_orders.this);
                }
            });
            return rowView;
        }
    }

    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);

            String s  = in.get_token_type()+" "+in.get_token();
            String s1 = "http://"+in.get_url()+"/Orders/GetClientOrders";

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/Orders/GetClientOrders")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();
                String ddd ="";

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                Gson gson = new Gson();
                msArrayOrders = gson.fromJson(result, Order[].class);
                AddList();
            }catch (Exception e){}

            //start_activity();
            super.onPostExecute(result);
        }
    }

    public static class MyAdapter extends FragmentPagerAdapter {
    public MyAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.d("qwerty", "Fragment 1");
            return new My_frag_orders_cons();
            case 1:
                Log.d("qwerty", "Fragment 2");
                return new My_frag_orders_doc();
            case 2:
                Log.d("qwerty", "Fragment 3");
                return new My_frag_orders_doc_pack();
            default:
                return null;
        }
    }
}
}
