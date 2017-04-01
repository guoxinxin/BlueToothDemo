package com.dev.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.open)
    Button open;
    @Bind(R.id.close)
    Button close;
    @Bind(R.id.scan)
    Button scan;
//    @Bind(R.id.recycle)
//    RecyclerView recycle;

    private BluetoothAdapter adapter;
    private MyBlueToothReceiver receiver;
    private ArrayList<BluetoothDevice>arrayList;
    private BlueAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBlueTooth();
//        initRecycler();
        //4.设置广播过滤器
        receiver=new MyBlueToothReceiver();
        IntentFilter intetnFilter=new IntentFilter();
        intetnFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描的广播
        intetnFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描完成的广播事件
        intetnFilter.addAction(BluetoothDevice.ACTION_FOUND);//找到可用的蓝牙设备的广播事件
        registerReceiver(receiver, intetnFilter);
    }

//    private void initRecycler() {
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recycle.setAdapter(rvAdapter=new BlueAdapter());
//        if(rvAdapter==null){}
//
//    }

    private void initBlueTooth() {
        //1.建立蓝牙初始化
        adapter = BluetoothAdapter.getDefaultAdapter();
    }
//3.创建广播接受者
    private  class MyBlueToothReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())){
            //开始扫描广播
            Toast.makeText(getApplicationContext(),"开始扫描",Toast.LENGTH_SHORT).show();
        }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
            //扫描结束
            Toast.makeText(getApplicationContext(),"扫描结束",Toast.LENGTH_SHORT).show();
        }else if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
            //找到了蓝牙设备
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(arrayList == null) arrayList = new ArrayList<BluetoothDevice>();
            arrayList.add(device);//将蓝牙设备添加到集合

        }
    }
}

    @OnClick({R.id.open, R.id.close, R.id.scan})
    public void onClick(View view) {
        //2.蓝牙地操作
        switch (view.getId()) {
            case R.id.open:
                if(adapter.isEnabled())return ;
                adapter.enable();//打开蓝牙
                break;
            case R.id.close:
                if(!adapter.isEnabled())return ;
                adapter.disable();//关闭蓝牙
                break;
            case R.id.scan:
                adapter.startDiscovery();//开始扫描
                //讲扫描到的设备加载到集合中，然后进行显示
//                if (arrayList != null) {
//                    arrayList.clear();
//                    adapter.setData(arrayList);
//                    myBlueToothAdapter.notifyDataSetChanged();
//                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

     class BlueAdapter extends RecyclerView.Adapter<BlueAdapter.BlueHolder>{


        @Override
        public BlueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BlueHolder holder=new BlueHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.detail_blue,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(BlueHolder holder, int position) {
             holder.tv.setText(arrayList.get(position)+"null");}

        @Override
        public int getItemCount() {
            if(arrayList==null){return 1;}
            return arrayList.size();
        }
        class BlueHolder extends RecyclerView.ViewHolder {
            private TextView tv;

            public BlueHolder(View itemView) {
                super(itemView);
                tv= (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
