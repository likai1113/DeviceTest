/*******************************************************************
 * Company:     Fuzhou Rockchip Electronics Co., Ltd
 * Description:   
 * @author:     fxw@rock-chips.com
 * Create at:   2014年5月12日 下午2:08:44  
 * 
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014年5月12日      fxw         1.0         create
 *******************************************************************/

package com.rockchip.devicetest.testcase.impl;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ethernet.EthernetDevInfo;
import android.net.ethernet.EthernetManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.EthernetDataTracker;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemProperties;

import com.rockchip.devicetest.R;
import com.rockchip.devicetest.model.TestCaseInfo;
import com.rockchip.devicetest.model.TestResult;
import com.rockchip.devicetest.testcase.BaseTestCase;
import com.rockchip.devicetest.utils.StringUtils;

import android.provider.Settings;
import android.provider.Settings.System;

public class LanTest extends BaseTestCase {

	private EthernetManager mEthManager;
	private boolean hasRegister;

	private ConnectivityManager mConnectivityManageranager;

	public LanTest(Context context, Handler handler, TestCaseInfo testcase) {
		super(context, handler, testcase);
		mEthManager = (EthernetManager) mContext.getSystemService(Context.ETH_SERVICE);
		mConnectivityManageranager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	@Override
	public boolean onTesting() {
		IntentFilter ifilter = new IntentFilter(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
		mContext.registerReceiver(mEthernetReceiver, ifilter);
		hasRegister = true;
		setTestTimeout(DEFAULT_TEST_TIMEOUT);

		int ethEnabler = mEthManager.getEthState();
		if (ethEnabler == 2) {// 以太网已开启
			// boolean mConnect = (mEthManager.getEthernetConnectState() ==
			// EthernetDataTracker.ETHERNET_CONNECTED);
			//
			// if(!mConnect){//waiting
			// //onTestFail(R.string.lan_err_disconnect);
			// //return;
			// }else{
			// testEthernet();
			// }
			// NetworkInfo info =
			// mConnectivityManageranager.getActiveNetworkInfo();
			// if (info.getDetailedState()
		} else {
			boolean enabledRes = mEthManager.setEthernetEnabled(true);
			if (!enabledRes) {
				onTestFail(R.string.lan_err_enable);
			}
		}
		return true;
	}

	public boolean onTestHandled(TestResult result) {
		if (hasRegister) {
			hasRegister = false;
			mContext.unregisterReceiver(mEthernetReceiver);
		}
		return super.onTestHandled(result);
	}

	public void stop() {
		if (hasRegister) {
			hasRegister = false;
			mContext.unregisterReceiver(mEthernetReceiver);
		}
		super.stop();
	}

	/**
	 * 测试以太网
	 */
	public void testEthernet() {
		ContentResolver contentResolver = mContext.getContentResolver();

		// int useStaticIp = System.getInt(contentResolver,
		// System.ETHERNET_USE_STATIC_IP, 0);

		String ipaddress = null;
		// if (useStaticIp == 1) {
		// ipaddress = System.getString(contentResolver,
		// System.ETHERNET_STATIC_IP);
		// ipaddress += "(static)";
		// }else{
		// ipaddress = getEthInfoFromDhcp();
		// }
		if (mEthManager.isStatic()) {
			EthernetDevInfo info = mEthManager.getSavedEthConfig();
			ipaddress = info.getIpAddress();
			ipaddress += "(static)";
		} else {
			DhcpInfo info = mEthManager.getDhcpInfo();
			ipaddress = intToIp(info.ipAddress);
		}
		if (StringUtils.isEmptyObj(ipaddress)) {
			onTestFail(R.string.lan_err_ip);
		} else {
			String ipdetail = mContext.getString(R.string.lan_ip_address, ipaddress);
			onTestSuccess(ipdetail);
		}
	}

	/**
	 * 把int->ip地址
	 * 
	 * @param ipInt
	 * @return String
	 */
	private static String intToIp(int ipInt) {
		return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.').append((ipInt >> 16) & 0xff).append('.')
				.append((ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff)).toString();
	}

	// public String getEthInfoFromDhcp() {
	// String tempIpInfo;
	// String mEthIpAddress;
	// String iface = mEthManager.getEthernetIfaceName();
	//
	// tempIpInfo = SystemProperties.get("dhcp." + iface + ".ipaddress");
	// if ((tempIpInfo != null) && (!tempIpInfo.equals(""))) {
	// mEthIpAddress = tempIpInfo;
	// } else {
	// mEthIpAddress = "";
	// }
	// return mEthIpAddress;
	// }

	private final BroadcastReceiver mEthernetReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)) {
				int ethernetState = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, 0);
				if (ethernetState == EthernetManager.ETHERNET_CONNECTED) {
					testEthernet();
				}
			}
		}
	};

}
