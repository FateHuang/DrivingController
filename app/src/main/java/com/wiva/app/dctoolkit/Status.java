package com.wiva.app.dctoolkit;


/**
 * Created by Wiva on 14/10/31.
 * 用于储存按键状态的类，向外提供get，set方法
 */
public class Status {
    private static byte[] result=new byte[19];
    private static final byte[] receive={0X55,1,0,0,2,0,0,3,0,0,4,0,0,6,0,0};//“读”指令
    private static final byte SEND= (byte) 0xaa,RECEIVE=0x55,ONE=1,TWO=2,THREE=3,FOUR=4,FIVE=5,SIX=6;
    private static byte[][] status=new byte[6][2];//用于储存各个按键状态的二维byte数组
    private static final byte[] split={62,61,59,55,47,31};//用于将byte数据指定的位清零
    private static final byte[] cons={1,2,4,8,16,32};//
    /**
     * 测试用的初始化块，给status赋值
     */
    static{
        status[0][0]=0;status[0][1]=0;
        status[1][0]=0;status[1][1]=0;
        status[2][0]=0;status[2][1]=1;
        status[3][0]=0;status[3][1]=1;
        status[4][0]=0;status[4][1]=0;
        status[5][0]=0;status[5][1]=0;
    }
    /**
     * 根據ID將byte傳到status[][]的对应位置
     * @param id 按键的id
     * @param num 6个数字的byte
     * @param direction 6个方向的byte
     */
    public static void setStatus(int id,byte num,byte direction) {
        int i=0;
        switch (id)
        {
            case R.id.b1:status[0][0]=num;status[0][1]=direction;i=0;break;
            case R.id.b2:status[1][0]=num;status[1][1]=direction;i=1;break;
            case R.id.b3:status[2][0]=num;status[2][1]=direction;i=2;break;
            case R.id.b4:status[3][0]=num;status[3][1]=direction;i=3;break;
            case R.id.b5:status[4][0]=num;status[4][1]=direction;i=4;break;
            case R.id.b6:status[5][0]=num;status[5][1]=direction;i=5;break;
        }
        /**
         * 下面是处理重叠部分的代码
         */
        byte[] buffer=new byte[6];
        buffer[0]= (byte) (num&1);
        buffer[1]= (byte) (num&2);
        buffer[2]= (byte) (num&4);
        buffer[3]= (byte) (num&8);
        buffer[4]= (byte) (num&16);
        buffer[5]= (byte) (num&32);
        byte[] buffer2=new byte[6];
        for(int j=0;j<6;j++)
        {
            if(buffer[j]==0)
            {
                buffer2[j]=0;
            }
            else
            {
                buffer2[j]=cons[i];
            }
        }
        /**
         * 想想怎么把重复得代码整合起来
         */
        switch(i)
        {
            case 0: status[1][0]= (byte) (buffer2[1]+(status[1][0]&split[i]));
                    status[2][0]= (byte) (buffer2[2]+(status[2][0]&split[i]));
                    status[3][0]= (byte) (buffer2[3]+(status[3][0]&split[i]));
                    status[4][0]= (byte) (buffer2[4]+(status[4][0]&split[i]));
                    status[5][0]= (byte) (buffer2[5]+(status[5][0]&split[i]));
                break;
            case 1: status[0][0]= (byte) (buffer2[0]+(status[0][0]&split[i]));
                    status[2][0]= (byte) (buffer2[2]+(status[2][0]&split[i]));
                    status[3][0]= (byte) (buffer2[3]+(status[3][0]&split[i]));
                    status[4][0]= (byte) (buffer2[4]+(status[4][0]&split[i]));
                    status[5][0]= (byte) (buffer2[5]+(status[5][0]&split[i]));
                break;
            case 2: status[0][0]= (byte) (buffer2[0]+(status[0][0]&split[i]));
                    status[1][0]= (byte) (buffer2[1]+(status[1][0]&split[i]));
                    status[3][0]= (byte) (buffer2[3]+(status[3][0]&split[i]));
                    status[4][0]= (byte) (buffer2[4]+(status[4][0]&split[i]));
                    status[5][0]= (byte) (buffer2[5]+(status[5][0]&split[i]));
                break;
            case 3: status[0][0]= (byte) (buffer2[0]+(status[0][0]&split[i]));
                    status[1][0]= (byte) (buffer2[1]+(status[1][0]&split[i]));
                    status[2][0]= (byte) (buffer2[2]+(status[2][0]&split[i]));
                    status[4][0]= (byte) (buffer2[4]+(status[4][0]&split[i]));
                    status[5][0]= (byte) (buffer2[5]+(status[5][0]&split[i]));
                break;
            case 4: status[0][0]= (byte) (buffer2[0]+(status[0][0]&split[i]));
                    status[1][0]= (byte) (buffer2[1]+(status[1][0]&split[i]));
                    status[2][0]= (byte) (buffer2[2]+(status[2][0]&split[i]));
                    status[3][0]= (byte) (buffer2[3]+(status[3][0]&split[i]));
                    status[5][0]= (byte) (buffer2[5]+(status[5][0]&split[i]));
                break;
            case 5: status[0][0]= (byte) (buffer2[0]+(status[0][0]&split[i]));
                    status[1][0]= (byte) (buffer2[1]+(status[1][0]&split[i]));
                    status[2][0]= (byte) (buffer2[2]+(status[2][0]&split[i]));
                    status[3][0]= (byte) (buffer2[3]+(status[3][0]&split[i]));
                    status[4][0]= (byte) (buffer2[4]+(status[4][0]&split[i]));
                break;
        }

    }

    /**
     * 根据id来获取按键对应的status
     * @param id 按键的ID
     * @return 返回status
     */
    public static byte[] getStatus(int id) {
        byte[] buffer=new byte[2];
        switch (id)
        {
            case R.id.b1:buffer[0]=status[0][0];buffer[1]=status[0][1];break;
            case R.id.b2:buffer[0]=status[1][0];buffer[1]=status[1][1];break;
            case R.id.b3:buffer[0]=status[2][0];buffer[1]=status[2][1];break;
            case R.id.b4:buffer[0]=status[3][0];buffer[1]=status[3][1];break;
            case R.id.b5:buffer[0]=status[4][0];buffer[1]=status[4][1];break;
            case R.id.b6:buffer[0]=status[5][0];buffer[1]=status[5][1];break;
        }
        return buffer;
}
    /**
     * 向外提供一个发送数据的方法
     */
    public static void sendStatus(){
        /*
        将status传给给result[19]数组，用于发送给遥控器
         */
        result[0]=SEND;
        result[1]=ONE;
        result[2]=status[0][1];
        result[3]=status[0][0];
        result[4]=TWO;
        result[5]=status[1][1];
        result[6]=status[1][0];
        result[7]=THREE;
        result[8]=status[2][1];
        result[9]=status[2][0];
        result[10]=FOUR;
        result[11]=status[3][1];
        result[12]=status[3][0];
        result[13]=FIVE;
        result[14]=status[4][1];
        result[15]=status[4][0];
        result[16]=SIX;
        result[17]=status[5][1];
        result[18]=status[5][0];
        /*
        预留位置，用于编写发送数据的流程
         */
    }

    /**
     * 向外提供获取数据的方法，获取到的数据将会存储到Status里
     */
    public static void receiveStatus(){
        /*
        向遥控器发送“读”指令，即receive数组
        这里要编写接收数据读流程
         */
    }
}
