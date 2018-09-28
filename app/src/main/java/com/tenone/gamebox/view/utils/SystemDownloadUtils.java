package com.tenone.gamebox.view.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

/**
 * Created by Eddy on 2018/3/1.
 */

public class SystemDownloadUtils {
    //������
    private DownloadManager downloadManager;
    //������
    private Context mContext;
    //���ص�ID
    private long downloadId;

    public SystemDownloadUtils(Context context){
        this.mContext = context;
    }

    //����apk
    public void downloadAPK(String url, String name) {
        //������������
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //�ƶ�����������Ƿ���������
        request.setAllowedOverRoaming(false);
        //��֪ͨ������ʾ��Ĭ�Ͼ�����ʾ��
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(name);
        request.setDescription(name);
        request.setVisibleInDownloadsUi(true);
        //�������ص�·��
        request.setDestinationInExternalPublicDir( Environment.getExternalStorageDirectory().getAbsolutePath() , name);
        //��ȡDownloadManager
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //����������������ض��У��������ض��к��������񷵻�һ��long�͵�id��ͨ����id����ȡ�������������񡢻�ȡ���ص��ļ��ȵ�
        downloadId = downloadManager.enqueue(request);
        //ע��㲥�����ߣ���������״̬
        mContext.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //�㲥�������صĸ���״̬
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };


    //�������״̬
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //ͨ�����ص�id����
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //������ͣ
                case DownloadManager.STATUS_PAUSED:
                    break;
                //�����ӳ�
                case DownloadManager.STATUS_PENDING:
                    break;
                //��������
                case DownloadManager.STATUS_RUNNING:
                    break;
                //�������
                case DownloadManager.STATUS_SUCCESSFUL:
                    //������ɰ�װAPK
                    installAPK();
                    break;
                //����ʧ��
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "����ʧ��", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        c.close();
    }

    //���ص����غ�ִ�а�װ
    private void installAPK() {
        //��ȡ�����ļ���Uri
        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downloadId);
        if (downloadFileUri != null) {
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            mContext.unregisterReceiver(receiver);
        }
    }
}
