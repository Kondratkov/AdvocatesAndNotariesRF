package kondratkov.advocatesandnotariesrf.doc_create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.consultation.Consult_phone_time;
import kondratkov.advocatesandnotariesrf.start_help.Start_activity_no_login;

public class Vid_create_doc extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vid_create_doc);

        findViewById(R.id.but_doc_create_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vid_create_doc.this.finish();
            }
        });

        findViewById(R.id.but_doc_create_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Vid_create_doc.this, create_document.class));
            }
        });

        findViewById(R.id.but_doc_create_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Vid_create_doc.this, Doc_package.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IN in = new IN();
        if(in.getOnemay()){

        }else{
            Intent intent = new Intent(Vid_create_doc.this, Start_activity_no_login.class);
            startActivity(intent);
            Vid_create_doc.this.finish();
        }
    }
}
