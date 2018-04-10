package in.kanishkkumar.programmingtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import in.kanishkkumar.programmingtest.model.ResponseItem;
import in.kanishkkumar.programmingtest.net.ApiClient;
import in.kanishkkumar.programmingtest.net.ApiInterface;
import in.kanishkkumar.programmingtest.ui.DetailActivity;
import in.kanishkkumar.programmingtest.ui.ListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        ApiInterface apiService =
                ApiClient.getClient(this).create(ApiInterface.class);

        Call<List<ResponseItem>> call = apiService.getServiceResponse();
        call.enqueue(new Callback<List<ResponseItem>>() {
            @Override
            public void onResponse(Call<List<ResponseItem>> call, Response<List<ResponseItem>> response) {
                Log.e(TAG, "SUCCESS: Got response from server " + response.body().get(0).getName());
                recyclerView.setAdapter(new ListAdapter(MainActivity.this, response.body(), MainActivity.this));
//                Toast.makeText(MainActivity.this, response.body().get(0).getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<ResponseItem>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "FAILURE: " + t.toString());
            }
        });
    }

    @Override
    public void onItemClicked(ResponseItem item) {
        Intent mapIntent = new Intent(this, DetailActivity.class);
        mapIntent.putExtra("data", item);
        startActivity(mapIntent);
    }
}
