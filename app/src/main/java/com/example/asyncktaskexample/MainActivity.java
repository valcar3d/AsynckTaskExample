package com.example.asyncktaskexample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //Whe need to declare global variables to use in our AsyncTasks and our MainActivity
    ImageView imageView = null;
    ProgressDialog progressDialog = null;
    Integer drawablePath;
    DoSomeWorkInBackground asynckTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image);
        Button btnDownload = findViewById(R.id.btnDownload);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AsyncTasks can't be reused.
                // You have to create a new instance every time you want to run one.
                asynckTask = new DoSomeWorkInBackground();
                asynckTask.execute();//Calling the AsynckTask method to execute
            }
        });

    }

    //AsyncTasks is dependant of the activity lifecycle, and inner classes are the way to run it
    private class DoSomeWorkInBackground extends AsyncTask<String, String, Integer> {

        //AsyncTask<Params, Progress, Result>
        //Params - the type that is passed into the execute() method.
        //Progress - the type that is used within the task to track progress.
        //Result - the type that is returned by doInBackground().

        //onPreExecute - executed in the main thread to do things like create the initial progress bar view.
        //or Cancel the execution
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Downloading...wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    asynckTask.cancel(true);
                    //Click anywhere to cancel download
                    Toast.makeText(MainActivity.this, "Download canceled", Toast.LENGTH_LONG).show();
                }
            });
            progressDialog.show();
        }


        //doInBackground - executed in the background thread to do things like network downloads.
        @Override
        protected Integer doInBackground(String... strings) {
            //Use any method that needs to run an extended period of time here
            try {
                //Simulate a "long running operation"
                Thread.sleep(6000);
                drawablePath = R.drawable.xpsimage;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return drawablePath;
        }


        //onPostExecute - executed in the main thread to do things like set image views.
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (imageView != null) {
                progressDialog.hide();
                imageView.setImageResource(integer);
            } else {
                progressDialog.show();
            }
        }
    }
}