package com.example.stack.welearn.views.activities;

import android.os.Handler;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.utils.ToastUtils;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

/**
 * Created by stack on 2018/1/8.
 */

public class SocketTestActivity extends BaseActivity {

    Socket mSocket;
    Handler mHandler=new Handler();
    IO.Options options=new IO.Options();
    static final String TAG=SocketTestActivity.class.getSimpleName();
    @Override
    public void doRegister() {

        try{
            mSocket= IO.socket("http://10.0.3.2:3001");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport=(Transport)args[0];
                transport.on(Transport.EVENT_REQUEST_HEADERS,(argss)->{

                });
            }
        });
        mSocket.on("msg", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String msg=args[0].toString();

                mHandler.post(()->{
                    ToastUtils.getInstance(WeLearnApp.getContext()).showMsgShort(""+args.length);
                });
//                mHandler.post(()-> ToastUtils.getInstance(WeLearnApp.getContext()).showMsgShort(msg));
            }
        });
        mSocket.on("connect",(args)->{

            mHandler.post(()->{
                ToastUtils.getInstance(WeLearnApp.getContext()).showMsgShort(mSocket.id());
            });
        });
        mSocket.connect();
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.act_test_push;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    @Override
    public void refresh() {

    }
}
