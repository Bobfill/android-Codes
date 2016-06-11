package one.socketdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import bl.BubbleLayout;
import bl.BubblePopupHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.button2)
    Button button2;
    @Bind(R.id.editText2)
    EditText editText2;
    @Bind(R.id.sentButton1)
    Button sentButton1;
    @Bind(R.id.sentButton2)
    Button sentButton2;
    @Bind(R.id.spinner)
    Spinner spinner;

    Socket socket1, socket2;
     String URL_PATH="192.168.142.1";
//    String URL_PATH = "10.248.145.150";
   // String URL_PATH = "";

    Integer SOCKET_PORT = 50000;//端口号
    ClientThread1 clientThread1;
    ClientThread2 clientThread2;
    mHandler myHandler = new mHandler();
    BufferedReader bufferedReader1, bufferedReader2;
    DataOutputStream daout;

    private List<MyMessage> msglist = new ArrayList<MyMessage>();
    RecyclerView mRecyclerView;
    RvAdapter_chat rvAdapter;
    public String username="USER_ONE";
    private PopupWindow popupWindow;
    boolean sLive=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //下拉spinner选择当前用户
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                username = MainActivity.this.getResources().getStringArray(R.array.user)[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                username ="匿名";
            }
        });
        textView2.setText("群聊");
        //bl_arrowDirection
       // View rootView= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_view2, null);

        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.item_view2, null);
        popupWindow = BubblePopupHelper.create(this, bubbleLayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        rvAdapter=new RvAdapter_chat(this, msglist);
        mRecyclerView.setAdapter(rvAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        sentButton2.setVisibility(View.INVISIBLE);//单机测试的时候注释掉该行代码
        editText2.setVisibility(View.INVISIBLE);//单机测试的时候注释掉该行代码
        button2.setVisibility(View.INVISIBLE);//单机测试的时候注释掉该行代码
    }

    @OnClick({R.id.button1, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                //新建一个线程运行socket1连接
                new mThread1().start();
                sLive=true;
                break;
            case R.id.button2:
                //新建一个线程运行socket2连接
                new mThread2().start();
                break;
        }
    }

    @OnClick({R.id.sentButton1, R.id.sentButton2})
    public void onClick2(View view) {
        switch (view.getId()) {
            case R.id.sentButton1:
                try {
                    if (sLive==false) {
                        Toast.makeText(MainActivity.this, "连接失败，请检查创建连接！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    daout = new DataOutputStream(socket1.getOutputStream());
                    String eds1 = editText1.getText().toString();
                    eds1=username+eds1+"\n";
                    //daout.writeUTF("USER_ONE" + eds1 + "\n");//测试用户1
                    daout.writeUTF(eds1);
                    daout.flush();
                    //  daout.close();
                } catch (IOException E) {
                    E.printStackTrace();
                }
                break;
            case R.id.sentButton2:
                try {
                    daout = new DataOutputStream(socket2.getOutputStream());
                    // String ss1=new String("USER_TWO"+"请求访问2！\n".toString().getBytes("GBK"));
                    String eds2 = editText2.getText().toString();
                    daout.writeUTF("USER_TWO" + eds2 + "\n");
                    daout.flush();
                    //     daout.close();
                } catch (IOException E) {
                    E.printStackTrace();
                }
                break;
        }
    }

    //UI更新
    private class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
         //   rvAdapter.notifyDataSetChanged();
            rvAdapter.notifyItemInserted(msglist.size());//更新最新消息

            mRecyclerView.smoothScrollToPosition(msglist.size());//显示到最新的消息条目

            switch (msg.arg1) {
                case 1:
                    Bundle bundle = msg.getData();            //获取Message中发送过来的数据
                    String content = bundle.getString("content_key");
                    textView1.setText(content);
                    System.out.print("hander1：" + content + "\n");
                    break;
                case 2:
                    Bundle bundle2 = msg.getData();            //获取Message中发送过来的数据
                    String content2 = bundle2.getString("content_key2");
                    textView3.setText(content2);
                    System.out.println("hander2：" + content2 + "\n");
                    break;
                default:
                   // System.out.println("stupid debug!");
                    break;
            }
        }
    }

    //对接收到的群发消息的处理线程
    private class ClientThread1 extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                String content = null;
                //不断接受消息
                while ((content = bufferedReader1.readLine()) != null) {
                    MyMessage myMessage = new MyMessage();
                    myMessage.setContent(content);
                    msglist.add(myMessage);//将数据保存到MyMessage对象中
                    //   rvAdapter.notifyDataSetChanged();//更新数据源(在非UI线程更新UI会出问题！！！)
                    Bundle bundle = new Bundle();
                    bundle.putString("content_key", content);
                    System.out.println(("获取广播内容1：" + content));
                    Message msg = new Message();
                    msg.setData(bundle);            //将数据封装到Message对象中
                    msg.arg1 = 1;
                    myHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientThread2 extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                String content = null;
                while ((content = bufferedReader2.readLine()) != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("content_key2", content);

                    MyMessage myMessage = new MyMessage();
                    myMessage.setContent(content);
                    msglist.add(myMessage);//将数据保存到MyMessage对象中

                    // rvAdapter.notifyDataSetChanged();//更新数据源

                    Message msg = new Message();
                    msg.setData(bundle);     //将数据封装到Message对象中
                    msg.arg1 = 2;
                    myHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //初始化socket1
    private void initSocket() {
        try {
            socket1 = new Socket(URL_PATH, SOCKET_PORT);            //用户1的客户端Socket
            System.out.println("成功创建socket");
            clientThread1 = new ClientThread1();        //客户端启动ClientThread线程，读取来自服务器的数据
            System.out.println("成功创建thread");
            bufferedReader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            clientThread1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //初始化socket2
    private void initSocket2() {
        try {
            socket2 = new Socket(URL_PATH, SOCKET_PORT);            //用户2的客户端Socket
            //客户端启动ClientThread线程，读取来自服务器的数据
            clientThread2 = new ClientThread2();
            bufferedReader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            clientThread2.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //网络线程1
    private class mThread1 extends Thread {
        @Override
        public void run() {
            initSocket();
        }
    }

    //网络线程2
    private class mThread2 extends Thread {
        @Override
        public void run() {
            initSocket2();
        }
    }

    //注意关闭流数据流后再关闭socket连接
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            bufferedReader1.close();
            bufferedReader2.close();
            daout.close();
            socket1.close();
            socket2.close();
        } catch (IOException e) {
            System.out.print("somting close error! ---->");
            e.printStackTrace();
        }
    }
}
