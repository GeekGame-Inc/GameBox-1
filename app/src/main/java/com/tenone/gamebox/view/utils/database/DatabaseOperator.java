/**
 * Project Name:GameBox
 * File Name:DatabaseOperator.java
 * Package Name:com.tenone.gamebox.view.utils.database
 * Date:2017-3-28����5:22:43
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * ����Ϊ�������ݿ���࣬���಻������ģ�ֻ����ֱ��ʹ�û��װʹ�á�����Ϊ���� ClassName:DatabaseOperator <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 ����5:22:43 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class DatabaseOperator {
    public static DatabaseHelper dbHelper;
    Context context;
    private volatile static DatabaseOperator intance;
    SQLiteDatabase db = null;

    /**
     * ���캯��
     */
    private DatabaseOperator(Context mycontext) {
        context = mycontext;
        // ��ʼ�����ݿ���
        dbHelper =  DatabaseHelper.getInstance( context );
    }

    public static DatabaseOperator getIntance(Context context) {
        if (intance == null) {
            synchronized (DatabaseOperator.class) {
                if (intance == null) {
                    intance = new DatabaseOperator( context );
                }
            }
        }
        return intance;
    }

    /**
     * ��������
     *
     * @param table
     * @param values
     * @param selection
     */
    public void update(String table, ContentValues values, String selection,
                       String[] args) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            db.update( table, values, selection, args );
        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ɾ������
     *
     * @param table
     * @param whereClause
     * @param whereArgs
     */
    public int delete(String table, String whereClause, String whereArgs[]) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            return db.delete( table, whereClause, whereArgs );
        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return -1;
    }

    /**
     * ɾ������
     *
     * @param tablename
     */
    public boolean deleteAll(String tablename) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            db.delete( tablename, null, null );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return true;
    }

    /**
     * ͨ��SQL��insert���������� ����ʾ���� String
     * sql="insert into tb_content_userinfo(username)values('test')";
     * insertBySql(sql);
     */
    public void insertBySQL(String sql) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            db.execSQL( sql );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ͨ��SQL��update���������� ����ʾ���� String
     * sql="update tb_content_userinfo set username='hah' where id=1";
     * updateBySQL(sql);
     */
    public void updateBySQL(String sql) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            db.execSQL( sql );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ͨ��SQL��delete���ɾ������ ����ʾ���� String
     * sql="delete from tb_content_userinfo  where id=1"; deleteBySQL(sql);
     */
    public boolean deleteBySQL(String sql) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            db.execSQL( sql );
        } catch (Exception e) {
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return true;
    }

    /**
     * ��ѯ����,����Cursor�����
     *
     * @param table
     *            ���������൱��SQL��from����Ĳ��֡�������Ƕ�����ϲ�ѯ��ô�죿�Ǿ��ö��Ž����������ֿ���
     *            ƴ��һ���ַ�����Ϊtable��ֵ ��
     * @param columns
     *            ��Ҫ��ѯ�������������൱��SQL��select����Ĳ��֡�
     * @param selection
     *            ����ѯ�������൱��SQL��where����Ĳ��֣�ʾ����id=1 //
     * @param groupBy
     *            ���൱��SQL��group by����Ĳ���
     * @param having
     *            ���൱��SQL��having����Ĳ���
     * @param orderBy
     *            ���൱��SQL��order by����Ĳ��֣�����ǵ��򣬻������������򣬿���д������������String orderBy =
     *            ��id desc, name��;
     * @param limit
     *            ��ָ��������Ĵ�С������Mysql��limit�÷���̫һ����mysql����ָ���Ӷ����п�ʼ֮��ȡ�����������硰limit
     *            100,10������������ֻ֧��һ����ֵ��
     * @return
     */
    public Cursor query(String table, String[] columns, String selection,
                        String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            // cursor=db.query(table, columns, selection, null, groupBy, having,
            // orderBy, limit);
            cursor = db.query( table, columns, selection, null, groupBy, having,
                    orderBy, limit );
            // cursor=db.query(table, columns, null, null, null, null, null,
            // limit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            /*
             * if(cursor!=null) { cursor.close(); } if(db!=null) { db.close(); }
             */
        }
        return cursor;
    }

    public Cursor query(String sql) {
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery( sql, null );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

        }
        return cursor;
    }

    /**
     * ��ѯ����
     *
     * @param tablename
     * @return
     */
    public Cursor select(String tablename, String selection) {
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query( tablename, null, selection, null, null, null,
                    null );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /*
             * if (cursor != null) { cursor.close(); } if (db != null) {
             * db.close(); }
             */
        }
        return cursor;
    }

    /**
     * ��ѯ����
     *
     * @param tablename
     * @return
     */
    public Cursor selectAll(String tablename) {
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query( tablename, null, null, null, null, null, null );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // if (cursor != null) {
            // cursor.close();
            // }
            // if (db != null) {
            // db.close();
            // }
        }
        return cursor;
    }

    /**
     * ��ѯ����
     *
     * @param table
     *            ����
     * @param columns
     *            ��ѯ�ֶ�
     * @param selection
     *            ��ѯ�������
     * @param selectionArgs
     *            ��ѯ��������
     * @param groupBy
     *            ����
     * @param having
     * @param orderBy
     *            ����
     * @return add by wxq 2012-8-4
     */
    public Cursor selectAll(String table, String[] columns, String selection,
                            String[] selectionArgs, String groupBy, String having,
                            String orderBy) {
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query( table, columns, selection, selectionArgs,
                    groupBy, having, orderBy );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return cursor;
    }

    public void closeDB(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
    }

    public void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    /**
     * ��һ��������ѯ
     *
     * @param tablename
     * @param columnname
     * @return
     */
    public Cursor selectAll(String tablename, String columnname) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query( tablename, new String[]{columnname}, null,
                null, null, null, null );

        return cursor;
    }

    public Cursor selectByKey(String tablename, String key, String value,
                              String column) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] keyvalue = new String[]{value};
        String where = key + " = ?";
        Cursor cursor = db.query( tablename, null, where, keyvalue, null, null,
                null );
        return cursor;
    }

    public Cursor selectByWhere(String tablename, String where, String[] values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query( tablename, null, where, values, null, null,
                null );
        return cursor;
    }

    public Cursor selectByKey(String tablename, String key, String value) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String where = key + "='" + value + "'";
        Cursor cursor = db
                .query( tablename, null, where, null, null, null, null );

        return cursor;
    }

    /**
     * ��������
     * @param tablename
     */
    public void insert(String tablename, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // ��ʼ����
        db.beginTransaction();
        try {
            db.insert( tablename, null, values );
            db.setTransactionSuccessful();
        } finally {
            // ��������
            db.endTransaction();
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    /**
     * ��������
     * */
    public void insert(String tablename, String[] column, String[] value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < column.length; i++) {
            cv.put( column[i], value[i] );
        }
        // ��ʼ����
        db.beginTransaction();
        try {
            db.insert( tablename, "", cv );
            db.setTransactionSuccessful();
        } finally {
            // ��������
            db.endTransaction();
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public void insert(String tablename, String[] column, String key,
                       byte[] value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put( column[0], key );
        cv.put( column[1], value );
        db.insert( tablename, "", cv );
        db.close();
    }

    public void insetAppsForTransaction(String tablename, String[] column,
                                        ArrayList<String[]> valus, boolean deleteTabel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            if (deleteTabel) {
                db.delete( tablename, null, null );
            }
            ContentValues cv = new ContentValues();
            for (int i = 0; i < valus.size(); i++) {
                String[] value = valus.get( i );
                cv.clear();
                for (int j = 0; j < column.length; j++) {
                    cv.put( column[j], value[j] );
                }
                db.insert( tablename, "", cv );
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * ɾ������
     */
    public void delete(String tablename, String column[], String value[]) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = "";
        for (int i = 0; i < column.length; i++) {
            where += column[i] + " = ?";
            if (i != column.length - 1) {
                where += "&";
            }
        }
        db.delete( tablename, where, value );
        db.close();
    }

    public void deleteBySql(String sql) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL( sql );
    }

    /**
     * ��������
     *
     * @param tablename
     * @param key
     * @param keyvalue
     * @param column
     * @param cardid
     * @param value
     */

    public void update(String tablename, String key, String keyvalue,
                       String[] column, byte[] value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = key + " = ?";
        String[] idvalue = {keyvalue};
        ContentValues cv = new ContentValues();
        // for(int i=0;i<index.length;i++){
        cv.put( column[0], keyvalue );

        cv.put( column[1], value );
        // }
        db.update( tablename, cv, where, idvalue );
        db.close();
    }

    public void update(String tablename, String key, String keyvalue,
                       String[] column, String[] value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = key + " = ?";
        String[] idvalue = {keyvalue};
        ContentValues cv = new ContentValues();
        for (int i = 0; i < column.length; i++) {
            cv.put( column[i], value[i] );
        }
        db.update( tablename, cv, where, idvalue );
        db.close();
    }
}
