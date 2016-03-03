package com.test.electronicbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooseModeActivity extends Activity implements OnClickListener {
	
	private Information info;
	private Button button1;
	private Button button2;
	//private Button button3;
	private Button button4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosemode_layout);
		
		ActivityCollector.addActivity(this);
		
		Intent intent = getIntent();
		Information information = (Information) intent.getSerializableExtra("information");
		info = new Information(information);
		button1 = (Button) findViewById(R.id.firstmodebutton);
		button2 = (Button) findViewById(R.id.secondmodebutton);
		//button3 = (Button) findViewById(R.id.thirdmodebutton);
		button4 = (Button) findViewById(R.id.fourthmodebutton);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		//button3.setOnClickListener(this);
		button4.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.firstmodebutton:
			info.setmode(0);
			break;
		case R.id.secondmodebutton:
			info.setmode(1);
			break;
		//case R.id.thirdmodebutton:
		//	info.setmode(2);
		//	break;
		case R.id.fourthmodebutton:
			info.setmode(3);
			break;
		default:
			break;
		}
		Bundle data = new Bundle();
		data.putSerializable("information", info);
		Intent mintent = new Intent(ChooseModeActivity.this, StudyStageActivity.class);
		mintent.putExtras(data);
		startActivity(mintent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
