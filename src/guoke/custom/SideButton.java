package guoke.custom;

/*
 * ������������ͼ��ť
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

	// ����
	private String[] assort = { "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" ,"#" ,"?"};
	private Paint paint = new Paint();
	// ѡ�������
	private int selectIndex = -1;
	// ��ĸ������
		private OnTouchSideButtonListener onTouch;

	//ʵ�����ӿ�
	public void setOnTouchSideButtonListener(OnTouchSideButtonListener onTouch) {
		this.onTouch = onTouch;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int interval = height / (assort.length+1);//������ĸ֮��ļ��

		for (int i = 0, length = assort.length; i < length; i++) {
			paint.setTextSize(39);
			// �����
			paint.setAntiAlias(true);
			// Ĭ�ϴ���
			//paint.setTypeface(Typeface.DEFAULT_BOLD);
			// ��ɫ
			if(!isTouch){//û�д�����ʱ��-->����Ϊ��͸���ĺ�ɫ
				paint.setColor(Color.argb(51, 0, 0, 0));
				//paint.setColor(Color.alpha(150));
			}else{
				paint.setColor(Color.alpha(0));//͸����Ϊ0,����͸��
				paint.setColor(Color.WHITE);
			}
			
			if (i == selectIndex) {
				// ��ѡ�����ĸ�ı���ɫ�ʹ���
				paint.setColor(Color.WHITE);
				paint.setFakeBoldText(true);
				paint.setTextSize(43);
			}
			// ������ĸ��X����
			float xPos = width / 2 - paint.measureText(assort[i]) / 2;
			// ������ĸ��Y����
			float yPos = interval * i + interval;
			
			canvas.drawText(assort[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	/*
	 * �д�����Ļ�¼�ʱ������������
	 * ���´��ݶ����¼���Ŀ��view
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		//�ı䱳��ɫ
		setBackgroundColor(Color.argb(224, 143, 141, 140));
		// TODO Auto-generated method stub
		float y = event.getY();
		//��õ����ĸ���±�,ͨ������¼���������������������ĸ
		int index = (int) (y / getHeight() * assort.length);
		if (index >= 0 && index < assort.length) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				// ��������ı�
				if (selectIndex != index) {
					selectIndex = index;
					if (onTouch != null) {
						//��������Ĵ�����MainActivity����ʵ����
						onTouch.onTouchSideButtonDown(assort[selectIndex]);
					}

				}
				break;
			case MotionEvent.ACTION_DOWN:
				//��Ļ������
				isTouch=true;
				selectIndex = index;
				if (onTouch != null) {
					//�����������OnTouchAssortListener�ӿڶ���ķ���
					//���ľ��������MainActivity����ʵ��
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
		invalidate();//�ػ�

		
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return true;
	}
}
