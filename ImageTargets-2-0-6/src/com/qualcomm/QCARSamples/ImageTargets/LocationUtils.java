/**
 * 
 */
package com.qualcomm.QCARSamples.ImageTargets;

import android.location.Location;

public class LocationUtils
{
	private static double EARTH_RADIUS = 6378137;// µØÇò°ë¾¶

	private static double rad(double d)
	{
		return d * Math.PI / 180.0;
	}

	public static double getAimAzimuth(double phoneLon, double phoneLat,
			double targetLon, double targetLat)
	{
		double aimAzimuth = 0;

		if (phoneLat < targetLat && phoneLon < targetLon)
		{
			float[] result1 = new float[1];
			float[] result2 = new float[1];
			Location.distanceBetween(targetLat, targetLon, targetLat, phoneLon,
					result1);
			Location.distanceBetween(targetLat, phoneLon, phoneLat, phoneLon,
					result2);
			aimAzimuth = Math.abs(Math.atan(result1[0] / result2[0]));
			aimAzimuth = aimAzimuth * 180 / Math.PI;
			// aimAzimuth = Math.abs(Math.atan((targetLon - phoneLon)
			// * Math.cos(Math.toRadians(targetLat))
			// / (targetLat - phoneLat)));
		} else if (phoneLat > targetLat && phoneLon < targetLon)
		{
			float[] result1 = new float[1];
			float[] result2 = new float[1];
			Location.distanceBetween(targetLat, targetLon, targetLat, phoneLon,
					result1);
			Location.distanceBetween(targetLat, phoneLon, phoneLat, phoneLon,
					result2);
			aimAzimuth = Math.abs(Math.atan(result1[0] / result2[0]));
			aimAzimuth = aimAzimuth * 180 / Math.PI;
			aimAzimuth = 180 - aimAzimuth;
			// aimAzimuth = 180 - Math.abs(Math.atan((targetLon - phoneLon)
			// * Math.cos(Math.toRadians(targetLat))
			// / (phoneLat - targetLat)));
		} else if (phoneLat > targetLat && phoneLon > targetLon)
		{
			float[] result1 = new float[1];
			float[] result2 = new float[1];
			Location.distanceBetween(targetLat, targetLon, targetLat, phoneLon,
					result1);
			Location.distanceBetween(targetLat, phoneLon, phoneLat, phoneLon,
					result2);
			aimAzimuth = Math.abs(Math.atan(result1[0] / result2[0]));
			aimAzimuth = aimAzimuth * 180 / Math.PI;
			aimAzimuth = aimAzimuth + 180;
		} else if (phoneLat < targetLat && phoneLon > targetLon)
		{
			float[] result1 = new float[1];
			float[] result2 = new float[1];
			Location.distanceBetween(targetLat, targetLon, targetLat, phoneLon,
					result1);
			Location.distanceBetween(targetLat, phoneLon, phoneLat, phoneLon,
					result2);
			aimAzimuth = Math.abs(Math.atan(result1[0] / result2[0]));
			aimAzimuth = aimAzimuth * 180 / Math.PI;
			aimAzimuth = 360 - aimAzimuth;
		} else if (phoneLat == targetLat)
		{
			if (targetLon > phoneLon)
			{
				aimAzimuth = 90;
			} else if (targetLon < phoneLon)
			{
				aimAzimuth = 270;
			}
		} else if (phoneLon == targetLon)
		{
			if (targetLat > phoneLat)
			{
				aimAzimuth = 0;
			} else if (targetLat < phoneLat)
			{
				aimAzimuth = 180;
			}
		}
		return aimAzimuth;
	}
}
