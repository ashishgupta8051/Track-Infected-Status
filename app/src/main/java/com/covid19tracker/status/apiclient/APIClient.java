package com.covid19tracker.status.apiclient;

import com.covid19tracker.status.utils.Credentials;
import com.covid19tracker.status.utils.DataInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    /*private static Retrofit retrofit;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Retrofit getApiClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60,
                TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                // Customize the request
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-Type", "form-data")
                        .method(original.method(), original.body())
                        .build();
                Response response = chain.proceed(request);
                // Customize or return the response
                return response;
            }
        });
        OkHttpClient OkHttpClient = httpClient.build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Credentials.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient)
                    .build();
        }
        return retrofit;
    }*/

    private static APIClient apiClient;
    private static Retrofit retrofit;

    private APIClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized APIClient getApiClient(){
        if (apiClient == null){
            apiClient = new APIClient();
        }
        return apiClient;
    }

    public DataInterface getDataInterface(){
        return retrofit.create(DataInterface.class);
    }

}
