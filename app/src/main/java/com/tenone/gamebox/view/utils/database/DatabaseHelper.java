/**
 * Project Name:GameBox
 * File Name:DatabaseHelper.java
 * Package Name:com.tenone.gamebox.view.utils.database
 * Date:2017-3-28����5:19:43
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ClassName:DatabaseHelper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 ����5:19:43 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * ���ݿ����Ƴ���
     */
    private static final String COMMON_NAME = "GameBox.db";

    /**
     * ���ݿ�汾����
     */
    private static final int DATABASE_VERSION = 1;

    private static volatile DatabaseHelper instance;


    private DatabaseHelper(Context context) {
        // �������ݿ�
        super( context, COMMON_NAME, null, DATABASE_VERSION );
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( GameNameTab.CREATE_TABLE_SQL );
        db.execSQL( GameDownloadTab.CREATE_TABLE_SQL );
        db.execSQL( WarnTab.CREATE_TABLE_SQL );
        db.execSQL( AlredyOpenServerTab.CREATE_TABLE_SQL );
   //     db.execSQL( ReservedTab.CREATE_TABLE_SQL );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * ɾ�����б�
         */
        db.execSQL( GameNameTab.DROP_TABLE_SQL );
        db.execSQL( GameDownloadTab.DROP_TABLE_SQL );
        db.execSQL( WarnTab.DROP_TABLE_SQL );
        db.execSQL( AlredyOpenServerTab.DROP_TABLE_SQL );
     //   db.execSQL( ReservedTab.DROP_TABLE_SQL );
        /**
         * ���´���
         */
        onCreate( db );
    }
}
