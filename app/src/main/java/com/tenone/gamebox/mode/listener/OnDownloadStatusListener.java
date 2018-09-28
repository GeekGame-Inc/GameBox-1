package com.tenone.gamebox.mode.listener;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;


public abstract class OnDownloadStatusListener implements
		OnFileDownloadStatusListener {

	
	@Override
	public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
		onFileDownCompleted(downloadFileInfo);
	}

	
	@Override
	public void onFileDownloadStatusDownloading(
			DownloadFileInfo downloadFileInfo, float downloadSpeed,
			long remainingTime) {
		onFileDownloading(downloadFileInfo, downloadSpeed, remainingTime);
	}

	
	
	@Override
	public void onFileDownloadStatusFailed(String arg0,
			DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason arg2) {
		onFileDownFailed(arg0, downloadFileInfo, arg2);
	}

	
	@Override
	public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
		
	}

	
	@Override
	public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {
		
	}

	
	@Override
	public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
		
	}

	@Override
	public void onFileDownloadStatusWaiting(DownloadFileInfo arg0) {

	}

	public abstract void onFileDownCompleted(DownloadFileInfo downloadFileInfo);

	public abstract void onFileDownloading(DownloadFileInfo downloadFileInfo,
			float downloadSpeed, long remainingTime);

	public abstract void onFileDownFailed(String arg0,
			DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason arg2);

}
