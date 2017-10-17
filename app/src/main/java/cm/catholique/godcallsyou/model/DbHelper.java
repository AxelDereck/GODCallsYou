package cm.catholique.godcallsyou.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by KimAx on 03/10/2017.
 */

public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static String DB_NAME = "gcy_db.sqlite";
    private static int DB_VERSION = 3;

    private Context context;

    private Dao<Texte, Integer> texteDAO;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Texte.class);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Texte.class, true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public Dao<Texte, Integer> getTexteDAO() {
        if(null == texteDAO)
            try {
                texteDAO = getDao(Texte.class);
            } catch (SQLException ex) {
                ex.printStackTrace();
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        return texteDAO;
    }

    public void setTexteDAO(Dao<Texte, Integer> texteDAO) {
        this.texteDAO = texteDAO;
    }
}