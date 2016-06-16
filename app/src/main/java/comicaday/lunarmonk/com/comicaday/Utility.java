package comicaday.lunarmonk.com.comicaday;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by madhur on 16/06/16.
 */
public class Utility {

    public static String COMIC_PREFS = "comicprefs";
    public static String ID_COMIC_URL = "COMIC_URL_ID";
    public static String ID_LAUNCH = "launch";
    public static String ID_TIME = "time";
    public static final int SEED = 1350;
    public static final String URL_PAGE_COLLECTION = "http://lunarmonk.net16.net/calvinpages.json";

    public static boolean checkConnectivity(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }

    // Using HTTP_NOT_MODIFIED
    public static boolean Changed(String url){
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET THE LAST MODIFIED TIME
    public static long LastModified(String url) throws Exception
    {
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        long date = con.getLastModified();

        if (date == 0)
            System.out.println("No last-modified information.");
        else
            System.out.println("Last-Modified: " + new Date(date));

        return date;
    }
}
