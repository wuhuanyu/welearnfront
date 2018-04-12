package com.example.stack.welearn;

import android.app.Application;
import android.content.Context;
import android.util.SparseArray;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.example.stack.welearn.utils.ACache;
import com.example.stack.welearn.utils.Constants;
import com.squareup.leakcanary.LeakCanary;

import org.json.JSONObject;

import java.util.concurrent.Callable;

/**
 * Created by stack on 2018/1/2.
 */

public class WeLearnApp extends Application {

    private static  Context context;
    private static ACache mCache;
    private static MYINFO myinfo;

    private Callable<String> getToken=new Callable<String>() {
        @Override
        public String call() throws Exception {
            JSONObject auth=new JSONObject();
            auth.put("name",myinfo.getUserName());
            auth.put("password",myinfo.getPassword());
            auth.put("action","login");
            ANRequest request=AndroidNetworking.post(Constants.Net.API_URL+"/acc")
                    .addJSONObjectBody(auth)
                    .build();
            ANResponse _response=request.executeForJSONObject();
            JSONObject  response=(JSONObject) _response.getResult();
            String newToken=response.getString("token");
            return newToken;
        }
    };
//    Interceptor interceptor=new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request=chain.request();
//
//            Request.Builder requestBuilder=request.newBuilder();
//
//            requestBuilder.addHeader(
//                    "authorization",
//                    Base64.getEncoder().encodeToString((myinfo.getId()+":"+myinfo.getToken()).getBytes(Charset.forName("UTF-8")))
//                    );
//
//            Response response=chain.proceed(request);
//
//            if(response.code()==401||response.code()==403){
//                synchronized (WeLearnApp.this){
//                    //Login to get new token
//                    JSONObject auth=new JSONObject();
//                    try{
//                        //验证相关字段
//                        auth.put("name",myinfo.getUserName());
//                        auth.put("password",myinfo.getPassword());
//                        auth.put("action","login");
//                        auth.put("type",myinfo.getUserType());
//
//                        ANRequest loginRequest=AndroidNetworking.post(Constants.Net.API_URL+"/acc")
//                                .addJSONObjectBody(auth)
//                                .build();
//                        ANResponse _result=loginRequest.executeForJSONObject();
//                        JSONObject  result=(JSONObject) _result.getResult();
//                        String newToken=result.getString("token");
//
//                        WeLearnApp.myinfo.setToken(newToken);
//                        requestBuilder.addHeader("authorization", Base64.getEncoder().encodeToString(
//                                (myinfo.getId()+":"+newToken).getBytes(Charset.forName("UTF-8"))
//                        ));
//                        return chain.proceed(requestBuilder.build());
//                    }catch (JSONException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return response;
//        }
//    };
//    private OkHttpClient httpClient(){
//
//        OkHttpClient client=new OkHttpClient()
//                .newBuilder()
//                .addNetworkInterceptor(interceptor).build();
//        return client;
//    }
    @Override
    public void onCreate() {
        super.onCreate();
        WeLearnApp.context=getApplicationContext();
        AndroidNetworking.initialize(WeLearnApp.getContext());
        myinfo=new MYINFO();
        mCache=ACache.get(this);
        if(LeakCanary.isInAnalyzerProcess(this))
            return;
        LeakCanary.install(this);

    }
    public static ACache cache(){
        return mCache;
    }
    public static Context getContext(){
        return WeLearnApp.context;
    }
    public static MYINFO info(){return myinfo;}

    public static void reset(){
        myinfo=new MYINFO();

    }

    public static class MYINFO{
        private  String userName;
        private  String password;
        private  int userType;
        private int id;
        private String auth;
        private String avatar;
        private boolean isTeacher;

        public boolean isTeacher() {
            return isTeacher;
        }

        public MYINFO setTeacher(boolean teacher) {
            isTeacher = teacher;
            return this;
        }

        public String getAvatar() {
            return avatar;
        }

        public MYINFO setAvatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public String getAuth(){
            return auth;
        }

        public MYINFO setAuth(String auth) {
            this.auth = auth;
            return this;
        }

        private SparseArray<String> myCourses=new SparseArray<>();
        private MYINFO(){};
        private String token;

        public String getToken() {
            return token;
        }

        public int getId() {
            return id;
        }

        public MYINFO setId(int id) {
            this.id = id;
            return this;
        }

        public MYINFO setToken(String token) {
            this.token = token;
            return this;
        }

        public String getUserName() {
            return userName;
        }

        public MYINFO setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public MYINFO setPassword(String password) {
            this.password = password;
            return this;
        }

        public int getUserType() {
            return userType;
        }

        public MYINFO setUserType(int userType) {
            this.userType = userType;
            this.isTeacher=(this.userType==Constants.ACC_T_Tea);
            return this;
        }

        public SparseArray<String> getMyCourses() {
            return myCourses;
        }

        public MYINFO setMyCourses(SparseArray<String> myCourses) {
            this.myCourses = myCourses;
            return this;
        }
    }
}
