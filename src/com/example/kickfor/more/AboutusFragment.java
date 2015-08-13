package com.example.kickfor.more;

import com.example.kickfor.R;
import com.example.kickfor.more.MoreInterface;
import com.example.kickfor.utils.IdentificationInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AboutusFragment extends Fragment implements MoreInterface,
		IdentificationInterface {

	private ImageView aboutus;

	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about_us, container, false);
		aboutus = (ImageView) view.findViewById(R.id.about_us_back);
		aboutus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();

			}
		});
		return view;
	}

}
