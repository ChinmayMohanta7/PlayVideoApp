package com.mobiotics.playvideoapp.model.repository;

import com.mobiotics.playvideoapp.model.Highlight;

import java.util.List;

import io.realm.Realm;

public class DBHelper {
    private Realm realm;
    private static final DBHelper INSTANCE = new DBHelper();

    public static DBHelper getInstance() {
        return INSTANCE;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    private DBHelper() {
        realm=Realm.getDefaultInstance();
    }

    public void savehighlights(List<Highlight> highlights){
        realm.beginTransaction();
        for (Highlight highlight:highlights){
            realm.copyToRealmOrUpdate(highlight);
        }
        realm.commitTransaction();
    }

    public List<Highlight> getHighlights(){
        return realm.where(Highlight.class).findAll();
    }

    public void setHighlightLastDuration(int position,long duration){
        realm.beginTransaction();
        Highlight highlight=getHighlights().get(position);
        highlight.setDuration(duration);
        realm.commitTransaction();
    }
}
