package org.md2k.mcerebrum.opioidlabstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.md2k.datakitapi.DataKitAPI;
import org.md2k.datakitapi.datatype.DataType;
import org.md2k.datakitapi.datatype.DataTypeIntArray;
import org.md2k.datakitapi.exception.DataKitException;
import org.md2k.datakitapi.messagehandler.OnConnectionListener;
import org.md2k.datakitapi.source.datasource.DataSourceBuilder;
import org.md2k.datakitapi.source.datasource.DataSourceClient;
import org.md2k.datakitapi.time.DateTime;

import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;

public class ActivityBloodPressure extends AppCompatActivity {
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);
        title = getIntent().getStringExtra("title");
        setSaveButtonBP();
        updateBPText();
        setNumberPickers();
    }
    private void setSaveButtonBP(){
        FancyButton b = findViewById(R.id.button_save_bp);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sys = ((NumberPicker)findViewById(R.id.number_picker_sys)).getValue();
                int dia = ((NumberPicker)findViewById(R.id.number_picker_dia)).getValue();
                int pulse = ((NumberPicker)findViewById(R.id.number_picker_pulse)).getValue();
                MySharedPreference.set(ActivityBloodPressure.this, title+"_SYS", String.valueOf(sys));
                MySharedPreference.set(ActivityBloodPressure.this, title+"_DIA", String.valueOf(dia));
                MySharedPreference.set(ActivityBloodPressure.this, title+"_PULSE", String.valueOf(pulse));
                MySharedPreference.set(ActivityBloodPressure.this, title+"_TIME", String.valueOf(DateFormat.format("hh:mm:ss a", new Date())));
                updateBPText();
                long curTime = DateTime.getDateTime();
                DataTypeIntArray bloodPressure = new DataTypeIntArray(curTime, new int[]{sys, dia});
                saveToDataKit("BLOOD_PRESSURE", bloodPressure);
//                DataTypeInt heartRate = new DataTypeInt(curTime, pulse);
//                saveToDataKit("HEART_RATE", heartRate);

            }
        });
    }
    private void updateBPText(){
        TextView d = findViewById(R.id.textView_bp);
        String sys = MySharedPreference.get(this, title+"_SYS");
        if(sys==null){
            d.setText("Last saved: ");
            return;
        }
        String text = "Last saved:      "
                +MySharedPreference.get(this, title+"_TIME")   // only time
                +"       "+MySharedPreference.get(this, title+"_SYS")
                +"/"+MySharedPreference.get(this, title+"_DIA")
                +",  " +MySharedPreference.get(this, title+"_PULSE")+" bpm";
        d.setText(text);
    }

    private void saveToDataKit(final String dataSourceType, final DataType dataType){
        try {
            DataKitAPI.getInstance(this).connect(new OnConnectionListener() {
                @Override
                public void onConnected() {
                    DataSourceClient dc = null;
                    try {
                        dc = DataKitAPI.getInstance(ActivityBloodPressure.this).register(new DataSourceBuilder().setType(dataSourceType));
                        DataKitAPI.getInstance(ActivityBloodPressure.this).insert(dc, dataType);
                    } catch (DataKitException e) {
                        finish();
                    }
                }
            });
        } catch (DataKitException e) {
            finish();
        }

    }
    private void setNumberPickers(){
        NumberPicker np_sys = findViewById(R.id.number_picker_sys);

        np_sys.setMinValue(30);
        np_sys.setMaxValue(250);
        np_sys.setValue(120);
        np_sys.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np_sys.setOnValueChangedListener(onValueChangeListener);
        NumberPicker np_dia = findViewById(R.id.number_picker_dia);

        np_dia.setMinValue(30);
        np_dia.setMaxValue(250);
        np_dia.setValue(80);
        np_dia.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np_dia.setOnValueChangedListener(onValueChangeListener);

        NumberPicker np_pulse = findViewById(R.id.number_picker_pulse);

        np_pulse.setMinValue(30);
        np_pulse.setMaxValue(250);
        np_pulse.setValue(72);
        np_pulse.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np_pulse.setOnValueChangedListener(onValueChangeListener);
    }
    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                }
            };


}
