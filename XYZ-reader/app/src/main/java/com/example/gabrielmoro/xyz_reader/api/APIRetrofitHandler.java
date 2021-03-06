package com.example.gabrielmoro.xyz_reader.api;

import com.example.gabrielmoro.xyz_reader.model.XyzReaderJson;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Provides the retrofit api service to send
 * requests to the server.
 * Reference: https://medium.com/3xplore/handling-api-calls-using-retrofit-2-and-rxjava-2-1871c891b6ae
 */
public class APIRetrofitHandler {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/March/58c5d68f_xyz-reader/";
    private static APIRetrofitHandler myInstance = null;
    private APIService api;

    private APIRetrofitHandler() {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(APIService.class);
    }

    public static APIRetrofitHandler getMyInstance() {
        if (myInstance == null) {
            myInstance = new APIRetrofitHandler();
        }
        return myInstance;
    }

    public void getAllXYZReaderObjects(final APICallBackResult<List<XyzReaderJson>> contractToPlubishTheResult) {
        api.getRecipes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<XyzReaderJson>>() {
                    @Override
                    public void call(List<XyzReaderJson> recipes) {
                        contractToPlubishTheResult.onSucess(recipes);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        contractToPlubishTheResult.onFailure(throwable);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        contractToPlubishTheResult.onCompleted();
                    }
                });
    }

}