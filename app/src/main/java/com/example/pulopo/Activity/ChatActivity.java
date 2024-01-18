package com.example.pulopo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.pulopo.Adapter.ChatAdapter;
import com.example.pulopo.Adapter.UserAdapter;
import com.example.pulopo.R;
import com.example.pulopo.Retrofit.ApiServer;
import com.example.pulopo.Retrofit.RetrofitClient;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.Utils.UtilsCommon;
import com.example.pulopo.model.ChatMessage;
import com.example.pulopo.model.User;
import com.example.pulopo.model.response.ChatByUserResponse;
import com.example.pulopo.model.response.ListChatUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    int iduser;
    String username;
    RecyclerView recyclerView;
    ImageView imgSend;
    EditText edtMess;
    ChatAdapter adapter;
    List<ChatMessage> list;
    ApiServer apiServer;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String inputGpt ="";
    static String textRs ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        iduser = getIntent().getIntExtra("id", 0);//id nguoi nhan
        username = getIntent().getStringExtra("username");
        apiServer = RetrofitClient.getInstance(UtilsCommon.BASE_URL).create(ApiServer.class);
        initView();
        initToolbar();
        initControl();
        listenMess();
    }


    private void initControl() {
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessToServer();
            }
        });
    }

    private void sendMessToServer() {
        String str_mess = edtMess.getText().toString().trim();
        inputGpt = str_mess;
        if (iduser == 1) {
            gptSendMess();
        }
        if (TextUtils.isEmpty(str_mess)) {

        } else {
            //call api send mess
            compositeDisposable.add(apiServer.sendMessChat(UserUtil.getId(), iduser, str_mess, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                Toast.makeText(getApplicationContext(), "Đã gửi tin nhắn", Toast.LENGTH_LONG).show();
                            },
                            throwable -> {
                                Log.e("SEND", throwable.getMessage());
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
            edtMess.setText("");
        }
    }

    private void listenMess() {
        //connect api chuyen ve list<ChatMess>
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        int count = list.size();
        Log.e("infor", UserUtil.getId() + " " + iduser);
        compositeDisposable.add(apiServer.getChatByUser(UserUtil.getId(), iduser, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            ChatByUserResponse chatByUserResponse;
                            chatByUserResponse = userModel;
                            for (int i = chatByUserResponse.getData().size() - 1; i >= 0; i--) {
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.sendid = String.valueOf(chatByUserResponse.getData().get(i).getSenderId());
                                chatMessage.receivedid = String.valueOf(chatByUserResponse.getData().get(i).getReceiverId());
                                chatMessage.mess = chatByUserResponse.getData().get(i).getMessage();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    Date date = dateFormat.parse(chatByUserResponse.getData().get(i).getDateSend());
                                    chatMessage.dateObj = date;
                                    chatMessage.datetime = formatDate(date);
                                }
                                list.add(chatMessage);
                                Log.e("listchat", list.toString());
                            }

                        },
                        throwable -> {
                            Log.e("listchatbug", throwable.getMessage());
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));

        if (count == 0) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemRangeChanged(list.size(), list.size());
            recyclerView.smoothScrollToPosition(list.size() - 1);
        }

    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd MMM, yyyy- hh:mm a", new Locale("en", "US")).format(date);

    }


    private void initView() {
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleview_chat);
        imgSend = findViewById(R.id.imagechat);
        edtMess = findViewById(R.id.edtinputtext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ChatAdapter(getApplicationContext(), list, String.valueOf(UserUtil.getId()));
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbarChat);
        toolbar.setTitle(username);
    }

    //GPT
    public static String contentGPt(String textPost) {

        String apiKey = "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";
        String endpoint = "https://api.openai.com/v1/engines/gpt-3.5-turbo-instruct/completions";

        OkHttpClient client = new OkHttpClient();
        String rs = textPost.replace('\"', ' ').replace('{', ' ').replace('}', ' ').replace(',', ' ').replace('[', ' ')
                .replace(']', ' ').replace(':', ' ').replace('/', ' ').replace('\\', ' ').replace('\'', ' ');

        String input = rs.replace("\n", " ");

        byte[] inputByte = input.getBytes();

        String requestInput = new String(inputByte, StandardCharsets.UTF_8);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"prompt\": \"" + input + "\", \"max_tokens\": 80}");
        Request request = new Request.Builder().url(endpoint).post(body).addHeader("Authorization", "Bearer " + apiKey)
                .build();


            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                public void run() {
                    // Thực hiện các hoạt động mạng ở đây
                    try {
                        Response response = client.newCall(request).execute();
                        byte[] responseBodyBytes = response.body().bytes();
                        String responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);
                        JSONObject json = new JSONObject(responseBody);
                        System.out.println(json);
                        JSONArray choices = json.getJSONArray("choices");
                        JSONObject choice = choices.getJSONObject(0);
                        textRs = choice.getString("text");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        return textRs;
    }

    public void gptSendMess(){
        Log.e("GPT",inputGpt);
        compositeDisposable.add(apiServer.sendMessChat(1, UserUtil.getId(), contentGPt(inputGpt), 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            Toast.makeText(getApplicationContext(), "Đã gửi tin nhắn", Toast.LENGTH_LONG).show();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }
}