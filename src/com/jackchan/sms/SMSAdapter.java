package com.jackchan.sms;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SMSAdapter extends BaseAdapter{
	
	private Context context;//��ǰ����������
	private List<Map<String, Object>> list; //�ѷ�����Ϣ�б�
	private class ViewHolder{ 
		TextView person;
		TextView address;
		TextView body;
		TextView date;
		TextView type;
	}
	
	public SMSAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.list = list;
	}
	
	public void changeList(List<Map<String, Object>> list){
		this.list = list;
	}
	
	public void clearList(){
		list.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
			viewHolder.person = (TextView)convertView.findViewById(R.id.person);
			viewHolder.address = (TextView)convertView.findViewById(R.id.address);
			viewHolder.body = (TextView)convertView.findViewById(R.id.body);
			viewHolder.date = (TextView)convertView.findViewById(R.id.date);
			viewHolder.type = (TextView)convertView.findViewById(R.id.type);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		//��ȡ���Ż�����Ϣ
		viewHolder.person.setText("Name��" + list.get(position).get(StaticValues.PERSON));
		viewHolder.address.setText("Number��" + list.get(position).get(StaticValues.ADDRESS));
		viewHolder.body.setText("Content��" + list.get(position).get(StaticValues.BODY));
		viewHolder.date.setText("Date��" + list.get(position).get(StaticValues.DATE));
		viewHolder.type.setText("Type��" + list.get(position).get(StaticValues.TYPE));
		return convertView;
	}
}
