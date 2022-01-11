package tw.tcnr03.m2205;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class M2205 extends AppCompatActivity {

    private TextView mTxtResult;
    private TextView mDesc;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout laySwipe;
    private String[] listName;
    private String[] listDescr;
    ArrayList<Post> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m2205);
        setupViewComponent();
    }

    private void setupViewComponent() {

        mTxtResult = (TextView) findViewById(R.id.m2205_t001);
        mDesc = (TextView) findViewById(R.id.m2205_t002);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//---------------------------------------------------------------------------------------------------
        laySwipe=(SwipeRefreshLayout)findViewById(R.id. laySwipe);
        laySwipe.setSize(SwipeRefreshLayout.LARGE);
        //設置下拉多少距離之後開始刷新數據
        laySwipe.setDistanceToTriggerSync(500);  //dp
        //設置進度條背景顏色 //圓圈顏色
        laySwipe.setProgressBackgroundColorSchemeColor(getColor(android.R.color.holo_blue_light));
        //設置刷新動畫的顏色，可設置1或更多
        laySwipe.setColorSchemeResources(
                R.color.red,
                R.color.orange,
                R.color.yellow,
                R.color.green,
                R.color.deepskyblue,
                R.color.blue,
                R.color.purple);
        laySwipe.setOnRefreshListener(onSwipeToRefresh); //reload監聽
        //---------------------------------------------------------------
        setImgtolist();
    }

    private void setImgtolist() { //抓資料 __類似initData
        //抓資料放到陣列中
        listName = getResources().getStringArray(R.array.teacname);
        listDescr = getResources().getStringArray(R.array.descr);
        //設定Adapter
        mData = new ArrayList<>();
        //做7張樣板圖片
        for (int i = 0; i < listName.length; i++) {
            mData.add(new Post(listName[i], "https://tcnr2021-02.000webhostapp.com/post_img/t00" + (i + 1) + ".JPG", listDescr[i]));
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
// ************************************

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
                public void onItemClick(View view, int position) {
                Toast.makeText(M2205.this, "onclick" + listName[position], Toast.LENGTH_SHORT).show();
                mTxtResult.setText(getString(R.string.m2205_t002) + listName[position]);
                mDesc.setText(getString(R.string.m2205_descr) + listDescr[position]);
                }
        });

//********************************* ****
//-------------------------------------------------
                recyclerView.setAdapter(adapter);

    }

    //===========================
    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            laySwipe.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "重新載入完成", Toast.LENGTH_SHORT).show();
                    setImgtolist();
                    laySwipe.setRefreshing(false);
                }
            }, 300);
        }
    };

    //--------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}