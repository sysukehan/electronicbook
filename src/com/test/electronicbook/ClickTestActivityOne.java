package com.test.electronicbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ClickTestActivityOne extends Activity implements OnTouchListener {
	
	private ImageView clickImage;
	private TextView title;
	private TextView question;
	private TextView yes;
	private TextView no;
	private Information info;
	private Context context;
	private Handler oothandler;
	private Intent sendintent;
	private boolean clicksign;
	
	private Runnable OutofTime = new Runnable() {

		@Override
		public void run() {
			info.setendtime(3, info.getSystemTime());
			info.setjudgement(3, false);
			Bundle data = new Bundle();
			data.putSerializable("information", info);
			Intent sendintent = new Intent(context, ClickTestActivityTwo.class);
			sendintent.putExtras(data);
			startActivity(sendintent);
			
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clicktestone_layout);
		
		ActivityCollector.addActivity(this);
		
		context = this;
		clicksign = false;
		Intent getIntent = getIntent();
		Information information = (Information) getIntent.getSerializableExtra("information");
		info = new Information(information);
		
		//info = new Information();
		info.setbegintime(3, info.getSystemTime());
		oothandler = new Handler();
		oothandler.postDelayed(OutofTime, 30000);
		
		AssetManager mgr=getAssets();//得到AssetManager
		Typeface tf= Typeface.createFromAsset(mgr, "fonts/SIMYOU.TTF");//根据路径得到Typeface
		
		clickImage = (ImageView) findViewById(R.id.click_picture_one);
		title = (TextView) findViewById(R.id.title);
		question = (TextView) findViewById(R.id.test_question_four);
		yes = (TextView) findViewById(R.id.clicktestoneyes);
		//no = (TextView) findViewById(R.id.clicktestoneno);
		title.setTypeface(tf);
		question.setTypeface(tf);
		question.setText("请\n你\n指\n出\n小\n鸟\n在\n哪\n？");
		
		clickImage.setOnTouchListener(this);
		
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (clicksign) {
					return;
				}
				clicksign = true;
				oothandler.removeCallbacks(OutofTime);
				info.setendtime(3, info.getSystemTime());
				info.setjudgement(3, true);
						
				Bundle data = new Bundle();
				data.putSerializable("information", info);
				sendintent = new Intent(context, ClickTestActivityTwo.class);
				sendintent.putExtras(data);

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
				AlertDialog.Builder builder = new AlertDialog.Builder(ClickTestActivityOne.this);
				builder.setMessage("好像不对呢，再试一次吧~");
				builder.setPositiveButton("下一个", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Bundle data = new Bundle();
						data.putSerializable("information", info);
						Intent sendintent = new Intent(ClickTestActivityOne.this, ClickTestActivityTwo.class);
						sendintent.putExtras(data);
						
						info.setendtime(3, info.getSystemTime());
						info.setjudgement(3, false);
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
		float oldheight = 0f;
		float oldwidth = 0f;
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			oldheight = view.getHeight() * 0.1f / 2;
			oldwidth = view.getWidth() * 0.1f / 2;
			FrameLayout.LayoutParams downparams =
				new FrameLayout.LayoutParams((int) Math.round(view.getWidth() * 1.1),
												(int) Math.round(view.getHeight() * 1.1));
			FrameLayout.LayoutParams downold = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
			downparams.setMargins((int) Math.round(downold.leftMargin - oldwidth),
								  (int) Math.round(downold.topMargin - oldheight), 0, 0);
			view.setLayoutParams(downparams);
			
			//Vibrator vib = (Vibrator) ClickTestActivityOne.this.getSystemService(Service.VIBRATOR_SERVICE);   
            //vib.vibrate(500);
			
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			oldheight = view.getHeight() * 0.095f / 2;
			oldwidth = view.getWidth() * 0.095f / 2;
			FrameLayout.LayoutParams upparams =
				new FrameLayout.LayoutParams((int) Math.round(view.getWidth() * 0.91),
												(int) Math.round(view.getHeight() * 0.91));
			FrameLayout.LayoutParams upold = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
			upparams.setMargins((int) Math.round(upold.leftMargin + oldwidth),
								  (int) Math.round(upold.topMargin + oldheight), 0, 0);
			view.setLayoutParams(upparams);
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
