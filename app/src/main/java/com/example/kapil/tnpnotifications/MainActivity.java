package com.example.kapil.tnpnotifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private TextView messageView;
    private String url_prefix = "https://tnp.iitd.ac.in/portal/notify?type=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        messageView = (TextView) findViewById(R.id.messageView);
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String def = "not_set";
        String topic = sharedPreferences.getString("topic_choice", def);
        if(!topic.equals(def)){
            String message = "You are receiving notifications on the " + topic + " channel.";
            messageView.setText(message);
            messageView.setVisibility(View.VISIBLE);
        }
    }

    public void register(View view){

        RadioButton selectedButton = (RadioButton)
                radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
        int choice;
        final String message = "You've been registered for ";
        if(selectedButton.getId() == R.id.trainingButton){
            choice = 0;
            FirebaseMessaging.getInstance().unsubscribeFromTopic("placement");
        }
        else{
            choice = 1;
            FirebaseMessaging.getInstance().unsubscribeFromTopic("training");
        }
        Log.d("DEBUG", "Choice:" + String.valueOf(choice));
        final String topic = choice == 0 ? "training" : "placement";
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String def = "not_set";
        String curr_choice = sharedPreferences.getString("topic_choice", def);

        if(topic.equals(curr_choice)){
            Toast.makeText(getApplicationContext(),
                    "You're already receiving notifications on this channel",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            messageView.setText(message + topic);
                            messageView.setVisibility(View.VISIBLE);
                            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("topic_choice", topic);
                            editor.apply();
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Registration failed", Toast.LENGTH_SHORT).show();
                                messageView.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }
    public void openNotifications(View view){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String def = "not_set";
        String topic = sharedPreferences.getString("topic_choice", def);
        if(topic.equals(def)){
            Toast.makeText(getApplicationContext(),
                    "Channel not set. Opening Training notifications.",
                    Toast.LENGTH_SHORT).show();
            topic = "training";
        }
        String url = url_prefix + topic;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        startActivity(browserIntent);

    }
}
