package com.test.electronicbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class MoveTestActivityOne extends Activity implements OnTouchListener {
	
	private boolean sign;
	private boolean clicksign;
	private float signx;
	private float signy;
	private FrameLayout framelayout;
	private ImageView imageview;
	private TextView yes;
	private TextView no;
	private TextView title;
	private TextView question;
	private Information info;
	private Context context;
	private Handler oothandler;
	private Intent sendintent;
	private Runnable OutofTime = new Runnable() {

		@Override
		public void run() {
			info.setendtime(6, info.getSystemTime());
			info.setjudgement(6, false);
			Bundle data = new Bundle();
			data.putSerializable("information", info);
			Intent sendintent = new Intent(context, MoveTestActivityTwo.class);
			sendintent.putExtras(data);
			startActivity(sendintent);
			
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movetestone_layout);
		
		ActivityCollector.addActivity(this);
		
		context = this;
		clicksign = false;
		Intent getIntent = getIntent();
		Information information = (Information) getIntent.getSerializableExtra("information");
		info = new Information(information);
		
		//info = new Information();
		
		info.setbegintime(6, info.getSystemTime());
		oothandler = new Handler();
		oothandler.postDelayed(OutofTime, 45000);
		
		framelayout = (FrameLayout) findViewById(R.id.movelayout_1);
		imageview = (ImageView) findViewById(R.id.moveimage_1);
		imageview.setOnTouchListener(this);
		
		AssetManager mgr=getAssets();//得到AssetManager
		Typeface tf= Typeface.createFromAsset(mgr, "fonts/SIMYOU.TTF");//根据路径得到Typeface
		title = (TextView) findViewById(R.id.title);
		question = (TextView) findViewById(R.id.test_question_seven);
		title.setTypeface(tf);
		question.setTypeface(tf);
		
		yes = (TextView) findViewById(R.id.movetestoneyes);
		//no = (TextView) findViewById(R.id.movetestoneno);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (clicksign) {
					return;
				}
				clicksign = true;
				oothandler.removeCallbacks(OutofTime);
				Bundle data = new Bundle();
				data.putSerializable("information", info);
				sendintent = new Intent(context, MoveTestActivityTwo.class);
				sendintent.putExtras(data);
						
				info.setendtime(6, info.getSystemTime());
				info.setjudgement(6, true);
				
				AssetManager am = getAssets();
				MediaPlayer mplayer = new MediaPlayer();
				try {
					AssetFileDescriptor afd = am.openFd("Yes.mp3");
					mplayer.reset();
					mplayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
					mplayer.prepare();
					mplayer.start();
				} catch(Exception e) {
					e.printStackTrace();
				}
				Handler passhandler = new Handler();
				passhandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						startActivity(sendintent);
					}
					
				}, mplayer.getDuration());
			}
		});
		
		/*
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MoveTestActivityOne.this);
				builder.setMessage("好像不对呢，再试一次吧~");
				builder.setPositiveButton("下一个", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Bundle data = new Bundle();
						data.putSerializable("information", info);
						Intent sendintent = new Intent(MoveTestActivityOne.this, MoveTestActivityTwo.class);
						sendintent.putExtras(data);
						
						info.setendtime(6, info.getSystemTime());
						info.setjudgement(6, false);
						startActivity(sendintent);
					}
				
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
				builder.create().show();
				
			}
			
		});
		*/
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
			int margintop = (int) (y - signy);
			if (marginleft < 0) marginleft = 0;
			if (marginleft > framelayout.getWidth() - view.getWidth()) marginleft = framelayout.getWidth() - view.getWidth();
			if (margintop < 0) margintop = 0;
			if (margintop > framelayout.getHeight() - view.getHeight()) margintop = framelayout.getHeight() - view.getHeight();
			FrameLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
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
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
