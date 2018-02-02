package com.example.stack.welearn.views.activities;

import android.os.Bundle;

import com.example.stack.welearn.R;
import com.example.stack.welearn.config.MQTTClient;
import com.example.stack.welearn.utils.ToastUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by stack on 2018/1/18.
 */

public class MQTTTestAct extends BaseActivity {
    private MQTTClient mClient;
    private MqttAndroidClient mDefeferdClient;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }
    @Override
    public void doRegister() {

    }

    @Override
    public void initView() {
        mClient=MQTTClient.instance();
        mDefeferdClient=mClient.getClient();
        try {
            mClient.connect(new MQTTClient.ConnectCallback() {
                @Override
                public void onConnectionFail(Throwable t) {
                    ToastUtils.getInstance().showMsgLong("connection fail");
                    t.printStackTrace();
                }
                @Override
                public void onConnectionOK() {
                    ToastUtils.getInstance().showMsgLong("connection ok");
                    try {
                        mClient.subscribe("1", new MQTTClient.SubscribeCallback() {
                            @Override
                            public void onSubscribeFail() {

                            }

                            @Override
                            public void onSubscribeOK() {
                                ToastUtils.getInstance().showMsgShort("subscribe ok");
                            }
                        });
                        mClient.subscribe("2", new MQTTClient.SubscribeCallback() {
                            @Override
                            public void onSubscribeFail() {

                            }

                            @Override
                            public void onSubscribeOK() {
                                ToastUtils.getInstance().showMsgLong("subscribe 2 ok");
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    mDefeferdClient.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            ToastUtils.getInstance().showMsgShort(new String(message.getPayload()));
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getLayout() {
        return R.layout.act_mqtt_test;
    }
    public void onStop(){
        super.onStop();
    }

    @Override
    public void refresh() {

    }
}
