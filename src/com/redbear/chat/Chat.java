package com.redbear.chat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Chat extends Activity {
	private final static String TAG = Chat.class.getSimpleName();

	public static final String EXTRAS_DEVICE = "EXTRAS_DEVICE";
	private Button shotButton;
	private String mDeviceName;
	private String mDeviceAddress;
	private RBLService mBluetoothLeService;
	private Map<UUID, BluetoothGattCharacteristic> map = new HashMap<UUID, BluetoothGattCharacteristic>();

	//eArchery Data
	private ArrayList<String> accelData;
    private ArrayList<String> allResults;
	private boolean record = false;
	long startTime = 0;
	private ListView listView;

	//notifaction for watch
	private static final int MY_NOTIFICATION_ID=1;
	private NotificationManager notificationManager;
	private Notification myNotification;
	private int currentPic;

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((RBLService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (RBLService.ACTION_GATT_DISCONNECTED.equals(action)) {
			} else if (RBLService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				getGattService(mBluetoothLeService.getSupportedGattService());
			} else if (RBLService.ACTION_DATA_AVAILABLE.equals(action)) {
				displayData(intent.getByteArrayExtra(RBLService.EXTRA_DATA));
			}
		}
	};
	void startNotification(String message){
		int notificationId = 001;
// Build intent for notification content
		Intent viewIntent = new Intent(this, Chat.class);
		//viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
		PendingIntent viewPendingIntent =
				PendingIntent.getActivity(this, 0, viewIntent, 0);

		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.redbear)
						.setContentTitle("eArchery Practice")
						.setContentText("Swipe Right")
						.setContentIntent(viewPendingIntent)
						.addAction(R.drawable.redbear,
								message + "Start", viewPendingIntent);

// Get an instance of the NotificationManager service
		NotificationManagerCompat notificationManager =
				NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
		notificationManager.notify(notificationId, notificationBuilder.build());
	}
	void stopNotification(){
		int notificationId = 001;
// Build intent for notification content
		Intent viewIntent = new Intent(this, Chat.class);
		//viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
		PendingIntent viewPendingIntent =
				PendingIntent.getActivity(this, 0, viewIntent, 0);

		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.redbear)
						.setContentTitle("eArchery Practice")
						.setContentText("Swipe Right")
						.setContentIntent(viewPendingIntent)
						.addAction(R.drawable.stop,
								"Stop", viewPendingIntent);

