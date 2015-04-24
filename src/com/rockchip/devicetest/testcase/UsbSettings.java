package com.rockchip.devicetest.testcase;

import java.io.File;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import com.rockchip.devicetest.utils.FileUtils;
import com.rockchip.devicetest.utils.LogUtil;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class UsbSettings {

	public enum UsbMode {
		HOST("1"), SLAVE("2");

		public String value;

		private UsbMode(String value) {
			this.value = value;
		}

		public static UsbMode getMode(String mode) {
			for (UsbMode um : UsbMode.values()) {
				if (um.value.equals(mode)) {
					return um;
				}
			}
			return null;
		}
	}

	private static final String SYS_USBIDTIMER = "/sys/devices/lm0/ctl_usbidtimer";
	private static final String SYS_USBMODE = "/sys/devices/lm0/ctl_usbmode";
	private static final String SYS_USBENABLE = "/sys/class/android_usb/android0/enable";

	/**
	 * 设置成Host mode
	 */
	public static boolean setUsbHostMode() {
		LogUtil.d("UsbSetting", "=== in setUsbHostMode ===");
		File file_usbtimer = new File(SYS_USBIDTIMER);
		FileUtils.write2File(file_usbtimer, "1");

		File file_usbmode = new File(SYS_USBMODE);
		FileUtils.write2File(file_usbmode, "0");

		return true;
	}

	/**
	 * 设置成Slave mode
	 */
	public static boolean setUsbSlaveMode() {
		LogUtil.d("UsbSetting", "=== in setUsbSlaveMode ===");
		File file_usbtimer = new File(SYS_USBIDTIMER);
		FileUtils.write2File(file_usbtimer, "1");

		File file_usbmode = new File(SYS_USBMODE);
		FileUtils.write2File(file_usbmode, "1");

		try{
			Thread.sleep(1000);
		}catch (Exception e) {}
		File file_usbenable = new File(SYS_USBENABLE);
		FileUtils.write2File(file_usbenable, "0");

		try{
			Thread.sleep(200);
		}catch (Exception e) {}
		FileUtils.write2File(file_usbenable, "1");

		return true;
	}

	/**
	 * 开启ADB
	 * 
	 * @param context
	 */
	@SuppressLint("NewApi")
	public static void enableADB(Context context) {
		Settings.Global.putInt(context.getContentResolver(),
				Settings.Global.ADB_ENABLED, 1);
	}

	/**
	 * 关闭ADB
	 * 
	 * @param context
	 */
	@SuppressLint("NewApi")
	public static void disableADB(Context context) {
		Settings.Global.putInt(context.getContentResolver(),
				Settings.Global.ADB_ENABLED, 0);
	}

}
