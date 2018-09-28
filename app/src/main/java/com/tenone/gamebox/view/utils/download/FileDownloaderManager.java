package com.tenone.gamebox.view.utils.download;

import android.content.Context;
import android.text.TextUtils;

import com.tenone.gamebox.mode.listener.OnDownloadStatusListener;
import com.tenone.gamebox.view.base.Configuration;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDeleteDownloadFileListener;
import org.wlf.filedownloader.listener.OnDeleteDownloadFilesListener;
import org.wlf.filedownloader.listener.OnRenameDownloadFileListener;

import java.util.ArrayList;
import java.util.List;

import static org.wlf.filedownloader.base.Status.DOWNLOAD_STATUS_DOWNLOADING;
import static org.wlf.filedownloader.base.Status.DOWNLOAD_STATUS_PAUSED;

public class FileDownloaderManager {

	/**
	 * 下载数据库表名
	 */
	public static final String FILEDOWNLOADERTABNAME = "tb_download_file";

	private static FileDownloaderManager instance = null;

	public static FileDownloaderManager getInstance() {
		if (instance == null) {
			instance = new FileDownloaderManager();
		}
		return instance;
	}

	/**
	 * 初始化 initFileDownloader:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author John Lie
	 * @param context
	 * @param taskSize
	 *            task数量
	 * @param retryTime
	 *            重试次数
	 * @since JDK 1.6
	 */
	public static void initFileDownloader(Context context, int taskSize,
			int retryTime) {
		FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(
				context);
		builder.configFileDownloadDir(Configuration.getDownloadpath());
		builder.configDownloadTaskSize(taskSize);
		builder.configRetryDownloadTimes(retryTime);
		builder.configDebugMode(true);
		builder.configConnectTimeout(Configuration.getDownloadTimeOut());
		FileDownloadConfiguration configuration = builder.build();
		FileDownloader.init(configuration);
	}

	/**
	 * 开始下载 startDownloadFile:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author John Lie
	 * @param url
	 * @return
	 * @since JDK 1.6
	 */
	public static boolean startDownloadFile(String url) {
		if (!TextUtils.isEmpty(url)) {
			FileDownloader.start(url);
			return true;
		}
		return false;
	}

	/**
	 * 有监听的下载 startDownFile:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param url
	 * @param listener
	 * @since JDK 1.6
	 */
	public static void startDownFile(String url,
			OnDownloadStatusListener listener) {
		if (!TextUtils.isEmpty(url)) {
			FileDownloader.start(url);
			FileDownloader.registerDownloadStatusListener(listener);
		}
	}

	/**
	 * 开始下载多个文件 startDownloadFiles:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param urls
	 * @return
	 * @since JDK 1.6
	 */
	public boolean startDownloadFiles(List<String> urls) {
		if (urls != null && urls.size() > 0) {
			FileDownloader.start(urls);
			return true;
		}
		return false;
	}

	/**
	 * 暂停某个文件的下载 pauseDownloadFile:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param url
	 * @return
	 * @since JDK 1.6
	 */
	public boolean pauseDownloadFile(String url) {
		if (!TextUtils.isEmpty(url)) {
			FileDownloader.pause(url);
			return true;
		}
		return false;
	}

	/**
	 * 有监听的暂停 pauseDownloadFile:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param url
	 * @param listener
	 * @since JDK 1.6
	 */
	public static void pauseDownloadFile(String url,
			OnDownloadStatusListener listener) {
		if (!TextUtils.isEmpty(url)) {
			FileDownloader.pause(url);
			FileDownloader.registerDownloadStatusListener(listener);
		}
	}

	/**
	 * 暂停某些文件的下载 pauseDownloadFiles:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param urls
	 * @return
	 * @since JDK 1.6
	 */
	public boolean pauseDownloadFiles(List<String> urls) {
		if (urls != null && urls.size() > 0) {
			FileDownloader.pause(urls);
		}
		return false;
	}

	/**
	 * 暂停所以文件的下载 pauseAllDownloadFiles:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public void pauseAllDownloadFiles() {
		FileDownloader.pauseAll();
	}

	/**
	 * 继续下载某个文件 continueDownloadFiles:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param url
	 * @return
	 * @since JDK 1.6
	 */
	public boolean continueDownloadFiles(String url) {
		if (!TextUtils.isEmpty(url)) {
			FileDownloader.start(url);
			return true;
		}
		return false;
	}

