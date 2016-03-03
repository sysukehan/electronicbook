package com.test.electronicbook;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class StudyStageActivity extends Activity {

	private Context context;
	private Information info;
	private AssetManager am;
	private MediaPlayer mplayer;
	
	private AssetManager cam;
	private MediaPlayer cmplayer;
	
	private LinearLayout linearlayout;
	private FrameLayout framelayout;
	private ImageView picture;
	private TextView title;
	private TextView subtitle;
	private Handler pictureHandler;
	private Handler subtitlesHandler;
	private Handler soundHandler;
	private Handler vibrationHandler;
	private Handler touchimageHandler;
	private Handler caculatorHandler;
	private Handler continueHandler;
	private String[] subtitles;
	private int[] ImageSource;
	private int[] anotherImageSource;
	private int[] smallImageSource;
	private String[] SoundSource;
	private int[] TimeArray;
	private ArrayList<BorderPosition> positionarraylist;
	private int picturecount;
	private int subtitlescount;
	private int soundcount;
	private int vibrationcount;
	private int smallimagecount;
	private int count;
	private float mx4ppi;
	
	//private int[] widtharray = new int[]{100, 130, 150, 199, 119, 197, 134, 96, 216, 173};
	//private int[] heightarray = new int[]{137, 76, 121, 175, 98, 168, 124, 78, 177, 177};
	//private int[] mlarray = new int[]{155, 155, 205, 115, 165, 95, 105, 145, 155, 125};
	//private int[] mtarray = new int[]{100, 80, 140, 80, 10, 10, 10, 70, 120, 10};
	//private int[] hasborder = new int[]{1, 2, 1, 1, 3, 3, 1, 2, 0, 0, 1, 1, 2, 0, 1, 1, 1, 0, 1, 0};
	
	private int[] widtharray = new int[]{150, 199, 119, 216, 173};
	private int[] heightarray = new int[]{121, 175, 98, 177, 177};
	private int[] mlarray = new int[]{205, 115, 165, 155, 125};
	private int[] mtarray = new int[]{140, 80, 10, 120, 10};
	private int[] hasborder = new int[]{0, 2, 0, 1, 3, 3, 1, 2, 0, 0, 1, 0, 2, 0, 0, 0, 1, 0, 1, 0};
	
	private int[] smallwidth = new int[]{0, 152, 0, 0, 340, 110, 0, 136, 0, 0, 0, 0, 88, 0, 0, 0, 0, 0, 0, 0};
	private int[] smallheight = new int[]{0, 88, 0, 0, 215, 127, 0, 56, 0, 0, 0, 0, 130, 0, 0, 0, 0, 0, 0, 0};
	private int[] smallml = new int[]{0, 179, 0, 0, 43, 175, 0, 148, 0, 0, 0, 0, 232, 0, 0, 0, 0, 0, 0, 0};
	private int[] smallmt = new int[]{0, 167, 0, 0, 122, 169, 0, 235, 0, 0, 0, 0, 209, 0, 0, 0, 0, 0, 0, 0};
	
	private boolean picturesign;//是否显示图片
	private boolean subtitlesign;//是否显示字幕
	private boolean touchsign;//是否手动播放
	private boolean vibrationsign;//是否可振动
	//mode = 0 -> picturesign = false, subtitlesign = false, touchsign = false, vibrationsign = false
	//mode = 1 -> picturesign = true, subtitlesign = false, touchsign = false, vibrationsign = false
	//mode = 2 -> picturesign = true, subtitlesign = true, touchsign = true, vibrationsign = false
	//mode = 3 -> picturesign = true, subtitlesign = false, touchsign = false, vibrationsign = true
	
	private boolean stopsign;//是否暂停
	private boolean continuesign;//在3s内只允许点击一次
	private TextView border;
	private ImageView smallimage;
	private ImageView touchsignimage;
	private boolean presssign;
	
	private boolean movesign;
	private float movesignx;
	private float movesigny;
	
	private boolean zoomsign;
	private float zoomdistance;
	
	private boolean touchimagesign;
	
	private GestureDetector detector;
	
	private Runnable picturerunnable = new Runnable() {

		@Override
		public void run() {
			if (picturecount >= 20) {
				pictureHandler.removeCallbacks(picturerunnable);
				return;
			}
			if (vibrationsign) {
				picture.setImageResource(anotherImageSource[picturecount]);
				touchimageHandler.removeCallbacks(touchimagerunnable);
				touchimagesign = false;
				touchsignimage.setAlpha(0f);
				FrameLayout.LayoutParams newlayout = new FrameLayout.LayoutParams(Math.round(smallwidth[picturecount] * mx4ppi),
																				  Math.round(smallheight[picturecount] * mx4ppi));
				newlayout.setMargins(Math.round(smallml[picturecount] * mx4ppi), Math.round(smallmt[picturecount] * mx4ppi), 0, 0);
				smallimage.setLayoutParams(newlayout);
				if (hasborder[picturecount] == 2) {
					smallimage.setBackgroundResource(smallImageSource[smallimagecount]);
					smallimage.setOnTouchListener(new MoveImage());
					smallimage.setAlpha(1f);
					smallimagecount++;
				} else if (hasborder[picturecount] == 3) {
					smallimage.setBackgroundResource(smallImageSource[smallimagecount]);
					smallimage.setOnTouchListener(new ZoomImage());
					smallimage.setAlpha(1f);
					smallimagecount++;
				} else {
					smallimage.setOnTouchListener(null);
					smallimage.setAlpha(0f);
				}
			} else {
				picture.setImageResource(ImageSource[picturecount]);
			}
			picturecount++;
			if (touchsign) {
				pictureHandler.removeCallbacks(picturerunnable);
				return;
			}
			pictureHandler.postDelayed(picturerunnable, TimeArray[picturecount - 1] + 1000);
		}
		
	};
	
	private Runnable subtitlesrunnable = new Runnable() {

		@Override
		public void run() {
			if (subtitlescount >= 20) {
				subtitlesHandler.removeCallbacks(subtitlesrunnable);
				return;
			}
			//subtitle.setText(subtitles[subtitlescount]);
			subtitlescount++;
			if (touchsign) {
				subtitlesHandler.removeCallbacks(subtitlesrunnable);
				return;
			}
			subtitlesHandler.postDelayed(subtitlesrunnable, TimeArray[subtitlescount - 1] + 1000);
		}
		
	};
	
	private Runnable soundrunnable = new Runnable() {

		@Override
		public void run() {
			if (soundcount >= 20) {
				soundHandler.removeCallbacks(soundrunnable);
				Bundle data = new Bundle();
				data.putSerializable("information", info);
				Intent sendintent = new Intent(StudyStageActivity.this, SpeakTestActivityOne.class);
				sendintent.putExtras(data);
				startActivity(sendintent);
				return;
			}
			prepareAndplay(SoundSource[soundcount]);
			soundcount++;
			if (touchsign) {
				soundHandler.removeCallbacks(soundrunnable);
				return;
			}
			soundHandler.postDelayed(soundrunnable, TimeArray[soundcount - 1] + 1000);
		}
		
	};

	private Runnable vibrationrunnable = new Runnable() {

		@Override
		public void run() {
			if (vibrationcount >= 20) {
				vibrationHandler.removeCallbacks(vibrationrunnable);
				return;
			}
			BorderPosition bp = positionarraylist.get(vibrationcount);
			if (bp.getSign()) {
				FrameLayout.LayoutParams newlayout = new FrameLayout.LayoutParams(bp.getWidth(), bp.getHeight());
				newlayout.setMargins(bp.getMarginleft(), bp.getMarginTop(), 0, 0);
				border.setLayoutParams(newlayout);
				border.setAlpha(1f);
				presssign = false;
			} else {
				border.setAlpha(0f);
				presssign = true;
			}
			vibrationcount++;
			vibrationHandler.postDelayed(vibrationrunnable, TimeArray[vibrationcount - 1] + 1000);
		}
		
	};
	
	private Runnable touchimagerunnable = new Runnable() {

		@Override
		public void run() {
			touchsignimage.setAlpha(1f);
		}
		
	};
	
	private Runnable caculatorrunnable = new Runnable() {

		@Override
		public void run() {
			if (count >= 20) {
				caculatorHandler.removeCallbacks(caculatorrunnable);
				return;
			}
			TimeArray[count] = prepare(SoundSource[count]);
			count++;
			caculatorHandler.post(caculatorrunnable);
		}
		
	};
	
	private Runnable continuerunnable = new Runnable() {

		@Override
		public void run() {
			continuesign = true;
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studystage_layout);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		ActivityCollector.addActivity(this);
		
		context = this;
		Intent getIntent = getIntent();
		Information information = (Information) getIntent.getSerializableExtra("information");
		info = new Information(information);
		//info = new Information();
		//info.setmode(3);
		mx4ppi = 3f;
		int hascount = 0;
		positionarraylist = new ArrayList<BorderPosition>();
		for (int i = 0; i < hasborder.length; i++) {
			BorderPosition temp;
			if (hasborder[i] == 1) {
				
				temp = new BorderPosition(true,
										  (int)Math.round(widtharray[hascount] * mx4ppi),
										  (int)Math.round(heightarray[hascount] * mx4ppi),
										  (int)Math.round(mlarray[hascount] * mx4ppi),
										  (int)Math.round(mtarray[hascount] * mx4ppi));
				
				//temp = new BorderPosition(true, widtharray[hascount], heightarray[hascount], mlarray[hascount], mtarray[hascount]);
				hascount++;
			} else {
				temp = new BorderPosition(false, 0, 0, 0, 0);
			}
			positionarraylist.add(temp);
		}
		switch(info.getMode()) {
		case 0:
			picturesign = false;
			subtitlesign = false;
			touchsign = false;
			vibrationsign = false;
			break;
		case 1:
			picturesign = true;
			subtitlesign = false;
			touchsign = false;
			vibrationsign = false;
			break;
		case 2:
			picturesign = true;
			subtitlesign = true;
			touchsign = true;
			vibrationsign = false;
			break;
		case 3:
			picturesign = true;
			subtitlesign = false;
			touchsign = false;
			vibrationsign = true;
		default:
			break;
		}
		stopsign = false;
		continuesign = true;
		
		AssetManager mgr=getAssets();//得到AssetManager
		Typeface tf= Typeface.createFromAsset(mgr, "fonts/SIMYOU.TTF");//根据路径得到Typeface
		
		linearlayout = (LinearLayout) findViewById(R.id.study_linearlayout);
		framelayout = (FrameLayout) findViewById(R.id.study_framelayout);
		picture = (ImageView) findViewById(R.id.study_picture_container);
		//subtitle = (TextView) findViewById(R.id.study_subtitles_container);
		title = (TextView) findViewById(R.id.title);
		border = (TextView) findViewById(R.id.border);
		smallimage = (ImageView) findViewById(R.id.small_picture_container);
		touchsignimage = (ImageView) findViewById(R.id.touchsign);
		//title.setTypeface(tf);//设置字体
		//subtitle.setTypeface(tf);
		picturecount = 0;
		subtitlescount = 0;
		soundcount = 0;
		vibrationcount = 0;
		smallimagecount = 0;
		count = 0;
		zoomsign = true;
		subtitles = new String[]{"霍斯今天有一箩筐不如意的事。",
								 "他想不出第三题的答案。",
								 "同学们说胖胖的琳达喜欢他。",
								 "而且，妈妈居然找珍珠阿姨来接他回家。",
								 "珍珠阿姨嗓门很大，而且开车横冲直撞，差点儿压死路上的贵宾狗。",
								 "霍斯气得想打人！他用力踩了一朵花。",
								 "妈妈问他：“今天好不好啊？”" + "\n\n" + "霍斯吼了一声。",
								 "“我们来煮汤吧！”妈妈说。" + "\n\n" + "霍斯一动也不动。他正生气呢，才不想煮什么鬼汤。",
								 "妈妈把锅子装满水放在炉子上。",
								 "然后，她深深吸一口气，对着锅子尖叫。",
								 "“该你啦！”她说。" + "\n\n" + "于是，霍斯爬上凳子，也对着锅子尖叫。",
								 "霍斯吼吼吼了好几声，还对着锅子龇牙咧嘴。",
								 "妈妈对着锅子吐舌头。",
								 "霍斯也吐舌头，吐了二十下。",
								 "他拿起汤勺乒乒乓乓地敲锅子。",
								 "然后，他笑了。",
								 "妈妈也笑了。",
								 "霍斯问：“我们到底在煮什么汤啊？”",
								 "“生气汤。”妈妈回答。",
								 "他们就这样肩并肩在一起， 把一天的不如意搅散在汤里。" + "\n\n" + "霍斯变快乐了。"};
		ImageSource = new int[]{R.drawable.study_picture_0, R.drawable.study_picture_1,
								R.drawable.study_picture_2, R.drawable.study_picture_3,
								R.drawable.study_picture_4, R.drawable.study_picture_5,
								R.drawable.study_picture_6, R.drawable.study_picture_7,
								R.drawable.study_picture_8, R.drawable.study_picture_9,
								R.drawable.study_picture_10, R.drawable.study_picture_11,
								R.drawable.study_picture_12, R.drawable.study_picture_13,
								R.drawable.study_picture_14, R.drawable.study_picture_15,
								R.drawable.study_picture_16, R.drawable.study_picture_17,
								R.drawable.study_picture_18, R.drawable.study_picture_19};
		anotherImageSource = new int[]{R.drawable.study_picture_0, R.drawable.study_picture_move_1,
									   R.drawable.study_picture_2, R.drawable.study_picture_3,
									   R.drawable.study_picture_zoom_4, R.drawable.study_picture_zoom_5,
									   R.drawable.study_picture_6, R.drawable.study_picture_move_7,
									   R.drawable.study_picture_8, R.drawable.study_picture_9,
									   R.drawable.study_picture_10, R.drawable.study_picture_11,
									   R.drawable.study_picture_move_12, R.drawable.study_picture_13,
									   R.drawable.study_picture_14, R.drawable.study_picture_15,
									   R.drawable.study_picture_16, R.drawable.study_picture_17,
									   R.drawable.study_picture_18, R.drawable.study_picture_19};
		smallImageSource = new int[]{R.drawable.study_picture_move_item_1, R.drawable.study_picture_zoom_item_4,
									 R.drawable.study_picture_zoom_item_5, R.drawable.study_picture_move_item_7,
									 R.drawable.study_picture_move_item_12};
		SoundSource = new String[]{"1.mp3", "2.mp3", "3.mp3", "4.mp3", "5.mp3", "6.mp3", "7.mp3", "8.mp3", "9.mp3", "10.mp3",
				"11.mp3", "12.mp3", "13.mp3", "14.mp3", "15.mp3", "16.mp3", "17.mp3", "18.mp3", "19.mp3", "20.mp3"};
		TimeArray = new int[20];
		am = getAssets();
		mplayer = new MediaPlayer();
		
		cam = getAssets();
		cmplayer = new MediaPlayer();
		caculatorHandler = new Handler();
		caculatorHandler.post(caculatorrunnable);
		
		
		pictureHandler = new Handler();
		subtitlesHandler = new Handler();
		soundHandler = new Handler();
		vibrationHandler = new Handler();
		touchimageHandler = new Handler();
		soundHandler.post(soundrunnable);
		//控制图片播放
		if (picturesign) {
			pictureHandler.post(picturerunnable);
		}
		
		//控制字幕播放
		if (subtitlesign) {
			subtitlesHandler.post(subtitlesrunnable);
		} else {
			//linearlayout.removeView(subtitle);
		}
		
		//控制是否手动
		if (touchsign) {
			detector = new GestureDetector(this, new GestureListener());
		}
		
		//控制是否振动
		if (vibrationsign) {
			vibrationHandler.post(vibrationrunnable);
			border.setOnTouchListener(new TouchVibrator());
		} else {
			framelayout.removeView(border);
		}
		
		continueHandler = new Handler();
	}
	
	private void prepareAndplay(String voice) {
		try {
			AssetFileDescriptor afd = am.openFd(voice);
			mplayer.reset();
			mplayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mplayer.prepare();
			mplayer.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private int prepare(String voice) {
		try {
			AssetFileDescriptor afd = cam.openFd(voice);
			cmplayer.reset();
			cmplayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			cmplayer.prepare();
			return cmplayer.getDuration();
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (touchsign) {
			detector.onTouchEvent(me);
		}
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (stopsign) {
			soundHandler.post(soundrunnable);
			if (subtitlesign) {
				subtitlesHandler.post(subtitlesrunnable);
			}
			if (picturesign) {
				pictureHandler.post(picturerunnable);
			}
			if (vibrationsign) {
				vibrationHandler.post(vibrationrunnable);
			}
			stopsign = false;
		}
		Log.d("electronic", stopsign + "");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mplayer.pause();
		soundHandler.removeCallbacks(soundrunnable);
		soundcount--;
		if (subtitlesign) {
			subtitlesHandler.removeCallbacks(subtitlesrunnable);
			subtitlescount--;
		}
		if (picturesign) {
			pictureHandler.removeCallbacks(picturerunnable);
			picturecount--;
		}
		if (vibrationsign) {
			vibrationHandler.removeCallbacks(vibrationrunnable);
			vibrationcount--;
		}
		stopsign = true;
		Log.d("electronic", stopsign + "");
	}
	
	private class GestureListener implements OnGestureListener {

		@Override
		public boolean onDown(MotionEvent event) {
			if (continuesign) {
				continuesign = false;
				continueHandler.postDelayed(continuerunnable, 3000);
				pictureHandler.post(picturerunnable);
				subtitlesHandler.post(subtitlesrunnable);
				soundHandler.post(soundrunnable);
			}
			
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event0, MotionEvent event1, float velocityX, float velocityY) {
			//Log.d("electronic", "onFling");
			//System.exit(0);
			return true;
		}

		@Override
		public void onLongPress(MotionEvent event) {
			//Log.d("electronic", "onLongPress");
		}

		@Override
		public boolean onScroll(MotionEvent event0, MotionEvent event1, float distanceX, float distanceY) {
			//Log.d("electronic", "onScroll");
			return true;
		}

		@Override
		public void onShowPress(MotionEvent event) {
			//Log.d("electronic", "onShowPress");
		}

		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			//Log.d("electronic", "onSingleTapUp");
			return true;
		}
	}
	
	private class TouchVibrator implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			float oldheight = 0f;
			float oldwidth = 0f;
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (presssign) {
					return true;
				}
				presssign = true;
				oldheight = view.getHeight() * 0.1f / 2;
				oldwidth = view.getWidth() * 0.1f / 2;
				FrameLayout.LayoutParams downparams =
					new FrameLayout.LayoutParams((int) Math.round(view.getWidth() * 1.1),
													(int) Math.round(view.getHeight() * 1.1));
				FrameLayout.LayoutParams downold = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
				downparams.setMargins((int) Math.round(downold.leftMargin - oldwidth),
									  (int) Math.round(downold.topMargin - oldheight), 0, 0);
				view.setLayoutParams(downparams);
				
				Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);   
	            vib.vibrate(500);
				
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				view.setAlpha(0f);
				break;
			default:
				break;
			}
			return true;
		}
	}
	
	private class BorderPosition {
		private boolean sign;
		private int width;
		private int height;
		private int marginLeft;
		private int marginTop;
		
		public BorderPosition(boolean psign, int pwidth, int pheight, int pmarginLeft, int pmarginTop) {
			sign = psign;
			width = pwidth;
			height = pheight;
			marginLeft = pmarginLeft;
			marginTop = pmarginTop;
		}
		
		public boolean getSign() {
			return sign;
		}
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
		
		public int getMarginleft() {
			return marginLeft;
		}
		
		public int getMarginTop() {
			return marginTop;
		}
	}
	
	private class MoveImage implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				movesign = true;
				break;
			case MotionEvent.ACTION_MOVE:
				if (!touchimagesign) {
					touchimagesign = true;
					touchimageHandler.postDelayed(touchimagerunnable, 500);
				}
				float x = event.getRawX();
				float y = event.getRawY();
				if (movesign) {
					movesignx = event.getX();
					movesigny = event.getY();
					movesign = false;
				}
				int marginleft = (int) (x - movesignx);
				int margintop = (int) (y - movesigny);
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
	}
	
	private class ZoomImage implements OnTouchListener {
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
					if (!touchimagesign) {
						touchimagesign = true;
						touchimageHandler.postDelayed(touchimagerunnable, 500);
					}
					//如果不是连续的操作
					if (zoomsign) {
						//获取初始化distance
						zoomdistance = FloatMath.sqrt(view.getHeight() * view.getHeight() + view.getWidth() * view.getWidth()) / 3;
						//将是否为非连续的操作的标志位置为false
						zoomsign = false;
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
						if (newdistance - zoomdistance > 2) {
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
						else if (zoomdistance - newdistance > 2) {
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
						zoomdistance = newdistance;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				//手指离开view，将非连续操作的标志位置为true
				zoomsign = true;
				break;
			default:
				break;
			}
			return true;
		}
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
