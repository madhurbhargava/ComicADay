package comicaday.lunarmonk.com.comicaday;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DisplayActivity extends AppCompatActivity {

    private WebView webview;
    private ProgressDialog progressBar;

    int id = Utility.SEED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isConnected = Utility.checkConnectivity(this);
        if(isConnected)
        {
            fetchFileList();
            setContentView(R.layout.activity_comic_display);

        }
        else
        {
            Toast.makeText(this, getString(R.string.error_connectivity), Toast.LENGTH_LONG);
        }

    }

    private void fetchFileList()
    {
        FetchPages pages=  new FetchPages();
        pages.execute();
    }

    private class FetchPages extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String comicurl = null;
            try {
                URL url = new URL(Utility.URL_PAGE_COLLECTION);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(1000);
                InputStream stream = urlConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(stream));
                String line = "";
                String result = "";
                while((line = bufferedReader.readLine()) != null)
                    result += line;

                stream.close();

                int id = getId();

                JSONObject jsonObject = new JSONObject(result);
                comicurl = jsonObject.getString(Integer.toString(id));
            } catch (Exception ex) {
                return null;
            }
            return comicurl;
        }

        @Override
        protected void onPostExecute(String result) {
            loadComic(result);
        }

        @Override
        protected void onPreExecute() {

        }


    }

    private void loadComic(String url)
    {

        this.webview = (WebView)findViewById(R.id.comicwebview);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);


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

    private int getId()
    {
        SharedPreferences sharedpreferences = getSharedPreferences(Utility.COMIC_PREFS, Context.MODE_PRIVATE);
        String str = sharedpreferences.getString(Utility.ID_COMIC_URL, null);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(str == null)
        {
            editor.putString(Utility.ID_COMIC_URL, Integer.toString(Utility.SEED));
            editor.commit();
            return Utility.SEED;
        }

        return Integer.parseInt(str);

    }


}
