package kondratkov.advocatesandnotariesrf.my_info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.Pay_br_yandex;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Sidebar;
import kondratkov.advocatesandnotariesrf.api_classes.Comment;
import kondratkov.advocatesandnotariesrf.api_classes.DocumentOrder;
import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;
import kondratkov.advocatesandnotariesrf.api_classes.Order;
import kondratkov.advocatesandnotariesrf.api_classes.PostConsultation;
import kondratkov.advocatesandnotariesrf.data_theme.AllMess;
import kondratkov.advocatesandnotariesrf.data_theme.mess_data;

public class My_perep_orders_mess extends Activity {

    private static final String DEBUG_TAG = "qwerty_order_mess";

    public ListView lv_mess;
    public JSONArray jsonArrayMess = null;
    public JSONObject jsonObjectMess = null;
    public EditText etTextQues;
    public Button butQues;
    public JSONObject res = null;
    public int [] mas_view;


    public IN in;
    public Calendar date = Calendar.getInstance();
    public boolean bool_ulr_newmess = true;
    public int int_my_mess = 0;
    public String json_str;
    public Thread thread;
    TextView ask_tv_forum;

    public FrameLayout asked_frameProg;
    public ProgressBar asked_progressBar;
    public FrameLayout asked_add_frameProg;
    public ProgressBar asked_add_progressBar;
    public boolean my_yes_no = true;
    public boolean bu = true;

    public Comment[] msArraycomments;
    public ArrayList<Comment>arrayList;

    public ImageView imageView;

    public DocumentOrder documentOrder;
    public PostConsultation postConsultation;

    public String VID_MESS="", TIP_MESS="", DATE="";
    public int STATUS=0;
    public Order ORDER;
    public int selection_list=100;
    public int topOffset;

