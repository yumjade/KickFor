package com.example.kickfor.wheelview;

import java.util.Arrays;
import java.util.List;

import android.view.View;

import com.example.kickfor.R;
import com.example.kickfor.wheelview.WheelView.OnWheelChangedListener;

public class WheelDate {
	
	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
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

	public WheelDate(View view) {
		super();
		this.view = view;
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDatePicker(int year, int month, int day) {
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DATE);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 年
		wv_year = (WheelView) view.findViewById(R.id.wheel_year);
		wv_year.setVisibleItems(3);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) view.findViewById(R.id.wheel_month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		wv_month.setVisibleItems(3);
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (WheelView) view.findViewById(R.id.wheel_day);
		wv_day.setCyclic(true);
		wv_day.setVisibleItems(3);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
		}
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day - 1);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		int textSize = 0;
		textSize = (screenheight / 100) * 3;
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

	}

	public String getDate() {
		String str1=String.format("%02d", (wv_year.getCurrentItem() + START_YEAR));
		String str2=String.format("%02d", (wv_month.getCurrentItem() + 1));
		String str3=String.format("%02d", (wv_day.getCurrentItem() + 1));
		return str1+"-"+str2+"-"+str3;
	}
	
	public String getDate1() {
		String str1=String.format("%02d", (wv_year.getCurrentItem() + START_YEAR));
		String str2=String.format("%02d", (wv_month.getCurrentItem() + 1));
		String str3=String.format("%02d", (wv_day.getCurrentItem() + 1));
		return str1+"/"+str2+"/"+str3;
	}
	
	
	public String getYear(){
		return String.format("%02d", (wv_year.getCurrentItem() + START_YEAR));
	}
	
	public String getMonth(){
		return String.format("%02d", (wv_month.getCurrentItem() + 1));
	}
	
	public String getDay(){
		return String.format("%02d", (wv_day.getCurrentItem() + 1));
	}
}

