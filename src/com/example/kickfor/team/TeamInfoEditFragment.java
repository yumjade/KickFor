package com.example.kickfor.team;

import com.example.kickfor.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamInfoEditFragment extends Fragment {
	
	private ImageView back;
	private ImageView photo;
	private TextView hintPhoto;
	private EditText teamName;
	private EditText foundTime;
	private EditText district;
	private EditText number;
	private TextView confirmation;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.team_info_edit, container, false);
		back = (ImageView) view.findViewById(R.id.info_edit_back);
		photo = (ImageView) view.findViewById(R.id.iv_info_team_photo);
		hintPhoto = (TextView) view.findViewById(R.id.tv_info_team_photo);
		teamName = (EditText) view.findViewById(R.id.info_team_name);
		foundTime = (EditText) view.findViewById(R.id.info_team_time);
		district = (EditText) view.findViewById(R.id.info_team_district);
		number = (EditText) view.findViewById(R.id.info_team_number);
		confirmation = (TextView) view.findViewById(R.id.info_team_confirmation);
		return view;
	}

}
