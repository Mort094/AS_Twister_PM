package com.example.twister_pm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class commentActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static final String LOG_TAG = "APPLE";
    public static final String MESSAGE = "comment";
    private TextView messageView;
    private Message originalMessage;
    private GestureDetector gestureDetector;

    private FirebaseAuth mAuth;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://anbo-restmessages.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    AnboService service = retrofit.create(AnboService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gestureDetector = new GestureDetector(this, this);

        messageView = findViewById(R.id.addCommentMessageTextView);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        originalMessage = (Message) intent.getSerializableExtra(MESSAGE);

        TextView heading = findViewById(R.id.commentHeading);
        heading.setText(originalMessage.getContent());

        getAndShowData();


       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }



    private void getAndShowData() {

        Call<List<Comment>> callComment = service.getComment(originalMessage.getId());
        callComment.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                List<Comment> comments = response.body();
                VISDET(comments);
                Log.d(LOG_TAG, comments.toString());
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                //Log.d("apple", t.getComment());
            }
        });
    }

    private void VISDET(List<Comment> comments) {
        RecyclerView recyclerView = findViewById(R.id.commentRecyclerView);

        RecyclerViewSimpleAdapter<Comment> adapter = new RecyclerViewSimpleAdapter<>(comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.d("banana", comments.toString());

    }

    public void sendComment(View view) {
        Log.d("ananas", "Nu er jeg i sendComment");
        EditText commentField = findViewById(R.id.makeAComment);

        String commentContent = commentField.getText().toString().trim();

        Comment comment = new Comment();
        comment.setContent(commentContent);
        comment.setUser(mAuth.getCurrentUser().getEmail());
        comment.setMessageId(originalMessage.getId());


        Call<Comment> saveCommentCall = service.saveComment(originalMessage.getId(), comment);
        Log.d("ananas", comment.toString());

        saveCommentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()){
                    Comment theNewComment = response.body();
                    Log.d("apple", theNewComment.toString());
                    Toast.makeText(commentActivity.this, "Comment added, id: "+ theNewComment.getId(), Toast.LENGTH_LONG).show();

                }
                else {
                    String problem = "Problem: " + response.code() + " " + response.message();
                    Log.d(LOG_TAG, problem);
                    messageView.setText(problem);
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                messageView.setText(t.getMessage());
                Log.e(LOG_TAG, t.getMessage());
            }
        });

    }

    public void deleteMessage(View view )
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("apple", "currentUser: " + currentUser.getEmail());
        Log.d("apple","originalMessage " + originalMessage.getUser());
        if (!currentUser.getEmail() .equals(originalMessage.getUser())){
                Toast.makeText(getBaseContext(),
                 "Can not delete others messages",
                  Toast.LENGTH_SHORT).show();
            return;
    }


        int messageId = originalMessage.getId();
        Call<Message> deleteMessageCall = service.deleteMessage(messageId);

        deleteMessageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()){
                    Log.d("apple", "Virker det");
                   finish();
                }else {
                    String problem = call.request().url() + "\n" + response.code() + " " + response.message();
                    messageView.setText(problem);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e(LOG_TAG, "Problem: " + t.getMessage());
            }
        });
    }




    @Override
    public boolean onTouchEvent(MotionEvent event){
        return gestureDetector.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(LOG_TAG, "onFling " + e1.toString() + "::::::" + e2.toString());
        boolean rightSwipe = e1.getX() < e2.getX();
        Log.d(LOG_TAG, "onFling right: " + rightSwipe);
        if (rightSwipe) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}