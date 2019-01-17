package hymn.esrichina.thisapp;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/11/20.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<ImageView> list;
    public MyPagerAdapter(List<ImageView> list){
        this.list=list;
    }
    @Override
    public int getCount(){
        return Integer.MAX_VALUE;
    }
    @Override
    public boolean isViewFromObject(View view,Object object){
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container,int position){
        ImageView imageView=list.get(position % list.size());
        container.addView(imageView);
        return imageView;
    }
    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView((View) object);
        object=null;
    }
}
