/** 
 * Project Name:GameBox 
 * File Name:PermissionModel.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-4-26����2:36:25 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import android.Manifest;

/**
 * ClassName:PermissionModel <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-26 ����2:36:25 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class PermissionModel {
	public static PermissionModel instance;

	public static PermissionModel getInstance() {
		if (instance == null) {
			instance = new PermissionModel();
		}
		return instance;
	}

	private String readFilePermission = Manifest.permission.READ_EXTERNAL_STORAGE;// ��ȡ�ⲿ�洢
	private String writeFilePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;// д���ⲿ�洢
	private String mountUnmountFilePermission = Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS;// ��SDCard�д�����ɾ���ļ�Ȩ��

	/**
	 * ��ȡ�ⲿ�洢
	 */
	public String getReadFilePermission() {
		return readFilePermission;
	}

	public void setReadFilePermission(String readFilePermission) {
		this.readFilePermission = readFilePermission;
	}

	/**
	 * д���ⲿ�洢
	 */
	public String getWriteFilePermission() {
		return writeFilePermission;
	}

	public void setWriteFilePermission(String writeFilePermission) {
		this.writeFilePermission = writeFilePermission;
	}

	/**
	 * ��SDCard�д�����ɾ���ļ�Ȩ��
	 */
	public String getMountUnmountFilePermission() {
		return mountUnmountFilePermission;
	}

	public void setMountUnmountFilePermission(String mountUnmountFilePermission) {
		this.mountUnmountFilePermission = mountUnmountFilePermission;
	}

}
