/*******************************************************************
* Company:     Fuzhou Rockchip Electronics Co., Ltd
* Description:   
* @author:     fxw@rock-chips.com
* Create at:   2014年5月14日 下午5:42:07  
* 
* Modification History:  
* Date         Author      Version     Description  
* ------------------------------------------------------------------  
* 2014年5月14日      fxw         1.0         create
*******************************************************************/   

package com.rockchip.devicetest.aging;


import com.rockchip.devicetest.R;
import com.rockchip.devicetest.aging.cpu.CpuInfoReader;
import com.rockchip.devicetest.aging.cpu.LinpackLoop;

import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;

public class CpuTest extends BaseAgingTest {

	public static final int UPDATE_DELAY = 3000;
	private Activity mActivity;
	private TextView mCpuModelText;
	private TextView mCpuCoreNumText;
	private TextView mCpuFreqText;
	private TextView mCpuCurrFreqText;
	private TextView mCpuUsageText;
	private TextView mCpu0UsageText;
	private TextView mCpu1UsageText;
	private TextView mCpu2UsageText;
	private TextView mCpu3UsageText;
	private TextView mCpuTempText;
	private TextView mGpuFreqText;
	private boolean isRunning;
	private long[] mCpuInfo = new long[10];
	private Handler mMainHandler = new Handler();
	
	public CpuTest(AgingConfig config, AgingCallback agingCallback){
		super(config, agingCallback);
	}

	@Override
	public void onCreate(Activity activity) {
		mActivity = activity;
		mCpuModelText = (TextView)mActivity.findViewById(R.id.tv_cpu_model);
		mCpuCoreNumText = (TextView)mActivity.findViewById(R.id.tv_cpu_corenum);
		mCpuFreqText = (TextView)mActivity.findViewById(R.id.tv_cpu_freq);
		mCpuCurrFreqText = (TextView)mActivity.findViewById(R.id.tv_cpu_currfreq);
		mCpuUsageText = (TextView)mActivity.findViewById(R.id.tv_cpu_usage);
		mCpu0UsageText = (TextView)mActivity.findViewById(R.id.tv_cpu0_usage);
		mCpu1UsageText = (TextView)mActivity.findViewById(R.id.tv_cpu1_usage);
		mCpu2UsageText = (TextView)mActivity.findViewById(R.id.tv_cpu2_usage);
		mCpu3UsageText = (TextView)mActivity.findViewById(R.id.tv_cpu3_usage);
		mCpuTempText = (TextView)mActivity.findViewById(R.id.tv_cpu_temp);
		mGpuFreqText = (TextView)mActivity.findViewById(R.id.tv_gpu_freq);

		isRunning = true;
	}

	@Override
	public void onStart() {
		int coreNum = CpuInfoReader.getCpuCores();
		String freqRange = CpuInfoReader.getCpuMinFreq()/1000+"~"+CpuInfoReader.getCpuMaxFreq()/1000+" MHz";
		mCpuModelText.setText(CpuInfoReader.getCpuModel());//CPU型号
		mCpuCoreNumText.setText(coreNum<=0?"Unknow":coreNum+"");//CPU核心数
		mCpuFreqText.setText(freqRange);//CPU频率范围
		mCpuCurrFreqText.setText(CpuInfoReader.getCpuCurrentFreq()/1000+" MHz");//CPU当前频率
		mGpuFreqText.setText(CpuInfoReader.getGpuCurrentFreq()+" MHz");
		mCpuTempText.setText(CpuInfoReader.getCpuTemp()+" C");
		mMainHandler.postDelayed(mUpdateAction, 50);//CPU使用率
		for(int i=0; i<0; i++){
			new Thread(){
				public void run() {
					while(isRunning){
						LinpackLoop.main();
					}
				};
			}.start();
		}
	}
	
	private Runnable mUpdateAction = new Runnable(){
		public void run() {
			updateCpuUsage();
			mMainHandler.postDelayed(this, UPDATE_DELAY);
			mCpuCurrFreqText.setText(CpuInfoReader.getCpuCurrentFreq()/1000+" MHz");//CPU当前频率
			mGpuFreqText.setText(CpuInfoReader.getGpuCurrentFreq()+" MHz");
			mCpuTempText.setText(CpuInfoReader.getCpuTemp()+" C");
		};
	};
	
	public void updateCpuUsage(){
		long[] cpuInfo = CpuInfoReader.getCpuTime();
		if(cpuInfo[0]==0||cpuInfo[1]==0){
			return;
		}
		if(mCpuInfo[0]==0||mCpuInfo[1]==0){
			mCpuInfo = cpuInfo;
			return;
		}
		long totalTime = cpuInfo[0]-mCpuInfo[0];
		long iddleTime = cpuInfo[1]-mCpuInfo[1];
		int percent = (int)((totalTime-iddleTime)*1.00f/totalTime*100);
		if(percent==0) percent = 1;
		mCpuUsageText.setText(percent+"%");

		long totalTime0 = cpuInfo[2]-mCpuInfo[2];
		long iddleTime0 = cpuInfo[3]-mCpuInfo[3];
		int percent0 = (int)((totalTime0-iddleTime0)*1.00f/totalTime0*100);
		if(percent0==0) percent0 = 1;
		mCpu0UsageText.setText(percent0+"%");

		long totalTime1 = cpuInfo[4]-mCpuInfo[4];
		long iddleTime1 = cpuInfo[5]-mCpuInfo[5];
		int percent1 = (int)((totalTime1-iddleTime1)*1.00f/totalTime1*100);
		if(percent1==0) percent1 = 1;
		mCpu1UsageText.setText(percent1+"%");

		long totalTime2 = cpuInfo[6]-mCpuInfo[6];
		long iddleTime2 = cpuInfo[7]-mCpuInfo[7];
		int percent2 = (int)((totalTime2-iddleTime2)*1.00f/totalTime2*100);
		if(percent2==0) percent2 = 1;
		mCpu2UsageText.setText(percent2+"%");

		long totalTime3 = cpuInfo[8]-mCpuInfo[8];
		long iddleTime3 = cpuInfo[9]-mCpuInfo[9];
		int percent3 = (int)((totalTime3-iddleTime3)*1.00f/totalTime3*100);
		if(percent3==0) percent3 = 1;
		mCpu3UsageText.setText(percent3+"%");

		mCpuInfo = cpuInfo;
	}

	@Override
	public void onStop() {
		isRunning = false;
	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void onFailed() {
		isRunning = false;
	}

}
