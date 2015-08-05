package com.example.kickfor.wheelview;

import java.util.Arrays;
import java.util.List;

import android.view.View;

import com.example.kickfor.R;
import com.example.kickfor.wheelview.WheelView.OnWheelChangedListener;

public class WheelYear {
	
	private View view;
	private WheelView wv_year;
	public int screenheight;
	private static int START_YEAR = 1960, END_YEAR = 2020;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelYear(View view) {
		super();
		this.view = view;
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDatePicker(int year) {
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DATE);
		// 添加大小月月份并将其转换为list,方便之后的判断

		// 年
		wv_year = (WheelView) view.findViewById(R.id.wheel_year1);
		wv_year.setVisibleItems(3);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
			}
		};
		wv_year.addChangingListener(wheelListener_year);

		int textSize = 0;
		textSize = (screenheight / 100) * 3;
		wv_year.TEXT_SIZE = textSize;

	}

	
	
	
	public String getYear(){
		return String.format("%02d", (wv_year.getCurrentItem() + START_YEAR));
	}
	
}



