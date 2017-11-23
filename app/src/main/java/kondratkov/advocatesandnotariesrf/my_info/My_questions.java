package kondratkov.advocatesandnotariesrf.my_info;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import kondratkov.advocatesandnotariesrf.AddQuestion;
import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.New_sidebar;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Sidebar;
import kondratkov.advocatesandnotariesrf.api_classes.ClientQuestion;

public class My_questions  extends FragmentActivity {

    private MyAdapter mAdapter;
    private ViewPager mPager;

    public TextView tv_1, tv_2;
    public ImageView iv_1, iv_2;
    public IN in;

    public int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_questions);

        tv_1 = (TextView)findViewById(R.id.my_quest_tv_1);
        tv_2 = (TextView)findViewById(R.id.my_quest_tv_2);

        iv_1 = (ImageView)findViewById(R.id.my_quest_iv_1);
        iv_2 = (ImageView)findViewById(R.id.my_quest_iv_2);

        in = new IN();

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0); // выводим one экран

        mPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View v, float pos) {
                final float invt = Math.abs(Math.abs(pos) - 1);
                v.setAlpha(invt);
                int alpha = (int)(pos*215);
                if(mPager.getCurrentItem()==0) {

                    tv_2.setTextColor(Color.argb(40, 255, 255, 255));
                    iv_2.setBackgroundColor(Color.argb(40, 254, 179, 42));

                    tv_1.setTextColor(Color.argb(255 , 255, 255, 255));
                    iv_1.setBackgroundColor(Color.argb(255 , 254, 179, 42));
                }

                else if(mPager.getCurrentItem()==1){

                    tv_1.setTextColor(Color.argb(40, 255, 255, 255));
                    iv_1.setBackgroundColor(Color.argb(40, 254, 179, 42));

                    tv_2.setTextColor(Color.argb(255, 255, 255, 255));
                    iv_2.setBackgroundColor(Color.argb(255, 254, 179, 42));}
                /*tv_1.setTextColor(Color.argb(alpha + 40, 255, 255, 255));
                iv_1.setBackgroundColor(Color.argb(alpha + 40, 254, 179, 42));

                tv_2.setTextColor(Color.argb((int) (215 - alpha) + 40, 255, 255, 255));
                iv_2.setBackgroundColor(Color.argb((int) (215 - alpha) + 40, 254, 179, 42));*/
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        if(in.getOnemay())
        {}
        else
        {
            My_questions.this.finish();
        }

        in.setChoice_of_menus(1);
        in.set_context(My_questions.this);
        in.set_activity(My_questions.this);
    }

    public void onClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.my_quest_but_close:
                My_questions.this.finish();
                break;

            case R.id.my_quest_but_menu:
                intent = new Intent(My_questions.this, New_sidebar.class);
                startActivity(intent);
                break;

            case R.id.my_quest_tv_1:
                mPager.setCurrentItem(0);
                break;

            case R.id.my_quest_tv_2:
                mPager.setCurrentItem(1);
                break;
        }
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Log.d("qwerty", "Fragment 1");
                    return new My_frag_quest_private();
                case 1:
                    Log.d("qwerty", "Fragment 2");
                    return new My_frag_quest_forum();//forum();
                default:
                    return null;
            }
        }
    }
}

