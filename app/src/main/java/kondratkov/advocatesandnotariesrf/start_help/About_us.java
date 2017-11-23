package kondratkov.advocatesandnotariesrf.start_help;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import kondratkov.advocatesandnotariesrf.R;

public class About_us extends AppCompatActivity {

    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.for_about_us);

        tv_version = (TextView)findViewById(R.id.textView_version);
        tv_version.setText(R.string.version);//
    }


    public void onClick(View v){
        switch (v.getId()) {
            case R.id.about_but_cancel:
                About_us.this.finish();
                break;
            case R.id.button3:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"2211107@mmka.info"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Адвокаты и нотариусы РФ: обратная связь");
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;

            case  R.id.button2:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mmka.info/"));
                startActivity(browserIntent);
                break;
        }
    }
}
