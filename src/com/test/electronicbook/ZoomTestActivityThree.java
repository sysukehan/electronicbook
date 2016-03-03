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
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ZoomTestActivityThree extends Activity implements OnTouchListener {
	
	private TextView yes;
	private TextView no;
	private TextView title;
	private TextView question;
	
	private Information info;
	
	private ImageView imageview;
	private FrameLayout framelayout;
	private float distance;
	private boolean sign;
	private boolean clicksign;
	private Context context;
	private Handler oothandler;
	private Intent sendintent;
	private Runnable OutofTime = new Runnable() {

		@Override
		public void run() {
			info.setendtime(11, info.getSystemTime());
			info.setjudgement(11, false);
			Bundle data = new Bundle();
			data.putSerializable("information", info);
			Intent sendintent = new Intent(context, ShowResultActivity.class);
			sendintent.putExtras(data);
			startActivity(sendintent);
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoomtestthree_layout);
		
		ActivityCollector.addActivity(this);
		
		context = this;
		clicksign = false;
		oothandler = new Handler();
		oothandler.postDelayed(OutofTime, 60000);
		
		Intent getIntent = getIntent();
		Information information = (Information) getIntent.getSerializableExtra("information");
		info = new Information(information);
		
		//info = new Information();
		info.setbegintime(11, info.getSystemTime());
		info.setnumberOfstage(12);
		
		AssetManager mgr=getAssets();//得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/SIMYOU.TTF");//根据路径得到Typeface
		title = (TextView) findViewById(R.id.title);
		question = (TextView) findViewById(R.id.test_question_twelve);
		question.setText("请\n你\n把\n汤\n锅\n变\n小\n再\n变\n大");
		title.setTypeface(tf);
		question.setTypeface(tf);
		yes = (TextView) findViewById(R.id.zoomtestthreeyes);
		//no = (TextView) findViewById(R.id.zoomtestthreeno);
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
				sendintent = new Intent(context, ShowResultActivity.class);
				sendintent.putExtras(data);
						
				info.setendtime(11, info.getSystemTime());
				info.setjudgement(11, true);
						
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
				AlertDialog.Builder builder = new AlertDialog.Builder(ZoomTestActivityThree.this);
				builder.setMessage("好像不对呢，再试一次吧~");
				builder.setPositiveButton("结束测试", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Bundle data = new Bundle();
						data.putSerializable("information", info);
						Intent sendintent = new Intent(ZoomTestActivityThree.this, ShowResultActivity.class);
						sendintent.putExtras(data);
						
						info.setendtime(11, info.getSystemTime());
						info.setjudgement(11, false);
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
		sign = true;
		framelayout = (FrameLayout) findViewById(R.id.zoomarea3);
		imageview = (ImageView) findViewById(R.id.zoomimage_3);
		imageview.setOnTouchListener(this);
	}
	
	
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			//Log.d("electronic", "true");
			int count = event.getPointerCount();
			//多点触碰组件
			if (count >= 2) {
				//如果不是连续的操作
				if (sign) {
					//获取初始化distance
					distance = FloatMath.sqrt(view.getHeight() * view.getHeight() + view.getWidth() * view.getWidth()) / 3;
					//将是否为非连续的操作的标志位置为false
					sign = false;
				}
				//第一个触点相对于view的横纵坐标
				float firstx = event.getX(0);
				float firsty = event.getY(0);
				//最后一个触点相对于view的横纵坐标
				float secondx = event.getX(event.getPointerId(count - 1));
				float secondy = event.getY(event.getPointerId(count - 1));
				//如果触点均在view范围内并且两点距离大于10dp，则满足多点触碰要求
				if (firstx >= 0 & firsty >= 0 & secondx >= 0 & secondy >= 0 & spacing(event) > 10f) {
					//获取两点间距离
					float newdistance = spacing(event);
					//初始化view新高度
					int newheight = 0;
					//初始化view新宽度
					int newwidth = 0;
					//初始化view与父组件顶部的距离
					int newtopmargin = 0;
					//初始化view与父组件左边界的距离
					int newleftmargin = 0;
					//获取view原来的布局信息
					FrameLayout.LayoutParams old = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
					//如果两点间距离比distance大2dp，则为放大操作
					if (newdistance - distance > 2) {
						//如果view的高度已经大于或等于父组件的高度，则不再进行放大（因为该Activity为横屏显示，所以高度的限制优先）
						if (view.getHeight() >= framelayout.getHeight()) return true;
						//长和宽等比放大0.01倍
						newheight = (int) Math.round(view.getHeight() * 1.03);
						newwidth = (int) Math.round(view.getWidth() * 1.03);
						//初始化view与父组件顶部的距离
						newtopmargin = 0;
						//初始化view与父组件左边界的距离
						newleftmargin = 0;
						
							newtopmargin = (int) Math.round(old.topMargin - 0.015 * view.getHeight());
							newleftmargin = (int) Math.round(old.leftMargin - 0.015 * view.getWidth());

						//将view新的高度和宽度放进新的布局信息中
						FrameLayout.LayoutParams newlayout = new FrameLayout.LayoutParams(newwidth, newheight);
						//如果view与父组件顶部的新的距离加上view的新的高度大于父组件的高度，则将view与父组件顶部的新的距离设置为父组件的高度减去view的新的高度
						if (newtopmargin + newheight > framelayout.getHeight()) {
							newtopmargin = framelayout.getHeight() - newheight;
						}
						//如果view与父组件左边界的新的距离加上view的新的宽度大于父组件的宽度，则将view与父组件左边界的新的距离设置为父组件的宽度减去view的新的宽度
						if (newleftmargin + newwidth > framelayout.getWidth()) {
							newleftmargin = framelayout.getWidth() - newwidth;
						}
						//如果view与父组件左边界的新的距离小于0，则令它等于0
						if (newleftmargin < 0) newleftmargin = 0;
						//如果view与父组件顶部的新的距离小于0，则令它等于0
						if (newtopmargin < 0) newtopmargin = 0;
						//将view与父组件顶部的新的距离和view与父组件左边界的新的距离放进新的布局信息中
						newlayout.setMargins(newleftmargin, newtopmargin, 0, 0);
						//给view配置新的布局信息
						view.setLayoutParams(newlayout);
						
					}
					//如果两点间距离比distance小2dp，则为缩小操作
					else if (distance - newdistance > 2) {
						//长和宽等比缩小0.01倍
						newheight = (int) Math.round(view.getHeight() * 0.97);
						newwidth = (int) Math.round(view.getWidth() * 0.97);
						//初始化view与父组件顶部的距离
						newtopmargin = 0;
						//初始化view与父组件左边界的距离
						newleftmargin = 0;
						//如果是第一次缩放操作，则view与父组件顶部的距离和view与父组件左边界的距离计算给出，因为居中显示，两个距离此时均为0，然后加上由于view变小所减少的空间的一半

							//不是第一次缩放操作，由view的布局信息给出两个距离，并加上由于view变小所减少的空间的一半
							newtopmargin = (int) Math.round(old.topMargin + 0.015 * view.getHeight());
							newleftmargin = (int) Math.round(old.leftMargin + 0.015 * view.getWidth());

						//将view新的高度和宽度放进新的布局信息中
						FrameLayout.LayoutParams newlayout = new FrameLayout.LayoutParams(newwidth, newheight);
						//将view与父组件顶部的新的距离和view与父组件左边界的新的距离放进新的布局信息中
						newlayout.setMargins(newleftmargin, newtopmargin, 0, 0);
						//给view配置新的布局信息
						view.setLayoutParams(newlayout);
					}
					//将distance设置为两点触碰的距离
					distance = newdistance;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			//手指离开view，将非连续操作的标志位置为true
			sign = true;
			break;
		default:
			break;
		}
		return true;
	}
	//获取事件中两点的距离
	private float spacing(MotionEvent event) {  
        float x = event.getX(0) - event.getX(event.getPointerId(event.getPointerCount() - 1));  
        float y = event.getY(0) - event.getY(event.getPointerId(event.getPointerCount() - 1));  
        return FloatMath.sqrt(x * x + y * y);  
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
