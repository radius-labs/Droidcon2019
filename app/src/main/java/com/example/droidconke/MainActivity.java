package com.example.droidconke;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // create an input, a button and a display window
    TextView info;
    EditText accountName;
    Button checkBalance;
    String nodeUrl = "https://jovial-poitras.api.dfuse.dev/";

    List<String> logs = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // attach them to the respective id in the xml layout
        info = (TextView) findViewById(R.id.info);
        accountName = (EditText) findViewById(R.id.account_name);
        checkBalance = (Button) findViewById(R.id.check);

        // function to when the button is clicked
        this.checkBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBalance(nodeUrl);
            }
        });
    }

    private void update() {
        StringBuilder updateContentBuilder = new StringBuilder();
        for (String log : this.logs) {
            updateContentBuilder.append(log);
        }

        info.setText(Html.fromHtml(updateContentBuilder.toString()));
    }


    private void checkBalance(String nodeUrl) {
        // Collecting necessary data to check account balance
        final String account = ((EditText) findViewById(R.id.account_name)).getText().toString();

        checkBalance.setEnabled(false);
        new CheckBalanceTask(new CheckBalanceTask.CheckBalanceTaskCallback() {
            @Override
            public void update(String updateContent) {
                logs.add("<p>" + updateContent + "</p>");
                MainActivity.this.update();
            }

            @Override
            public void finish(boolean success, String updateContent, String balance) {
                String message = success ? htmlSuccessFormat(updateContent) : htmlErrorFormat(updateContent);
                message += "<p/>";
                logs.add(message);
                MainActivity.this.update();
                checkBalance.setEnabled(true);

                if (success) {
                    info.setVisibility(View.VISIBLE);
                    info.setText(String.format("%s %s", "Account balance is ", balance));
                } else {
                    info.setVisibility(View.GONE);
                }
            }
        }).execute(nodeUrl, account);
    }


    private String htmlErrorFormat(String error) {
        return "<p style='color: #FF6B68;'>" + error + "</p>";
    }

    private String htmlSuccessFormat(String msg) {
        return "<p style='color: #008000;'>" + msg + "</p>";
    }

}
