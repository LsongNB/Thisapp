package hymn.esrichina.thisapp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/20.
 */

public class Page1_1 extends LinearLayout {
    private ViewPager mViewPager;
    private TextView tvTitle;
    private LinearLayout pointGroup;
    private List<ImageView> list;
    // 上一个页面的位置
    protected int lastPosition;
    // 图片资源ID
    private final int[] imageIds = {R.drawable.a, R.drawable.a, R.drawable.a,
            R.drawable.a, R.drawable.a};
    //图片标题集合
    private final String[] title = {
            "图片1",
            "图片2",
            "图片3",
            "图片4",
            "图片5"
    };

    public Page1_1(Context context, AttributeSet attrs) {
        super(context, attrs);
            View.inflate(getContext(), R.layout.page1_1, this);
            mViewPager = (ViewPager)findViewById(R.id.viewpager);
            tvTitle = (TextView)findViewById(R.id.tv_title);
            pointGroup = (LinearLayout)findViewById(R.id.point_group);
            init();
            initListener();
            isRunning = true;
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }

    //判断是否在自动滚动
    private boolean isRunning = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //让viewPager 滑动到下一页
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            if (isRunning) {
                // 继续发送延时2 秒的消息, 形成类似递归的效果, 使广告一直循环切换
                mHandler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    };

    private void init() {
        list = new ArrayList<>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(imageIds[i]);
            list.add(imageView);
            //动态添加指示点
            ImageView point = new ImageView(getContext());
            point.setBackgroundResource(R.drawable.point);
            pointGroup.addView(point);
            //布局参数
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = 20;
            }
            point.setLayoutParams(params); //设置布局参数
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(list);
        mViewPager.setAdapter(myPagerAdapter);
        tvTitle.setText(title[0]);
        //把当前页面设置为中间的，这样左右都有很多页面
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % list.size()));
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                position = position % list.size();
                //设置指示点状态
                pointGroup.getChildAt(position).setEnabled(true);
                pointGroup.getChildAt(lastPosition).setEnabled(false);
                lastPosition = position;
                tvTitle.setText(title[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
