package com.test.electronicbook;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowResultActivity extends Activity {
	
	private Information info;
	private TextView basictext;
	private TextView testtext;
	private ListView listview;
	private String filename;
	private String content;
	private LinearLayout clicklayout;
	private String filepath;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result_layout);
		
		ActivityCollector.addActivity(this);
		
		Intent getintent = getIntent();
		Information information = (Information) getintent.getSerializableExtra("information");
		info = new Information(information);
		//info = new Information();
		//info.setmode(3);
		//info.setnumberOfstage(1);
		//info.setbegintime(0, info.getSystemTime());
		//info.setendtime(0, info.getSystemTime());
		
		basictext = (TextView) findViewById(R.id.basicinformation);
		testtext = (TextView) findViewById(R.id.testinformation);
		listview = (ListView) findViewById(R.id.tabledetail);
		clicklayout = (LinearLayout) findViewById(R.id.exitlayout);
		filepath = info.getId() + info.getName() + "的数据";
		String basicinfo = "";
		String testinfo = "";

		testinfo = "测试模式：";
		String modestring = "";
		switch(info.getMode()) {
		case 0:
			modestring = "听";
			break;
		case 1:
			modestring = "视+听";
			break;
		case 2:
			modestring = "视+听+说";
			break;
		case 3:
			modestring = "视+听+触";
			break;
		default:
			break;
		}
		testinfo = testinfo + modestring + "\t\t\t" + "测试时间：" + info.getBigSystemtime() + "\t\t\t" + "设备熟悉度：" + info.getFamiliarity();
		testtext.setText(testinfo);
		
		filename = "/" + info.getName() + "的测试结果" + ".txt";
		content = "姓名：" + info.getName() + "\t" + "性别：" + info.getSex() + "\t" + "年龄：" + info.getAge() + "岁" + "\t" + "ID：" + info.getId() + "\r\n"
				  + "测试模式：" + modestring + "\t" + "测试时间：" + info.getBigSystemtime() + "\t" + "设备熟悉度：" + info.getFamiliarity() + "\r\n"
				  + "阶段" + "\t" + "开始时间" + "\t" + "结束时间" + "\t" + "持续时间" + "\t" + "是否通过测试" + "\r\n";
		
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		int finaltime = 0;
		for (int i = 0; i < info.getnumberOfstage(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("stage", i + 1);
			listItem.put("begintime", info.getBegintime()[i]);
			listItem.put("endtime", info.getEndtime()[i]);
			int temp = differ(info.getBegintime()[i], info.getEndtime()[i]);
			listItem.put("duration", temp + "秒");
			finaltime += temp;
			if (i < 3) {
				listItem.put("passornot", "");
				content = content + String.valueOf(i + 1) + "\t" + info.getBegintime()[i] + "\t" + info.getEndtime()[i] + "\t" + temp + "秒" + "\t" + "" + "\r\n";				
			} else {
				if (info.getJudgement()[i]) {
					listItem.put("passornot", "是");
					content = content + String.valueOf(i + 1) + "\t" + info.getBegintime()[i] + "\t" + info.getEndtime()[i] + "\t" + temp + "秒" + "\t" + "是" + "\r\n";
				} else {
					listItem.put("passornot", "否");
					content = content + String.valueOf(i + 1) + "\t" + info.getBegintime()[i] + "\t" + info.getEndtime()[i] + "\t"  + temp + "秒" + "\t" + "否" + "\r\n";
				}
			}
			listItems.add(listItem);
		}
		basicinfo = "姓名：" + info.getName() + "\t\t\t" + "性别：" + info.getSex() + "\t\t\t"
					+ "年龄：" + info.getAge() + "岁" + "\t\t\t" + "ID: " + info.getId() + "\t\t\t"
					+ "总时间：" + finaltime + "秒";
		basictext.setText(basicinfo);
		content = content + "总时间：" + finaltime + "秒";
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.listview_item_layout,
				new String[] {"stage", "begintime", "endtime", "duration", "passornot"},
				new int[] {R.id.stageitem, R.id.begintimeitem, R.id.endtimetiem, R.id.durationitem, R.id.passornotitem});
		listview.setAdapter(simpleAdapter);

		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File targetFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/电子绘本测试数据" + "/" + filepath + filename);
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				raf.seek(targetFile.length());
				raf.write(content.getBytes());
				raf.close();
				//Toast.makeText(ShowResultActivity.this, "测试结果已写入SD卡", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ShowResultActivity.this, "手机中未插入SD卡", Toast.LENGTH_SHORT).show();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		clicklayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				ActivityCollector.finishAll();
			}
		});

	}
	
	private int differ(String small, String big) {
		int second = 0;
		int x = Integer.valueOf(big.substring(0, 2));
		int y = Integer.valueOf(small.substring(0, 2));
		second = second + (x - y) * 3600;
		x = Integer.valueOf(big.substring(3, 5));
		y = Integer.valueOf(small.substring(3, 5));
		second = second + (x - y) * 60;
		x = Integer.valueOf(big.substring(6, 8));
		y = Integer.valueOf(small.substring(6, 8));
		second = second + (x - y);		
		return second;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
