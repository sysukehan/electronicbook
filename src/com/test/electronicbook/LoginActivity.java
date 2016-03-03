package com.test.electronicbook;

import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private RadioGroup sexrg;
	private EditText etname;
	private EditText etage;
	private EditText etid;
	private EditText etfam;
	private Button loginbutton;
	private Information info;
	private String sex;
	private File rootdir;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login_layout);
		
		ActivityCollector.addActivity(this);
		
		sexrg = (RadioGroup) findViewById(R.id.choosesex);
		etname = (EditText) findViewById(R.id.name);
		etage = (EditText) findViewById(R.id.age);
		etid = (EditText) findViewById(R.id.id);
		etfam = (EditText) findViewById(R.id.familiarity);
		loginbutton = (Button) findViewById(R.id.loginbutton);
		sex = "";
		sexrg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radiogroup, int checkedId) {
				sex = (checkedId == R.id.male ? "男": "女");
			}
		});
		loginbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (etname.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入你的姓名", Toast.LENGTH_SHORT).show();
					return;
				}
				if (etage.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入你的年龄", Toast.LENGTH_SHORT).show();
					return;
				}
				if (etid.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入你的ID", Toast.LENGTH_SHORT).show();
					return;
				}
				if (etfam.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入设备熟悉度", Toast.LENGTH_SHORT).show();
					return;
				}
				if (sex.equals("")) {
					Toast.makeText(LoginActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
					Toast.makeText(LoginActivity.this, "手机未插入SD卡", Toast.LENGTH_SHORT).show();
					return;
				}
				
				try {
					info = new Information(etname.getText().toString(), Integer.valueOf(etage.getText().toString()), sex,
							etid.getText().toString(), Integer.valueOf(etfam.getText().toString()));
					rootdir = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/电子绘本测试数据");
					if (!rootdir.exists()) {
						rootdir.mkdirs();
					}
					String filepath = info.getId() + info.getName() + "的数据";
					File testfile = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/电子绘本测试数据" + "/" + filepath);
					if (!testfile.exists()) {
						testfile.mkdirs();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				Bundle data = new Bundle();
				data.putSerializable("information", info);
				Intent intent = new Intent(LoginActivity.this, ChooseModeActivity.class);
				intent.putExtras(data);
				startActivity(intent);
				//Toast.makeText(LoginActivity.this, info.getName() + "\n" + info.getAge() + "\n" + info.getSex() + "\n" + info.getId() + "\n" + info.getFamiliarity(), Toast.LENGTH_LONG).show();
			}
		});
	}
	
	/*
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
			if (isExit) {
				this.finish();
			}
	    }
	}
	*/
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
