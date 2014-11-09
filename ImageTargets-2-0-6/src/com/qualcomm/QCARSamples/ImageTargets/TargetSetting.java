package com.qualcomm.QCARSamples.ImageTargets;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qualcomm.QCARSamples.ImageTargets.R;

public class TargetSetting extends Activity
{
	EditText m_editLongitude;
	EditText m_editLatitude;

	SQLiteDatabase sld;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.target_settings); // 设置layout
		m_editLongitude = (EditText) findViewById(R.id.editlongitude);
		m_editLatitude = (EditText) findViewById(R.id.editlatitude);

//		creatDB();

		Button submit = (Button) findViewById(R.id.button1);
		submit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!checkEdit())
				{
					return;
				}
				try
				{
					ZhonglouConst.JING_DU=Double.parseDouble(m_editLongitude.getText().toString());
					ZhonglouConst.WEI_DU =Double.parseDouble(m_editLatitude.getText().toString());
					Toast.makeText(TargetSetting.this, "修改成功", Toast.LENGTH_SHORT)
					.show();
					return;
				} catch (Exception e)
				{
					Toast.makeText(TargetSetting.this, "经纬度必须为Double类型", Toast.LENGTH_SHORT)
					.show();
					return;
				}
				

//				try
//				{// 生成插入记录的sql语句
//					String sql = "delete from target;";
//					sld.execSQL(sql); // 执行sql语句
//					
//					String sql1 = "insert into target values" + "("
//							+ editLongitude + "," + editLatitude + ")";
//					sld.execSQL(sql); // 执行sql语句
//					Toast.makeText(getBaseContext(), "成功插入一条记录。",
//							Toast.LENGTH_LONG).show();
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
			}
		});
	}

	private void creatDB()
	{
		try
		{
			sld = SQLiteDatabase.openDatabase(
					"/data/data/com.qualcomm.QCARSamples.imageTarget/targetdb", // 数据库所在路径
					null, // 游标工厂，默认为null
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY // 模式为读写，若不存在则创建
			);
			// 生成创建数据库的sql语句
			String sql = "create table if not exists target"
					+ "(longitude DOUBLE,latitude DOUBLE";
			sld.execSQL(sql); // 执行sql语句

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private boolean checkEdit()
	{
		if (m_editLongitude.getText().toString().trim().equals(""))
		{
			Toast.makeText(TargetSetting.this, "经度不能为空", Toast.LENGTH_SHORT)
					.show();
		} else if (m_editLatitude.getText().toString().trim().equals(""))
		{
			Toast.makeText(TargetSetting.this, "纬度不能为空", Toast.LENGTH_SHORT)
					.show();
		} else
		{
			return true;
		}
		return false;
	}
}
