package com.example.kickfor.utils;


import com.example.kickfor.R;
import com.example.kickfor.Tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class SexangleView3 extends View {

	private float px;
	private static final double randian30 = 30 * Math.PI / 180;
	private static final double COS_CAPACITY = Math.cos(randian30);
	private static final double SIN_CAPACITY = Math.sin(randian30);

	private int attack = 0;
	private int defense = 0;
	private int skills = 0;
	private int speed = 0;
	private int power = 0;
	private int stamina = 0;

	private Drawable src;
	private int backColor;
	ShapeDrawable mDrawable;
	private Paint paint;

	private int width;
	private int height;
	private float attack_x;
	private float attack_y;
	private float defense_x;
	private float defense_y;
	private float skills_x;
	private float skills_y;
	private float speed_x;
	private float speed_y;
	private float power_x;
	private float power_y;
	private float stamina_x;
	private float stamina_y;
	

	public SexangleView3(Context context) {
		super(context);
	}

	public SexangleView3(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typeA = context.obtainStyledAttributes(attrs, R.styleable.SexangleView);
		src = typeA.getDrawable(R.styleable.SexangleView_src);
		backColor = typeA.getColor(R.styleable.SexangleView_backColor, getResources().getColor(R.color.sexangle_show));
		typeA.recycle();
	}

	public SexangleView3(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		getDot();
		if (paint == null) {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Style.FILL);
			paint.setColor(backColor);
			paint.setAlpha(127);
		} else {
			paint.setStyle(Style.FILL);
			paint.setColor(backColor);
			paint.setAlpha(1127);
		}

		Path path = new Path();
		path.moveTo(attack_x, attack_y);
		path.lineTo(speed_x, speed_y);
		path.lineTo(stamina_x, stamina_y);
		path.lineTo(defense_x, defense_y);
		path.lineTo(power_x, power_y);
		path.lineTo(skills_x, skills_y);
		path.close();
		canvas.drawPath(path, paint);

		if (src != null) {
			BitmapDrawable bitmapD = (BitmapDrawable) src;
			Bitmap bitmap = bitmapD.getBitmap();

			mDrawable = new ShapeDrawable(new PathShape(path, width, height));
			Shader aShader = new BitmapShader(zoomBitmap(bitmap, width, height), Shader.TileMode.REPEAT,
					Shader.TileMode.REPEAT);

			mDrawable.getPaint().setShader(aShader); // 填充位图
			mDrawable.setBounds(0, 0, width, height);
			mDrawable.draw(canvas);
			
			
		}
	}

	public void setValue(int attack, int defence, int stamina, int power, int skills, int speed){
		this.attack=attack;
		this.defense=defence;
		this.stamina=stamina;
		this.power=power;
		this.skills=skills;
		this.speed=speed;
	}
	
	private Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int mwidth = bitmap.getWidth();
		int mheight = bitmap.getHeight();
		float scaleWidht, scaleHeight, x, y;
		Bitmap newbmp;
		Matrix matrix = new Matrix();
		if (mwidth > mheight) {
			scaleWidht = ((float) h / mheight);
			scaleHeight = ((float) h / mheight);
			x = (mwidth - w * mheight / h) / 2;// 获取bitmap源文件中x坐标需要偏移的像数大小
			y = 0;
		} else if (mwidth < mheight) {
			scaleWidht = ((float) w / mwidth);
			scaleHeight = ((float) w / mwidth);
			x = 0;
			y = (mheight - h * mwidth / w) / 2;// 获取bitmap源文件中y坐标需要偏移的像数大小
		} else {
			scaleWidht = ((float) w / mwidth);
			scaleHeight = ((float) w / mwidth);
			x = 0;
			y = 0;
		}
		matrix.postScale(scaleWidht, scaleHeight);
		try {
			newbmp = Bitmap.createBitmap(bitmap, (int) x, (int) y, (int) (mwidth - x), (int) (mheight - y), matrix,
					true);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newbmp;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void getDot() {
		
		px=Tools.dip2px(getContext(), 3.0f);
		
		width = getWidth();
		height = getHeight();
		


		attack_x = (float)width / 2;
		attack_y = (float)height / 2 - px * attack;
		defense_x = (float)width / 2;
		defense_y = (float)(height / 2 + px * defense);
		skills_x =  (float) (width / 2 - px * COS_CAPACITY * skills);
		skills_y =  (float) (height / 2 - px * SIN_CAPACITY * skills);
		speed_x =  (float) (width / 2 + px * COS_CAPACITY * speed);
		speed_y =  (float) (height / 2 - px * SIN_CAPACITY * speed);
		power_x = (float) (width / 2 - px * COS_CAPACITY * power);
		power_y =  (float) (height / 2 + px * SIN_CAPACITY * power);
		stamina_x =  (float) (width / 2 + px * COS_CAPACITY * stamina);
		stamina_y =  (float) (height / 2 + px * SIN_CAPACITY * stamina);
	}

}
