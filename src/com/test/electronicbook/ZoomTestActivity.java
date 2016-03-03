package com.test.electronicbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
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

public class ZoomTestActivity extends Activity implements OnTouchListener{
	private Information info;
	private FrameLayout framelayout;
	private LinearLayout nextstagelayout;
	private RadioGroup rg;
	private ImageView image;
	private float distance;
	private boolean sign;
	private boolean marginsign;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//导入布局文件
		setContentView(R.layout.zoom_test_layout);
		//获取上一个界面传过来的信息，并使用拷贝构造函数将信息拷贝到本Activity
		//Intent getintent = getIntent();
		//Information information = (Information) getintent.getSerializableExtra("information");
		//info = new Information(information);
		
		info = new Information();
		//设置任务3的开始时间
		info.setbegintime(2, info.getSystemTime());
		//设置此时进行到第三个任务
		info.setnumberOfstage(3);
		
		framelayout = (FrameLayout) findViewById(R.id.middle);
		nextstagelayout = (LinearLayout) findViewById(R.id.stagethreetoend);
		rg = (RadioGroup) findViewById(R.id.zoomgroup);
		image = (ImageView) findViewById(R.id.zoomimage);
		//为image添加触碰事件监听器
		image.setOnTouchListener(this);
		sign = true;
		marginsign = true;
		//点击下一阶段按钮
		nextstagelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int x = rg.getCheckedRadioButtonId();
				String message = "";
				if (x == R.id.zoomyes || x == R.id.zoomno) {
					if (x == R.id.zoomyes) {
						info.setjudgement(2, true);
						message = "非常高兴你通过了测试";
					} else if (x == R.id.zoomno) {
						info.setjudgement(2, false);
						message = "很遗憾你没有通过测试";
					} else {
						Toast.makeText(ZoomTestActivity.this, "What the Hell", Toast.LENGTH_SHORT).show();
						return;
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(ZoomTestActivity.this);
					builder.setMessage(message);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Bundle data = new Bundle();
							data.putSerializable("information", info);
							Intent sendintent = new Intent(ZoomTestActivity.this, ShowResultActivity.class);
							sendintent.putExtras(data);
							
							info.setendtime(2, info.getSystemTime());
							
							startActivity(sendintent);
						}
						
					});
					
					builder.create().show();
					
				} else {
					Toast.makeText(ZoomTestActivity.this, "请选择测试是否通过", Toast.LENGTH_LONG).show();
				}
			}
			
		});
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
						//如果是第一次缩放操作，则view与父组件顶部的距离和view与父组件左边界的距离计算给出，因为居中显示，两个距离此时均为0，然后减去由于view变大所增加空间的一半
						if (marginsign) {
							newtopmargin = (int) Math.round((framelayout.getHeight() - view.getHeight()) / 2 - 0.015 * view.getHeight());
							newleftmargin = (int) Math.round((framelayout.getWidth() - view.getWidth()) / 2 - 0.015 * view.getWidth());
							//将第一次缩放操作的标志位置为false
							marginsign = false;
						} else {
							//不是第一次缩放操作，由view的布局信息给出两个距离，并减去由于view变大所增加空间的一半
							newtopmargin = (int) Math.round(old.topMargin - 0.015 * view.getHeight());
							newleftmargin = (int) Math.round(old.leftMargin - 0.015 * view.getWidth());
						}
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
						if (marginsign) {
							newtopmargin = (int) Math.round((framelayout.getHeight() - view.getHeight()) / 2 + 0.015 * view.getHeight());
							newleftmargin = (int) Math.round((framelayout.getWidth() - view.getWidth()) / 2 + 0.015 * view.getWidth());
							//将第一次缩放操作的标志位置为false
							marginsign = false;
						} else {
							//不是第一次缩放操作，由view的布局信息给出两个距离，并加上由于view变小所减少的空间的一半
							newtopmargin = (int) Math.round(old.topMargin + 0.015 * view.getHeight());
							newleftmargin = (int) Math.round(old.leftMargin + 0.015 * view.getWidth());
						}
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
}
