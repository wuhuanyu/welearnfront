package com.example.stack.welearn.config;

import android.util.Log;

import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.utils.Constants;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by stack on 2018/1/18.
 */

public class MQTTClient {
    private  MqttAndroidClient client;
    private   boolean haveSetUpMqtt=false;
    public static final String TAG=MQTTClient.class.getSimpleName();


    /**
     * 内部静态类实现单例
     */
    private static class MQTTClientHolder {
        private static MQTTClient instance=new MQTTClient();
    }
    private MQTTClient(){
        this.client=new MqttAndroidClient(WeLearnApp.getContext(), Constants.Net.BROKER_URL,WeLearnApp.info().getToken());
    }
    public static MQTTClient instance(){
        return MQTTClientHolder.instance;
    }
    public  MqttAndroidClient getClient(){
        if(this.client==null){
            this.client=new MqttAndroidClient(WeLearnApp.getContext(), Constants.Net.BROKER_URL, WeLearnApp.info().getToken());
        }
        return this.client;
    }
    public void connect(ConnectCallback cbk) throws MqttException {
        IMqttToken token=client.connect(getMqttConnectOptions());
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(TAG,"connect to mqtt server ok");
                cbk.onConnectionOK();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d(TAG,"connect to mqtt srver fail");
                cbk.onConnectionFail(exception);
                exception.printStackTrace();
            }
        });
    }
    public void subscribe(String topic,SubscribeCallback cbk) throws MqttException {
        IMqttToken token=client.subscribe(topic,0);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                cbk.onSubscribeOK();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                cbk.onSubscribeFail();
            }
        });
    }
    public void unsubscribe(String topic,unSubscribeCallback callback) throws MqttException{
        IMqttToken token=client.unsubscribe(topic);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                callback.onUnSubscribeOK();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                callback.onUnSubscribeFail();
            }
        });

    }

    public void setUpCallback(MqttCallback callback){
        if(client.isConnected()){
            if(!haveSetUpMqtt) {
                client.setCallback(callback);
                haveSetUpMqtt=true;
            }
        }
    }


    private MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions  options=new MqttConnectOptions();
        options.setCleanSession(false);
        options.setAutomaticReconnect(true);
        return options;
    }
    private DisconnectedBufferOptions getDisconnectedBufferOptions(){
        DisconnectedBufferOptions options=new DisconnectedBufferOptions();
        options.setBufferEnabled(true);
        options.setBufferSize(100);
        options.setPersistBuffer(true);
        options.setDeleteOldestMessages(false);
        return options;
    }
    public interface ConnectCallback{
        void onConnectionFail(Throwable e);
        void onConnectionOK();
    }
    public interface SubscribeCallback{
        void onSubscribeFail();
        void onSubscribeOK();
    }
    public interface unSubscribeCallback{
        void onUnSubscribeOK();
        void onUnSubscribeFail();
    }
}
