package com.jackchan.sms;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jackchan.sms.ChangeSMSWindow.onOkClick;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SMS extends Activity {
	
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(); //已发送信息列表
	private SMSAdapter adapter;
	private Button btnAll;
	private Button btnInbox;
	private Button btnSend;
	private Button btnDraft;
	public static String url;
	private ListView listView;
	private final String SMS_URI_ALL   = "content://sms/";      
	private final String SMS_URI_INBOX = "content://sms/inbox";    
	private final String SMS_URI_SEND  = "content://sms/sent";    
	private final String SMS_URI_DRAFT = "content://sms/draft";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initComponet();
        setBtnClick();
        url = SMS_URI_ALL;//默认获取全部短信
        adapter = new SMSAdapter(this, getSmsInPhone());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ChangeSMSWindow change = new ChangeSMSWindow(SMS.this, list.get(arg2).get(StaticValues.BODY).toString(),
						(Long) list.get(arg2).get(StaticValues._ID));
				change.setOkClick(new onOkClick() {
					
					@Override
					public void dataChange() {
						listChange();
					}
				});
				change.showAtLocation(listView,
						//LayoutInflater.from(context).inflate(R.layout.main, null), 
						Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 
						0, 0);
			}
		
        });
    }

    private void initComponet(){
    	listView = (ListView)findViewById(R.id.listview);
        btnAll = (Button)findViewById(R.id.btnAll);
        btnInbox = (Button)findViewById(R.id.btnInbox);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnDraft = (Button)findViewById(R.id.btnDraft);
    }
    
    private void setBtnClick(){
    	btnAll.setOnClickListener(click);
    	btnInbox.setOnClickListener(click);
    	btnSend.setOnClickListener(click);
    	btnDraft.setOnClickListener(click);
    }
    /*
     * listview数据发生改变
     */
    private void listChange(){
    	adapter.clearList();
		adapter.changeList(getSmsInPhone());
		adapter.notifyDataSetChanged();
    }
    private btnClick click = new btnClick();
    private class btnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnAll){
				url = SMS_URI_ALL;
			}
			else if(v == btnInbox){
				url = SMS_URI_INBOX;
			}
			else if(v == btnSend){
				url = SMS_URI_SEND;
			}
			else if(v == btnDraft){
				url = SMS_URI_DRAFT;
			}
			listChange();
		}
    	
    }
    /*
     * 获取指定类型短信
     */
    public List<Map<String, Object>> getSmsInPhone()    
    {    
        try{    
            ContentResolver cr = getContentResolver();    
            String[] projection = new String[]{"_id", "address", "person",     
                    "body", "date", "type"};    
            Uri uri = Uri.parse(url);    
            Cursor cur = cr.query(uri, projection, null, null, "date desc");    
       
            if (cur.moveToFirst()) {
            	long id;
                String name;     
                String phoneNumber;           
                String smsbody;    
                String date;    
                String type;    
                
                int idColumn = cur.getColumnIndex(StaticValues._ID);
                int nameColumn = cur.getColumnIndex(StaticValues.PERSON);    
                int phoneNumberColumn = cur.getColumnIndex(StaticValues.ADDRESS);    
                int smsbodyColumn = cur.getColumnIndex(StaticValues.BODY);    
                int dateColumn = cur.getColumnIndex(StaticValues.DATE);    
                int typeColumn = cur.getColumnIndex(StaticValues.TYPE);    
                 
                do{ 
                	id = cur.getLong(idColumn);
                    name = cur.getString(nameColumn);                 
                    phoneNumber = cur.getString(phoneNumberColumn);    
                    smsbody = cur.getString(smsbodyColumn);    
                        
                    SimpleDateFormat dateFormat = new SimpleDateFormat(    
                            "yyyy-MM-dd hh:mm:ss");    
                    Date d = new Date(Long.parseLong(cur.getString(dateColumn)));    
                    date = dateFormat.format(d);    
                        
                    int typeId = cur.getInt(typeColumn);    
                    if(typeId == 1){    
                        type = "inbox";    
                    } else if(typeId == 2){    
                        type = "sent";    
                    }else if(typeId == 3){    
                        type = "draft";    
                    } else {    
                        type = "";    
                    }
                    if(smsbody == null) 
                    	smsbody = "";
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(StaticValues._ID, id);
                    map.put(StaticValues.PERSON, name);
                    map.put(StaticValues.ADDRESS, phoneNumber);
                    map.put(StaticValues.BODY, smsbody);
                    map.put(StaticValues.DATE, date);
                    map.put(StaticValues.TYPE, type);
                    list.add(map);
                }while(cur.moveToNext());  
                cur.close();
            }   
        } catch(SQLiteException ex) {    
            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());    
        }    
        return list;    
    }   
}
