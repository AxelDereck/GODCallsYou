package cm.catholique.godcallsyou;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;

import cm.catholique.godcallsyou.model.DbManager;
import cm.catholique.godcallsyou.model.Texte;

public class MainActivity extends AppCompatActivity implements TextesDownloaderHandler {
    private DbManager dbManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /// Initialisation de l'accès à la base de données
        DbManager.init(this);
        /// Vérification de la présence des textes du jour
        /*
        dbManager = DbManager.getInstance();
        Calendar now = Calendar.getInstance();
        int mois = now.get(Calendar.MONTH) + 1;
        int jour = now.get(Calendar.DAY_OF_MONTH);
        List<Texte> textesOfMonth = dbManager.getTextesByMonthNumber(mois);
        Texte curTexte = null;
        if (textesOfMonth.size() != 0) {
            curTexte = dbManager.getTexteByJour(jour + "-" + mois);
            if (null == curTexte)
                launchTextesDownloader();
            else
                showTexte(curTexte);
        } else
            launchTextesDownloader();
        */

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Choix du texte à afficher (Fonctionnalité à venir)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        /// Vérification de la présence des textes du jour
        dbManager = DbManager.getInstance();
        Calendar now = Calendar.getInstance();
        int mois = now.get(Calendar.MONTH) + 1;
        int jour = now.get(Calendar.DAY_OF_MONTH);
        List<Texte> textesOfMonth = dbManager.getTextesByMonthNumber(mois);
        Texte curTexte = null;
        if (textesOfMonth.size() != 0) {
            curTexte = dbManager.getTexteByJour(jour + "-" + mois);
            if (null == curTexte)
                launchTextesDownloader();
            else
                showTexte(curTexte);
        } else
            launchTextesDownloader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Dialog dialogDownload = null;
    private TextView textWaiting;

    @Override
    public void notifyTextesReady(List<Texte> textes) {
        Toast.makeText(this, "Fin du téléchargement des textes. Enregistrement des textes ...", Toast.LENGTH_LONG)
                .show();
        DbManager dbManager = DbManager.getInstance();
        if(dbManager != null) {
            for (Texte texte : textes)
                dbManager.createTexte(texte);
            Toast.makeText(this, "Enregistrement des textes effectué avec succès !", Toast.LENGTH_LONG)
                    .show();
        } else
            Toast.makeText(this, "Echec de l'enregistrement des textes ! Impossible d'accédder à la base de données !", Toast.LENGTH_LONG)
                    .show();
        if(null != dialogDownload)
            dialogDownload.dismiss();

        showTexteOfDay();
    }

    private void launchTextesDownloader() {
        /// Vérification de la connexion internet
        if(! checkInternetConnection() ) {
            Toast.makeText(this, "Pas de connexion internet ! Activez votre connexion internet et réessayer SVP !",
                    Toast.LENGTH_LONG )
                    .show();
            return;
        }

        /// si oui affichage de la fenêtre d'attente et de téléchargement
        dialogDownload = new Dialog(this);
        dialogDownload.setContentView(R.layout.waiting_fragment);
        dialogDownload.setTitle("GOD Calls You - Textes de Méditation Quotidienne");
        textWaiting = (TextView) dialogDownload.findViewById(R.id.waiting_text);
        textWaiting.setText("Téléchargement des textes ...");
        dialogDownload.show();
        try {
            TextesDownloader downloader = new TextesDownloader();
            downloader.setMonthNumber(Calendar.getInstance().get(Calendar.MONTH) + 1);
            downloader.addHandler(this);
            Toast.makeText(this, "Début du téléchargement des textes ...", Toast.LENGTH_LONG)
                    .show();
            downloader.execute();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean checkInternetConnection() {
        Toast.makeText(MainActivity.this, "Vérification de la Connexion Internet ...", Toast.LENGTH_LONG)
                .show();
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean connection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(connection)
            Toast.makeText(MainActivity.this, "Connexion Internet activée !", Toast.LENGTH_LONG)
                    .show();
        else
            Toast.makeText(MainActivity.this, "Pas de Connexion Internet !", Toast.LENGTH_LONG)
                    .show();

        return connection;
    }

    public void showTexteOfDay() {
        if(null == dbManager) {
            Calendar now = Calendar.getInstance();
            int mois = now.get(Calendar.MONTH) + 1;
            int jour = now.get(Calendar.DAY_OF_MONTH);
            Texte curTexte = dbManager.getTexteByJour(jour + "-" + mois);;
            showTexte(curTexte);
        }
    }

    public void showTexte(Texte texte) {
        TextView tvJour = (TextView) findViewById(R.id.jour);
        TextView tvTitre = (TextView) findViewById(R.id.title);
        TextView tvPrelude = (TextView) findViewById(R.id.prelude);
        TextView tvMessage = (TextView) findViewById(R.id.message);

        if(null == texte) {
            tvJour.setText("");
            tvTitre.setText("");
            tvPrelude.setText("");
            tvMessage.setText("Message indisponible !! \n Activez votre connexion internet et relancez la synchronisation SVP !\n Merci !!!");
        } else {
            tvJour.setText( texte.getJour() );
            tvTitre.setText( texte.getTitle() );
            tvPrelude.setText( texte.getPrelude() );
            tvMessage.setText( texte.getMessage() );
        }
    }
}
