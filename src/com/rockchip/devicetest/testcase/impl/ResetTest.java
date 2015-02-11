/*******************************************************************
 * Company:     Fuzhou Rockchip Electronics Co., Ltd
 * Description:   
 * @author:     fxw@rock-chips.com
 * Create at:   2014年5月13日 上午9:06:23  
 * 
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014年5月13日      fxw         1.0         create
 *******************************************************************/

package com.rockchip.devicetest.testcase.impl;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import com.rockchip.devicetest.R;
import com.rockchip.devicetest.model.TestCaseInfo;
import com.rockchip.devicetest.testcase.BaseTestCase;
import com.rockchip.devicetest.utils.FileUtils;
import com.rockchip.devicetest.utils.StringUtils;

public class ResetTest extends BaseTestCase {
	public static final String RESETKEY_ADC = "/sys/class/saradc/saradc_ch0";
	public static final int SUCCESS = 1;
	public static final int FAILE = 0;
	private Handler handler;

	public ResetTest(Context context, Handler handler, TestCaseInfo testcase) {
		super(context, handler, testcase);
	}

	@Override
	public boolean onTesting() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.reset_title);
		builder.setMessage(R.string.reset_msg);
		final AlertDialog dialog = builder.create();
		dialog.show();
		handler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
					onTestSuccess();
					dialog.dismiss();
					break;
				case FAILE:
					onTestFail(0);
					dialog.dismiss();
					break;

				}
				;

			};
		};

		// AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		// builder.setTitle(R.string.reset_title);
		// builder.setMessage(R.string.reset_msg);
		// builder.setNeutralButton(mContext.getString(R.string.pub_cancel), new
		// OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// onTestFail(0);
		// }
		// });
		// builder.setCancelable(false);
		// AlertDialog dialog = builder.create();
		// dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
		// public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent
		// event) {
		// if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
		// onTestSuccess();
		// dialog.dismiss();
		// return true;
		// }
		// return false;
		// }
		// });
		// dialog.show();
		// return true;

		Runnable r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int time = 0;
				String resetADC = "";
				int adcValue = 1024;
				boolean isSuccess = false;
				while (time < 400) {
					time++;

					resetADC = FileUtils.readFromFile(new File(RESETKEY_ADC));
					if (resetADC != null) {
						adcValue = StringUtils.parseInt(resetADC, -1);
					}
					if (adcValue < 100) {
						isSuccess = true;
						handler.sendEmptyMessage(SUCCESS);
						return;
					}
					try {
						Thread.sleep(50);
					} catch (Exception e) {
					}
				}
				if (!isSuccess) {
					handler.sendEmptyMessage(FAILE);
				}

			}
		};
		Thread t = new Thread(r);// 这里比第一种创建线程对象多了个任务对象
		t.start();
		return true;

	}

}
