package com.example.kickfor.utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.kickfor.utils.KickforApplication;
import com.example.kickfor.R;
import com.example.kickfor.utils.User;
import com.squareup.picasso.Picasso;

public class UserUtils {

    public static User getUserInfo(String username){
        User user = KickforApplication.getInstance().getContactList().get(username);
        if(user == null){
            user = new User(username);
        }
            
        if(user != null){
            //demo娌℃湁杩欎簺鏁版嵁锛屼复鏃跺～鍏�?
            user.setNick(username);
//            user.setAvatar("http://downloads.easemob.com/downloads/57.png");
        }
        return user;
    }
    
    /**
     * 璁剧疆鐢ㄦ埛澶村�?
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
        User user = getUserInfo(username);
        if(user != null){
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.team_default).into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.team_default).into(imageView);
        }
    }
    
}
