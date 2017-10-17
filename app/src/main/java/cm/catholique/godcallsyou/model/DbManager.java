package cm.catholique.godcallsyou.model;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KimAx on 03/10/2017.
 */

public class DbManager {
    private DbHelper dbHelper;
    private static DbManager singleDbManager = null;

    public static void init(Context context) {
        if (singleDbManager == null)
            singleDbManager = new DbManager(context);
    }

    public static DbManager getInstance() {
        return singleDbManager;
    }

    private DbManager(Context context) {
        dbHelper = new DbHelper(context);
    }

    private DbHelper getHelper() {
        return dbHelper;
    }

    /** Methods related to 'Texte' entity **/

    public List<Texte> getAllTextes() {
        try {
            return getHelper().getTexteDAO().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Texte>();
        }
    }

    public long createTexte(Texte texte) {
        try {
            getHelper().getTexteDAO().create(texte);
            return  texte.getId();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void removeTexte(Texte texte) {
        try {
            getHelper().getTexteDAO().delete(texte);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long updateTexte(Texte texte) {
        try {
            getHelper().getTexteDAO().update(texte);
            return texte.getId();
        } catch (SQLException e) {
            e.printStackTrace();
            return  -1;
        }
    }

    public List<Texte> getTextesByMonthNumber(int idMonth) {
        try {
            List<Texte> listTextes = getHelper().getTexteDAO().queryForAll();
            String criteria = "-" + idMonth;
            for(Texte texte : listTextes)
                if( ! texte.getJour().endsWith(criteria) )
                    listTextes.remove( texte );
            return listTextes;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Texte>();
        }
    }

    public Texte getTexteById(int id) {
        try {
            return getHelper().getTexteDAO().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Texte getTexteByJour(String jour) {
        try {
            List<Texte> listTextes = getHelper().getTexteDAO().queryForAll();
            for(Texte texte : listTextes)
                if( texte.getJour().equals(jour) )
                    return texte;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
