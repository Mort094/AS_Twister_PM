package com.example.twister_pm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity {
    private ShareActionProvider shareActionProvider;
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String MESSAGE = "message";
    private TextView messageView;
    private FirebaseAuth mAuth;
    private final List<Message> messagesList = new ArrayList<>();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://anbo-restmessages.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    AnboService service = retrofit.create(AnboService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        messageView = findViewById(R.id.messageView);
        mAuth = FirebaseAuth.getInstance();
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

    @Override
    protected void onStart() {
        super.onStart();
        getAndShowData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
    public void logout(){
        mAuth.signOut();
        Toast.makeText(SecondActivity.this, "You are now logged out!", Toast.LENGTH_LONG).show();

        Intent i = new Intent(SecondActivity.this, MainActivity.class);        // Specify any activity here e.g. home or splash or login etc
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    };

    private void getAndShowData() {

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
    public void sendMessage(View view){
        EditText contentField = findViewById(R.id.messageContent);
        String content = contentField.getText().toString().trim();

        Message message = new Message();
        message.setContent(content);
        message.setUser(mAuth.getCurrentUser().getEmail());
        Call<Message> saveMessageCall = service.saveMessage(message);
        saveMessageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()){
                    Message theNewMessage = response.body();
                    Log.d("apple", theNewMessage.toString());
                    Toast.makeText(SecondActivity.this, "Message added: " + theNewMessage.getId(), Toast.LENGTH_SHORT).show();
                }else {
                    String problem = "Problem: " + response.code() + " " + response.message();
                    Log.e("apple", problem);
                    messageView.setText("Problem");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

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