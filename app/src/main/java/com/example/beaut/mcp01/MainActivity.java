package com.example.beaut.mcp01;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.beaut.mcp01.dao.PhpDataAccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String jsonResult;

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "epi_id";
    private static final String TAG_NAME = "epi_nm";
    private static final String TAG_DT ="create_dt";

    JSONArray resultData = null;
    ArrayList<HashMap<String, String>> epiList;

    ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.listView);
        epiList = new ArrayList<HashMap<String,String>>();

        String dataFilePath = "db_retrieveEpiInfo.php";     // DB Access하는 php 파일명
        String psmt = "select * from epi_info";             // Query

        try {
            // phpDao는 AsyncTask이므로 jsonResult 객체를 필요로 하는 showList() 메소드가 항상 잘 호출 되는지 확인 필요
            PhpDataAccess phpDao = new PhpDataAccess();
            jsonResult = phpDao.execute(dataFilePath, psmt).get();
        } catch(Exception e) {
            e.printStackTrace();
        }

        showList();

        inflateLayout();

    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(jsonResult);
            resultData = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<resultData.length();i++){
                JSONObject c = resultData.getJSONObject(i);
                String epiId = c.getString(TAG_ID);
                String epiNm = c.getString(TAG_NAME);
                String createDt = c.getString(TAG_DT);

                HashMap<String,String> resultMap = new HashMap<String,String>();

                resultMap.put(TAG_ID,epiId);
                resultMap.put(TAG_NAME,epiNm);
                resultMap.put(TAG_DT,createDt);

                epiList.add(resultMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, epiList, R.layout.list_item,
                    new String[]{TAG_ID,TAG_NAME,TAG_DT},
                    new int[]{R.id.epiId, R.id.epiNm, R.id.createDt}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void inflateLayout() {
        LinearLayout footerTabsLayout = (LinearLayout)findViewById(R.id.footertabs);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.footer_tabs, footerTabsLayout, true);

        TextView footertab1 = (TextView)findViewById(R.id.footertab1);
    }

}
