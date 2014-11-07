package com.wiva.app.dctoolkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Dialog extends Activity implements View.OnClickListener {
    Button cancel,confirm;
    static final byte ONE_ISCHECKED=1,TWO_ISCHECKED=2,THREE_ISCHECKED=4,FOUR_ISCHECKED=8,FIVE_ISCHECKED=16,SIX_ISCHECKED=32
                     ,UP_ISCHECKED=1,DOWN_ISCHECKED=2,EAST_ISCHECKED=4,WEST_ISCHECKED=8,SOUTH_ISCHECKED=16,NORTH_ISCHECKED=32,DEFAULT=0;
    //兩組checkbox
    CheckBox[] numView,directionView;
    Intent intent;
    /**
     * status用于接收主界面传过来的两个byte
     * num和direction用于存储每个checkbox的当前状态
     */
    private byte[] status,num,direction;

    /**
     * 该activity被运行，获取启动该activity的按钮的ID
     * 和按钮传过来的两个byte数据
     * 将byte数据分解为checkbox的状态，设定checkbox的初始勾选状态
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog1);
        num=new byte[6];            //储存6个数字checkbox的byte
        direction=new byte[6];      //储存6个方向checkbox的byte
        cancel= (Button) findViewById(R.id.cancel);
        confirm= (Button) findViewById(R.id.confirm);
        numView=new CheckBox[6];
        directionView=new CheckBox[6];
        directionView[0]= (CheckBox) findViewById(R.id.up);
        directionView[1]= (CheckBox) findViewById(R.id.down);
        directionView[2]= (CheckBox) findViewById(R.id.east);
        directionView[4]= (CheckBox) findViewById(R.id.south);
        directionView[3]= (CheckBox) findViewById(R.id.west);
        directionView[5]= (CheckBox) findViewById(R.id.north);
        numView[0]= (CheckBox) findViewById(R.id.one);
        numView[1]= (CheckBox) findViewById(R.id.two);
        numView[2]= (CheckBox) findViewById(R.id.three);
        numView[3]= (CheckBox) findViewById(R.id.four);
        numView[4]= (CheckBox) findViewById(R.id.five);
        numView[5]= (CheckBox) findViewById(R.id.six);
        /**
         * CheckBox的监听器
         */
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                /**
                 * 根据checkbox的状态选择byte数据
                 */
                if(isChecked)
                {
                    /**
                     * checkbox的状态为true时，根据它的id，选择对应的byte，将byte传给数组
                     */
                    switch (compoundButton.getId())
                    {
                        case R.id.one:      num[0]=ONE_ISCHECKED;break;
                        case R.id.two:      num[1]=TWO_ISCHECKED;break;
                        case R.id.three:    num[2]=THREE_ISCHECKED;break;
                        case R.id.four:     num[3]=FOUR_ISCHECKED;break;
                        case R.id.five:     num[4]=FIVE_ISCHECKED;break;
                        case R.id.six:      num[5]=SIX_ISCHECKED;break;
                        case R.id.up:       direction[0]=UP_ISCHECKED;break;
                        case R.id.down:     direction[1]=DOWN_ISCHECKED;break;
                        case R.id.east:     direction[2]=EAST_ISCHECKED;break;
                        case R.id.west:     direction[3]=WEST_ISCHECKED;break;
                        case R.id.south:    direction[4]=SOUTH_ISCHECKED;break;
                        case R.id.north:    direction[5]=NORTH_ISCHECKED;break;
                    }
                }else
                {
                    /**
                     * checkbox的状态为false的时候，byte＝00000000，将byte传给数组
                     */
                    switch (compoundButton.getId())
                    {
                        case R.id.one:      num[0]=DEFAULT;break;
                        case R.id.two:      num[1]=DEFAULT;break;
                        case R.id.three:    num[2]=DEFAULT;break;
                        case R.id.four:     num[3]=DEFAULT;break;
                        case R.id.five:     num[4]=DEFAULT;break;
                        case R.id.six:      num[5]=DEFAULT;break;
                        case R.id.up:       direction[0]=DEFAULT;break;
                        case R.id.down:     direction[1]=DEFAULT;break;
                        case R.id.east:     direction[2]=DEFAULT;break;
                        case R.id.west:     direction[3]=DEFAULT;break;
                        case R.id.south:    direction[4]=DEFAULT;break;
                        case R.id.north:    direction[5]=DEFAULT;break;
                    }
                }
            }
        };
        /**
         * 设置监听器
         */
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        for(int i=0;i<6;i++)
        {
            numView[i].setOnCheckedChangeListener(onCheckedChangeListener);
            directionView[i].setOnCheckedChangeListener(onCheckedChangeListener);
        }
        intent=getIntent();//intent用于获取与传递数据
        int id=intent.getIntExtra("id",0);
        status=intent.getByteArrayExtra("status");
        /**
         * 将接收到的status数组里的两个byte分别分解为6个byte，并调用setCheckbox方法，
         * 将12个byte分别传给num[]和direction[],并设置checkbox的勾选状态
         */
        setCheckbox("num",status[0]);
        setCheckbox("direction",status[1]);

        /**
         * 获取maincheck的状态，将false的设为unclickable
         */
        boolean[] mainCheck=intent.getBooleanArrayExtra("mainCheck");
        for (int i=0;i<6;i++) {
            numView[i].setClickable(mainCheck[i]);
        }
        /**
         * 用findViewbyId()找到对应的View，然后调用setClickable()将该View锁定
         */
        findViewById(transformId(id)).setClickable(false);
        this.setFinishOnTouchOutside(false);//用于设置点击对话框外围后，对话框是否会销毁
    }

    /**
     * 确定或取消键被点击后回调该方法
     * @param view 被点击的按键
     */
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.confirm){
            /**
             * 预留位置，将数组打包进intent
             */
        intent.putExtra("num",num);
        intent.putExtra("direction",direction);
        this.setResult(0,intent);
        }else{
            this.setResult(1,intent);
        }
        this.finish();
    }
    /**
     * 预留位置写一个setCheckbox方法，将byte分解，并传递给num[]或direction[]
     */
    public void setCheckbox(String category,byte state)
    {
        byte[] buffer=new byte[6];
        buffer[0]= (byte) (state&1);
        buffer[1]= (byte) (state&2);
        buffer[2]= (byte) (state&4);
        buffer[3]= (byte) (state&8);
        buffer[4]= (byte) (state&16);
        buffer[5]= (byte) (state&32);
        if(category.equals("num"))
        {
            for(int i=0;i<buffer.length;i++)
            {
                num[i]=buffer[i];
                if(num[i]==0)
                {
                    numView[i].setChecked(false);
                }
                else {
                    numView[i].setChecked(true);
                }
            }
        }
        else
        {
            for(int i=0;i<buffer.length;i++)
            {
                direction[i]=buffer[i];
                if(direction[i]==0)
                {
                    directionView[i].setChecked(false);
                }
                else{
                    directionView[i].setChecked(true);
                }
            }
        }
    }
    /**
     * 将主界面的按键ID转换为对应的checkbox的ID
     * @param id 主界面的按键ID
     * @return 返回对应的checkbox的ID
     */
    int transformId(int id)
    {
        int checkboxid=0;
        switch (id) {
            case R.id.b1:checkboxid = R.id.one;break;
            case R.id.b2:checkboxid = R.id.two;break;
            case R.id.b3:checkboxid = R.id.three;break;
            case R.id.b4:checkboxid = R.id.four;break;
            case R.id.b5:checkboxid = R.id.five;break;
            case R.id.b6:checkboxid = R.id.six;break;
        }
        return checkboxid;
    }

}
