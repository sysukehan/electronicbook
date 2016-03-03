package com.test.electronicbook;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SpeakTestActivityOne extends Activity {
	
	private Context context;
	private AssetManager am;
	private MediaPlayer mplayer;
	private Intent sendintent;
	
	private ImageView recordbutton;
	private TextView title;
	private TextView question;
	private TextView yes;
	private TextView no;
	private Information info;
	private boolean recordsign;
	private File soundFile;
	private MediaRecorder mRecorder;
	
	private File testfile;
	private String filepath;
	
	private boolean stopsign;
	private boolean clicksign;
	
//	private Runnable outOftime = new Runnable() {
//
//		@Override
//		public void run() {
//			info.setendtime(0, info.getSystemTime());
//			info.setjudgement(0, false);
//			Bundle data = new Bundle();
//			data.putSerializable("information", info);
//			sendintent = new Intent(context, SpeakTestActivityTwo.class);
//			sendintent.putExtras(data);
//		}
//		
//	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speaktestone_layout);
		
		ActivityCollector.addActivity(this);
		
		context = this;

		//info = new Information();
		
		Intent getIntent = getIntent();
		Information information = (Information) getIntent.getSerializableExtra("information");
		info = new Information(information);
		
		info.setbegintime(0, info.getSystemTime());
		filepath = info.getId() + info.getName() + "的数据";
		AssetManager mgr=getAssets();//得到AssetManager
		Typeface tf= Typeface.createFromAsset(mgr, "fonts/SIMYOU.TTF");//根据路径得到Typeface
		recordsign = false;
		clicksign = false;
		recordbutton = (ImageView) findViewById(R.id.recordone);
		title = (TextView) findViewById(R.id.title);
		question = (TextView) findViewById(R.id.test_question_one);
		yes = (TextView) findViewById(R.id.speaktestoneyes);
		//no = (TextView) findViewById(R.id.speaktestoneno);
		title.setTypeface(tf);
		question.setTypeface(tf);
		question.setText("哪\n些\n事\n让\n霍\n斯\n不\n开\n心\n？");
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (clicksign) {
					return;
				}
				clicksign = true;
				info.setendtime(0, info.getSystemTime());
				info.setjudgement(0, true);
				Bundle data = new Bundle();
				data.putSerializable("information", info);
				sendintent = new Intent(context, SpeakTestActivityTwo.class);
				sendintent.putExtras(data);
				
				if (recordsign) {
					if (soundFile != null && soundFile.exists()) {
						mRecorder.stop();
						mRecorder.release();
						mRecorder = null;
					}
					recordsign = false;
					recordbutton.setImageResource(R.drawable.play);
					//Toast.makeText(context, "录音已保存", Toast.LENGTH_SHORT).show();
				}
				
				am = getAssets();
				mplayer = new MediaPlayer();
				try {
					AssetFileDescriptor afd = am.openFd("Yes.mp3");
					mplayer.reset();
					mplayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
					mplayer.prepare();
					mplayer.start();
				} catch(Exception e) {
					e.printStackTrace();
				}
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
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
				AlertDialog.Builder builder = new AlertDialog.Builder(SpeakTestActivityOne.this);
				builder.setMessage("好像不对呢，再试一次吧~");
				builder.setPositiveButton("下一个", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Bundle data = new Bundle();
						data.putSerializable("information", info);
						Intent sendintent = new Intent(SpeakTestActivityOne.this, SpeakTestActivityTwo.class);
						sendintent.putExtras(data);
						
						info.setendtime(0, info.getSystemTime());
						info.setjudgement(0, false);
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
		recordbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				//recordsign为true，则正在录音，为false，则不在录音
				if (recordsign) {
					if (soundFile != null && soundFile.exists()) {
						mRecorder.stop();
						mRecorder.release();
						mRecorder = null;
					}
					recordsign = false;
					recordbutton.setImageResource(R.drawable.play);
				} else {
					if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
						Toast.makeText(SpeakTestActivityOne.this, "手机未插入SD卡", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						
						soundFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/电子绘本测试数据" + "/" + filepath + "/one.amr");
						mRecorder = new MediaRecorder();
						mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
						mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
						mRecorder.setOutputFile(soundFile.getAbsolutePath());
						mRecorder.prepare();
						mRecorder.start();
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("electronic", e.toString());
					}
					recordsign = true;
					recordbutton.setImageResource(R.drawable.pause);
				}
			}
			
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
