package com.test.electronicbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ClickTestActivity extends Activity implements OnTouchListener {
	
	private LinearLayout nextstagelayout;
	private Information info;
	private ImageView image0;
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private RadioGroup rg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.click_test_layout);
		Intent getintent = getIntent();
		Information information = (Information) getintent.getSerializableExtra("information");
		info = new Information(information);
		//info = new Information();
		info.setbegintime(0, info.getSystemTime());
		info.setnumberOfstage(1);
		image0 = (ImageView) findViewById(R.id.testimage0);
		image1 = (ImageView) findViewById(R.id.testimage1);
		image2 = (ImageView) findViewById(R.id.testimage2);
		image3 = (ImageView) findViewById(R.id.testimage3);
		image0.setOnTouchListener(this);
		image1.setOnTouchListener(this);
		image2.setOnTouchListener(this);
		image3.setOnTouchListener(this);
		rg = (RadioGroup) findViewById(R.id.clickgroup);
		nextstagelayout = (LinearLayout) findViewById(R.id.stageonetostagetwo);
		nextstagelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int x = rg.getCheckedRadioButtonId();
				String message = "";
				if (x == R.id.clickyes || x == R.id.clickno) {
					if (x == R.id.clickyes) {
						info.setjudgement(0, true);
						message = "非常高兴你通过了测试";
					} else if (x == R.id.clickno) {
						info.setjudgement(0, false);
						message = "很遗憾你没有通过测试";
					} else {
						Toast.makeText(ClickTestActivity.this, "What the Hell", Toast.LENGTH_SHORT).show();
						return;
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(ClickTestActivity.this);
					builder.setMessage(message);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Bundle data = new Bundle();
							data.putSerializable("information", info);
							Intent sendintent = new Intent(ClickTestActivity.this, MoveTestActivity.class);
							sendintent.putExtras(data);
							
							info.setendtime(0, info.getSystemTime());
							
							startActivity(sendintent);
						}
						
					});
					
					builder.create().show();
					
				} else {
					Toast.makeText(ClickTestActivity.this, "请选择测试是否通过", Toast.LENGTH_LONG).show();
				}
			}
			
		});
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			RelativeLayout.LayoutParams downparams =
				new RelativeLayout.LayoutParams((int) Math.round(view.getWidth() * 1.1),
												(int) Math.round(view.getHeight() * 1.1));
			downparams.addRule(RelativeLayout.CENTER_IN_PARENT);
			view.setLayoutParams(downparams);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			RelativeLayout.LayoutParams upparams =
				new RelativeLayout.LayoutParams((int) Math.round(view.getWidth() * 0.91),
												(int) Math.round(view.getHeight() * 0.91));
			upparams.addRule(RelativeLayout.CENTER_IN_PARENT);
			view.setLayoutParams(upparams);
			break;
		default:
			break;
		}
		return true;
	}
}
