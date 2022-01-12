package tw.tcnr03.m2206;

import static android.os.Build.VERSION_CODES.M;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class M2206 extends AppCompatActivity {
    private String ul="https://gis.taiwan.net.tw/XMLReleaseALL_public/scenic_spot_C_f.json";
    private LinearLayout li01;
    private TextView mTxtResult;
    private TextView mDesc;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout laySwipe;
    private View u_loading;
    private ArrayList<Map<String, Object>> mList;
    private int total;
    private int t_total =0;
    private TextView t_count;
    private int nowposition = 0;
    //旅館 https://tcnr2021-03.000webhostapp.com/opendata/hotel_C_f.json
    //旅館http://192.168.10.3/opendata/hotel_C_f.json
    //旅館https://gis.taiwan.net.tw/XMLReleaseALL_public/hotel_C_f.json
    //景點 https://gis.taiwan.net.tw/XMLReleaseALL_public/scenic_spot_C_f.json
    //餐廳 https://gis.taiwan.net.tw/XMLReleaseALL_public/restaurant_C_f.json

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //遇到大量資料處理要加此兩行，管理手機記憶體--------------------------------------------
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //---------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m2206);
        setupViewComponent();
    }

    private void setupViewComponent() {
        li01 = (LinearLayout) findViewById(R.id.li01);
        li01.setVisibility(View.GONE);
        mTxtResult = (TextView) findViewById(R.id.m2206_name);
        mDesc = (TextView) findViewById(R.id.m2206_descr);

        mDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
        mDesc.scrollTo(0, 0);//textview 回頂端


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        t_count = (TextView) findViewById(R.id.count);

//        ----------------RecyclerView設定上下滑動------------
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                li01.setVisibility(View.GONE);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑動時要做什麼

            }
        });
        //---------------------下載資料-------------------------
        u_loading = findViewById(R.id.u_loading);
        u_loading.setVisibility(View.GONE);
        //-------------------------------------------------------

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
        laySwipe.setProgressViewOffset(true, 0, 50);
        //開始轉圈下載資料
        onSwipeToFresh.onRefresh();
        //---------------------------------------------------------------
    }

    private final SwipeRefreshLayout.OnRefreshListener onSwipeToFresh =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mTxtResult.setText("");
                    MyAlertDialog myAltDlg = new MyAlertDialog(M2206.this);
                    myAltDlg.setTitle(getString(R.string.dialog_title));
                    myAltDlg.setMessage(getString(R.string.dialog_t001) + getString(R.string.dialog_b001));
                    myAltDlg.setIcon(android.R.drawable.ic_menu_rotate);
                    myAltDlg.setCancelable(false);
                    myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dialog_positive), altDlgOnClkPosiBtnLis);
                    myAltDlg.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.dialog_neutral), altDlgOnClkNeutBtnLis);
                    myAltDlg.show();
                }
            };

//按是
    private DialogInterface.OnClickListener altDlgOnClkPosiBtnLis =new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        laySwipe.setRefreshing(true);
        u_loading.setVisibility(View.VISIBLE);
        mTxtResult.setText(getString(R.string.m2206_name) + "");
        mDesc.setText("");
        mDesc.scrollTo(0, 0);//textview 回頂端
