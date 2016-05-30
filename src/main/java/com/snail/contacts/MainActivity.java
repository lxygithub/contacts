package com.snail.contacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.main_lv)
    ListView mMainLv;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initData();
        initEvent();
        //注册上下文菜单
        registerForContextMenu(mMainLv);
    }

    private void initData() {
        ContactDao contactDao = new ContactDao(this);
        List<Person> persons = contactDao.findAllContacts();
        mAdapter = new MyAdapter(persons, this);
        this.mMainLv.setAdapter(mAdapter);
    }

    private void initEvent() {
        this.mMainLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person = (Person) mAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + person.getPhone()));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,0,0,"添加");
        menu.add(0,1,0,"删除");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Toast.makeText(this,"添加",Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(this,"删除",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private final class MyAdapter extends BaseAdapter {

        private List<Person> mData;
        private Context mContext;

        public MyAdapter(List<Person> data, Context context) {
            mData = data;
            mContext = context;
        }

        @Override
        public int getCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(mContext, R.layout.contact_item,null);
                holder.name= (TextView) convertView.findViewById(R.id.tv_name);
                holder.phone= (TextView) convertView.findViewById(R.id.tv_phone);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            Person person = mData.get(position);
            holder.phone.setText(person.getPhone());
            holder.name.setText(person.getName());
            return convertView;
        }

        private class ViewHolder{
            TextView name,phone;
        }

    }

}
