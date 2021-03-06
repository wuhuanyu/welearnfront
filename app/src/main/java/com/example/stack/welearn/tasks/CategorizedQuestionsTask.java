package com.example.stack.welearn.tasks;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.entities.CategorizedQuestionCourse;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.entities.Question;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * Created by stack on 15/01/18.
 */

public class CategorizedQuestionsTask extends BaseTask {
    private static final String TAG = CategorizedQuestionsTask.class.getSimpleName();
    private static CategorizedQuestionsTask instance;
    private String auth;
    private int id;

    //请求倒计时
    private CountDownLatch mCountDown;

    //    private Map<Integer,List<Question>> categorizedQuestions=new HashMap<>();
    private List<CategorizedQuestionCourse> categorizedQuestionCourses = new ArrayList<>();

    private CategorizedQuestionsTask() {
    }

    public static CategorizedQuestionsTask instance() {
        if (instance == null) {
            instance = new CategorizedQuestionsTask();
        }
        return instance;
    }

    private List<Runnable> tasks;

    public Runnable getCategorizedQuestion(boolean toRefresh,String auth) {
        return () -> {
            //假定我的课程已经缓存了 todo:check
            JSONArray myCourseJSONS = WeLearnApp.cache().getAsJSONArray("my_unfinished_courses");

            List<Course> myCourses = Course.toCourses(myCourseJSONS);
            //设置倒计时
            this.mCountDown = new CountDownLatch(myCourses.size());
            tasks = myCourses.stream()
                    .map(course -> {
                        int courseId = course.getId();
                        return new Runnable() {
                            @Override
                            public void run() {
                                JSONArray cachedQuestions = mCache.getAsJSONArray("course-" + courseId + "-questions");
                                //没有缓存，而且请求刷新
                                if (cachedQuestions == null || toRefresh) {
                                    AndroidNetworking.get(Constants.Net.API_URL + "/course/" + courseId + "/question")
                                            .build().getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                Log.d(TAG, "-------fetch question ok of course " + courseId + "--------- ");
                                                Log.d(TAG, response.toString());
                                                JSONArray questionJsons = response.getJSONArray("data");
                                                mCache.put("course-" + courseId + "-questions", questionJsons);
                                                //再次转化
                                                categorizedQuestionCourses.add(toCategorizedQuestion(course, questionJsons));
                                                mCountDown.countDown();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            //出错放入空
                                            Log.e(TAG, anError.getErrorBody());
                                            mCountDown.countDown();
                                        }
                                    });
                                }
                                //缓存中有
                                else {
                                    Log.d(TAG, "缓存中存在课程的习题----course id:" + courseId);
                                    categorizedQuestionCourses.add(toCategorizedQuestion(course, cachedQuestions));
                                    mCountDown.countDown();
                                }
                            }
                        };
                    }).collect(Collectors.toList());

            //所有的线程设置完毕,放入线程池
            tasks.forEach(task -> {
                ThreadPoolManager.getInstance().getService().execute(task);
            });
            try {
                mCountDown.await();
                //所有线程工作完毕，不管有没有获取成功
                EventBus.getDefault().post(new Event<List<CategorizedQuestionCourse>>(
                        Event.CATEGORIZED_QUESTION_FETCH_OK, categorizedQuestionCourses
                ));
                categorizedQuestionCourses = new ArrayList<>();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };
    }


        /**
         *
         * @param course
         * @param questionJSONS
         * @return
         * 再次处理，将categorized Question 处理为 CategorizedQuestionCourse
         */
    private CategorizedQuestionCourse toCategorizedQuestion(Course course,JSONArray questionJSONS){
        CategorizedQuestionCourse categorizedQuestionCourse;
        List<Question> questions = Question.toQuestions(questionJSONS);
        String image=null;
        if(course.getImages()!=null)
            image=course.getImages().get(0);
        long updateTime=0;
        for(int i=0;i<questions.size();i++){
            if(questions.get(i)!=null)
                if(questions.get(i).getPublishTime()>updateTime)
                    updateTime=questions.get(i).getPublishTime();
        }
        categorizedQuestionCourse=new CategorizedQuestionCourse(
                image,updateTime,questions.size(),course.getId(),course.getName()
        );
        return categorizedQuestionCourse;
    }
}