//------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setDatatolist();
// =================================
//----------SwipeLayout 結束 --------
//可改放到最終位置 u_importopendata()
                u_loading.setVisibility(View.GONE);
                laySwipe.setRefreshing(false);
                Toast.makeText(getApplicationContext(), getString(R.string.loadover), Toast.LENGTH_SHORT).show();
            }
        },1000); //讓圓圈轉1秒
    }
};
    //按取消
    private DialogInterface.OnClickListener altDlgOnClkNeutBtnLis  =new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            u_loading.setVisibility(View.GONE);
            laySwipe.setRefreshing(false);
        }
    };

    private void setDatatolist() {
        //==================================
        u_importopendata();  //下載Opendata
        //==================================
        //設定Adapter
        final ArrayList<Post> mData = new ArrayList<>();
//        for (Map<String, Object> m : mList) {
//            if (m != null) {
//                String Name = m.get("Name").toString().trim(); //名稱
//                String Add = m.get("Add").toString().trim(); //住址
//                String Picture1 = m.get("Picture1").toString().trim(); //圖片
//                if (Picture1.isEmpty() || Picture1.length() < 1) {
//                    Picture1 = "https://taiwan.taiwanstay.net.tw/twpic/15545.jpg";
//                }
//                String Description = m.get("Description").toString().trim(); //描述
//                String Zipcode = m.get("Zipcode").toString().trim(); //描述
////************************************************************
//                mData.add(new Post(Name, Picture1, Add, Description, Zipcode));
////************************************************************
//            } else {
//                return;
//            }
//        }
        for(Map<String, Object> m : mList){
            if(m!= null){
                String Name = m.get("Name").toString().trim(); //名稱
                String Add = m.get("Add").toString().trim(); //住址
                String Picture1 = m.get("Picture1").toString().trim(); //圖片
//                --------------------------------------------------
                if (Picture1.isEmpty() || Picture1.length() < 1) {
                    Picture1 = "https://tcnr2021-03.000webhostapp.com/opendata/nopic1.jpg";
                }
//                --------------------------------------------------
                String Description = m.get("Description").toString().trim(); //描述
                String Zipcode = m.get("Zipcode").toString().trim(); //zip
                String Website = m.get("Website").toString().trim(); //商家網頁,

//************************************************************
                mData.add(new Post(Name, Picture1, Add, Description, Zipcode,Website));
//************************************************************
            }else{
                return;
            }
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
// ************************************
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                li01.setVisibility(View.VISIBLE);
                Toast.makeText(M2206.this, "onclick" + mData.get(position).Name.toString(), Toast.LENGTH_SHORT).show();
                mTxtResult.setText(getString(R.string.m2206_name) + mData.get(position).Name);
                mDesc.setText(mData.get(position).Content);
                mDesc.scrollTo(0, 0); //textview 回頂端
                nowposition = position;
                t_count.setText(getString(R.string.ncount) + "   (目前為第" + (nowposition + 1) + "筆)"+ total + "/" + t_total  );
            }
        });

