package com.example.kickfor.more;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.InitSmackStaticCode;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.PreferenceData;
import com.example.kickfor.R;
import com.example.kickfor.applib.HXSDKHelper;
import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.utils.KickforHXSDKModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingsFragment extends Fragment implements MoreInterface, OnClickListener, IdentificationInterface{

	private RelativeLayout rl_switch_notification;
	private RelativeLayout rl_switch_changingroom_notification;
	private RelativeLayout rl_switch_sound;
	private RelativeLayout rl_switch_vibrate;
	private RelativeLayout rl_switch_speaker;

	private LinearLayout ll_modify_password;

	private ImageView back;
	private ImageView iv_switch_open_notification;
	private ImageView iv_switch_close_notification;
	private ImageView iv_switch_open_changingroom_notification;
	private ImageView iv_switch_close_changingroom_notification;
	private ImageView iv_switch_open_sound;
	private ImageView iv_switch_close_sound;
	private ImageView iv_switch_open_vibrate;
	private ImageView iv_switch_close_vibrate;
	private ImageView iv_switch_open_speaker;
	private ImageView iv_switch_close_speaker;

	private TextView textview1, textview2, textview3;

	private Button logoutBtn;

	private EMChatOptions chatOptions;
	KickforHXSDKModel model;
	protected String groupId;
	private EMGroup group;

	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	private void Init(){
		
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_conversation_settings, container, false);
		
//		 // 获取传过来的groupid
//        groupId = getIntent().getStringExtra("groupId");
//        group = EMGroupManager.getInstance().getGroup(groupId);

		back = (ImageView) view.findViewById(R.id.settings_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().onBackPressed();
			}

		});

		rl_switch_notification = (RelativeLayout) view.findViewById(R.id.rl_switch_notification);
		rl_switch_changingroom_notification = (RelativeLayout) view.findViewById(R.id.rl_switch_changingroom_notification);
		rl_switch_sound = (RelativeLayout) view.findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) view.findViewById(R.id.rl_switch_vibrate);
		rl_switch_speaker = (RelativeLayout) view.findViewById(R.id.rl_switch_speaker);

		iv_switch_open_notification = (ImageView) view.findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView) view.findViewById(R.id.iv_switch_close_notification);
		iv_switch_open_changingroom_notification = (ImageView) view
				.findViewById(R.id.iv_switch_open_changingroom_notification);
		iv_switch_close_changingroom_notification = (ImageView) view
				.findViewById(R.id.iv_switch_close_changingroom_notification);
		iv_switch_open_sound = (ImageView) view.findViewById(R.id.iv_switch_open_sound);
		iv_switch_close_sound = (ImageView) view.findViewById(R.id.iv_switch_close_sound);
		iv_switch_open_vibrate = (ImageView) view.findViewById(R.id.iv_switch_open_vibrate);
		iv_switch_close_vibrate = (ImageView) view.findViewById(R.id.iv_switch_close_vibrate);
		iv_switch_open_speaker = (ImageView) view.findViewById(R.id.iv_switch_open_speaker);
		iv_switch_close_speaker = (ImageView) view.findViewById(R.id.iv_switch_close_speaker);

		textview1 = (TextView) view.findViewById(R.id.textview1);
		textview2 = (TextView) view.findViewById(R.id.textview2);
		textview3 = (TextView) view.findViewById(R.id.textview3);
		ll_modify_password = (LinearLayout) view.findViewById(R.id.ll_modify_password);

		logoutBtn = (Button) view.findViewById(R.id.btn_logout);

		ll_modify_password.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_changingroom_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		rl_switch_speaker.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);

		chatOptions = EMChatManager.getInstance().getChatOptions();
		model = (KickforHXSDKModel) HXSDKHelper.getInstance().getModel();

		// 震动和声音总开关，来消息时，是否允许此开关打开
		// the vibrate and sound notification are allowed or not?
		if (model.getSettingMsgNotification()) {
			iv_switch_open_notification.setVisibility(View.VISIBLE);
			iv_switch_close_notification.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_notification.setVisibility(View.INVISIBLE);
			iv_switch_close_notification.setVisibility(View.VISIBLE);
		}

		// 是否打开声音
		// sound notification is switched on or not?
		if (model.getSettingMsgSound()) {
			iv_switch_open_sound.setVisibility(View.VISIBLE);
			iv_switch_close_sound.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_sound.setVisibility(View.INVISIBLE);
			iv_switch_close_sound.setVisibility(View.VISIBLE);
		}

		// 是否打开震动
		// vibrate notification is switched on or not?
		if (model.getSettingMsgVibrate()) {
			iv_switch_open_vibrate.setVisibility(View.VISIBLE);
			iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
			iv_switch_close_vibrate.setVisibility(View.VISIBLE);
		}

		// 是否打开扬声器
		// the speaker is switched on or not?
		if (model.getSettingMsgSpeaker()) {
			iv_switch_open_speaker.setVisibility(View.VISIBLE);
			iv_switch_close_speaker.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_speaker.setVisibility(View.INVISIBLE);
			iv_switch_close_speaker.setVisibility(View.VISIBLE);
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_switch_notification:
			if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
				rl_switch_changingroom_notification.setVisibility(View.GONE);
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);
				textview1.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);
				textview3.setVisibility(View.GONE);
				chatOptions.setNotificationEnable(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);

				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(false);
			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
				rl_switch_changingroom_notification.setVisibility(View.VISIBLE);
				rl_switch_sound.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				textview1.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.VISIBLE);
				textview3.setVisibility(View.VISIBLE);
				chatOptions.setNotificationEnable(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(true);
			}
			break;
		case R.id.rl_switch_changingroom_notification:
			if (this.iv_switch_open_changingroom_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_changingroom_notification.setVisibility(View.INVISIBLE);
            	iv_switch_close_changingroom_notification.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {

					public void run() {
                        try {
                            EMGroupManager.getInstance().unblockGroupMessage(groupId);
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
				
			} else {
				iv_switch_open_changingroom_notification.setVisibility(View.VISIBLE);
            	iv_switch_close_changingroom_notification.setVisibility(View.INVISIBLE);
				new Thread(new Runnable() {
                    public void run() {
                        try {
                            EMGroupManager.getInstance().blockGroupMessage(groupId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                    }
                }).start();
			}
			break;
		case R.id.rl_switch_sound:
			if (iv_switch_open_sound.getVisibility() == View.VISIBLE) {
				iv_switch_open_sound.setVisibility(View.INVISIBLE);
				iv_switch_close_sound.setVisibility(View.VISIBLE);
				chatOptions.setNoticeBySound(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(false);
			} else {
				iv_switch_open_sound.setVisibility(View.VISIBLE);
				iv_switch_close_sound.setVisibility(View.INVISIBLE);
				chatOptions.setNoticeBySound(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(true);
			}
			break;
		case R.id.rl_switch_vibrate:
			if (iv_switch_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
				iv_switch_close_vibrate.setVisibility(View.VISIBLE);
				chatOptions.setNoticedByVibrate(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(false);
			} else {
				iv_switch_open_vibrate.setVisibility(View.VISIBLE);
				iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
				chatOptions.setNoticedByVibrate(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.rl_switch_speaker:
			if (iv_switch_open_speaker.getVisibility() == View.VISIBLE) {
				iv_switch_open_speaker.setVisibility(View.INVISIBLE);
				iv_switch_close_speaker.setVisibility(View.VISIBLE);
				chatOptions.setUseSpeaker(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSpeaker(false);
			} else {
				iv_switch_open_speaker.setVisibility(View.VISIBLE);
				iv_switch_close_speaker.setVisibility(View.INVISIBLE);
				chatOptions.setUseSpeaker(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.ll_modify_password:
			Bundle bundle=new Bundle();
			PreferenceData pd=new PreferenceData(getActivity());
			bundle.putInt("resource", R.layout.fragment_reset_psw);
			bundle.putString("phone", pd.getData(new String[]{"phone"}).get("phone").toString());
			((HomePageActivity)getActivity()).openFindPasswords(bundle);
			break;
		case R.id.btn_logout:
			((HomePageActivity) getActivity()).close();
		default:
			break;
		}

	}

}
