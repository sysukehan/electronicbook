package com.test.electronicbook;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path.FillType;
import android.graphics.PointF;
import android.os.Bundle;

import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class TestStageActivity extends Activity implements OnTouchListener {
	/*
	private ImageView image0;
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	*/
	private ImageView imageview;
	private ImageView imageview2;
	private FrameLayout framelayout;
	private LinearLayout toplayout;
	private boolean othersign;
	private boolean marginsign;
	private float signx;
	private float signy;
	private float distance;
	private boolean sign;
	
	private LinearLayout taskcontainer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teststage_layout);
		//toplayout = (LinearLayout) findViewById(R.id.top);
		//framelayout = (FrameLayout) findViewById(R.id.middle);
		//test = (ZoomImageView) findViewById(R.id.moveimage);
		//Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.testpicture1);
		//test.setImage(bitmap);
		//imageview = (ImageView) findViewById(R.id.moveimage2);
		//imageview2 = (ImageView) findViewById(R.id.moveimage2);
		//sign = true;
		//marginsign = true;
		//imageview.setOnTouchListener(this);
		//imageview2.setOnTouchListener(this);
		
		//myimageview.setOnTouchListener(new ImageViewOnTouchListener());
		
		//taskcontainer = (LinearLayout) findViewById(R.id.task_container);
		//getFragmentManager().beginTransaction().add(R.id.task_container, fragment);
		/*
		setContentView(R.layout.teststage_click_layout);
		image0 = (ImageView) findViewById(R.id.testimage0);
		image1 = (ImageView) findViewById(R.id.testimage1);
		image2 = (ImageView) findViewById(R.id.testimage2);
		image3 = (ImageView) findViewById(R.id.testimage3);
		image0.setOnTouchListener(this);
		image1.setOnTouchListener(this);
		image2.setOnTouchListener(this);
		image3.setOnTouchListener(this);
		*/
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	/*
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			RelativeLayout.LayoutParams downparams = new RelativeLayout.LayoutParams(view.getWidth() + 10, view.getHeight() + 10);
			downparams.addRule(RelativeLayout.CENTER_IN_PARENT);
			view.setLayoutParams(downparams);
			Log.d("electronic", "down");
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			RelativeLayout.LayoutParams upparams = new RelativeLayout.LayoutParams(view.getWidth() - 10, view.getHeight() - 10);
			upparams.addRule(RelativeLayout.CENTER_IN_PARENT);
			view.setLayoutParams(upparams);
			Log.d("electronic", "up");
			break;
		default:
			break;
		}
		return true;
	}
	*/
	/*
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
			int margintop = (int) (y - signy - toplayout.getHeight());
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
	*/
	/*
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		distance = FloatMath.sqrt(imageview.getHeight() * imageview.getHeight() + imageview.getWidth() * imageview.getWidth()) / 3;
		//Log.d("electronic", distance + "," + imageview.getHeight() + "," + imageview.getWidth());
		switch(event.getAction()&MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_POINTER_DOWN:
				if (spacing(event) > 10f) {
					Log.d("electronic", "true");
					float tempdis = spacing(event);
					Log.d("electronic", tempdis + "," + distance);
					FrameLayout.LayoutParams old = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
					if (tempdis > distance) {
						int newheight = (int) (view.getHeight() + (tempdis - distance) / 1.414f);
						int newwidth = (int) (view.getWidth() + (tempdis - distance) / 1.414f);
						int newtopmargin = (int) (old.topMargin - (tempdis - distance) / 1.414f / 2);
						int newleftmargin = (int) (old.leftMargin - (tempdis - distance) / 1.414f / 2);
						FrameLayout.LayoutParams newlayout = new FrameLayout.LayoutParams(newheight, newwidth);
						if (newtopmargin + newheight > framelayout.getHeight()) {
							newtopmargin = framelayout.getHeight() - newheight - toplayout.getHeight();
						}
						if (newleftmargin + newwidth > framelayout.getWidth()) {
							newleftmargin = framelayout.getWidth() - newwidth;
						}
						Log.d("electronic", newheight + "," + newwidth + "," + newtopmargin + "," + newleftmargin + "," + old.topMargin + "," + old.leftMargin);
						newlayout.setMargins(newleftmargin, newtopmargin, 0, 0);
						view.setLayoutParams(newlayout);
						//distance = tempdis;
					} else {
						return true;
						
						Log.d("electronic", "false");
						int newheight = (int) (old.height - (tempdis - distance) / 1.414f);
						int newwidth = (int) (old.width - (tempdis - distance) / 1.414f);
						int newtopmargin = (int) (old.topMargin + (tempdis - distance) / 1.414f / 2);
						int newleftmargin = (int) (old.leftMargin + (tempdis - distance) / 1.414f / 2);
						FrameLayout.LayoutParams newlayout = new FrameLayout.LayoutParams(newheight, newwidth);
						newlayout.setMargins(newleftmargin, newtopmargin, 0, 0);
						view.setLayoutParams(newlayout);
						distance = tempdis;
						
					}
				}
			default:
				break;
		}
		return true;
	}
	*/
	private float spacing(MotionEvent event) {  
        float x = event.getX(0) - event.getX(event.getPointerId(event.getPointerCount() - 1));  
        float y = event.getY(0) - event.getY(event.getPointerId(event.getPointerCount() - 1));  
        return FloatMath.sqrt(x * x + y * y);  
    }
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			//Log.d("electronic", "true");
			int count = event.getPointerCount();
			/*
			if (count == 1) {
				float x = event.getRawX();
				float y = event.getRawY();
				if (othersign) {
					signx = event.getX();
					signy = event.getY();
					othersign = false;
				}
				int marginleft = (int) (x - signx);
				int margintop = (int) (y - signy - toplayout.getHeight());
				if (marginleft < 0) marginleft = 0;
				if (marginleft > framelayout.getWidth() - view.getWidth()) marginleft = framelayout.getWidth() - view.getWidth();
				if (margintop < 0) margintop = 0;
				if (margintop > framelayout.getHeight() - view.getHeight()) margintop = framelayout.getHeight() - view.getHeight();
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(marginleft, margintop, 0, 0);
				view.setLayoutParams(params);
				break;
			}
			*/
			if (count >= 2) {
				if (sign) {
					distance = FloatMath.sqrt(view.getHeight() * view.getHeight() + view.getWidth() * view.getWidth()) / 2;
					sign = false;
				}
				float firstx = event.getX(0);
				float firsty = event.getY(0);
				float secondx = event.getX(event.getPointerId(count - 1));
				float secondy = event.getY(event.getPointerId(count - 1));
				if (firstx >= 0 & firsty >= 0 & secondx >= 0 & secondy >= 0 & spacing(event) > 10f) {
					float newdistance = spacing(event);
					int newheight = 0;
					int newwidth = 0;
					int newtopmargin = 0;
					int newleftmargin = 0;
					FrameLayout.LayoutParams old = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
					if (newdistance - distance > 5) {
						if (view.getHeight() >= framelayout.getHeight()) return true;
						newheight = (int) Math.round(view.getHeight() * 1.01);
						newwidth = (int) Math.round(view.getWidth() * 1.01);
						newtopmargin = 0;
						newleftmargin = 0;
						if (marginsign) {
							newtopmargin = (int) Math.round((framelayout.getHeight() - view.getHeight()) / 2 - 0.005 * view.getHeight());
							newleftmargin = (int) Math.round((framelayout.getWidth() - view.getWidth()) / 2 - 0.005 * view.getWidth());
							marginsign = false;
						} else {
							newtopmargin = (int) Math.round(old.topMargin - 0.005 * view.getHeight());
							newleftmargin = (int) Math.round(old.leftMargin - 0.005 * view.getWidth());
						}
						
						FrameLayout.LayoutParams newlayout = new FrameLayout.LayoutParams(newwidth, newheight);
						if (newtopmargin + newheight > framelayout.getHeight()) {
							newtopmargin = framelayout.getHeight() - newheight;
						}
						if (newleftmargin + newwidth > framelayout.getWidth()) {
							newleftmargin = framelayout.getWidth() - newwidth;
						}
						//Log.d("electronic", newheight + "," + newwidth + "," + newtopmargin + "," + newleftmargin + "," + old.topMargin + "," + old.leftMargin);
						if (newleftmargin < 0) newleftmargin = 0;
						if (newtopmargin < 0) newtopmargin = 0;
						newlayout.setMargins(newleftmargin, newtopmargin, 0, 0);
						view.setLayoutParams(newlayout);
					} else if (distance - newdistance > 5) {
						newheight = (int) Math.round(view.getHeight() * 0.99);
						newwidth = (int) Math.round(view.getWidth() * 0.99);
						newtopmargin = 0;
						newleftmargin = 0;
						if (marginsign) {
							newtopmargin = (int) Math.round((framelayout.getHeight() - view.getHeight()) / 2 + 0.005 * view.getHeight());
							newleftmargin = (int) Math.round((framelayout.getWidth() - view.getWidth()) / 2 + 0.005 * view.getWidth());
							marginsign = false;
						} else {
							newtopmargin = (int) Math.round(old.topMargin + 0.005 * view.getHeight());
							newleftmargin = (int) Math.round(old.leftMargin + 0.005 * view.getWidth());
						}
						FrameLayout.LayoutParams newlayout = new FrameLayout.LayoutParams(newwidth, newheight);
						//Log.d("electronic", newheight + "," + newwidth + "," + newtopmargin + "," + newleftmargin + "," + old.topMargin + "," + old.leftMargin);
						newlayout.setMargins(newleftmargin, newtopmargin, 0, 0);
						view.setLayoutParams(newlayout);
					}
					distance = newdistance;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			sign = true;
			break;
		default:
			break;
		}
		return true;
	}
}
