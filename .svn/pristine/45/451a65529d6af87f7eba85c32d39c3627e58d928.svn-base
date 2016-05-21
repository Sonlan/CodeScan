package com.example.qr_codescan;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.LocationService;
import com.google.gson.JsonObject;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	/**
	 * 弹出选项菜单
	 */
	private Button funcMenu;
	/**
	 * funcMenu选择结果
	 */
	private String setting = "query";
	/**
	 * 定位功能
	 */
	private LocationService locationService;
	/**
	 * 地址信息
	 */
	private String addr;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		funcMenu = (Button) findViewById(R.id.FuncSelect);
		//点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		//扫描完了之后调到该界面
		Button mButton = (Button) findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
		funcMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PopupMenu popupMenu = new PopupMenu(MainActivity.this, funcMenu);
				popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
				popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						case R.id.menu_logistic:
							setting = "logistic";
							break;
						case R.id.menu_query:
							setting = "query";
							break;
						case R.id.menu_custom:
							setting = "custom";
							break;
						default:
							setting = "query";
							break;
						}
						Log.i(this.getClass().toString(),setting );
						Toast.makeText(getApplicationContext(), setting, Toast.LENGTH_SHORT).show();
						return true;
					}
				});
				if("logistic".equals(setting))
					popupMenu.getMenu().findItem(R.id.menu_logistic).setChecked(true);
				else if("query".equals(setting)) 
					popupMenu.getMenu().findItem(R.id.menu_query).setChecked(true);
				else if("custom".equals(setting))
					popupMenu.getMenu().findItem(R.id.menu_custom).setChecked(true);
				popupMenu.show();
			}
		});
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				//定位(此处不一定来得及执行，主要还是onStart方法)
				locationService.start();
				GetRemoteDate getRemoteDate = new GetRemoteDate( data.getExtras().getString("result"),setting,addr);
				getRemoteDate.start();
			}
			break;
		}
    }	
	
	/***
	 * Stop location service
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		locationService.unregisterListener(mListener); //注销掉监听
		locationService.stop(); //停止定位服务
		super.onStop();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// -----------location config ------------
		locationService = ((LocationApplication) getApplication()).locationService; 
		//获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationService.registerListener(mListener);
		//注册监听
		int type = getIntent().getIntExtra("from", 0);
		if (type == 0) {
			locationService.setLocationOption(locationService.getDefaultLocationClientOption());
		} else if (type == 1) {
			locationService.setLocationOption(locationService.getOption());
		}
		locationService.start();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	/*****
	 * @see copy funtion to you project
	 * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 *
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
			/*	StringBuffer sb = new StringBuffer(256);
				sb.append("time : ");
				/**
				 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
				 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
				 */
			/*	sb.append(location.getTime());
				sb.append("\nerror code : ");
				sb.append(location.getLocType());
				sb.append("\nlatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nradius : ");
				sb.append(location.getRadius());
				sb.append("\nCountryCode : ");
				sb.append(location.getCountryCode());
				sb.append("\nCountry : ");
				sb.append(location.getCountry());
				sb.append("\ncitycode : ");
				sb.append(location.getCityCode());
				sb.append("\ncity : ");
				sb.append(location.getCity());
				sb.append("\nDistrict : ");
				sb.append(location.getDistrict());
				sb.append("\nStreet : ");
				sb.append(location.getStreet());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\nDescribe: ");
				sb.append(location.getLocationDescribe());
				sb.append("\nDirection(not all devices have value): ");
				sb.append(location.getDirection());
				sb.append("\nPoi: ");
				if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
					for (int i = 0; i < location.getPoiList().size(); i++) {
						Poi poi = (Poi) location.getPoiList().get(i);
						sb.append(poi.getName() + ";");
					}
				}
				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
					sb.append("\nspeed : ");
					sb.append(location.getSpeed());// 单位：km/h
					sb.append("\nsatellite : ");
					sb.append(location.getSatelliteNumber());
					sb.append("\nheight : ");
					sb.append(location.getAltitude());// 单位：米
					sb.append("\ndescribe : ");
					sb.append("gps定位成功");
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
					// 运营商信息
					sb.append("\noperationers : ");
					sb.append(location.getOperators());
					sb.append("\ndescribe : ");
					sb.append("网络定位成功");
				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
					sb.append("\ndescribe : ");
					sb.append("离线定位成功，离线定位结果也是有效的");
				} else if (location.getLocType() == BDLocation.TypeServerError) {
					sb.append("\ndescribe : ");
					sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					sb.append("\ndescribe : ");
					sb.append("网络不同导致定位失败，请检查网络是否通畅");
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					sb.append("\ndescribe : ");
					sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				}*/
				addr = location.getAddrStr();
			}
		}

	};
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 在这里可以进行UI操作
				String result =  msg.getData().get("response").toString();
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if("query".equals(setting)){
					try {
						if("1".equals(jsonObj.getString("statuscode"))){
							Bundle bundle = new Bundle();
							bundle.putString("result",result);
							Intent intent = new Intent(MainActivity.this,ShowActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}else Toast.makeText(getApplicationContext(), jsonObj.getString("parameter"), Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "获取数据失败，请稍后重试", Toast.LENGTH_SHORT).show();
					}
				}else{
					try {
						Toast.makeText(getApplicationContext(), jsonObj.getString("parameter"), Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "获取数据失败，请稍后重试", Toast.LENGTH_SHORT).show();
					}
					
				}
				locationService.stop();
				
				break;
			default:
				break;
			}
		}

	};


	
	class GetRemoteDate extends Thread{
		private String key;
		private String setting;
		private String addr;

		public  GetRemoteDate(String key,String setting,String addr) {
			this.key = key;
			this.setting = setting;
			this.addr = addr;
		}
		@Override
		public void run() {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://192.168.191.1:8099/TobaccoTracings/servlet/TestExtUtils");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("data", key));
			params.add(new BasicNameValuePair("setting", setting));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			params.add(new BasicNameValuePair("date", df.format(new Date())));
			params.add(new BasicNameValuePair("addr", addr));
			
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
				httpPost.setEntity(entity);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// 请求和响应都成功了
					HttpEntity httpentity = httpResponse.getEntity();
					String response = EntityUtils.toString(httpentity, "utf-8");
					Message message = new Message();
					message.what = 0;
					Bundle bundle = new Bundle();
					bundle.putString("response", response);
					message.setData(bundle);
					handler.sendMessage(message);
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
