package com.example.retrofitone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.text_view_result);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        //here retrofit will create method body for us
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        //getPosts();
        //getComments();
        //createPost();
        updatePost();
        //deletePost();
    }

    private void getPosts(){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");

        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,6}, null, null);
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    resultTextView.setText("Code: " + String.valueOf(response.code()));
                    return;
                }

                List<Post> posts = response.body();
                for( Post post : posts){
                    String content = "";
                    content += "UserID: " + post.getUserId() + "\n";
                    content += "ID: " + post.getId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Body: " + post.getText() + "\n\n";

                    resultTextView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                resultTextView.setText(t.getMessage());
            }
        });
    }

    private void getComments(){
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments(new Integer[]{2,5,7});
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    resultTextView.setText("Code: " + response.code());
                    return;
                }

                List<Comment> comments = response.body();
                for(Comment comment : comments){
                    String content = "";
                    content += "ID: " + comment.getId() + "\n";
                    content += "PostId: " + comment.getPostId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";

                    resultTextView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                resultTextView.setText(t.getMessage());
            }
        });
    }

    private void createPost(){
        // use this one while sending parameter in the body
        // in the form of a JSON
        //Post post = new Post(22, "Pratik", "My post");
        //Call<Post> call = jsonPlaceHolderApi.createPost(post);
        // now instead of Post object send parameters directly to the server as
        // url encoded way
        //Call<Post> call = jsonPlaceHolderApi.createPost(22, "Pratik", "My post");
        // now use the field map method to send data to server
        Map<String, String> map = new HashMap<>();
        map.put("userId", "22");
        map.put("title", "Pratik");
        map.put("body", "My post");
        Call<Post> call = jsonPlaceHolderApi.createPost(map);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    resultTextView.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();
                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "user ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                resultTextView.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                resultTextView.setText(t.getMessage());
            }
        });
    }

    private void updatePost(){
        Post post = new Post(22, null, "Testing post");
        // use this line to test PUT
        Call<Post> call = jsonPlaceHolderApi.putPost(7, post);
        //Call<Post> call = jsonPlaceHolderApi.patchPost(7, post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    resultTextView.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();
                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "user ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                resultTextView.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                resultTextView.setText(t.getMessage());
            }
        });
    }

    private void deletePost(){
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                resultTextView.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                resultTextView.setText(t.getMessage());
            }
        });
    }
}
