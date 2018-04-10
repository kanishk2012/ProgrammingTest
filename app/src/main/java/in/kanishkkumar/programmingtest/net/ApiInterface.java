package in.kanishkkumar.programmingtest.net;

import java.util.List;

import in.kanishkkumar.programmingtest.model.ResponseItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @GET("/api/json/get/cfdlYqzrfS")
    Call<List<ResponseItem>> getServiceResponse();

}
