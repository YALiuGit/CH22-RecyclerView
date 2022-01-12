package tw.tcnr03.m2206;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Post> mData;
    //    -------------------------------------------------------------------
    private OnItemClickListener mOnItemClickListener = null;
    //--------------------------------------------
    public RecyclerViewAdapter(Context context, ArrayList<Post> data) {
        this.mContext = context;
        this.mData = data;
    }
    //    -------------------------------------------------------------------
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //-------------------------------------------------------------------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.cell_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.img = (ImageView) view.findViewById(R.id.img);
        holder.Name = (TextView) view.findViewById(R.id.Name);
        holder.Content = (TextView) view.findViewById(R.id.Content);
        holder.Add = (TextView) view.findViewById(R.id.Addr);
        holder.Zipcode = (TextView) view.findViewById(R.id.Zipcode);
        holder.Website = (TextView) view.findViewById(R.id.mWebsite);
        //----------------------------------------------------
        //將創建的View註冊點擊事件
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Post post = mData.get(position);
        holder.Name.setText(post.Name);
        holder.Add.setText(post.Add);
        holder.Content.setText(post.Content);
        if (post.Zipcode.length()>0){
            holder.Zipcode.setText("["+post.Zipcode+"]");
        }else{
            holder.Zipcode.setText("[000]");
        }
        holder.Website.setText(post.Website);
//============================================
        Glide.with(mContext)
                .load(post.posterThumbnailUrl)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(100, 75)
                .transition(withCrossFade())
                .error(
                        Glide.with(mContext)
                                .load("https://tcnr2021-03.000webhostapp.com/opendata/something-lost.png"))
                .into(holder.img);

        //將position保存在itemView的Tag中，以便點擊時進行獲取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意這裡使用getTag方法獲取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //======= sub class   ==================
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView Name;
        public TextView Add;
        public TextView Content;
        public TextView Zipcode;
        public TextView Website;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
//-----------------------------------------------

}