package cm.catholique.godcallsyou;

import java.util.List;

import cm.catholique.godcallsyou.model.Texte;

/**
 * Created by KimAx on 05/10/2017.
 */

public interface TextesDownloaderHandler {
    public void notifyTextesReady(List<Texte> textes);
}
