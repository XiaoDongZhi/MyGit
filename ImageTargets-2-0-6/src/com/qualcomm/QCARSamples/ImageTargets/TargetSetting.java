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
		setContentView(R.layout.target_settings); // ����layout
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
					Toast.makeText(TargetSetting.this, "�޸ĳɹ�", Toast.LENGTH_SHORT)
					.show();
					return;
				} catch (Exception e)
				{
					Toast.makeText(TargetSetting.this, "��γ�ȱ���ΪDouble����", Toast.LENGTH_SHORT)
					.show();
					return;
				}
				

//				try
//				{// ���ɲ����¼��sql���
//					String sql = "delete from target;";
//					sld.execSQL(sql); // ִ��sql���
//					
//					String sql1 = "insert into target values" + "("
//							+ editLongitude + "," + editLatitude + ")";
//					sld.execSQL(sql); // ִ��sql���
//					Toast.makeText(getBaseContext(), "�ɹ�����һ����¼��",
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
					"/data/data/com.qualcomm.QCARSamples.imageTarget/targetdb", // ���ݿ�����·��
					null, // �α깤����Ĭ��Ϊnull
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.CREATE_IF_NECESSARY // ģʽΪ��д�����������򴴽�
			);
			// ���ɴ������ݿ��sql���
			String sql = "create table if not exists target"
					+ "(longitude DOUBLE,latitude DOUBLE";
			sld.execSQL(sql); // ִ��sql���

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private boolean checkEdit()
	{
		if (m_editLongitude.getText().toString().trim().equals(""))
		{
			Toast.makeText(TargetSetting.this, "���Ȳ���Ϊ��", Toast.LENGTH_SHORT)
					.show();
		} else if (m_editLatitude.getText().toString().trim().equals(""))
		{
			Toast.makeText(TargetSetting.this, "γ�Ȳ���Ϊ��", Toast.LENGTH_SHORT)
					.show();
		} else
		{
			return true;
		}
		return false;
	}
}
