package tw.tcnr03.m2201;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>      implements
        View.OnClickListener,
        View.OnLongClickListener {
    private final ArrayList<Integer> mHeight;
    private List<String> mDataSet;
    private OnItemClickListener mOnItemClickListener=null;
    private OnItemLongClickListener mOnItemLongClickListener = null;

    //Adapter ，接受數據資料
    public RecyclerViewAdapter(List<String> data){
        mDataSet = data;
        mHeight = new ArrayList<Integer>();
//        隨機獲取一個mHeight值
        for (int i = 0; i < mDataSet.size(); i++){
            mHeight.add( (int) (100 + Math.random() * 300));
        }
    }

    //    -------------------------------------------------------------------
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加載布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout,parent,false);
        ViewHolder vh = new ViewHolder(view);
        //將創建的View註冊點擊事件
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
    }
    //    -------------------------------------------------------------------
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //將數據填入具體的view中
        holder.mTextView.setText(mDataSet.get(position));
//        綁定數據的同時，修改每個ItemView的高度
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.height = mHeight.get(position);
        holder.itemView.setLayoutParams(lp);
        //將position保存在itemView的Tag中，以便點擊時進行獲取
        holder.itemView.setTag(position);
    }

    //    -------------------------------------------------------------------
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
    //==========================================
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
//由於itemView是item的佈局文件，我們需要的是裡面的textView，因此利用itemView.findViewById獲
//取裡面的textView實例，後面通過onBindViewHolder方法能直接填充數據到每一個textView了
            mTextView = (TextView) itemView.findViewById(R.id.num);
        }
    }
    //---------------設定監聽-----------
    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public static interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }
    //--------------------------------
    //-------------------------------------------------------
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意這裡使用getTag方法獲取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    //    -------------------------------------------------------------------
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            //注意這裡使用getTag方法獲取position
            mOnItemLongClickListener.onItemLongClick(v,(int)v.getTag());
        }
        return false;
    }


    public void mOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;

    }
    //====================================================
}