//********************************* ****
        recyclerView.setAdapter(adapter);
    }




    private void u_importopendata() {
        try {
//        -------------------------------------------------------
            String Task_opendata  = new TransTask().execute(ul).get();   //旅館民宿 - 觀光資訊資料庫

            int a =0;
            /* "XML_Head": {
    "Listname": "4",
    "Language": "C",
    "Orgname": "315080000H",
    "Updatetime": "2022-01-12T01:16:09+08:00",
    "Infos": {
      "Info": [
        {
          "Id": "C4_315080000H_000008",
          "Name": "思源居民宿",
          "Description": "位於南投縣的民宿",
          "Grade": "",
          "Add": "南投縣埔里鎮水頭里水頭路1號",
          "Zipcode": "545",
          "Region": "南投縣",
          "Town": "埔里鎮",
          "Tel": "886-49-2927101",
          "Fax": "886--",
          "Gov": "315080000H",
          "Website": "",
          "Picture1": "https://taiwan.taiwanstay.net.tw/twpic/15545.jpg",
          "Picdescribe1": "外觀",
          "Picture2": "",
          "Picdescribe2": "",
          "Picture3": "",
          "Picdescribe3": "",
          "Px": 120.970365,
          "Py": 23.935199,
          "Class": "4",
          "Map": "",
          "Spec": "",
          "Serviceinfo": "",
          "Parkinginfo": "車位:小客車0輛、機車0輛、大客車0輛",
          "TotalNumberofRooms": null,
          "LowestPrice": null,
          "CeilingPrice": null,
          "TaiwanHost": null,
          "IndustryEmail": null,
          "TotalNumberofPeople": null,
          "AccessibilityRooms": null,
          "PublicToilets": null,
          "LiftingEquipment": null,
          "ParkingSpace": null*/
//            -------------------------解析JSON-----------------------
            mList = new ArrayList<Map<String, Object>>();
            JSONObject json_obj1 = new JSONObject(Task_opendata);
            JSONObject json_obj2 = json_obj1.getJSONObject("XML_Head");
            JSONObject infos = json_obj2.getJSONObject("Infos");
            JSONArray info = infos.getJSONArray("Info");
            total = 0;
            t_total = info.length(); //總筆數
            //------JSON 排序----------------------------------------
            info = sortJsonArray(info);
            total = info.length(); //有效筆數
            t_count.setText(getString(R.string.ncount) + total + "/" + t_total);
//----------------------------------------------------------
            //-----開始逐筆轉換-----
            total = info.length();
            t_count.setText(getString(R.string.ncount) + total);
            for (int i = 0; i < info.length(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                String Name = info.getJSONObject(i).getString("Name");
                String Description = info.getJSONObject(i).getString("Description");
                String Add = info.getJSONObject(i).getString("Add");
                String Picture1 = info.getJSONObject(i).getString("Picture1");
                String Zipcode = info.getJSONObject(i).getString("Zipcode"); //郵遞區號,
                String Website = info.getJSONObject(i).getString("Website"); //商家網頁,
                item.put("Name", Name);
                item.put("Description", Description);
                item.put("Add", Add);
                item.put("Picture1", Picture1);
                item.put("Zipcode", Zipcode);
                item.put("Website", Website);
                mList.add(item);
//-------------------
            }
            t_count.setText(getString(R.string.ncount) + total + "/" + t_total);


        }catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private JSONArray sortJsonArray(JSONArray array) {
        ArrayList<JSONObject> jsons = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            Collections.sort(jsons, new Comparator<JSONObject>() {

                @Override
                public int compare(JSONObject jsonOb1, JSONObject jsonOb2) {
                    // 用多重key 排序
                    String lidCounty = "", ridCounty = "";
                    String lidSiteName = "", ridSiteName = "";
//                String lidPM25="",ridPM25="";
                    try {
                        lidCounty = jsonOb1.getString("Zipcode");
                        ridCounty = jsonOb2.getString("Zipcode");

//                            lidSiteName = jsonOb1.getString("SiteName");
//                            ridSiteName = jsonOb2.getString("SiteName");
//                    整數判斷方法
//                    if(!jsonOb1.getString("PM2.5").isEmpty()&&!jsonOb2.getString("PM2.5").isEmpty()
//                            &&!jsonOb1.getString("PM2.5").equals("ND")&&!jsonOb2.getString("PM2.5").equals("ND")){
//                        lidPM25=String.format("%02d",Integer.parseInt(jsonOb1.getString("PM2.5")));
//                        ridPM25=String.format("%02d",Integer.parseInt(jsonOb2.getString("PM2.5")));
//                    }else{
//                        lidPM25="0";
//                        ridPM25="0";
//                    }
                    } catch (JSONException jsone) {
                        jsone.printStackTrace();
                    }
//                return lidCounty.compareTo(ridCounty)+lidSiteName.compareTo(ridSiteName);
                    return lidCounty.compareTo(ridCounty);
                }
            }
            );
        return new JSONArray(jsons);
    }



private class TransTask extends AsyncTask<String, Void, String> {
    String ans;

    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(params[0]);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            String line = in.readLine();
            while (line != null) {
                sb.append(line);
                line = in.readLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ans = sb.toString();
        //------------
        return ans;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        parseJson(s);
    }

    private void parseJson(String s) {
    }

}

    @Override
    protected void onStop() {
        super.onStop();
    }

 //    -----------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_top:
                nowposition = 0;
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_next:
                nowposition = nowposition + 100;
                if (nowposition > total - 1) {
                    nowposition = total - 1;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_back:
                nowposition = nowposition - 100;
                if (nowposition < 0) {
                    nowposition = 0;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_end:
                nowposition = total - 1;
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_load:
                onSwipeToFresh.onRefresh();  //開始轉圈下載資料
                break;
            case R.id.action_settings:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}