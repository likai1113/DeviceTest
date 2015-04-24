/*******************************************************************
* Company:     Fuzhou Rockchip Electronics Co., Ltd
* Filename:    BootReceiver.java  
* Description:   
* @author:     fxw@rock-chips.com
* Create at:   2012-4-23 上午09:13:38  
* 
* Modification History:  
* Date         Author      Version     Description  
* ------------------------------------------------------------------  
* 2012-4-6      fxw         1.0         create
*******************************************************************/   
package com.rockchip.devicetest;

import com.rockchip.devicetest.service.TestService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private Context mContext;
	public static  boolean bootcomplete = false;
    public static  boolean mounted = false;
    private String TAG = "AmpakTest";
	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		String action = intent.getAction();
		if(Intent.ACTION_MEDIA_MOUNTED.equals(action)){
            Log.d(TAG, "receiver media mounted");
			Intent newIntent = new Intent(mContext, TestService.class);
			String path = intent.getDataString();
			if(path!=null&&path.endsWith("/mnt/internal_sd")){
                Log.d(TAG, "media mounted, internal sd");
				newIntent.putExtra(TestService.EXTRA_KEY_TESTFROM, "app");
			}else{
                Log.d(TAG, "media mounted, external sd");
                mounted = true;
				newIntent.putExtra(TestService.EXTRA_KEY_TESTFROM, "mount");
			}
            Log.d(TAG, "mounted = " + mounted + "bootcomplete = " + bootcomplete);
            if(mounted && bootcomplete) {
                Log.d(TAG, "mounted and bootcomplete, start Testserivce!");
                mContext.startService(newIntent);
            }
		}else if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
            Log.d(TAG, "receiver boot completed");
            bootcomplete = true;
			Intent newIntent = new Intent(mContext, TestService.class);
			newIntent.putExtra(TestService.EXTRA_KEY_TESTFROM, "mount");
            Log.d(TAG, "mounted = " + mounted + "bootcomplete = " + bootcomplete);
            if(mounted && bootcomplete) {
                Log.d(TAG, "mounted and bootcomplete, start Testserivce!");
                mContext.startService(newIntent);
            }
		}
	}
	
}
