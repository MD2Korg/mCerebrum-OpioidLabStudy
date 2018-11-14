package org.md2k.mcerebrum.opioidlabstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

public class ActivitySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        setSaveButtonBP();
    }
    private void setSaveButtonBP(){
        FancyButton b = findViewById(R.id.button_save_medication);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivitySettings.this, "Medication information saved", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

}
