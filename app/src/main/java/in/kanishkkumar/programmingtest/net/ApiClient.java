package in.kanishkkumar.programmingtest.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://www.json-generator.com/";
    private static Retrofit retrofit = null;
    public static final String CACHE_NAME = "http-cache";
    public static final long CACHE_SIZE = 10 * 1024 * 1024; // 10 MB Cache

    final static String CACHE_CONTROL = "Cache-Control";

    private static Interceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    private static Cache provideCache(Context context) {
        File cacheFile = new File(context.getFilesDir(), CACHE_NAME);
        if (!cacheFile.exists()) {
            cacheFile.mkdir();
        }
        return new Cache(cacheFile, CACHE_SIZE);
    }

    private static Interceptor cacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                CacheControl cacheControl = new CacheControl.Builder().maxAge(2, TimeUnit.MINUTES).build();
                return response.newBuilder().header(CACHE_CONTROL, cacheControl.toString()).build();
            }
        };
    }

    private static Interceptor offlineInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isNetworkAvailable(context)) {
                    //offline
                    CacheControl cacheControl = new CacheControl.Builder().maxStale(7, TimeUnit.DAYS).build();
                    request = request.newBuilder().cacheControl(cacheControl).build();
                }
                return chain.proceed(request);
            }
        };
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static OkHttpClient getHttpClient(Context context) {
        return new OkHttpClient.Builder()
                .cache(provideCache(context))
                .addInterceptor(cacheInterceptor())
                .addInterceptor(offlineInterceptor(context))
                .addInterceptor(provideLoggingInterceptor())
                .build();
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .create();
    }

    public static Retrofit getClient(final Context context) {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getHttpClient(context))
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
        }
        return retrofit;
    }
}