package com.example.cameraenhance;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.objectloader.MyRenderer;

public class GpsInfo extends Activity
{
	static final int HIDE_LOADING_DIALOG = 0;
	static final int SHOW_LOADING_DIALOG = 1;

	Activity act;
	Context ctx;
	Camera camera;
	String fileName;
	Preview preview;
	private static final String TAG = "CamTestActivity";

	TextView textview;
	LocationManager manager;
	Location location;
	SensorManager sensorManager;
	private Sensor mSensor = null;

	// private RelativeLayout mUILayout;

	/* 方位角 */
	float azimuth;
	/* 倾斜角 */
	float pitch;
	/* 旋转角 */
	float roll;

	float x;
	float y;
	float z;

	double longitude;
	double latitude;
	private Sensor accelerateSensor;

	int n = 0;

	double distance = 0;
	double targetAzimuth = 0;
	private RelativeLayout mUILayout;

	
	int width,height;
	private FrameLayout frameLayout;
	
	
	private SurfaceView surfaceView;
	private MyRenderer render;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ctx = this;
		act = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		WindowManager wManager = (WindowManager)getSystemService(WINDOW_SERVICE);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		Display display = wManager.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();      

        setContentView(R.layout.gpsinfo);
		
        
        frameLayout = (FrameLayout)findViewById(R.id.preview);
        render = new MyRenderer(this);
        frameLayout.addView(render, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

        surfaceView = new SurfaceView(this);

        
        
		preview = new Preview(this,
				surfaceView);
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		preview.setKeepScreenOn(true);
		frameLayout.addView(surfaceView); 
		
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(mSensorEventListener, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);

		accelerateSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(mSensorEventListener, accelerateSensor,
				SensorManager.SENSOR_DELAY_GAME);

		textview = new TextView(this);
		textview.setGravity(Gravity.LEFT);
		frameLayout.addView(textview, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
		
		manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// 从GPS_PROVIDER获取最近的定位信息
		location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		updateInfo(location);
		updateView();
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locationListener);
	}

	// 更新显示内容的方法
	public void updateView()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("经度：" + longitude + "\n");
		buffer.append("纬度：" + latitude + "\n");
		buffer.append("方位角：" + azimuth + "\n(0到359) 0=北, 90=东, 180=南, 270=西"
				+ "\n");
		buffer.append("倾斜角：" + pitch + "\n(-180到180)" + "\n");
		buffer.append("旋转角：" + roll + "\n(-90到90)" + "\n");
		buffer.append("x: " + x + "\n");
		buffer.append("y: " + y + "\n");
		buffer.append("z: " + z + "\n");
		buffer.append("target distance： " + distance + "\n");
		buffer.append("targetAzimuth: " + targetAzimuth + "\n");
		buffer.append("target jingdu: " + TargetConst.JING_DU + "\n");
		buffer.append("target weidu: " + TargetConst.WEI_DU + "\n");
		if (isTargetNear(longitude, latitude))
		{
			buffer.append("钟楼出来了");
		}

		textview.setText(buffer.toString());
	}

	private final LocationListener locationListener = new LocationListener()
	{

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider)
		{
			updateInfo(manager.getLastKnownLocation(provider));
			updateView();
		}

		@Override
		public void onProviderDisabled(String provider)
		{
			// TODO Auto-generated method stub
			updateView();
		}

		@Override
		public void onLocationChanged(Location location)
		{
			updateInfo(location);
			updateView();
		}
	};

	private final SensorEventListener mSensorEventListener = new SensorEventListener()
	{

		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
			{
				/* 方位角 */
				azimuth = event.values[0];
				/* 倾斜角 */
				pitch = event.values[1];
				/* 旋转角 */
				roll = event.values[2];

				updateView();
			} else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			{
				x = event.values[0];
				y = event.values[1];
				z = event.values[2];
				updateView();
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
			return;

		}
	};

	private void updateInfo(Location location)
	{
		if (null == location)
		{
			return;
		}
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		this.location = location;
	}

	private boolean isTargetNear(double phoneLon, double phoneLat)
	{

		Location location1 = new Location("");
		location1.setLatitude(TargetConst.WEI_DU);
		location1.setLongitude(TargetConst.JING_DU);
		if (null == location)
		{
			return false;
		}
		distance = location.distanceTo(location1);
		targetAzimuth = 0;
		if (distance <= 100000000)
		{
			targetAzimuth = LocationUtils.getAimAzimuth(phoneLon, phoneLat,
					TargetConst.JING_DU, TargetConst.WEI_DU);
			double maxAzimuth = (targetAzimuth + 5) % 360;
			double minAzimuth = (targetAzimuth - 5 + 360) % 360;
			if (minAzimuth < maxAzimuth && azimuth < maxAzimuth
					&& azimuth > minAzimuth)
			{
				return true;
			} else if (minAzimuth > maxAzimuth
					&& (azimuth < maxAzimuth || azimuth > minAzimuth))
			{
				return true;
			} else
			{
				return false;
			}

		}
		return false;
	}

	public void onResume()
	{
		super.onResume();
		// preview.camera = Camera.open();
		camera = Camera.open();
		camera.setDisplayOrientation(90);
		camera.startPreview();
		preview.setCamera(camera);
		
		if (null!= manager)
		{
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
                locationListener);
		}
		
	}

	@Override
	protected void onPause()
	{
		if (camera != null)
		{
			camera.stopPreview();
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
		super.onPause();
		
		if (null!= manager)
		{
			manager.removeUpdates(locationListener);
		}
	}
}