	/**
	 * 移除某个下载任务 moveDownloadFile:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param url
	 * @param onDeleteDownloadFileListener
	 * @return
	 * @since JDK 1.6
	 */
	public boolean moveDownloadFile(String url,
			OnDeleteDownloadFileListener onDeleteDownloadFileListener) {
		if (!TextUtils.isEmpty(url)) {
			FileDownloader.delete(url, true, onDeleteDownloadFileListener);
		}
		return false;
	}

	/**
	 * 移除所有下载任务 moveDownloadFiles:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param urls
	 * @param onDeleteDownloadFilesListener
	 * @return
	 * @since JDK 1.6
	 */
	public boolean moveDownloadFiles(List<String> urls,
			OnDeleteDownloadFilesListener onDeleteDownloadFilesListener) {
		if (urls != null && urls.size() > 0) {
			FileDownloader.delete(urls, true, onDeleteDownloadFilesListener);
		}
		return false;
	}

	/**
	 * 删除下载的文件 deleteDownloadFile:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param url
	 * @param onDeleteDownloadFileListener
	 * @return
	 * @since JDK 1.6
	 */
	public boolean deleteDownloadFile(String url,
			OnDeleteDownloadFileListener onDeleteDownloadFileListener) {
		if (!TextUtils.isEmpty(url)) {
			FileDownloader.delete(url, true, onDeleteDownloadFileListener);
			return true;
		}
		return false;
	}

	/**
	 * 删除多个文件 deleteDownloadFiles:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param urls
	 * @param onDeleteDownloadFilesListener
	 * @return
	 * @since JDK 1.6
	 */
	public boolean deleteDownloadFiles(List<String> urls,
			OnDeleteDownloadFilesListener onDeleteDownloadFilesListener) {
		if (urls != null && urls.size() > 0) {
			FileDownloader.delete(urls, true, onDeleteDownloadFilesListener);
			return true;
		}
		return false;
	}

	/**
	 * 重命名 renameDownloadFile:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @param url
	 * @param newName
	 * @param onRenameDownloadFileListener
	 * @return
	 * @since JDK 1.6
	 */
	public boolean renameDownloadFile(String url, String newName,
			OnRenameDownloadFileListener onRenameDownloadFileListener) {
		if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(newName)) {
			FileDownloader.rename(url, newName, true,
					onRenameDownloadFileListener);
			return true;
		}
		return false;
	}

	/**
	 * 获取下载文件的状态 getDownloadFilesStatus:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author John Lie
	 * @return
	 * @since JDK 1.6
	 */
	public List<DownloadFileInfo> getDownloadFilesStatus() {

		List<DownloadFileInfo> downloadFileInfos = FileDownloader
				.getDownloadFiles();
		return downloadFileInfos;
	}

	public List<DownloadFileInfo> getDownloadingFiles() {
		List<DownloadFileInfo> downloadingFileList = new ArrayList<DownloadFileInfo>();
		List<DownloadFileInfo> downloadFileInfoList = getDownloadFilesStatus();
		if (downloadFileInfoList != null && downloadFileInfoList.size() > 0) {
			for (int i = 0; i < downloadFileInfoList.size(); i++) {
				if (downloadFileInfoList.get(i).getStatus() == DOWNLOAD_STATUS_DOWNLOADING) {
					downloadingFileList.add(downloadFileInfoList.get(i));
				}
			}
		}
		return downloadingFileList;
	}

	public List<DownloadFileInfo> getPauseFiles() {
		List<DownloadFileInfo> onPauseFileList = new ArrayList<DownloadFileInfo>();
		List<DownloadFileInfo> downloadFileInfoList = getDownloadFilesStatus();
		if (downloadFileInfoList != null && downloadFileInfoList.size() > 0) {
			for (int i = 0; i < downloadFileInfoList.size(); i++) {
				DownloadFileInfo fileInfo = downloadFileInfoList.get(i);
				if (fileInfo.getStatus() == DOWNLOAD_STATUS_PAUSED) {
					onPauseFileList.add(fileInfo);
				}
			}
		}
		return onPauseFileList;
	}

}
