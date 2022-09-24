package com.example.mdp_android.ui.main;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mdp_android.R;

import java.util.ArrayList;

public class Userinput extends Activity{

    Button mButton;
    EditText mEdit;
    TextView mText;
    Button closebutton;
    static ArrayList<String> aList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        mButton = (Button)findViewById(R.id.button1);
        closebutton = (Button)findViewById(R.id.btnclose);

        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                finish();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEdit   = (EditText)findViewById(R.id.editText1);
                mText = (TextView)findViewById(R.id.textView1);
                //mText.setText("Welcome "+mEdit.getText().toString()+"!");


                //aList.add("N");
                //aList.add("S");
                //aList.add("E");
                //aList.add("W");
                aList.add(mEdit.getText().toString().trim());

                System.out.println("Integer Number Added in ArrayList= " + aList);


            }
        });
    }

    public static ArrayList<String> getdirection(){
        return aList;
    }


}

