package com.example.kickfor;

import java.io.File;

import com.example.kickfor.utils.Constant;
import com.example.kickfor.utils.CustomDialog;
import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.utils.SexangleView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.platformtools.Util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShowCapacitiyFragment extends Fragment implements OnClickListener, HomePageInterface, IdentificationInterface{
	
	private static final int THUMB_SIZE = 150;
	
	private ImageView back=null;
	private ImageView share=null;
	private ImageView image=null;
	private TextView name=null;
	private TextView info=null;
	private TextView number=null;
	private ProgressBar power=null;
	private ProgressBar speed=null;
	private ProgressBar skills=null;
	private ProgressBar stamina=null;
	private ProgressBar attack=null;
	private ProgressBar defence=null;
	private TextView powerValue=null;
	private TextView speedValue=null;
	private TextView skillsValue=null;
	private TextView staminaValue=null;
	private TextView attackValue=null;
	private TextView defenceValue=null;
	private TextView evaluate=null;
	private TextView text=null;
	
	private Bitmap bitmap=null;
	
	private int n=0;
	private boolean isSelfEvaluate=false;
	private String phone=null;
	private String str;
	private String str1;
	
	ImageView itest;
	private IWXAPI api;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		api = WXAPIFactory.createWXAPI(getActivity(), Constant.APP_ID);
		
		Bundle bundle=getArguments();
		n=bundle.getInt("n");
		isSelfEvaluate=bundle.getBoolean("isEvaluate");
		if(bundle.getString("phone").equals("host")){
			phone=(new PreferenceData(getActivity())).getData(new String[]{"phone"}).get("phone").toString();
		}
		else{
			phone=bundle.getString("phone");
		}
		View view=inflater.inflate(R.layout.fragment_show_capacity, container, false);
		back=(ImageView)view.findViewById(R.id.show_capacity_back);
		share=(ImageView)view.findViewById(R.id.show_capacity_share);
		image=(ImageView)view.findViewById(R.id.show_capacity_photo1);
		name=(TextView)view.findViewById(R.id.show_capacity_name1);
		info=(TextView)view.findViewById(R.id.show_capacity_team1);
		number=(TextView)view.findViewById(R.id.show_capacity_number1);
		power=(ProgressBar)view.findViewById(R.id.show_capacity_power);
		speed=(ProgressBar)view.findViewById(R.id.show_capacity_speed);
		skills=(ProgressBar)view.findViewById(R.id.show_capacity_skills);
		stamina=(ProgressBar)view.findViewById(R.id.show_capacity_stamina);
		attack=(ProgressBar)view.findViewById(R.id.show_capacity_attack);
		defence=(ProgressBar)view.findViewById(R.id.show_capacity_defence);
		powerValue=(TextView)view.findViewById(R.id.show_capacity_value1);
		speedValue=(TextView)view.findViewById(R.id.show_capacity_value2);
		skillsValue=(TextView)view.findViewById(R.id.show_capacity_value3);
		staminaValue=(TextView)view.findViewById(R.id.show_capacity_value4);
		attackValue=(TextView)view.findViewById(R.id.show_capacity_value5);
		defenceValue=(TextView)view.findViewById(R.id.show_capacity_value6);
		evaluate=(TextView)view.findViewById(R.id.show_capacity_submit);
		text=(TextView)view.findViewById(R.id.show_capacity_number_e);
		
		back.setOnClickListener(this);
		share.setOnClickListener(this);
		evaluate.setOnClickListener(this);
		
		initiate();
		
		//测试
		itest=(ImageView)view.findViewById(R.id.test);
		//测试
		
		
		return view;
	}
	
	private void initiate(){
		SQLHelper helper=SQLHelper.getInstance(getActivity());
		Cursor cursor=helper.select("ich", new String[]{"image", "name", "team1", "position1", "number1", "power", "speed", "skills", "stamina", "attack", "defence"}, "phone=?", new String[]{"host"}, null);
		if(cursor.moveToNext()){
			String imgPath=cursor.getString(0);
			if(!imgPath.equals("-1")){
				bitmap=BitmapFactory.decodeFile(imgPath);
				image.setImageBitmap(bitmap);
			}
			else{
				bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
				image.setImageBitmap(bitmap);
			}
			name.setText(cursor.getString(1));
			str=cursor.getString(2);
			str1=cursor.getString(3);
			Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{str}, null);
			if(cursor1.moveToNext()){
				str=cursor1.getString(0);
			}
			info.setText(str+"—"+cursor.getString(3));
			number.setText(cursor.getString(4));
			setBarColor(power, powerValue, Integer.parseInt(cursor.getString(5)));
			setBarColor(speed, speedValue, Integer.parseInt(cursor.getString(6)));
			setBarColor(skills, skillsValue, Integer.parseInt(cursor.getString(7)));
			setBarColor(stamina, staminaValue, Integer.parseInt(cursor.getString(8)));
			setBarColor(attack, attackValue, Integer.parseInt(cursor.getString(9)));
			setBarColor(defence, defenceValue, Integer.parseInt(cursor.getString(10)));
			text.setText("已有"+n+"人给您评分，点击分享邀请更多好友来为您的足球水平进行评分");
		}
	}
	
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id=arg0.getId();
		switch(id){
		case R.id.show_capacity_back:{
			setEnable(false);
			((HomePageActivity)getActivity()).onBackPressed();
			break;
		}
		case R.id.show_capacity_submit:{
			setEnable(false);
			if(isSelfEvaluate==false){
				((HomePageActivity)getActivity()).openEvaluate(phone, bitmap, name.getText().toString(), info.getText().toString(), number.getText().toString());
			}
			else{
				Toast.makeText(getActivity(), "您不能重复对自己评分", Toast.LENGTH_LONG).show();
				setEnable(true);
			}
			break;
		}
		case R.id.show_capacity_share:{
			setEnable(false);
			if (api.isWXAppInstalled()) {
				
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				final View view = inflater.inflate(R.layout.share_dialog, null);
				final CustomDialog dialog = new CustomDialog(getActivity(), R.style.MyDialog);
				 WindowManager m = getActivity().getWindowManager();
		        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		        Window dialogWindow = dialog.getWindow();
		        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		        p.width = (int) (d.getWidth() * 1.2);
		        dialogWindow.setAttributes(p);
				dialog.show();
				
				final LinearLayout scenesession = (LinearLayout) dialog.findViewById(R.id.ll_wx_scenesession);
				scenesession.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						weixinShare(true);						
					}
				});
				final LinearLayout scenetimeline = (LinearLayout) dialog .findViewById(R.id.ll_wx_scenetimeline);
				scenetimeline.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						weixinShare(false);
					}
				});
			} else {

				Toast.makeText(getActivity(), "您还未安装微信应用",
						Toast.LENGTH_SHORT).show();

			}
			
			break;
		}
		}
	}

	private void setBarColor(ProgressBar progressBar, TextView value, int progress){
		if(progress<=10){
			Drawable d=getResources().getDrawable(R.drawable.progress_bar_style_1);
			progressBar.setProgressDrawable(d);
		}
		else{
			Drawable d=getResources().getDrawable(R.drawable.progress_bar_style);
			progressBar.setProgressDrawable(d);
		}
		progressBar.setProgress(progress);
		value.setText(""+progress);
	}
	
	private void setEnable(boolean enable){
		back.setEnabled(enable);
		share.setEnabled(enable);
		evaluate.setEnabled(enable);
	}

	public void setData(boolean isEvaluate, int n){
		this.isSelfEvaluate=isEvaluate;
		this.n=n;
		initiate();
	}
	
	private Bitmap getShareImage(){
		Bitmap bitmap=null;
		String fontPath1="fonts/ZZGFLH.ttf";
		String fontPath2="fonts/FZKTJT.ttf";
		String fontPath3="fonts/PGDQH.ttf";
		
		Typeface kickfor_tf=Typeface.createFromAsset(getActivity().getAssets(), fontPath1);
		Typeface n_tf=Typeface.createFromAsset(getActivity().getAssets(), fontPath2);
		Typeface other_tf=Typeface.createFromAsset(getActivity().getAssets(), fontPath3);
		
		LayoutInflater inflater=LayoutInflater.from(getActivity());
		View view=inflater.inflate(R.layout.capacity_share, null);
		TextView name=(TextView)view.findViewById(R.id.tv_share_name);
		TextView team=(TextView)view.findViewById(R.id.tv_share_team);
		TextView position=(TextView)view.findViewById(R.id.tv_share_position);
		TextView number=(TextView)view.findViewById(R.id.tv_share_number);
		ImageView image=(ImageView)view.findViewById(R.id.iv_share_photo);
		TextView attack=(TextView)view.findViewById(R.id.tv_share_attack);
		TextView speed=(TextView)view.findViewById(R.id.tv_share_speed);
		TextView stamina=(TextView)view.findViewById(R.id.tv_share_stamina);
		TextView defence=(TextView)view.findViewById(R.id.tv_share_defense);
		TextView power=(TextView)view.findViewById(R.id.tv_share_power);
		TextView skills=(TextView)view.findViewById(R.id.tv_share_skills);
		TextView n=(TextView)view.findViewById(R.id.tv_share_n);
		TextView time=(TextView)view.findViewById(R.id.tv_share_time);
		TextView kickfor=(TextView)view.findViewById(R.id.tv_my_kickfor);
		SexangleView six=(SexangleView)view.findViewById(R.id.iv_share_capacity_new);
		
		name.setText("昵称："+this.name.getText());
		name.setTypeface(other_tf);
		team.setText("效力于："+str);
		team.setTypeface(other_tf);
		position.setText("位置："+str1);
		position.setTypeface(other_tf);
		number.setText(this.number.getText());
		number.setTypeface(other_tf);
		image.setImageBitmap(this.bitmap);
		attack.setText("进攻"+this.attackValue.getText());
		attack.setTypeface(other_tf);
		speed.setText("速度"+this.speedValue.getText());
		speed.setTypeface(other_tf);
		stamina.setText("体能"+this.staminaValue.getText());
		stamina.setTypeface(other_tf);
		defence.setText("防守"+this.defenceValue.getText());
		defence.setTypeface(other_tf);
		power.setText("力量"+this.powerValue.getText());
		power.setTypeface(other_tf);
		skills.setText("技术"+this.skillsValue.getText());
		skills.setTypeface(other_tf);
		time.setText("截止至"+Tools.getDate());
		time.setTypeface(other_tf);
		n.setText("这是我在踢否平台上由"+this.n+"人评分得出的踢否\n足球战力值，你也快来踢否看看你在大家\n眼中的足球战力是多少吧！");
		n.setTypeface(n_tf);
//		kickfor.setTypeface(kickfor_tf);
		kickfor.setTypeface(kickfor_tf, Typeface.ITALIC);
		six.setValue(Integer.parseInt(this.attackValue.getText().toString()), Integer.parseInt(this.defenceValue.getText().toString()), Integer.parseInt(this.staminaValue.getText().toString()), Integer.parseInt(this.powerValue.getText().toString()), Integer.parseInt(this.skillsValue.getText().toString()), Integer.parseInt(this.speedValue.getText().toString()));
		bitmap=Tools.convertViewToBitmap(view);
		return bitmap;
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	private void weixinShare(boolean isFriend) {
		String path = Tools.saveMyBitmap(getShareImage(), "wxshare");
		
		File file = new File(path);
		 if (!file.exists()) {
             Toast.makeText(getActivity(),  " path = " + path + "不存在！",
                             Toast.LENGTH_LONG).show();
             return;
     }
		
		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(path);
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		
		Bitmap bmp = BitmapFactory.decodeFile(path);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, (THUMB_SIZE / 2), THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Tools.bmpToByteArray(thumbBmp, true);
		
//		int imageSize = msg.thumbData.length / 1024;
//		if (imageSize > 32) {
//			Toast.makeText(getActivity(), "您分享的图片过大", Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = isFriend ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);

	}
	
}
