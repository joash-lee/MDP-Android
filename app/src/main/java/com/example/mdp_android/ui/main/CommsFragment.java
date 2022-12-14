package com.example.mdp_android.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mdp_android.MainActivity;
import com.example.mdp_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.nio.charset.Charset;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "CommsFragment";
    private static long fastestTimer;
    private static TextView fastestTimetv;

    static Handler timerHandler = new Handler();
    private PageViewModel pageViewModel;

    // Declaration Variable
    // Shared Preferences
    SharedPreferences sharedPreferences;

    FloatingActionButton send;
    private static TextView messageReceivedtv;
    private EditText typeBoxEditText;
    private FloatingActionButton bluetoothsend;

    public static CommsFragment newInstance(int index) {
        CommsFragment fragment = new CommsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_comms, container, false);

        send = (FloatingActionButton) root.findViewById(R.id.messageButton);
        bluetoothsend =(FloatingActionButton) root.findViewById(R.id.messagebluetooth);
        // Message Box
        messageReceivedtv = (TextView) root.findViewById(R.id.messageReceivedTextView);
        messageReceivedtv.setMovementMethod(new ScrollingMovementMethod());
        typeBoxEditText = (EditText) root.findViewById(R.id.typeBoxEditText);
        fastestTimetv = root.findViewById(R.id.fastestTimeTextView);
        fastestTimer = 0;
        // get shared preferences
        sharedPreferences = getActivity().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLog("Clicked sendTextBtn");
                String sentText = "" + typeBoxEditText.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("message", sharedPreferences.getString("message", "") + '\n' + sentText);
                editor.commit();
                messageReceivedtv.setText(sharedPreferences.getString("message", ""));

                typeBoxEditText.setText("");

                if (BluetoothConnectionService.BluetoothConnectionStatus == true) {
                    byte[] bytes = sentText.getBytes(Charset.defaultCharset());
                    BluetoothConnectionService.write(bytes);
                }
                showLog("Exiting sendTextBtn");
            }
        });


        Runnable timerRunnableFastest2 = new Runnable() {
            @Override
            public void run() {
                long millisFastest = System.currentTimeMillis() - fastestTimer;
                int secondsFastest = (int) (millisFastest / 1000);
                int minutesFastest = secondsFastest / 60;
                secondsFastest = secondsFastest % 60;
                fastestTimetv.setText(String.format("%02d:%02d", minutesFastest, secondsFastest));
                timerHandler.postDelayed(this, 500);
            }
        };
        bluetoothsend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity.printMessage("ST "+GridMap.message);
                Toast.makeText(getActivity(), GridMap.message, Toast.LENGTH_LONG).show();

            }
        });

        return root;
    }

    private static void showLog(String message) {
        Log.d(TAG, message);
    }

    public static TextView getMessageReceivedTextView() {
        return messageReceivedtv;
    }
}