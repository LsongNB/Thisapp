package hymn.esrichina.thisapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/11/25.
 */

public class page1_3 extends LinearLayout {

    private TextView textView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView;
    private String mText;
    private Drawable mBackGround;
    private Drawable mBackGround2;
    private Drawable mBackGround3;
    private String string;
    int maxLine=0;  //TextView设置默认最大展示行数为5

    public page1_3(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(getContext(), R.layout.page1_3, this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Mynewpage);

        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int atrr = ta.getIndex(i);
            switch (atrr) {
                case R.styleable.Mynewpage_LText:
                    mText = ta.getString(atrr);
                    break;
                case R.styleable.Mynewpage_LBackGround:
                    mBackGround = ta.getDrawable(atrr);
                    break;
                case R.styleable.Mynewpage_LBackGround2:
                    mBackGround2 = ta.getDrawable(atrr);
                    break;
                case R.styleable.Mynewpage_LBackGround3:
                    mBackGround3 = ta.getDrawable(atrr);
                    break;
            }
        }
        ta.recycle();

        imageView1=(ImageView)findViewById(R.id.image);
        imageView2=(ImageView)findViewById(R.id.image1);
        imageView3=(ImageView)findViewById(R.id.image2);
        textView=(TextView)findViewById(R.id.adjust_text);
        textView.setText(mText);
        imageView1.setBackground(mBackGround);
        imageView2.setBackground(mBackGround2);
        imageView3.setBackground(mBackGround3);
        imageView= (ImageView) findViewById(R.id.turn_over_icon);
        string="剧情是一个叙事故事的戏剧和感情成份,多数描绘为一个故事(电视,电影)的大概戏剧冲突和感情爆发。\n剧情不等于情节,例如有的故事情节比较单一," +
                "但是剧情却很感人。\n而情节可能是故事的大致走向,即开始--高潮--结束。\n没有剧情的支持,情节就是一个骨架。";
        textView.setText(string);//设置文本内容
        textView.setHeight(textView.getLineHeight() * maxLine);  //设置默认显示高度
        imageView.setOnClickListener(new MyTurnListener());  //翻转监听
    }

    private class  MyTurnListener implements OnClickListener {
        boolean isExpand;  //是否翻转
        @Override
        public void  onClick(View v) {
            isExpand=!isExpand;
            textView.clearAnimation();  //清除动画
            final
            int  tempHight;
            final int  startHight=textView.getHeight();  //起始高度
            int durationMillis = 200;
            if(isExpand){

                /**
                 * 折叠效果，从长文折叠成短文
                 */
                tempHight = textView.getLineHeight() * textView.getLineCount() - startHight;  //为正值，长文减去短文的高度差
                //翻转icon的180度旋转动画
                RotateAnimation animation = new
                        RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(durationMillis);
                animation.setFillAfter(true);
                imageView.startAnimation(animation);
            }else
            {

                /**
                 * 展开效果，从短文展开成长文
                 */
                tempHight = textView.getLineHeight() * maxLine - startHight;//为负值，即短文减去长文的高度差
                //翻转icon的180度旋转动画
                RotateAnimation animation = new
                        RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(durationMillis);
                animation.setFillAfter(true);
                imageView.startAnimation(animation);

            }
            Animation animation = new Animation() {
                //interpolatedTime 为当前动画帧对应的相对时间，值总在0-1之间
                protected void  applyTransformation(float interpolatedTime, Transformation t) { //根据ImageView旋转动画的百分比来显示textview高度，达到动画效果
                    textView.setHeight((int) (startHight + tempHight * interpolatedTime));//原始长度+高度差*（从0到1的渐变）即表现为动画效果
                }
            };
            animation.setDuration(durationMillis);
            textView.startAnimation(animation);
        }
    }

    public void setSelected(boolean is){
        textView.setSelected(is);
        imageView.setSelected(is);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
