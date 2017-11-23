package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import kondratkov.advocatesandnotariesrf.advocates.Advocate_filter;
import kondratkov.advocatesandnotariesrf.start_help.Start_activity_no_login;

public class Form_advocates extends Activity {
    IN in;

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_dialog_form_advocate);
        in = new IN();

        findViewById(R.id.start_dialog_form_but_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Form_advocates.this, Advocate_filter.class));
            }
        });
        findViewById(R.id.start_dialog_form_but_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in.set_add_quest_tip(1);
                startActivity(new Intent(Form_advocates.this, kondratkov.advocatesandnotariesrf.AddQuestion.class));
            }
        });
        findViewById(R.id.start_dialog_form_but_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Form_advocates.this, kondratkov.advocatesandnotariesrf.consultation.consultation_online.class));
            }
        });
        findViewById(R.id.start_dialog_form_but_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Form_advocates.this, kondratkov.advocatesandnotariesrf.doc_create.Vid_create_doc.class));
            }
        });

        findViewById(R.id.start_dialog_form_but_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Form_advocates.this, kondratkov.advocatesandnotariesrf.start_help.Start_activity_no_login.class));
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();

        if(in.getOnemay()){

        }else{
            Intent intent = new Intent(Form_advocates.this, Start_activity_no_login.class);
            startActivity(intent);
            Form_advocates.this.finish();
        }
        //OneStart();
    }
}
