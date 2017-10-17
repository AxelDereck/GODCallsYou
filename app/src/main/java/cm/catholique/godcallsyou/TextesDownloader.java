package cm.catholique.godcallsyou;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cm.catholique.godcallsyou.model.DbManager;
import cm.catholique.godcallsyou.model.Texte;

/**
 * Created by KimAx on 05/10/2017.
 */

public class TextesDownloader extends AsyncTask<Void, Void, JSONObject> {
    private final String USER_AGENT = "Mozilla/5.0";

    private ArrayList<TextesDownloaderHandler> handlers = new ArrayList<TextesDownloaderHandler>();
    private URL url;

    public void setMonthNumber(int mois) throws MalformedURLException {
        if(mois != -1)
            url = new URL("http://www.catholique.cm/godcallsyou/ajax/month_textes?mois=" + mois);
        else
            url = new URL("http://www.catholique.cm/godcallsyou/ajax/textes");
    }

    public void addHandler(TextesDownloaderHandler handler) {
        handlers.add(handler);
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        if (!isCancelled()) {
            try {
                System.out.println("Ouverture de l'adresse : " + url);
                HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
                httpUrlConn.setRequestMethod("GET");
                httpUrlConn.setRequestProperty("User-Agent", USER_AGENT);

                int responseCode = httpUrlConn.getResponseCode();
                System.out.println("Code de retour : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpUrlConn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Contenu: " + response.toString());
                return new JSONObject( response.toString() );
            } catch(IOException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        if(null != jsonObject) {
            try {
                JSONArray textes = jsonObject.getJSONArray("textes");
                List<Texte> resultTextes = new ArrayList<Texte>();

                for (int i = 0; i < textes.length(); i++) {
                    JSONObject jsonTexte = textes.getJSONObject(i);
                    Texte curTexte = new Texte();
                    curTexte.setId(jsonTexte.getInt("id"));
                    curTexte.setJour(jsonTexte.getString("jour"));
                    curTexte.setPrelude(jsonTexte.getString("title"));
                    curTexte.setMessage(jsonTexte.getString("message"));

                    resultTextes.add( curTexte );
                }
                for (TextesDownloaderHandler handler : handlers)
                    handler.notifyTextesReady( resultTextes );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
