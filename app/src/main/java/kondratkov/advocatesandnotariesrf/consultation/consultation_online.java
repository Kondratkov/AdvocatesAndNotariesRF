package kondratkov.advocatesandnotariesrf.consultation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Sidebar;

public class consultation_online extends Activity implements View.OnTouchListener {
    ScrollView consult_scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation_online);

        consult_scrollView = (ScrollView)findViewById(R.id.consult_scrollView);
        consult_scrollView.setOnTouchListener(this);


        findViewById(R.id.my_onl_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultation_online.this.finish();
            }
        });

        findViewById(R.id.but_cons_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(consultation_online.this, Consult_phone.class));
            }
        });

        findViewById(R.id.but_cons_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(consultation_online.this, Consult_mess.class));
            }
        });

    }
    public float xX = 0;
    public float xNew = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_UP):
                if(xNew > 300){
                    Intent intent = new Intent(consultation_online.this, Sidebar.class);
                    startActivity(intent);
                }
                Log.d("qwerty", "ACTION_UP");
                break;
            case (MotionEvent.ACTION_DOWN):
                xX = event.getX();
                Log.d("qwerty", "ACTION_DOWN xX "+xX);
                break;
            case (MotionEvent.ACTION_MOVE):
                Log.d("qwerty", "ACTION_MOVE ");
                int historySize = event.getHistorySize();
                for (int i = 0; i <historySize; i++) {
                    float x = event.getHistoricalX(i);
                    processMovement(x);
                }
                float x = event.getX();
                processMovement(x);
        }
        return false;
    }
    private void processMovement(float _x) {
        // Todo: Обработка движения.
        xNew = xX -_x;
        Log.d("qwerty", "xNew " + xNew);
    }
}