    public TextView my_orders_tv1, my_orders_tv2, my_orders_tv3, my_orders_tv5, my_orders_tv4, my_orders_tv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_orders_perep_mess);

        in = new IN();

        imageView = (ImageView)findViewById(R.id.imageView37);

        jsonArrayMess = new JSONArray();
        arrayList = new ArrayList<Comment>();

        etTextQues = (EditText)findViewById(R.id.ask_et_new_mess_c);
        lv_mess = (ListView)findViewById(R.id.ask_listViewMess_c);

        my_orders_tv1 = (TextView)findViewById(R.id.my_orders_tv1);
        my_orders_tv2 = (TextView)findViewById(R.id.my_orders_tv2);
        my_orders_tv3 = (TextView)findViewById(R.id.my_orders_tv3);
        my_orders_tv4 = (TextView)findViewById(R.id.my_orders_tv4);
        my_orders_tv5 = (TextView)findViewById(R.id.my_orders_tv5);
        my_orders_tv6 = (TextView)findViewById(R.id.my_orders_tv6);

        Button new_file = (Button)findViewById(R.id.button_new_file);

        if(in.get_place()==0){
            new_file.layout(0,0,0,9999);
        }else{
            ViewPager.LayoutParams layoutParams;
            new_file.layout(0,0,0,12);
        }

    }

    public boolean bn = true;
    public void potock(boolean b){
        bn = b;
        bu = false;
        Runnable runnable = new Runnable() {
            public void run() {
                // Переносим сюда старый код
                long endTime = System.currentTimeMillis()
                        + 6* 1000;
                while (System.currentTimeMillis() < endTime) {
                    synchronized (this) {
                        try {
                            selection_list = lv_mess.getFirstVisiblePosition();
                            View v = lv_mess.getChildAt(0);
                            topOffset = (v == null) ? 0 : v.getTop();
                            new UrlConnectionTask1().execute();
                            //new AsyncTaskM().execute();

                            if(bn){wait(endTime - System.currentTimeMillis());
                                endTime = System.currentTimeMillis() + 6 * 1000;}
                            else{
                                wait(endTime - System.currentTimeMillis());
                                endTime = System.currentTimeMillis()-10;
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(in.getOnemay()==false){
            this.finish();
        }
        bu = true;

        bool_ulr_newmess = false;

        asked_frameProg = (FrameLayout)findViewById(R.id.asked_frameProg_c);
        asked_progressBar = (ProgressBar)findViewById(R.id.asked_progressBar_c);
        asked_add_frameProg = (FrameLayout)findViewById(R.id.asked_add_frameProg_c);
        asked_add_progressBar = (ProgressBar)findViewById(R.id.asked_add_progressBar_c);

        asked_frameProg.setBackgroundResource(R.color.frameOn);
        asked_frameProg.setClickable(true);
        asked_progressBar.setVisibility(ProgressBar.VISIBLE);

        in.set_date("0");
        potock(true);
    }

    @Override
    protected void onStop(){
        super.onStop();
        potock(false);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        potock(false);
    }

    public void start_new_list(){
        int i_d=0;

        if(bool_ulr_newmess){
            bool_ulr_newmess = false;
            MyAdapterList mam = new MyAdapterList(in.get_activity(), arrayList);
            lv_mess.setAdapter(mam);
            //new AsyncTaskMess().execute();
        }else{
            if(in.get_place()==0){
                if(postConsultation.ContactMethod == PostConsultation.preferredContactMethod.Email){
                    VID_MESS = "Консультация по вопросам";
                    imageView.setImageResource(R.drawable.ain_ic_iconforum);
                    DATE = postConsultation.ConsultationDate;
                }else if (postConsultation.ContactMethod == PostConsultation.preferredContactMethod.Phone){
                    VID_MESS = "Консультация по телефону";
                    imageView.setImageResource(R.drawable.ain_ic_we_will_call_phone_consult);
                    DATE = postConsultation.ConsultationDate;
                }else{
                    VID_MESS = "Нет данных";
                }
                try {
                    TIP_MESS = postConsultation.SpecializationSector.SectorName;
                }catch (Exception e){}
                STATUS = postConsultation.GetState();
                ORDER = postConsultation.Order;
            }else{
                VID_MESS = documentOrder.GetDocumentTyp() ;
                imageView.setImageResource(R.drawable.ain_ic_docrequest);
                try {
                    TIP_MESS = documentOrder.JuristSpecialization.SectorName;
                }catch (Exception e){}

                DATE = "";
                STATUS = documentOrder.GetState();
                ORDER = documentOrder.Order;
            }

            if(msArraycomments == null){
                Log.d("qwerty", "1");
                Comment comment = new Comment();
                comment.Message = "123123";//clientQuestions.Body;
                comment.CommentType = Comment.CommentType1.Question;
                comment.AccountType = Comment.AccountTypes.Client;
                msArraycomments = new Comment[]{comment};
                MyAdapterList mam = new MyAdapterList(in.get_activity(), arrayList);
                lv_mess.setAdapter(mam);
            }else{
                MyAdapterList mam = new MyAdapterList(in.get_activity(), arrayList);
                lv_mess.setAdapter(mam);
            }
        }

            lv_mess.setSelectionFromTop(selection_list, topOffset);

        //lv_mess.smoothScrollToPosition(0);

        my_orders_tv1.setText(VID_MESS);
        my_orders_tv2.setText(TIP_MESS);
        my_orders_tv3.setText(DATE);

        switch (STATUS){
            case 0:
                my_orders_tv4.setText("Cогласования объема и стоимости правовой помощи");
                my_orders_tv5.setText("");
                my_orders_tv3.setText("");
                findViewById(R.id.my_orders_but_pay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 1:
                my_orders_tv4.setText("Согласовано, ожидается оплата");
                my_orders_tv4.setTextColor(getResources().getColor(R.color.read1));
                my_orders_tv5.setText(ORDER.PaymentAmount + " \u20BD" + " (не оплачено)");
                my_orders_tv6.setText(ORDER.OrderNumber);
                findViewById(R.id.my_orders_but_pay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        in.set_idc(ORDER.Id);
                        Intent intent1 = new Intent(My_perep_orders_mess.this, Pay_br_yandex.class);
                        startActivity(intent1);
                    }
                });
                break;
            case 2:
                my_orders_tv4.setText("Исполнение заказа");
                my_orders_tv4.setTextColor(getResources().getColor(R.color.read1));
                try{
                    my_orders_tv5.setText(ORDER.PaymentAmount + " \u20BD" + " (оплачено)");
                    my_orders_tv6.setText(ORDER.OrderNumber);
                }catch (Exception e){my_orders_tv5.setText("");}


                findViewById(R.id.my_orders_but_pay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                break;
            case 3:
                my_orders_tv4.setText("Ваш заказ готов");
                my_orders_tv4.setTextColor(getResources().getColor(R.color.read0));
                try{
                    my_orders_tv5.setText(ORDER.PaymentAmount + " \u20BD" + " (оплачено)");
                    my_orders_tv6.setText(ORDER.OrderNumber);
                }catch (Exception e){my_orders_tv5.setText("");}
                my_orders_tv5.setTextColor(getResources().getColor(R.color.read0));

                findViewById(R.id.my_orders_but_pay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
        }

       /*
       Log.d("qwerty", "STATUS WHO "+ in.get_doc_status() +" PAY = "+ in.get_doc_pay());

        if(in.get_doc_status()==0){
            my_orders_tv3.setText("Заказ в обработке");
            my_orders_tv5.setText("");
            my_orders_tv4.setText("");
            findViewById(R.id.my_orders_but_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else if(in.get_doc_status()==4) {
            my_orders_tv3.setText("Выполнен");
            my_orders_tv3.setTextColor(getResources().getColor(R.color.read0));
            my_orders_tv5.setText(in.get_doc_pay() + " \u20BD");
            my_orders_tv5.setTextColor(getResources().getColor(R.color.read0));
            my_orders_tv4.setText(in.get_doc_num());
            findViewById(R.id.my_orders_but_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(My_perep_orders_mess.this, Pay_br_yandex.class);
                    startActivity(intent1);
                }
            });
        }else if(in.get_doc_status()==2) {
            my_orders_tv3.setText("Принят");
            my_orders_tv3.setTextColor(getResources().getColor(R.color.read1));
            my_orders_tv5.setText(in.get_doc_pay() + " \u20BD" + " (не оплачено)");
            my_orders_tv4.setText(in.get_doc_num());
            findViewById(R.id.my_orders_but_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(My_perep_orders_mess.this, Pay_br_yandex.class);
                    startActivity(intent1);
                }
            });
        }else if(in.get_doc_status()==1) {
            my_orders_tv3.setText("Приянт");
            my_orders_tv3.setTextColor(getResources().getColor(R.color.read1));
            my_orders_tv5.setText("");
            my_orders_tv4.setText(in.get_doc_num());
            findViewById(R.id.my_orders_but_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(My_perep_orders_mess.this, Pay_br_yandex.class);
                    startActivity(intent1);
                }
            });
        }else if(in.get_doc_status()==5) {
            my_orders_tv3.setText("Приянт");
            my_orders_tv3.setTextColor(getResources().getColor(R.color.read1));
            my_orders_tv5.setText("Бесплатно");
            my_orders_tv4.setText(in.get_doc_num());
            findViewById(R.id.my_orders_but_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(My_perep_orders_mess.this, Pay_br_yandex.class);
                    startActivity(intent1);
                }
            });
        }else{

        }*/
    }

    class MyAdapterList extends ArrayAdapter {

        private ArrayList<Comment> comments1;
        private Activity context;

        public String[] arrayTheme = getResources().getStringArray(R.array.ArraySpecialization);

        public MyAdapterList(Activity context, ArrayList<Comment>comments) {
            super(context, R.layout.ask_item_user, comments);
            this.context = context;
            this.comments1 = comments;
            mas_view = new int[comments.size()];
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;
            CharSequence dateISNow = DateFormat.format("dd.MM", System.currentTimeMillis());
            CharSequence dateJSON = null;

            if(comments1.get(position).AccountType == Comment.AccountTypes.Client){
                rowView = inflater.inflate(R.layout.ask_item_user, parent, false);

                TextView tv_mess = (TextView) rowView.findViewById(R.id.mess_user_tv);
                tv_mess.setTextSize(in.get_font_1());
                TextView tv_mess_date = (TextView) rowView.findViewById(R.id.mess_user_tv_date);
                tv_mess_date.setTextSize(in.get_font_2());
                TextView tv_nik_user_mess = (TextView) rowView.findViewById(R.id.mess_user_tv_nik);
                tv_nik_user_mess.setTextSize(in.get_font_2());

                tv_mess.setText(comments1.get(position).Message);

                tv_mess_date.setText(in.dateDisplay(comments1.get(position).Date));

                tv_nik_user_mess.setText("Вы");//list.get(position).get_name());
                //icon_user_mess.setBackgroundResource(Integer.parseInt(list.get(position).getString("iconuser")));
            } else{
                rowView = inflater.inflate(R.layout.ask_item_jur, parent, false);

                ImageView icon_mess_jur = (ImageView)rowView.findViewById(R.id.mess_jur_icon);
                TextView tv_surmname_mess_jur=(TextView) rowView.findViewById(R.id.mess_jur_tv_surmname);
                tv_surmname_mess_jur.setTextSize(in.get_font_2());
                TextView tv_name_mess_jur=(TextView)rowView.findViewById(R.id.mess_jur_tv_name);
                TextView tv_mess_jur=(TextView)rowView.findViewById(R.id.mess_jur_tv);
                tv_mess_jur.setTextSize(in.get_font_1());
                TextView tv_mess_jur_date=(TextView)rowView.findViewById(R.id.mess_jur_tv_date);
                tv_mess_jur_date.setTextSize(in.get_font_2());
                Button but_icon = (Button)rowView.findViewById(R.id.mess_jur_but_icon);
                mas_view[position] = but_icon.getId();

                //tv_mess_jur_date.setText(list.get(position).getString("date"));

                tv_surmname_mess_jur.setText(comments1.get(position).From);

                tv_mess_jur_date.setText(in.dateDisplay(comments1.get(position).Date));

                //tv_name_mess_jur.setText(list.get(position).getString("namejur"));
                tv_mess_jur.setText(comments1.get(position).Message);
                //icon_mess_jur.setBackgroundResource(Integer.parseInt(list.get(position).getString("iconjur")));

                but_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int iidj = 0;
                        for(int i=0; i<position+1; i++){
                            if(v.getId() == mas_view[i]){
                                iidj = i;
                            }
                        }
                        in.set_id_jur(msArraycomments[position].Serviceld);

                        //Intent intent = new Intent(AskAQuestion.this,Profile_Jur.class);
                        //startActivity(intent);
                    }
                });
            }
            return rowView;
        }
    }

    public void stopProgressBar(){
        asked_frameProg.setBackgroundResource(R.color.frameOff);
        asked_frameProg.setClickable(false);
        asked_progressBar.setVisibility(ProgressBar.INVISIBLE);

        if(my_yes_no = true) {
            asked_add_frameProg.setBackgroundResource(R.color.frameOff);
            asked_add_frameProg.setClickable(false);
            asked_add_progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    public void onClickMess(View v){
        switch (v.getId()){
            case R.id.ask_but_cancel_c:
                My_perep_orders_mess.this.finish();
                break;
            case R.id.ask_ibut_add_mess_c:

                bool_ulr_newmess = true;

                in.set_text(String.valueOf(etTextQues.getText()));
                etTextQues.setText("");
                asked_add_frameProg.setBackgroundResource(R.color.frameOn);
                asked_add_frameProg.setClickable(true);
                asked_add_progressBar.setVisibility(ProgressBar.VISIBLE);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                JSONObject json_st = new JSONObject();
                try {
                    json_st.put("MessageBody", in.get_text());//String.valueOf(in.get_id()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json_str = String.valueOf(json_st);

                new UrlConnectionTaskNewMess().execute(json_str);
                //new AsyncTaskMess().execute();
                break;
        }
    }

    class UrlConnectionTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String result = "";
            String URL = "";
            if(in.get_place()==0){
                URL = "http://"+in.get_url()+"/Consultations/GetConsultation/";
            }else{
                URL = "http://"+in.get_url()+"/DocumentOrders/GetDocumentOrder/";
            }
            OkHttpClient client = new OkHttpClient();
            String s= in.get_token_type()+" "+in.get_token();
            String s1=URL+in.get_idt();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
            //RequestBody formBody = RequestBody.create(JSON, json_signup);
            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url(URL+in.get_idt())
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
            if(result!=null){
                if(in.get_place()==0){
                    postConsultation = gson.fromJson(result, PostConsultation.class);
                    msArraycomments = postConsultation.Comments;
                }else {
                    documentOrder = gson.fromJson(result, DocumentOrder.class);
                    msArraycomments = documentOrder.Comments;
                }

                arrayList = new ArrayList<Comment>();
                for(int i = 0; i<msArraycomments.length; i++){
                    arrayList.add(msArraycomments[i]);
                }
                start_new_list();
            }

            super.onPostExecute(result);
        }
    }

    public int code =0;
    class UrlConnectionTaskNewMess extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            String URL ="";
            if(in.get_place()==0){
                URL = "http://"+in.get_url()+"/Consultations/AddCommentOne/";
            }else{
                URL = "http://"+in.get_url()+"/DocumentOrders/AddCommentOne/";
            }

            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            String s= in.get_token_type()+" "+in.get_token();

            //RequestBody formBody = RequestBody.create(JSON, json_signup);
            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url(URL+in.get_idt())
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
            if(code>200&&code<300&&result!=null){
                Gson gson = new Gson();
                selection_list = arrayList.size()-2;
                Comment comment = gson.fromJson(result, Comment.class);
                arrayList.add(comment);
                start_new_list();
            }
            //ClientQuestion clientQuestion = gson.fromJson(result, ClientQuestion.class);
            //msArrayclientQuestions = gson.fromJson(result, ClientQuestion[].class);
            // msArrayclientQuestions = new ClientQuestion[1];
            //msArrayclientQuestions[0] = clientQuestion;
            // start_new_list();
            super.onPostExecute(result);
        }
    }
}