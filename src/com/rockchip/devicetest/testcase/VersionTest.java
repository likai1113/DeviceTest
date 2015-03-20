package com.rockchip.devicetest.testcase.impl;

import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.SystemProperties;

import com.rockchip.devicetest.R;
import com.rockchip.devicetest.constants.ParamConstants;
import com.rockchip.devicetest.model.TestCaseInfo;
import com.rockchip.devicetest.testcase.BaseTestCase;

public class VersionTest extends BaseTestCase {
	private String mversion;
	

	public VersionTest(Context context, Handler handler, TestCaseInfo testcase) {
		super(context, handler, testcase);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onTestInit() {
		
	}
	@Override
	public boolean onTesting() {
		// TODO Auto-generated method stub
		
		Map<String, String> attachParams = mTestCaseInfo.getAttachParams();
		mversion = attachParams.get(ParamConstants.VERSION);//从配置文件中获取版本号
		//从服务中获取版本号
		if (mversion.equals(getSystemVersion().trim())) {
			onTestSuccess("成功");
		} else {
			onTestFail("配置版本" + mversion);
		}
		return true;
	}
	public String getSystemVersion() {
		return SystemProperties.get("ro.system.version.name", "") + "."
				+ SystemProperties.get("ro.build.date.utc", "");
	}
	

}
