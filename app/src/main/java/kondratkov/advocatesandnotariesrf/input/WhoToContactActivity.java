package kondratkov.advocatesandnotariesrf.input;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kondratkov.advocatesandnotariesrf.R;

public class WhoToContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_to_contact);

        Button who_but_cancel = (Button)findViewById(R.id.who_but_cancel);
        Button who_but_call = (Button)findViewById(R.id.who_but_call);

        who_but_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:88002501107"));
                startActivity(intent);
            }
        });

        who_but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhoToContactActivity.this.finish();
            }
        });
    }
}
