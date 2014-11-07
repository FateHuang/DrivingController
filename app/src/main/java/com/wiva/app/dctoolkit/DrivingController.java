package com.wiva.app.dctoolkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DrivingController extends Activity implements View.OnClickListener {
    Intent intent;//开启对话框
    Button[] b=new Button[6];
    Button save;
    CheckBox[] c=new CheckBox[6];
    static final byte DEFAULT_NUM=0,DEFAULT_DIRECTION=0;//出厂设置的值
    boolean[] mainCheckbox=new  boolean[6];//储存主界面checkbox的状态
    /**
     * 启动主界面时向遥控器发送“读”指令
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        save= (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *预留位置，调用Status提供读sendStatus方法，将数据发送出去
                 */
            }
        });
        b[0]= (Button) findViewById(R.id.b1);
        b[1]= (Button) findViewById(R.id.b2);
        b[2]= (Button) findViewById(R.id.b3);
        b[3]= (Button) findViewById(R.id.b4);
        b[4]= (Button) findViewById(R.id.b5);
        b[5]= (Button) findViewById(R.id.b6);
        for(int i=0;i<6;i++)
        {
            b[i].setOnClickListener(this);
        }

        c[0]= (CheckBox) findViewById(R.id.c1);
        c[1]= (CheckBox) findViewById(R.id.c2);
        c[2]= (CheckBox) findViewById(R.id.c3);
        c[3]= (CheckBox) findViewById(R.id.c4);
        c[4]= (CheckBox) findViewById(R.id.c5);
        c[5]= (CheckBox) findViewById(R.id.c6);
        /**
         * checkbox的监听器，用来处理按键是否有抑制功能
         */
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                Button buffer;
                int i;
                    switch (compoundButton.getId())
                    {
                        case R.id.c1:buffer=b[0];i=0;break;
                        case R.id.c2:buffer=b[1];i=1;break;
                        case R.id.c3:buffer=b[2];i=2;break;
                        case R.id.c4:buffer=b[3];i=3;break;
                        case R.id.c5:buffer=b[4];i=4;break;
                        case R.id.c6:buffer=b[5];i=5;break;
                        default:buffer=null;i=6;
                    }
                          if(ischecked)
                          {
                              buffer.setClickable(true);
                              /*
                              checkbox处于选中状态，按键具有抑制功能
                               */
                              mainCheckbox[i]=true;
                          }
                          else
                          {
                              /*
                              checkbox未被选中，按键不具有抑制功能，即除其本身外，其它全部为零
                               */
                              buffer.setClickable(false);
                              mainCheckbox[i]=false;
                              Status.setStatus(buffer.getId(),DEFAULT_NUM,DEFAULT_DIRECTION);
                              /**
                               * 测试用
                               */
                              TextView testText= (TextView) findViewById(R.id.testText);
                              byte[] test=Status.getStatus(R.id.b1);
                              String button1Num=String.valueOf(test[0]);
                              String button1Direction=String.valueOf(test[1]);
                              testText.setText(button1Num+"和"+button1Direction);
                              /**
                               * 测试用
                               */

                          }
            }
        };
        for(int i=0;i<6;i++) {
            c[i].setOnCheckedChangeListener(onCheckedChangeListener);
        }
        /**
         * 获取所有按键的status，并判断其是否全为零，若是则setchecked为false和setClickable为false，否则true，
         */
        byte[] allStatus=new byte[6];
        byte[] b1Status=Status.getStatus(R.id.b1);
        byte[] b2Status=Status.getStatus(R.id.b2);
        byte[] b3Status=Status.getStatus(R.id.b3);
        byte[] b4Status=Status.getStatus(R.id.b4);
        byte[] b5Status=Status.getStatus(R.id.b5);
        byte[] b6Status=Status.getStatus(R.id.b6);
        allStatus[0]= (byte) (b1Status[0]+b1Status[1]);
        allStatus[1]= (byte) (b2Status[0]+b2Status[1]);
        allStatus[2]= (byte) (b3Status[0]+b3Status[1]);
        allStatus[3]= (byte) (b4Status[0]+b4Status[1]);
        allStatus[4]= (byte) (b5Status[0]+b5Status[1]);
        allStatus[5]= (byte) (b6Status[0]+b6Status[1]);
        for(int i=0;i<allStatus.length;i++)
        {
            if(allStatus[i]==0)
            {
                c[i].setChecked(false);
                b[i].setClickable(false);
            }
            else
            {
                c[i].setChecked(true);
                b[i].setClickable(true);
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.driving_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuRestoration) {
            /**
             * 菜单栏的“恢复出厂设置”按钮
             * 执行恢复出厂设置的操作
             */
            for(int i=0;i<6;i++)
            {
                c[i].setChecked(false);
            }
            Toast.makeText(this,"设置成功，请点击保存按钮",Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id==R.id.menuInstruction){
            /**
             * 菜单栏“操作说明”按钮
             * 执行操作演示
             */
            ImageView reminder= (ImageView) findViewById(R.id.reminder);
            reminder.setVisibility(View.VISIBLE);
            return true;
        }else if(id==R.id.menuexit)
        {
            /**
             * 退出按钮
             */
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击按钮－>打开对话框，并将对应button的ID和获取到到status传给对话框
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id=view.getId();
        byte[] status=Status.getStatus(id);
        intent=new Intent(this,Dialog.class);
        intent.putExtra("id",id);
        intent.putExtra("status",status);
        intent.putExtra("mainCheck",mainCheckbox);
        startActivityForResult(intent, 0);
    }

    /**
     * requestCode==0&&resultCode==0，点击了“确定”按钮
     * requestCode==0&&resultCode==1，点击了“取消”按钮
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0&&resultCode==0)
        {
            /**
             * 获取byte数组，并进行运算
             * 然后传递数据给Status
             */
            byte[] num=data.getByteArrayExtra("num");
            byte[] direction=data.getByteArrayExtra("direction");
            /*
            将num数组和direction数组里的byte分别加起来，组成两个byte
             */
            byte numStatus=0,directionStatus=0;
            for(byte numByte:num)
            {
                numStatus+=numByte;
            }
            for(byte directionByte:direction)
            {
                directionStatus+=directionByte;
            }
            Status.setStatus(intent.getIntExtra("id",0),numStatus,directionStatus);
            /**
             * 测试用
             */
            TextView testText= (TextView) findViewById(R.id.testText);
            byte[] test=Status.getStatus(R.id.b1);
            String button1Num=String.valueOf(test[0]);
            String button1Direction=String.valueOf(test[1]);
            testText.setText(button1Num+"和"+button1Direction);
            /**
             * 测试用
             */
        }else if(requestCode==0&&resultCode==1)
        {
            /**
             * Do nothing
             */
        }
    }
    /**
     * reminder的点击回调方法，
     * 点击后让reminder不可见
     */
    public void onReminderClicked(View v)
    {
       v.setVisibility(View.GONE);
    }
}
