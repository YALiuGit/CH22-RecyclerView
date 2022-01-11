package tw.tcnr03.m2201;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final ArrayList<Integer> mHeight;
    private List<String> mDataSet;


    //Adapter ，接受數據資料
    public MyAdapter(List<String> data){
        mDataSet = data;
        mHeight = new ArrayList<Integer>();
////        隨機獲取一個mHeight值
        for (int i = 0; i < mDataSet.size(); i++){
            mHeight.add( (int) (100 + Math.random() * 300));  //高度用亂數  100~400 之間
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加載布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //將數據填入具體的view中
        holder.mTextView.setText(mDataSet.get(position));
//        綁定數據的同時，修改每個ItemView的高度
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.height = mHeight.get(position);
//        lp.height=150;
        holder.itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
//由於itemView是item的佈局文件，我們需要的是裡面的textView，因此利用itemView.findViewById獲
//取裡面的textView實例，後面通過onBindViewHolder方法能直接填充數據到每一個textView了
            mTextView = (TextView) itemView.findViewById(R.id.num);
        }
    }
}