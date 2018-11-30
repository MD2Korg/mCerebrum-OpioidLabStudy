package org.md2k.mcerebrum.opioidlabstudy;

import android.content.Intent;
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

}