// Get an instance of the NotificationManager service
		NotificationManagerCompat notificationManager =
				NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
		notificationManager.notify(notificationId, notificationBuilder.build());
	}
    private void exportData(){
        String export = "";
        for(int i=0;i<accelData.size();i++){
            export += (accelData.get(i) + "!");
        }
        String result = null;
        //make request
        new GetRequest().execute(export,null,result);

        /* old method
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, export);
        startActivity(intent);*/

        //clear local data
        accelData = new ArrayList<String>();
    }
    private void goodRelease(){
        allResults.add("Good Release");
        String[] values = new String[allResults.size()];
        for(int i=0;i<allResults.size();i++){
            values[i] = allResults.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        startNotification("Good! ");
    }
    private void deadRelease(){
        allResults.add("Dead Release");
        String[] values = new String[allResults.size()];
        for(int i=0;i<allResults.size();i++){
            values[i] = allResults.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        startNotification("Dead... ");
    }
    private void pluckRelease(){
        allResults.add("Pluck Release");
        String[] values = new String[allResults.size()];
        for(int i=0;i<allResults.size();i++){
            values[i] = allResults.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        startNotification("Pluck... ");
    }
    private void unknownRelease(String result){
        allResults.add(result);
        String[] values = new String[allResults.size()];
        for(int i=0;i<allResults.size();i++){
            values[i] = allResults.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        startNotification("??? ");
    }
    private String analysisRequest(String export){
        // Instantiate the RequestQueue.
        final String finalExport = export;
        /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {*/
                HttpClient client = new DefaultHttpClient();
                String query = "not encoded yet";
                try {
                    query = URLEncoder.encode(finalExport, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String url = "https://earchery.herokuapp.com?raw=" + query;
                HttpGet request = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    if(entity!=null){
                        String responseBody = EntityUtils.toString(entity);
                        Log.d("get response", responseBody);
                        if(responseBody.contains("good")){
                            Log.i(TAG,"going in");
                            //goodRelease();
                            return "good";
                        }
                        else if(responseBody.contains("pluck")){
                            //pluckRelease();
                            return "pluck";
                        }
                        else if(responseBody.contains("dead")){
                            //deadRelease();
                            return "dead";
                        }
                        else{
                            return "?????";
                        }
                    }
                    //shotButton.setText(response.toString());
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            /*}
        });
        t.start();*/
        return null;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		startNotification("");
		accelData = new ArrayList<String>();
        allResults = new ArrayList<String>();
        listView = (ListView)findViewById(R.id.list_view);

		shotButton = (Button) findViewById(R.id.shot_button);
		shotButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(record == true) {
					//Toast.makeText(getApplicationContext(), "Stopped recording data", Toast.LENGTH_SHORT).show();
					record = false;
					shotButton.setText("Start Shot");
					Log.i("debug", accelData.toString());
					//export data
					exportData();
				}
				else{
					Toast.makeText(getApplicationContext(), "Started recording data", Toast.LENGTH_SHORT).show();
					record = true;
					shotButton.setText("Stop Shot");
					startTime = System.currentTimeMillis();
					Log.i("Start Time", ""+startTime);
				}
			}
		});

		Intent intent = getIntent();

		mDeviceAddress = intent.getStringExtra(Device.EXTRA_DEVICE_ADDRESS);
		mDeviceName = intent.getStringExtra(Device.EXTRA_DEVICE_NAME);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent gattServiceIntent = new Intent(this, RBLService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

	}
	protected void onNewIntent(Intent intent) {
		if(record == true){//stop recording
			record = false;
            //analyze data here
            exportData();
			//startNotification("");
		}
		else if(record == false){//start recording
			record = true;
			stopNotification();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			mBluetoothLeService.disconnect();
			mBluetoothLeService.close();

			System.exit(0);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(mGattUpdateReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBluetoothLeService.disconnect();
		mBluetoothLeService.close();

		System.exit(0);
	}
	String data = "";
	boolean isEndOfMessage = false;
	private void displayData(byte[] byteArray) {
		if (byteArray != null) {
			data += new String(byteArray);
			if(data.contains("*")){
				data = data.substring(0,data.length());
				accelData.add((System.currentTimeMillis() - startTime) + ": " + data);
				Log.i("debug", (System.currentTimeMillis() - startTime) + ": " + data);
				data = "";
			}
        }
	}

	private void getGattService(BluetoothGattService gattService) {
		if (gattService == null)
			return;

		BluetoothGattCharacteristic characteristic = gattService
				.getCharacteristic(RBLService.UUID_BLE_SHIELD_TX);
		map.put(characteristic.getUuid(), characteristic);

		BluetoothGattCharacteristic characteristicRx = gattService
				.getCharacteristic(RBLService.UUID_BLE_SHIELD_RX);
		mBluetoothLeService.setCharacteristicNotification(characteristicRx,
				true);
		mBluetoothLeService.readCharacteristic(characteristicRx);
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(RBLService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(RBLService.ACTION_DATA_AVAILABLE);

		return intentFilter;
	}

    class GetRequest extends AsyncTask<String, Void, String> {
        String result;
        @Override
        protected String doInBackground(String... params) {
            result = analysisRequest(params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            if(result=="good"){
                goodRelease();
            }
            else if(result=="dead"){
                deadRelease();
            }
            else if(result=="pluck"){
                pluckRelease();
            }
            else{
                unknownRelease(result);
            }
        }
    }
}