package org.md2k.mcerebrum.opioidlabstudy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.repo.BubbleSeekBar;

import org.md2k.datakitapi.DataKitAPI;
import org.md2k.datakitapi.datatype.DataType;
import org.md2k.datakitapi.datatype.DataTypeDoubleArray;
import org.md2k.datakitapi.datatype.DataTypeInt;
import org.md2k.datakitapi.datatype.DataTypeIntArray;
import org.md2k.datakitapi.datatype.DataTypeString;
import org.md2k.datakitapi.exception.DataKitException;
import org.md2k.datakitapi.messagehandler.OnConnectionListener;
import org.md2k.datakitapi.source.datasource.DataSourceBuilder;
import org.md2k.datakitapi.source.datasource.DataSourceClient;
import org.md2k.datakitapi.source.datasource.DataSourceType;
import org.md2k.datakitapi.time.DateTime;

import java.util.Date;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class Tab1Fragment extends Fragment {
    String title;
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
        return view;
    }
    @Override
    public void onResume(){
        setSettings();
        super.onResume();
    }
    private void setNumberPickers(){
        NumberPicker np_sys = getView().findViewById(R.id.number_picker_sys);

        np_sys.setMinValue(30);
        np_sys.setMaxValue(250);
        np_sys.setValue(120);
        np_sys.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np_sys.setOnValueChangedListener(onValueChangeListener);
        NumberPicker np_dia = getView().findViewById(R.id.number_picker_dia);

        np_dia.setMinValue(30);
        np_dia.setMaxValue(250);
        np_dia.setValue(80);
        np_dia.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np_dia.setOnValueChangedListener(onValueChangeListener);

        NumberPicker np_pulse = getView().findViewById(R.id.number_picker_pulse);

        np_pulse.setMinValue(30);
        np_pulse.setMaxValue(250);
        np_pulse.setValue(72);
        np_pulse.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np_pulse.setOnValueChangedListener(onValueChangeListener);
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
        updateBPText();
        updatePainText();
        setSaveButtonPain();
        setNumberPickers();
    }
    private void updateBPText(){
        TextView d = getView().findViewById(R.id.textView_bp);
        String sys = MySharedPreference.get(getContext(), title+"_SYS");
        if(sys==null){
            d.setText("Last saved: ");
            return;
        }
        String text = "Last saved:      "
                +MySharedPreference.get(getContext(), title+"_TIME")   // only time
                +"       "+MySharedPreference.get(getContext(), title+"_SYS")
        +"/"+MySharedPreference.get(getContext(), title+"_DIA")
        +",  " +MySharedPreference.get(getContext(), title+"_PULSE")+" bpm";
        d.setText(text);
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
        for(int i =total;i>=0;i--){
            String s = MySharedPreference.get(getContext(), title+"_TIME_"+String.valueOf(i))
                    + "       Cur. pain="+MySharedPreference.get(getContext(), title+"_CP_"+String.valueOf(i))
                    + "     Max. pain="+MySharedPreference.get(getContext(), title+"_MP_"+String.valueOf(i));
            t = new TextView(getContext());
            t.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_500));
            t.setText(s);
            ll.addView(t);

        }
    }

    private void setSaveButtonBP(){
        FancyButton b = getView().findViewById(R.id.button_save_bp);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sys = ((NumberPicker)getView().findViewById(R.id.number_picker_sys)).getValue();
                int dia = ((NumberPicker)getView().findViewById(R.id.number_picker_dia)).getValue();
                int pulse = ((NumberPicker)getView().findViewById(R.id.number_picker_pulse)).getValue();
                MySharedPreference.set(getContext(), title+"_SYS", String.valueOf(sys));
                MySharedPreference.set(getContext(), title+"_DIA", String.valueOf(dia));
                MySharedPreference.set(getContext(), title+"_PULSE", String.valueOf(pulse));
                MySharedPreference.set(getContext(), title+"_TIME", String.valueOf(DateFormat.format("hh:mm:ss a", new Date())));
                updateBPText();
                long curTime = DateTime.getDateTime();
                DataTypeIntArray bloodPressure = new DataTypeIntArray(curTime, new int[]{sys, dia});
                saveToDataKit(DataSourceType.BLOOD_PRESSURE, bloodPressure);
                DataTypeInt heartRate = new DataTypeInt(curTime, pulse);
                saveToDataKit(DataSourceType.HEART_RATE, heartRate);

            }
        });
    }
    private void saveToDataKit(final String dataSourceType, final DataType dataType){
        try {
            DataKitAPI.getInstance(getContext()).connect(new OnConnectionListener() {
                @Override
                public void onConnected() {
                    DataSourceClient dc = null;
                    try {
                        dc = DataKitAPI.getInstance(getContext()).register(new DataSourceBuilder().setType(dataSourceType));
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
        FancyButton b = getView().findViewById(R.id.button_save_pain);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BubbleSeekBar cpSeekBar = getView().findViewById(R.id.bubbleSeekBarCurrentPain);
                BubbleSeekBar mpSeekBar = getView().findViewById(R.id.bubbleSeekBarMaximumPain);
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
                saveToDataKit("CURRENT_PAIN", curPain);
                DataTypeInt maxPain = new DataTypeInt(curTime, mp);
                saveToDataKit("MAXIMUM_PAIN", maxPain);
                DataTypeString labState = new DataTypeString(curTime, title);
                saveToDataKit("LAB_STATE", labState);

                updatePainText();
            }
        });
    }

    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                }
            };

}