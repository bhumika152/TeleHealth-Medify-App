package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.adapters.MessagesAdapter;
import com.example.myapplication.models.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendUserActivity extends AppCompatActivity {
    EditText message_input;
    ListView listView;
    SharedPreferences sharedPreferences;
    String did;
    String uid;
    List<Message> messageList;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference reference;
    StorageReference riversRef;
    FirebaseDatabase database;
    DatabaseReference dreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_user);
        sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        messageList = new ArrayList<>();
        TextView name = findViewById(R.id.unamecs);
        did = sharedPreferences.getString("uid",null);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dreference = database.getReference("messages");
        name.setText(sharedPreferences.getString("uname","Doctor"));
        user = auth.getCurrentUser();
        uid = user.getUid();
        listView = findViewById(R.id.messageslistdoc);
        ImageView profile_p = findViewById(R.id.profileuser);
        setImage(did,profile_p);
        message_input = findViewById(R.id.sendtextdoc);
        findViewById(R.id.sendbtndoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        updateMessages();
    }
    private  void  updateMessages(){
        dreference.orderByChild("timemills").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    if(dataSnapshot.exists()){
                        Message message = dataSnapshot.getValue(Message.class);
                        if((message.getSenderId().equals(uid) && message.getRecieverId().equals(did)) || (message.getSenderId().equals(did) && message.getRecieverId().equals(uid))){
                            messageList.add(message);
                        }
                    }
                    inflateMessages(messageList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void sendMessage(){
        String mes = message_input.getText().toString();
        if(!mes.isEmpty()){
            message_input.setText("");
            Message message = new Message(mes,uid,did);
            UUID uuid = UUID.randomUUID();
            dreference.child(uuid.toString()).setValue(message);
            messageList.add(message);
            inflateMessages(messageList);
        }
    }
    private void inflateMessages(List<Message> messageList){
        MessagesAdapter adapter = new MessagesAdapter(this,messageList,this,uid);
        listView.setAdapter(adapter);
    }
    private  void setImage(String id, ImageView profile_pic){
        riversRef = reference.child("images/"+id+".jpg");
        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_pic);
            }
        });
    }
}