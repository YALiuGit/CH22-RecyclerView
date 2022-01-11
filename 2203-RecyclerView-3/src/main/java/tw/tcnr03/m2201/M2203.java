package tw.tcnr03.m2201;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class M2203 extends AppCompatActivity {

    private RecyclerView r001;
    private Toolbar toolbar;
    private List<String> mData;
    private LinearLayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private TextView t001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m2203);
        setupViewComponent();
    }

    private void setupViewComponent() {
        initToolbar();
        initData();
        initRecyclerView();
    }

    private void initToolbar() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle(getString(R.string.m2203_demo));

        setSupportActionBar(toolbar);
        //-----------------------------------------------------
        //設置menu圖標、添加菜單點擊事件要在setSupportActionBar方法之後
        toolbar.setNavigationIcon(R.drawable.ico1);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_search:
                        Toast.makeText(M2203.this, getString(R.string.action_search), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_notifications:
                        Toast.makeText(M2203.this, getString(R.string.action_notifications), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                        Toast.makeText(M2203.this, getString(R.string.action_settings), Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                return true;
            }
        });
    }
    private void initData() {
        mData = new ArrayList<>();
        for(int i=0; i<100; i++){
            mData.add("item"+ i);
        }
    }
    private void initRecyclerView() {
        //1 實例化RecyclerView
        r001 = (RecyclerView) findViewById(R.id.m2203_r001);
        t001 =(TextView)findViewById(R.id.m2203_t001);
//2 為RecyclerView創建佈局管理器
        mLayoutManager = new LinearLayoutManager(this);
//        -------------------------------------------------
// 這裡使用的是LinearLayoutManager，表示裡面的Item排列是線性排列
//        mRecyclerView.setLayoutManager(mLayoutManager);
//                -------------------------------------------------
//        表格形式的佈局，採用GridView
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4,RecyclerView.VERTICAL,false));
// 第二個參數spanCount表示表格的行數或列數；
// 第三個參數表示是水平滑動或是垂直方向滑動；
// 最後一個參數表示從數據的尾部開始顯示。
//        -------------------------------------------------
//        Adapter 中，第一個參數表示列數或者行數，
//        第二個參數表示滑動方向
//        當然，只做這些收縮是不足夠的，因為對於每個項目視圖而言，它的高度都是一樣的，這樣就達不到瀑布流的效果了，
//        因此，我們要修改每一個項目View的高度，具體實現邏輯如下所示.
        r001.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));

//        -------------------------------------------------
        mAdapter = new RecyclerViewAdapter(mData);
//        -------------------------------------------------
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(M2203.this, "onclick"+position, Toast.LENGTH_SHORT).show();
                t001.setText(getString(R.string.m2203_t001)+"按下item"+position);
            }
        });

        mAdapter.mOnItemLongClickListener(new RecyclerViewAdapter.OnItemLongClickListener() {
            @Override
                public void onItemLongClick(View view, int position) {
                    Toast.makeText(M2203.this,"long click "+mData.get(position),Toast.LENGTH_SHORT).show();
                    t001.setText(getString(R.string.m2203_t001)+"長按item"+position);
                }
        });
//        -------------------------------------------------
        //3 設置數據Adapter
        r001.setAdapter(mAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}