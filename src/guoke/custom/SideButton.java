package guoke.custom;

/*
 * 边栏的索引视图按钮
 */
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.color;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class SideButton extends Button {

	private Boolean isTouch= false;
	
	public interface OnTouchSideButtonListener {
		public void onTouchSideButtonDown(String selectedChar);
		public void onTouchSideButtonUP();
	}

	public SideButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SideButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SideButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	// 分类
	private String[] assort = { "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" ,"#" ,"?"};
	private Paint paint = new Paint();
	// 选择的索引
	private int selectIndex = -1;
	// 字母监听器
		private OnTouchSideButtonListener onTouch;

	//实例化接口
	public void setOnTouchSideButtonListener(OnTouchSideButtonListener onTouch) {
		this.onTouch = onTouch;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int interval = height / (assort.length+1);//上下字母之间的间隔

		for (int i = 0, length = assort.length; i < length; i++) {
			paint.setTextSize(39);
			// 抗锯齿
			paint.setAntiAlias(true);
			// 默认粗体
			//paint.setTypeface(Typeface.DEFAULT_BOLD);
			// 白色
			if(!isTouch){//没有触摸的时候-->字体为半透明的黑色
				paint.setColor(Color.argb(51, 0, 0, 0));
				//paint.setColor(Color.alpha(150));
			}else{
				paint.setColor(Color.alpha(0));//透明度为0,即不透明
				paint.setColor(Color.WHITE);
			}
			
			if (i == selectIndex) {
				// 被选择的字母改变颜色和粗体
				paint.setColor(Color.WHITE);
				paint.setFakeBoldText(true);
				paint.setTextSize(43);
			}
			// 计算字母的X坐标
			float xPos = width / 2 - paint.measureText(assort[i]) / 2;
			// 计算字母的Y坐标
			float yPos = interval * i + interval;
			
			canvas.drawText(assort[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	/*
	 * 有触碰屏幕事件时候调用这个函数
	 * 向下传递动作事件给目标view
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		//改变背景色
		setBackgroundColor(Color.argb(224, 143, 141, 140));
		// TODO Auto-generated method stub
		float y = event.getY();
		//获得点击字母的下标,通过点击事件的坐标来计算出点击的字母
		int index = (int) (y / getHeight() * assort.length);
		if (index >= 0 && index < assort.length) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				// 如果滑动改变
				if (selectIndex != index) {
					selectIndex = index;
					if (onTouch != null) {
						//这个方法的代码在MainActivity里面实现了
						onTouch.onTouchSideButtonDown(assort[selectIndex]);
					}

				}
				break;
			case MotionEvent.ACTION_DOWN:
				//屏幕被按下
				isTouch=true;
				selectIndex = index;
				if (onTouch != null) {
					//这个方法是在OnTouchAssortListener接口定义的方法
					//它的具体代码在MainActivity里面实现
					onTouch.onTouchSideButtonDown(assort[selectIndex]);
					//onTouch.on
				}

				break;
			case MotionEvent.ACTION_UP:
				isTouch=false;
				setBackgroundResource(R.color.none);
				//
				if (onTouch != null) {
					onTouch.onTouchSideButtonUP();
				}
				selectIndex = -1;
				break;
			}
		} else {
			selectIndex = -1;
			if (onTouch != null) {
				onTouch.onTouchSideButtonUP();
			}
		}
		invalidate();//重绘

		
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return true;
	}
}
