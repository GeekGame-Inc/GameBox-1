package com.tenone.gamebox.view.utils.database;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmHelper {

    public static final String DB_NAME = "GameBox.realm";

    public static void init(Context context) {
        Realm.init( context );
        File file = context.getDatabasePath( "realm" );
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name( RealmHelper.DB_NAME )
                .deleteRealmIfMigrationNeeded()
                .directory( file )
                .build();
        Realm.setDefaultConfiguration( configuration );
    }


    //���������ʱ����������첽�߳�ִ��
    public  static <T extends RealmObject> void addObject(T t) {
       Realm.getDefaultInstance().executeTransaction( realm -> realm.copyToRealmOrUpdate( t ) );
    }

    public static <T extends RealmObject> void deleteObject(Class c, T t) {
        RealmResults<T> list = Realm.getDefaultInstance().where( c ).findAll();
       Realm.getDefaultInstance().executeTransaction( realm -> list.deleteFromRealm( list.indexOf( t ) ) );
    }

    public static <T extends RealmObject> void updateObject(T t) {
       Realm.getDefaultInstance().executeTransaction( realm -> {
            realm.copyToRealmOrUpdate( t );
        } );
    }

    public static <T extends RealmObject> RealmObject queryObjectByString(String key, String value, Class c) {
        return (T)Realm.getDefaultInstance().where( c ).equalTo( key, value ).findFirst();
    }

    public static <T extends RealmObject> RealmObject queryObjectByInt(String key, int value, Class c) {
        return (T)Realm.getDefaultInstance().where( c ).equalTo( key, value ).findFirst();
    }

    public static <T extends RealmObject> List<T> queryObjectAll(Class c) {
        RealmResults<T> realmResults =Realm.getDefaultInstance().where( c ).findAll();
        List<T> array = new ArrayList<T>();
        for (T t : realmResults) {
            array.add( t );
        }
        return array;
    }
}
