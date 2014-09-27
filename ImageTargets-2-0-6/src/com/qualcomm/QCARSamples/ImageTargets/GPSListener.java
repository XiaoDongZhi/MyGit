/**
 * 
 */
package com.qualcomm.QCARSamples.ImageTargets;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;

public class GPSListener extends Service implements OnClickListener
{
	private LocationManager lm;

	private final Context mContext;

	public GPSListener(Context context)
	{
		this.mContext = context;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.button_start_gps:
			startGps();
			break;
		}

	}

	private void startGps()
	{
		lm = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		// 判断GPS是否正常启动
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			mContext.startActivity(intent);
			return;
		} else
		{
			Intent intent = new Intent(mContext, GpsInfo.class);
			mContext.startActivity(intent);
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
