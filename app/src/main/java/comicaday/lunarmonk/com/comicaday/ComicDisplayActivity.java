package comicaday.lunarmonk.com.comicaday;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ComicDisplayActivity extends AppCompatActivity {

    private final int seed = 1350;
    int id = seed;
    private String COMIC_PREFS = "comicprefs";
    String ID_COMIC_URL = "COMIC_URL_ID";
    String ID_TIME = "time";
    private WebView webview;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnectivity();
        BootAndAlarmReceiver.setAlarm(this);

        //setContentView(R.layout.activity_comic_display);
        int id = getId();
        String url = UrlHolder.getUrl(id);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_comic_display);

        this.webview = (WebView)findViewById(R.id.comicwebview);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);

        //webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(this, "Comic", "Loading...");

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {

                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }


        });
        webview.loadUrl(url);

    }

    private void checkConnectivity()
    {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected)
        {
            Toast.makeText(this, "Please connect the device to internet and launch the app again", Toast.LENGTH_LONG);
            finish();
            return;
        }

    }

    private int getId()
    {
        SharedPreferences sharedpreferences = getSharedPreferences(COMIC_PREFS, Context.MODE_PRIVATE);
        String str = sharedpreferences.getString(ID_COMIC_URL, null);
        Editor editor = sharedpreferences.edit();
        if(str == null)
        {
            editor.putString(ID_COMIC_URL, Integer.toString(seed));
            editor.commit();
            return seed;
        }

        return Integer.parseInt(str);

    }



    private void launchComics(int id) throws Exception {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlHolder.getUrl(id)));//Uri.parse(url));
        startActivity(browserIntent);
        finish();

    }

    private class PageFetcher extends AsyncTask<Void, Void, String> {
        private static final String TAG = "PageFetcher";
        public static final String SERVER_URL = "http://kylewbanks.com/rest/posts";

        HttpURLConnection urlConnection;
        @Override
        protected String doInBackground(Void... params) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("https://api.github.com/users/dmnugent80/repos");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            //Do something with the JSON string

        }


    }
}
