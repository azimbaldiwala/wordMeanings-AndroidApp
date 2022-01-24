package com.azimbaldiwala.wordmeanings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.io.OutputStreamWriter;

import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;



import java.net.URL;




public class MainActivity extends AppCompatActivity {


    public void getWord_(View view) throws IOException {

        String[] wordMeanings = new String[2];

        int wordSize=0;
        String initial="*";

        EditText wordSize_ = (EditText) findViewById(R.id.wordSize);
        EditText wordInitial = (EditText) findViewById(R.id.wordInitial);

        String wordSize__ = wordSize_.getText().toString();
        String wordInitial__ = wordInitial.getText().toString();

        int fWordsize = Integer.parseInt(wordSize__);


        wordMeanings = cleanData(getWord(fWordsize, wordInitial__));

        TextView showWord = (TextView) findViewById(R.id.displayWord);
        TextView showMeaning = (TextView) findViewById(R.id.showMeaning);

        String finalWordMeaning = "Meaning: " + wordMeanings[1];

        showWord.setText(wordMeanings[0]);
        showMeaning.setText(finalWordMeaning);

    }


    public  String[] cleanData(String data){

        // Each time first element will be the word followed by meaning [meanings can be multiple strings]

        String[] wordMeanings = new String[2]; // At max it will take 1 meanings not more than 1
        wordMeanings[0] = "";
        wordMeanings[1] = "";
        for(int i=4; i<data.length(); i++){
            if(data.charAt(i) == ':')
            {
                break;
            }

            wordMeanings[0] = wordMeanings[0] + data.charAt(i);
          //  System.out.println(data.charAt(i));
        }


        // Now that we have got the word .. we need to find its meaning...
        int flag = 0, start=0;
        for(int i=0; i<data.length(); i++){

            if(data.charAt(i) == '['){
                i+=6;
                flag = 1;
                start = 0;

            }

            if(data.charAt(i)=='.' && start > 5){   // Start is kept cuz sometimes we get meanings like 1.meaninggggggg.
                break;
            }

            if(flag == 1){

                wordMeanings[1] = wordMeanings[1] + data.charAt(i);
               // System.out.println(data.charAt(i));
                start+=1;
            }
        }

        return wordMeanings;

    }



    public String getWord(int wordSize , String initial) throws IOException {

        String url_ = "http://azimbaldiwala.pythonanywhere.com/";
        if(wordSize > 0)
        {
            String wordSize_ = ""  + wordSize;
            url_ = url_ + wordSize_ + "/";

        }

        if(!initial.equals("*"))
        {
            url_ = url_ + initial;
        }


        URL urlForGetRequest = new URL(url_);
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
        int responseCode = conection.getResponseCode();


        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            // print result
            System.out.println("JSON String Result " + response.toString());
            //GetAndPost.POSTRequest(response.toString());
            String response_ = response.toString();
            return response.toString();
        } else {
            System.out.println("GET NOT WORKED");

            return " ";
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}