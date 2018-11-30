package org.md2k.mcerebrum.opioidlabstudy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import org.md2k.datakitapi.DataKitAPI;
import org.md2k.datakitapi.datatype.DataType;
import org.md2k.datakitapi.datatype.DataTypeInt;
import org.md2k.datakitapi.datatype.DataTypeString;
import org.md2k.datakitapi.exception.DataKitException;
import org.md2k.datakitapi.messagehandler.OnConnectionListener;
import org.md2k.datakitapi.source.datasource.DataSourceBuilder;
import org.md2k.datakitapi.source.datasource.DataSourceClient;
import org.md2k.datakitapi.time.DateTime;

import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;

public class Tab1Fragment extends Fragment {
    String title;
    Handler h;
    public static Tab1Fragment newInstance(String title){
        Tab1Fragment fragment = new Tab1Fragment();

        Bundle args = new Bundle();
        args.putCharSequence("title", title);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_one, container, false);
        Bundle args = getArguments();
        title = (String) args.getCharSequence("title", "NO TITLE FOUND");
        h = new Handler();
        return view;
    }
    @Override
    public void onResume(){
        setSettings();
        super.onResume();
    }
    void setSeekbars(){
        final BubbleSeekBar cpSeekBar = getView().findViewById(R.id.bubbleSeekBarCurrentPain);
        final BubbleSeekBar mpSeekBar = getView().findViewById(R.id.bubbleSeekBarMaximumPain);
        cpSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                cpSeekBar.correctOffsetWhenContainerOnScrolling();
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }
        });
        mpSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                mpSeekBar.correctOffsetWhenContainerOnScrolling();
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }
        });


    }
    public void setSettings(){
        setSaveButtonBP();
        setSeekbars();
        updatePainText();
        setSaveButtonPain();
        setThermalButton();
        updateThermal();
    }
    private void updateThermal(){
        final FancyButton start = getView().findViewById(R.id.button_thermal_start);
        final FancyButton stop = getView().findViewById(R.id.button_thermal_stop);
        final FancyButton pause = getView().findViewById(R.id.button_thermal_pause);
        final FancyButton resume = getView().findViewById(R.id.button_thermal_resume);
        final TextView textView = getView().findViewById(R.id.textview_thermal_state);

        String state = MySharedPreference.get(getContext(), title+"_THERMAL");
        if(state==null){
            textView.setText("NOT STARTED");
            enable(start);
            disable(stop);
            disable(pause);
            disable(resume);

        }else{
            switch(state){
                case "START":
                    textView.setText("RUNNING...");
                    disable(start);
                    enable(stop);
                    enable(pause);
                    disable(resume);
                    break;
                case "STOP":
                    textView.setText("STOPPED");
                    enable(start);
                    disable(stop);
                    disable(pause);
                    disable(resume);
                    break;
                case "PAUSE":
                    textView.setText("PAUSED");
                    disable(start);
                    disable(pause);
                    enable(stop);
                    enable(resume);
                    break;
                case "RESUME":
                    textView.setText("RUNNING...");
                    disable(start);
                    enable(pause);
                    enable(stop);
                    disable(resume);
                    break;
            }
        }
    }
    private void disable(FancyButton b){
        b.setEnabled(false);
    }
    private void enable(FancyButton b){
        b.setEnabled(true);
    }
    private void setThermalButton(){
        final FancyButton start = getView().findViewById(R.id.button_thermal_start);
        final FancyButton stop = getView().findViewById(R.id.button_thermal_stop);
        final FancyButton pause = getView().findViewById(R.id.button_thermal_pause);
        final FancyButton resume = getView().findViewById(R.id.button_thermal_resume);
        final TextView textView = getView().findViewById(R.id.textview_thermal_state);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreference.set(getContext(), title+"_THERMAL", "START");
                DataTypeString dataTypeString = new DataTypeString(DateTime.getDateTime(), "START");
                saveToDataKit("THERMAL_STIMULATION",title, dataTypeString);
                updateThermal();
                Vibration.vibrate(getContext());
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreference.set(getContext(), title+"_THERMAL", "STOP");
                DataTypeString dataTypeString = new DataTypeString(DateTime.getDateTime(), "STOP");
                saveToDataKit("THERMAL_STIMULATION",title, dataTypeString);
                updateThermal();
                Vibration.vibrate(getContext());
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreference.set(getContext(), title+"_THERMAL", "PAUSE");
                DataTypeString dataTypeString = new DataTypeString(DateTime.getDateTime(), "PAUSE");
                saveToDataKit("THERMAL_STIMULATION",title, dataTypeString);
                updateThermal();
                Vibration.vibrate(getContext());
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreference.set(getContext(), title+"_THERMAL", "RESUME");
                DataTypeString dataTypeString = new DataTypeString(DateTime.getDateTime(), "RESUME");
                saveToDataKit("THERMAL_STIMULATION",title, dataTypeString);
                updateThermal();
                Vibration.vibrate(getContext());
            }
        });

    }
    private void updatePainText(){
        LinearLayout ll = getView().findViewById(R.id.layout_painresult);
        ll.removeAllViews();
        TextView t = new TextView(getContext());
        t.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_500));
        t.setText("Last saved:");
        ll.addView(t);

        if(MySharedPreference.get(getContext(), title+"_TOTAL")==null) return;
        int total = Integer.parseInt(MySharedPreference.get(getContext(), title+"_TOTAL"));
        for(int i =0;i<=total;i++){
            t = new TextView(getContext());
            String ss = String.format("%2d.%15s    Cur.Pain=%4s    Max.Pain=%4s"
                    ,i+1
                    , MySharedPreference.get(getContext(), title+"_TIME_"+String.valueOf(i))
                    ,MySharedPreference.get(getContext(), title+"_CP_"+String.valueOf(i))
                    ,MySharedPreference.get(getContext(), title+"_MP_"+String.valueOf(i))
            );
            t.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_500));
            t.setText(ss);
            ll.addView(t);
        }
    }

    private void setSaveButtonBP(){
        FancyButton b = getView().findViewById(R.id.button_open_bp);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityBloodPressure.class);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }
    private void saveToDataKit(final String dataSourceType, final String dataSourceId, final DataType dataType){
        try {
            DataKitAPI.getInstance(getContext()).connect(new OnConnectionListener() {
                @Override
                public void onConnected() {
                    DataSourceClient dc = null;
                    try {
                        dc = DataKitAPI.getInstance(getContext()).register(new DataSourceBuilder().setType(dataSourceType).setId(dataSourceId));
                        DataKitAPI.getInstance(getContext()).insert(dc, dataType);
                    } catch (DataKitException e) {
                        getActivity().finish();
                    }
                }
            });
        } catch (DataKitException e) {
            getActivity().finish();
        }

    }
    private void setSaveButtonPain(){
        final FancyButton b = getView().findViewById(R.id.button_save_pain);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BubbleSeekBar cpSeekBar = getView().findViewById(R.id.bubbleSeekBarCurrentPain);
                BubbleSeekBar mpSeekBar = getView().findViewById(R.id.bubbleSeekBarMaximumPain);
                Vibration.vibrate(getContext());
                int cp = cpSeekBar.getProgress();
                int mp = mpSeekBar.getProgress();
                String tot = MySharedPreference.get(getContext(), title+"_TOTAL");
                int total=0;
                if(tot!=null)
                    total=Integer.valueOf(tot)+1;
                MySharedPreference.set(getContext(), title+"_CP_"+String.valueOf(total), String.valueOf(cp));
                MySharedPreference.set(getContext(), title+"_MP_"+String.valueOf(total), String.valueOf(mp));
                MySharedPreference.set(getContext(), title+"_TIME_"+String.valueOf(total), String.valueOf(DateFormat.format("hh:mm:ss a", new Date())));
                MySharedPreference.set(getContext(), title+"_TOTAL", String.valueOf(total));
                long curTime = DateTime.getDateTime();
                DataTypeInt curPain = new DataTypeInt(curTime, cp);
                saveToDataKit("CURRENT_PAIN", title, curPain);
                DataTypeInt maxPain = new DataTypeInt(curTime, mp);
                saveToDataKit("MAXIMUM_PAIN", title, maxPain);

                updatePainText();
                cpSeekBar.setProgress(0);
                mpSeekBar.setProgress(0);
                b.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_green_900));
                b.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                b.setText("Saved");
                h.postDelayed(r, 5000);

            }
        });
    }
    Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                FancyButton b = getView().findViewById(R.id.button_save_pain);
                b.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.backGroundDark));
                b.setTextColor(Color.RED);
                b.setText("Save");
            }catch (Exception e){

            }
        }
    };

}