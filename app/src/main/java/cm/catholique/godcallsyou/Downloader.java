package cm.catholique.godcallsyou;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by KimAx on 17/10/2017.
 */

public class Downloader {
    private Downloader actual;
    private MainActivity referee;
    private RequestQueue requestEngine = null;

    private String urlStringAll = "http://www.catholique.cm/godcallsyou/ajax/textes";
    private String urlStringMonth = "http://www.catholique.cm/godcallsyou/ajax/month_textes?mois=";

    public Downloader(MainActivity activity) {
        this.referee = activity;
        if(null == requestEngine)
            requestEngine = Volley.newRequestQueue(activity);
        actual = this;
    }

    public void addTarget(int target) {
        JsonObjectRequest jsonObjectRequest;
        if(-1 == target)
            jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, urlStringAll, null, referee, referee);
        else
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlStringMonth + target, null, referee, referee);
        requestEngine.add( jsonObjectRequest );
    }
}
