package hymn.esrichina.thisapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018/11/30.
 */

public class DrawerLayoutAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> dataList;
    private int layoutID;

    public DrawerLayoutAdapter(Context context, List<String> dataList, int layoutID) {
        mContext = context;
        this.dataList = dataList;
        this.layoutID = layoutID;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, layoutID, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTvTitle.setText(dataList.get(position));
        holder.mCheckBox.setChecked(true);

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mMyOnCheckedChangeListener != null) {
                    mMyOnCheckedChangeListener.onCheckedChanged(isChecked, position);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private CheckBox mCheckBox;
        private TextView mTvTitle;


        public ViewHolder(View convertView) {
            mCheckBox = convertView.findViewById(R.id.checkbox1);
            mTvTitle = convertView.findViewById(R.id.tv_title);
        }
    }

    interface MyOnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked, int position);
    }

    private MyOnCheckedChangeListener mMyOnCheckedChangeListener;

    public void setMyOnCheckedChangeListener(MyOnCheckedChangeListener myOnCheckedChangeListener) {
        mMyOnCheckedChangeListener = myOnCheckedChangeListener;
    }
}

