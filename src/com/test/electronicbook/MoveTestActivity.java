package com.test.electronicbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MoveTestActivity extends Activity implements OnTouchListener{
	private Information info;
	private ImageView image;
	private boolean sign;
	private float signx;
	private float signy;
	private FrameLayout framelayout;
	private LinearLayout linearlayout;
	private TextView question;
	private RadioGroup rg;
	private LinearLayout nextstagelayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.move_test_layout);
		Intent intent = getIntent();
		Information information = (Information) intent.getSerializableExtra("information");
		info = new Information(information);
		//info = new Information();
		info.setbegintime(1, info.getSystemTime());
		info.setnumberOfstage(2);
		image = (ImageView) findViewById(R.id.moveimage);
		question = (TextView) findViewById(R.id.topquestion);
		framelayout = (FrameLayout) findViewById(R.id.middle);
		linearlayout = (LinearLayout) findViewById(R.id.top);
		image.setOnTouchListener(this);
		//Log.d("electronic", info.getJudgement()[0] + "");
		//Log.d("electronic", "begintime: " + info.getBegintime()[0]);
		//Log.d("electronic", "endtime: " + info.getEndtime()[0]);
		rg = (RadioGroup) findViewById(R.id.movegroup);
		nextstagelayout = (LinearLayout) findViewById(R.id.stagetwotostagethree);
		nextstagelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int x = rg.getCheckedRadioButtonId();
				String message = "";
				if (x == R.id.moveyes || x == R.id.moveno) {
					if (x == R.id.moveyes) {
						info.setjudgement(1, true);
						message = "非常高兴你通过了测试";
					} else if (x == R.id.moveno) {
						info.setjudgement(1, false);
						message = "很遗憾你没有通过测试";
					} else {
						Toast.makeText(MoveTestActivity.this, "What the Hell", Toast.LENGTH_SHORT).show();
						return;
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(MoveTestActivity.this);
					builder.setMessage(message);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Bundle data = new Bundle();
							data.putSerializable("information", info);
							Intent sendintent = new Intent(MoveTestActivity.this, ZoomTestActivity.class);
							sendintent.putExtras(data);
							
							info.setendtime(1, info.getSystemTime());
							
							startActivity(sendintent);
						}
						
					});
					
					builder.create().show();
					
				} else {
					Toast.makeText(MoveTestActivity.this, "请选择测试是否通过", Toast.LENGTH_LONG).show();
				}
			}
			
		});
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			sign = true;
			break;
		case MotionEvent.ACTION_MOVE:
			float x = event.getRawX();
			float y = event.getRawY();
			if (sign) {
				signx = event.getX();
				signy = event.getY();
				sign = false;
			}
			int marginleft = (int) (x - signx);
			int margintop = (int) (y - signy - question.getHeight() - linearlayout.getHeight());
			if (marginleft < 0) marginleft = 0;
			if (marginleft > framelayout.getWidth() - view.getWidth()) marginleft = framelayout.getWidth() - view.getWidth();
			if (margintop < 0) margintop = 0;
			if (margintop > framelayout.getHeight() - view.getHeight()) margintop = framelayout.getHeight() - view.getHeight();
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(marginleft, margintop, 0, 0);
			view.setLayoutParams(params);
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return true;
	}
}
