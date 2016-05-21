package com.example.qr_codescan;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ShowActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show);
		
		TextView textview_warnInfo = (TextView) findViewById(R.id.textview_warnInfo);
		TextView textview_pName = (TextView) findViewById(R.id.textview_pName);
		TextView textview_gp = (TextView) findViewById(R.id.textview_gp);
		TextView textview_pd = (TextView) findViewById(R.id.textview_pd);
		TextView textview_price = (TextView) findViewById(R.id.textview_price);
		TextView textview_targetAddr = (TextView) findViewById(R.id.textview_targetAddr);
		
		Button backbtn = (Button) findViewById(R.id.button_back1);
		
		backbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowActivity.this.finish();
			}
		});
		Intent intent = getIntent();
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(intent.getExtras().getString("result"));
			//if(jsonObj.getString("statuscode"))
			jsonObj = new JSONObject(jsonObj.getString("parameter"));
			if(null == jsonObj.getString("parameter")){
				textview_warnInfo.setVisibility(View.VISIBLE);
				textview_warnInfo.setTextSize(25f);
				textview_warnInfo.setText("未查询到符合查询条件的产品，该产品为假冒产品，举报电话为027-88888889");
			}else{
				if(jsonObj.getString("state").equals("已消费")){
					textview_warnInfo.setVisibility(View.VISIBLE);
					textview_warnInfo.setText("您所查询的产品已被消费，消费时间为："+jsonObj.getString("consumeTime")
									+",消费地点为："+jsonObj.getString("consumeAddr"));
				}
				textview_pName.setText(jsonObj.getString("pName"));
				textview_gp.setText(jsonObj.getString("gp"));
				textview_pd.setText(jsonObj.getString("pd"));
				textview_price.setText(jsonObj.getString("price"));
				textview_targetAddr.setText(jsonObj.getString("targetAddr"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
