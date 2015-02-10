/*******************************************************************
* Company:     Fuzhou Rockchip Electronics Co., Ltd
* Description:   
* @author:     fxw@rock-chips.com
* Create at:   2014年5月13日 上午9:27:11  
* 
* Modification History:  
* Date         Author      Version     Description  
* ------------------------------------------------------------------  
* 2014年5月13日      fxw         1.0         create
*******************************************************************/   

package com.rockchip.devicetest.testcase.impl;

import java.util.HashMap;
import java.util.Map;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.TextView;


import com.rockchip.devicetest.IndexActivity;
import com.rockchip.devicetest.R;
import com.rockchip.devicetest.model.TestCaseInfo;
import com.rockchip.devicetest.model.TestResult;
import com.rockchip.devicetest.testcase.BaseTestCase;

import com.rockchip.devicetest.utils.SystemInfoUtils;
import com.rockchip.devicetest.constants.ParamConstants;
import com.rockchip.devicetest.utils.LogUtil;

public class FlashTest extends BaseTestCase {
	
	private LayoutInflater mLayoutInflater;
	private View mView;
	private WindowManager mWindowManager;
	private KeyguardLock kl;
    private AlertDialog mDialog;
    private Map<String, String> mAttachParams;
    private Context mContext;
    public FlashTest(Context context, Handler handler, TestCaseInfo testcase) {
        super(context, handler, testcase);
        mLayoutInflater = LayoutInflater.from(context);
        mAttachParams = testcase.getAttachParams();
        mContext = context;
    }

    @Override
    public void onTestInit() {
		super.onTestInit();

		mWindowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        KeyguardManager km = (KeyguardManager)mContext.getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("keyLock");
		
	}

    @Override
    public boolean onTesting() {
        if(mDialog!=null)
        {
            mDialog.show();
            return true;
        }
        kl.disableKeyguard(); 
        final boolean test_ret;
        final String flashspace = mAttachParams.get(ParamConstants.STORAGE_SIZE);
        long config_size = Long.parseLong(flashspace);
        final long flash_size = SystemInfoUtils.getFlashSize(mContext);
        final String detal = "\t flash大小:" + flash_size/1024/1024/1024 + "G"+",\t   配置大小:" + config_size + "G";
        LogUtil.d(detal);
        config_size = config_size * 1024 * 1024 * 1024;
        if((Math.abs(flash_size - config_size)) < 1024*1024*100)
        {
            test_ret = true;
        }
        else
        {
            test_ret = false;
        }
        if(test_ret)
        {
            onTestSuccess(detal);
        }
        else
        {
            onTestFail(detal);
        }

        return true;
    }
    @Override
public boolean onTestHandled(TestResult result) {
    kl.reenableKeyguard();
    return super.onTestHandled(result);
}
}
