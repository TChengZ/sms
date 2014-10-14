package com.jackchan.sms;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
/*
 * 修改短信内容弹出框
 * @author:jack chan
 */
public class ChangeSMSWindow extends PopupWindow{
	
	private Context context;
	private EditText editText;
	private Button btnOk;
	private Button btnCancel;
	private ContentValues cv = new ContentValues(); //存放修改信息
	private String body = null;//短信内容
	private long id = -1;//短信编号
	
	public ChangeSMSWindow(Context context, String body, long id) {
		super(context);
		this.context = context;
		this.body = body;
		this.id = id;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog, null);
		setContentView(view);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		editText = (EditText)view.findViewById(R.id.edittext);
		btnOk = (Button)view.findViewById(R.id.btnOk);
		btnCancel = (Button)view.findViewById(R.id.btnCancel);
		this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		editText.setText(body);
		btnClick click = new btnClick();
		btnOk.setOnClickListener(click);
		btnCancel.setOnClickListener(click);
	}
	
	private class btnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == R.id.btnOk){
				 cv.put(StaticValues.BODY, editText.getText().toString());
					context.getContentResolver().update(Uri.parse(SMS.url), 
							cv, StaticValues._ID + "=?", new String[]{id+""});
					if(okClick != null)
						okClick.dataChange();
			}
			if(isShowing())
				dismiss();
		}
		
	}
	
	public interface onOkClick{
		void dataChange();
	}
	
	private onOkClick okClick;


	public void setOkClick(onOkClick okClick) {
		this.okClick = okClick;
	}
}
