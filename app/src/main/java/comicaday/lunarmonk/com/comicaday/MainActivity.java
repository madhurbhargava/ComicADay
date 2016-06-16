package comicaday.lunarmonk.com.comicaday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedpreferences = getSharedPreferences(Utility.COMIC_PREFS, Context.MODE_PRIVATE);
        String str = sharedpreferences.getString(Utility.ID_LAUNCH, null);
        if(str == null) {

            BootAndAlarmReceiver.setAlarm(this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Utility.ID_LAUNCH, "already_launched");
            editor.commit();
        }




        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_display);
        Button buttonOne = (Button) findViewById(R.id.btn_launchcomics);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                startActivity(intent);
            }
        });



    }










}
