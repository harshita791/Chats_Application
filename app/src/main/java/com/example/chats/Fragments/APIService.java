package com.example.chats.Fragments;

import com.example.chats.Notifications.MyResponse;
import com.example.chats.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAkexdzv8:APA91bGPUeucmtLWWtmTln_-btzJOrBkF3weHAEwX1ieF-7uRzx9EjFVRsapws0n54JLtycLwh-PuHkbEBhRnf-_WTFq_1Vi4pCboyXzVbben3ZzOpDZ62T3lHYRd99XE61GPEfiEBCb"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
