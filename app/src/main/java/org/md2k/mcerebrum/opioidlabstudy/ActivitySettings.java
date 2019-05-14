package org.md2k.mcerebrum.opioidlabstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import org.md2k.datakitapi.DataKitAPI;
import org.md2k.datakitapi.datatype.DataType;
import org.md2k.datakitapi.datatype.DataTypeStringArray;
import org.md2k.datakitapi.exception.DataKitException;
import org.md2k.datakitapi.messagehandler.OnConnectionListener;
import org.md2k.datakitapi.source.datasource.DataSourceBuilder;
import org.md2k.datakitapi.source.datasource.DataSourceClient;
import org.md2k.datakitapi.time.DateTime;
import org.md2k.mcerebrum.opioidlabstudy.ema.EMAManager;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class ActivitySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        setCheckBox();
        setSaveButtonBP();
    }
    private void setSaveButtonBP(){
        FancyButton b = findViewById(R.id.button_save_medication);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> medications = getMedication();
                String[] bar = medications.toArray(new String[medications.size()]);
                DataTypeStringArray dataTypeStringArray = new DataTypeStringArray(DateTime.getDateTime(), bar);
                saveToDataKit("MEDICATION", dataTypeStringArray);
                MySharedPreference.setMedications(ActivitySettings.this, medications);
                EMAManager.create(ActivitySettings.this, medications);
                Toast.makeText(ActivitySettings.this, "Medication information saved", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    private void saveToDataKit(final String dataSourceType, final DataType dataType){
        try {
            DataKitAPI.getInstance(this).connect(new OnConnectionListener() {
                @Override
                public void onConnected() {
                    DataSourceClient dc = null;
                    try {
                        dc = DataKitAPI.getInstance(ActivitySettings.this).register(new DataSourceBuilder().setType(dataSourceType));
                        DataKitAPI.getInstance(ActivitySettings.this).insert(dc, dataType);
                    } catch (DataKitException e) {
                        finish();
                    }
                }
            });
        } catch (DataKitException e) {
            finish();
        }

    }

    void setCheckBox(){
        ArrayList<String> medications = MySharedPreference.getMedications(ActivitySettings.this);
        CheckBox c;
        for(int i=0;i<medications.size();i++){
            c = findViewById(R.id.checkbox_1);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_2);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_3);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_4);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_5);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_6);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_7);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_8);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_9);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_10);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_11);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_12);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_13);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_14);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_15);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_16);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_17);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_18);if(c.getText().equals(medications.get(i))) c.setChecked(true);
            c = findViewById(R.id.checkbox_19);if(c.getText().equals(medications.get(i))) c.setChecked(true);
        }
    }
    private ArrayList<String> getMedication(){
        ArrayList<String> medications=new ArrayList<>();
        CheckBox c;
        c = findViewById(R.id.checkbox_1);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_2);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_3);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_4);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_5);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_6);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_7);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_8);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_9);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_10);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_11);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_12);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_13);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_14);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_15);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_16);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_17);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_18);if(c.isChecked()) medications.add((String) c.getText());
        c = findViewById(R.id.checkbox_19);if(c.isChecked()) medications.add((String) c.getText());
        return medications;
    }
    @Override
    public void onDestroy(){
        try {
            DataKitAPI.getInstance(this).disconnect();
        }catch (Exception e){}
        super.onDestroy();
    }

}
