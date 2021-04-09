package com.example.twister_pm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity {
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String MESSAGE = "message";
    private final List<Message> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getAndShowData();
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        Intent userData = getIntent();
        String email = userData.getStringExtra(EMAIL);
        TextView emailView = findViewById(R.id.secondEmailView);
        emailView.setText(email);
    }

    private void getAndShowData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://anbo-restmessages.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AnboService service = retrofit.create(AnboService.class);

        Call<List<Message>> callMessage = service.getMessage();
        callMessage.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                List<Message> messages = response.body();
                VISDET(messages);
                Log.d("apple", messages.toString());
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.d("apple", t.getMessage());
            }
        });
    }

    private void VISDET(List<Message> messages) {
        RecyclerView recyclerView = findViewById(R.id.secondRecyclerView);

        RecyclerViewSimpleAdapter<Message> adapter = new RecyclerViewSimpleAdapter<>(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.d("banana", messages.toString());
        adapter.setOnItemClickListener(new RecyclerViewSimpleAdapter.OnItemClickListener<Message>() {
            @Override
            public void onItemClick(View view, int position, Message element) {
                Intent intent = new Intent(getBaseContext(), commentActivity.class);
                intent.putExtra(commentActivity.MESSAGE, element);
                startActivity(intent);
            }
        });
    }

}