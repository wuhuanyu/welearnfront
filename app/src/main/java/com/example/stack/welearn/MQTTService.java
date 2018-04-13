package com.example.stack.welearn;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.stack.welearn.config.MQTTClient;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.TimeUtils;
import com.example.stack.welearn.utils.ToastUtils;
import com.example.stack.welearn.views.activities.CourseDetailAct;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stack on 2/2/18.
 */

public class MQTTService extends Service {
    private int notificationId=0;

    private List<Integer> haveSubscribe;

    private Bitmap icon;
    private MQTTClient mqttClient=MQTTClient.instance();
    private static final String TAG=MQTTService.class.getSimpleName();


    /**
     *bind startAct here
     */
    private final IBinder mBinder=new MQTTBinder();
    public class MQTTBinder extends Binder{
        MQTTService getSerivce(){
            return MQTTService.this;
        }
    }

   public IBinder onBind(Intent intent){
       return  mBinder;
   }

   public void unSubscribeAll(){
        MqttAndroidClient client=mqttClient.getClient();
         for(int c : haveSubscribe){
            try{
               IMqttToken token= client.unsubscribe(String.valueOf(c));
               token.setActionCallback(new IMqttActionListener() {
                   @Override
                   public void onSuccess(IMqttToken asyncActionToken) {
                       Log.d(TAG,"unsubscribe to "+c+" ok");
                   }

                   @Override
                   public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                   }
               });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
   }

    /*
     * bind stop here
     */



    private void notification(String msg,PendingIntent actionIntent){
        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder=new Notification.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(icon)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setSound(sound);
        if(actionIntent!=null){
            builder.setContentIntent(actionIntent);
        }
        Notification notification=builder.build();
        notification.defaults|=Notification.DEFAULT_VIBRATE;
        NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId++,notification);
    }
    private MqttCallback mqttCallback=new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            Log.e(TAG,"connection to mqtt server lost");
            ToastUtils.getInstance().showMsgShort("connection to mqtt server lost");
        }
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            JSONObject jsonObject=new JSONObject(new String(message.getPayload()));
            if(jsonObject.has("type")){
                int type=jsonObject.getInt("type");
                JSONObject payload=jsonObject.getJSONObject("payload");

                Log.d(TAG,"receive message from "+topic+" :"+payload.toString());
                switch (type){
                    case Event.NEW_MESSAGE:
                        Intent msgIntent=new Intent("new_message");
                        msgIntent.putExtra("msg_json",payload.toString());
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(msgIntent);
                        break;
                    case Event.NEW_COMMENT_COURSE:
                    {
                        String courseName=payload.getString("course_name");
                        int courseId=Integer.parseInt(payload.getString("forId"));
                        String notification="你的课程"+courseName+"有新的评论啦!   "+payload.getString("body");
                        Intent intent=new Intent(getApplicationContext(),CourseDetailAct.class);
                        Bundle bundle=new Bundle();
                        bundle.putInt("course_id",courseId);
                        bundle.putString("course_name",payload.getString("course_name"));
                        intent.putExtras(bundle);
                        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
                        Toast.makeText(getApplicationContext(),notification,Toast.LENGTH_SHORT).show();
                        notification(notification,pendingIntent);
                    }
                        break;
                    case Event.NEW_COMMENT_QUESTION:
                    {

                    } break;

                    case Event.NEW_LIVE_RESERVED:
                    {
                        String reservedTime= TimeUtils.toDate(payload.getLong("time"));

                        String courseName=payload.getString("course_name");
                        String title=payload.getString("title");
                        String notification=courseName+" has a new live reservation ! "+title+" at "+reservedTime;
                        notification(notification,null);
                    }break;
                    case Event.NEW_LIVE_STARTED:
                    {
                        String courseName=payload.getString("course_name");
                        String title=payload.getString("title");
                        String notification=courseName+" live started !";
                        notification(notification,null);
                    }break;
                    default:break;
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    public void onCreate(){
        super.onCreate();
        this.haveSubscribe=new ArrayList<>();

        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,TAG+" started",Toast.LENGTH_SHORT).show();
        icon= BitmapFactory.decodeResource(getResources(),R.mipmap.app_icon);
        return START_NOT_STICKY;
    }




    public void onDestroy(){
        EventBus.getDefault().unregister(this);
        Log.i(TAG,"mqtt service destroy");
        super.onDestroy();
    }

    /**
     * TODO 重复subscribe / unsubscribe
     * @param cs
     */
    private void doSubscribe(List<Integer> cs){
        MqttAndroidClient client=MQTTClient.instance().getClient();
        if(!client.isConnected())
            return;
        unSubscribeAll();

        for(int c:cs){
            try {
                IMqttToken token=client.subscribe(String.valueOf(c),0);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d(TAG,"----------subscribe for course   "+c);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        haveSubscribe=cs;
    }
    private void subscribe(List<Integer> courses){
        if(!mqttClient.getClient().isConnected()){
            Log.d(TAG," have not connected ....");
            try {
                mqttClient.connect(new MQTTClient.ConnectCallback() {
                    @Override
                    public void onConnectionFail(Throwable e) {
                        Log.d(TAG,"connect to mqtt server fail");
                        EventBus.getDefault().post(new Event<String>(Event.MQTT_ERROR,"连接消息服务器失败，请向开发者提交反馈，或者检查网络连接"));
                    }
                    @Override
                    public void onConnectionOK() {
                        Log.d(TAG,"connect to mqtt server ok");
                        ToastUtils.getInstance().showMsgShort("连接消息服务器OK");
                        mqttClient.setUpCallback(mqttCallback);
                        doSubscribe(courses);
                    }
                });

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        doSubscribe(courses);
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = true)
    public void onEvent(Event<?> event){
        int code=event.code();
        switch (code){
            case Event.DO_SUBSCRIBE:
                subscribe((List<Integer>)event.t());
                EventBus.getDefault().removeStickyEvent(event);
                break;
            case Event.DO_UNSUBSCRIBE:
                unSubscribeAll();
                EventBus.getDefault().removeStickyEvent(event);
            default:break;
        }
    }

    public static void startService(Context context){
        Intent intent=new Intent(context,MQTTService.class);
        context.startService(intent);
    }
}
