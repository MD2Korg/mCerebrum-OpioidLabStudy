package org.md2k.mcerebrum.opioidlabstudy.dialog;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.md2k.datakitapi.DataKitAPI;
import org.md2k.datakitapi.datatype.DataTypeString;
import org.md2k.datakitapi.exception.DataKitException;
import org.md2k.datakitapi.messagehandler.OnConnectionListener;
import org.md2k.datakitapi.source.datasource.DataSourceBuilder;
import org.md2k.datakitapi.source.datasource.DataSourceClient;
import org.md2k.datakitapi.time.DateTime;
import org.md2k.mcerebrum.opioidlabstudy.R;

public class ActivityDialogPain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_pain);
        setTitle("Pain Medication");
        new MaterialDialog.Builder(this)
                .title("Pain Medication")
                .content("Did you take pain medication?")
                .positiveText("Yes")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            DataKitAPI.getInstance(ActivityDialogPain.this).connect(new OnConnectionListener() {
                                @Override
                                public void onConnected() {
                                    try {
                                        DataSourceClient dc = DataKitAPI.getInstance(ActivityDialogPain.this).register(new DataSourceBuilder().setType("SELF_REPORT"));
                                        DataTypeString dataTypeString = new DataTypeString(DateTime.getDateTime(), "PAIN");
                                        DataKitAPI.getInstance(ActivityDialogPain.this).insert(dc, dataTypeString);
                                        DataKitAPI.getInstance(ActivityDialogPain.this).disconnect();
                                        finish();
                                    }catch (Exception e){}
                                }
                            });
                        } catch (DataKitException e) {
                            e.printStackTrace();
                        }
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                finish();
            }
        })
                .show();
    }
}
