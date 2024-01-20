package com.example.pulopo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulopo.R;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.model.ChatMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChatMessage> chatMessageList;
    private  String sendid;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;
    private static final int TYPE_IMG = 3;
    public ChatAdapter(Context context, List<ChatMessage> chatMessageList, String sendid) {
        this.context = context;
        this.chatMessageList = chatMessageList;
        this.sendid = sendid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.item_send_mess,parent,false);
            return new SendMessViewHolder(view);

        }
        if(viewType == TYPE_IMG){
            view = LayoutInflater.from(context).inflate(R.layout.item_img_rec,parent,false);
            return new ImageViewHolder(view);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.item_received,parent,false);
            return new ReceviedViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // neu kieu du lieu la anh
        if(getItemViewType(position) == TYPE_IMG){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            String imgpath = "images/"+chatMessageList.get(position).mess;
            StorageReference storageRef = storage.getReference().child(imgpath);

            try {
                File localFile = File.createTempFile("image", "jpg");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // File đã được tải thành công
                        // Bạn có thể sử dụng localFile để thực hiện các thao tác khác, chẳng hạn như hiển thị nó trong ImageView

                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        if (bitmap != null) {
                            ((ImageViewHolder) holder).imgReceived.setImageBitmap(bitmap);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Xử lý lỗi khi không thể tải file từ Firebase Storage
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((ImageViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
        }
        // kieu du lieu tin nhan gui di
        if(getItemViewType(position) == TYPE_SEND){
            if(chatMessageList.get(position).typeMess==1){
                ( (SendMessViewHolder) holder).txtmess.setText("Đã chia sẻ một vị trí");
            }

            if(chatMessageList.get(position).typeMess==0){
                ( (SendMessViewHolder) holder).txtmess.setText(chatMessageList.get(position).mess);
            }
            ( (SendMessViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);

        }
        // kieu du lieu tin nhan nhan ve
        else{
            if(chatMessageList.get(position).typeMess==1){
                ( (ReceviedViewHolder) holder).txtmess.setText("Đã chia sẻ một vị trí");
                ( (ReceviedViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);

            }
            if(chatMessageList.get(position).typeMess==0){
                ( (ReceviedViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
                ( (ReceviedViewHolder) holder).txtmess.setText(chatMessageList.get(position).mess);
            }

        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("img",String.valueOf(chatMessageList.get(position).getTypeMess()));
        if(chatMessageList.get(position).getTypeMess() == 2){
            Log.e("logtype",String.valueOf(chatMessageList.get(position).getTypeMess()));
            return TYPE_IMG;
        }
        if(chatMessageList.get(position).sendid.equals(sendid)){
            return TYPE_SEND;
        } else{
            return TYPE_RECEIVE;
        }



    }

    public void clearData() {
       chatMessageList.clear();
    }
    class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView  txttime;
        ImageView imgReceived;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgReceived = itemView.findViewById(R.id.imgReceived);
            txttime = itemView.findViewById(R.id.timesendimg);

        }
    }

    class SendMessViewHolder extends RecyclerView.ViewHolder{
        TextView txtmess, txttime;
        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.txtmessSend);
            txttime = itemView.findViewById(R.id.txttimeSend);


        }
    }
    class ReceviedViewHolder extends RecyclerView.ViewHolder{
        TextView txtmess, txttime;
        public ReceviedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.txtmessrece);
            txttime = itemView.findViewById(R.id.txttimerece);
        }
    }


}
