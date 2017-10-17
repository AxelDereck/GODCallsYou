package cm.catholique.godcallsyou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by KimAx on 03/10/2017.
 */

@DatabaseTable(tableName = "texte")
public class Texte {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "jour", canBeNull = false)
    private String jour;
    @DatabaseField(columnName = "title", canBeNull = false)
    private String title;
    @DatabaseField(columnName = "prelude", canBeNull = true)
    private String prelude;
    @DatabaseField(columnName = "message", canBeNull = false)
    private String message;

    public Texte() {}

    public Texte(String jour) {
        this.jour =  jour;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrelude() {
        return prelude;
    }

    public void setPrelude(String prelude) {
        this.prelude = prelude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}