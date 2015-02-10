package com.rockchip.devicetest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.rockchip.devicetest.utils.LogUtil;

import android.content.Context;
import android.os.Environment;

public class ConfigFinder {

	/**
	 * 查找配置文件
	 * 
	 * @return
	 */
	public static File findConfigFile(String filename) {
		File existedFile=null;
		if (filename == null) {
			return null;
		} else {
			// File file=new File("/mnt/usb_storage/sda1/", filename);
			File f = new File("/mnt/usb_storage/");
			File[] fileInF = f.listFiles(); // 得到f文件夹下面的所有文件。
			if (fileInF != null && fileInF.length != 0) {
				for (File file : fileInF) {
					String name = file.getName();
					 existedFile = new File("/mnt/usb_storage/" + name,filename);
					 break;
				}

			}
			return existedFile;
			// File existedFile = null;
			// // String sdDir = "/mnt/usb_storage/sda1/";mnt/usb_storage
			// existedFile = new File("/mnt/usb_storage/sda1//",file);
			// if (existedFile.exists()) {
			// return existedFile;
			// }
			// return null;
			//
			// //0.Absolute
			//
			// if(file.startsWith("/")||file.startsWith("\\")){
			// return new File(file);
			// }
			//
			// File existedFile = null;
			//
			// //1.External SDCard
			// File sdDir = Environment.getSecondVolumeStorageDirectory();
			// existedFile = new File(sdDir, file);
			// if(existedFile.exists()){
			// return existedFile;
			// }
			//
			// //2.USB
			// List<String> usbList = getAliveUsbPath();
			// for(String usb : usbList){
			// existedFile = new File(getSubUsbPath(usb), file);
			// if(existedFile.exists()){
			// return existedFile;
			// }
			// }
			//
			// //3.Internal SDCard
			// sdDir = Environment.getExternalStorageDirectory();
			// existedFile = new File(sdDir, file);
			// if(existedFile.exists()){
			// return existedFile;
			// }
			// Not Found
			// return null;
		}
	}

	private static String getSubUsbPath(String usbPath) {
		Process process;
		String temp;
		try {
			process = Runtime.getRuntime().exec("/system/bin/ls " + usbPath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((temp = reader.readLine()) != null) {
				if (temp.startsWith("udisk") && !temp.equals("udisk")) {
					usbPath += "/" + temp;
					reader.close();
					process.destroy();
					return usbPath;
				}
			}
			return usbPath;
		} catch (IOException e) {
			e.printStackTrace();
			return usbPath;
		}
	}

	/**
	 * 是否存在此配置文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean hasConfigFile(String file) {
		File searchFile = findConfigFile(file);
		boolean isExisted = searchFile != null && searchFile.exists();
		return isExisted;
	}

	/**
	 * 获取已经挂载的U盘
	 * 
	 * @return
	 */
	public static List<String> getAliveUsbPath() {
		List<String> usbList = new ArrayList<String>();
		usbList.add("/mnt/usb_storage/sda1/");
		// if(Environment.MEDIA_MOUNTED.equals(Environment.getHostStorage_Extern_0_State())){
		// String udisk0 =
		// Environment.getHostStorage_Extern_0_Directory().getAbsolutePath();
		// usbList.add(udisk0);
		// }
		// if(Environment.MEDIA_MOUNTED.equals(Environment.getHostStorage_Extern_1_State())){
		// String udisk1 =
		// Environment.getHostStorage_Extern_1_Directory().getAbsolutePath();
		// usbList.add(udisk1);
		// }
		// if(Environment.MEDIA_MOUNTED.equals(Environment.getHostStorage_Extern_2_State())){
		// String udisk2 =
		// Environment.getHostStorage_Extern_2_Directory().getAbsolutePath();
		// usbList.add(udisk2);
		// }
		// if(Environment.MEDIA_MOUNTED.equals(Environment.getHostStorage_Extern_3_State())){
		// String udisk3 =
		// Environment.getHostStorage_Extern_3_Directory().getAbsolutePath();
		// usbList.add(udisk3);
		// }
		// if(Environment.MEDIA_MOUNTED.equals(Environment.getHostStorage_Extern_4_State())){
		// String udisk4 =
		// Environment.getHostStorage_Extern_4_Directory().getAbsolutePath();
		// usbList.add(udisk4);
		// }
		// if(Environment.MEDIA_MOUNTED.equals(Environment.getHostStorage_Extern_5_State())){
		// String udisk5 =
		// Environment.getHostStorage_Extern_5_Directory().getAbsolutePath();
		// usbList.add(udisk5);
		// }
		return usbList;
	}

}
