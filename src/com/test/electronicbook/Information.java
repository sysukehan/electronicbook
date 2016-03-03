package com.test.electronicbook;

import java.io.Serializable;
import java.util.Calendar;

public class Information implements Serializable {
	
	private String id;
	private int familiarity;
	private String name;
	private int age;
	private String sex;
	private int mode;//0(listen), 1(watch+listen), 2(watch+listen+speak), 3(watch+listen+touch), -1(initialized)
	private int number_of_stage;
	private String testtime;//time format: year年month月day日
	private String[] begintime;//time format: hour:minute:second
	private String[] endtime;
	private boolean[] judgement;
	
	public Information() {
		id = "";
		familiarity = -1;
		name = "";
		age = -1;
		sex = "";
		mode = -1;
		number_of_stage = 0;
		testtime = getBigSystemtime();
		begintime = new String[12];
		endtime = new String[12];
		judgement = new boolean[12];
	}
	
	public Information(Information copy) {
		id = copy.getId();
		familiarity = copy.getFamiliarity();
		name = copy.getName();
		age = copy.getAge();
		sex = copy.getSex();
		mode = copy.getMode();
		number_of_stage = copy.getnumberOfstage();
		testtime = copy.getTesttime();
		begintime = new String[12];
		endtime = new String[12];
		judgement = new boolean[12];
		for (int i = 0; i < 12; i++) {
			begintime[i] = copy.getBegintime()[i];
			endtime[i] = copy.getEndtime()[i];
			judgement[i] = copy.getJudgement()[i];
		}
	}
	
	
	public Information(String pname, int page, String psex, String pid, int pfam) {
		id = pid;
		familiarity = pfam;
		name = pname;
		age = page;
		sex = psex;
		mode = -1;
		number_of_stage = 0;
		testtime = getBigSystemtime();
		begintime = new String[12];
		endtime = new String[12];
		judgement = new boolean[12];
	}
	
	//for test
	public Information(String pname, int page, String psex, int pmode) {
		name = pname;
		age = page;
		sex = psex;
		mode = pmode;
		number_of_stage = 0;
		testtime = getBigSystemtime();
		begintime = new String[12];
		endtime = new String[12];
		judgement = new boolean[12];
	}
	
	private void setId(String pid) {
		id = pid;
	}
	
	private void setFamiliarity(int pfam) {
		familiarity = pfam;
	}
	
	public void setmode(int pmode) {
		mode = pmode;
	}
	
	public void setnumberOfstage(int number) {
		number_of_stage = number;
	}
	
	public void setbegintime(int stage, String time) {
		begintime[stage] = time;
	}
	
	public void setendtime(int stage, String time) {
		endtime[stage] = time;
	}
	
	public void setjudgement(int stage, boolean judge) {
		judgement[stage] = judge;
	}
	
	public String getId() {
		return id;
	}
	
	public int getFamiliarity() {
		return familiarity;
	}
	
	public int getMode() {
		return mode;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAge() {
		return age;
	}
	
	public String getSex() {
		return sex;
	}
	
	public int getnumberOfstage() {
		return number_of_stage;
	}
	
	public String getTesttime() {
		return testtime;
	}
	
	public String[] getBegintime() {
		return begintime;
	}
	
	public String[] getEndtime() {
		return endtime;
	}
	
	public boolean[] getJudgement() {
		return judgement;
	}
	
	public String getBigSystemtime() {
		String bigsystemtime = "";

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		bigsystemtime += year;
		bigsystemtime += "年";
		
		int month = c.get(Calendar.MONTH) + 1;
		if (month < 10) bigsystemtime = bigsystemtime + "0" + month;
		else bigsystemtime += month;
		bigsystemtime += "月";
		
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (day < 10) bigsystemtime = bigsystemtime + "0" + day;
		else bigsystemtime += day;
		bigsystemtime += "日";
		
		return bigsystemtime;
	}
	
	public String getSystemTime() {
		String systemtime = "";
		Calendar c = Calendar.getInstance();
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour < 10) systemtime = systemtime + "0" + hour;
		else systemtime += hour;
		systemtime += ":";
		
		int minute = c.get(Calendar.MINUTE);
		if (minute < 10) systemtime = systemtime + "0" + minute;
		else systemtime += minute;
		systemtime += ":";
		
		int second = c.get(Calendar.SECOND);
		if (second < 10) systemtime = systemtime + "0" + second;
		else systemtime += second;
		
		return systemtime;
	}
}
