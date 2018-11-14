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

import java.util.Date;

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

        np_sys.setOnValueChangedListener(onValueChangeListener);
        NumberPicker np_dia = getView().findViewById(R.id.number_picker_dia);

        np_dia.setMinValue(30);
        np_dia.setMaxValue(250);
        np_dia.setValue(80);

        np_dia.setOnValueChangedListener(onValueChangeListener);

        NumberPicker np_pulse = getView().findViewById(R.id.number_picker_pulse);

        np_pulse.setMinValue(30);
        np_pulse.setMaxValue(250);
        np_pulse.setValue(72);

        np_sys.setOnValueChangedListener(onValueChangeListener);
    }
    public void setSettings(){
        setSaveButtonBP();
        updateBPText();
        updatePainText();
        setSaveButtonPain();
        setNumberPickers();
    }
    private void updateBPText(){
        TextView d = getView().findViewById(R.id.textView_bp);
        String sys = MySharedPreference.get(getContext(), title+"_SYS");
        if(sys==null){
            d.setText("Last saved: -");
            return;
        }
        String text = "Last saved:"
                +MySharedPreference.get(getContext(), title+"_TIME")
                +"    SYS="+MySharedPreference.get(getContext(), title+"_SYS")
        +"    Dia="+MySharedPreference.get(getContext(), title+"_DIA")
        +"    Pulse="+MySharedPreference.get(getContext(), title+"_PULSE");
        d.setText(text);
    }
    private void updatePainText(){
        LinearLayout ll = getView().findViewById(R.id.layout_painresult);
        ll.removeAllViews();
        if(MySharedPreference.get(getContext(), title+"_TOTAL")==null) return;
        int total = Integer.parseInt(MySharedPreference.get(getContext(), title+"_TOTAL"));
        for(int i =total;i>=0;i--){
            String s = MySharedPreference.get(getContext(), title+"_TIME_"+String.valueOf(i))
                    + "  CurPain="+MySharedPreference.get(getContext(), title+"_CP_"+String.valueOf(i))
                    + " MaxPain="+MySharedPreference.get(getContext(), title+"_MP_"+String.valueOf(i));
            TextView t = new TextView(getContext());
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
                MySharedPreference.set(getContext(), title+"_TIME", String.valueOf(DateFormat.format("yyyy-MM-dd hh:mm:ss a", new Date())));
                updateBPText();

                Toast.makeText(getContext(), title+" - Blood pressure saved", Toast.LENGTH_LONG).show();
            }
        });
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
                String tot = MySharedPreference.get(getContext(), title+"_count");
                int total=0;
                if(tot!=null)
                    total=Integer.valueOf(tot)+1;
                MySharedPreference.set(getContext(), title+"_CP_"+String.valueOf(total), String.valueOf(cp));
                MySharedPreference.set(getContext(), title+"_MP_"+String.valueOf(total), String.valueOf(mp));
                MySharedPreference.set(getContext(), title+"_TIME_"+String.valueOf(total), String.valueOf(DateFormat.format("yyyy-MM-dd hh:mm:ss a", new Date())));
                MySharedPreference.set(getContext(), title+"_TOTAL", String.valueOf(total));
                updatePainText();

                Toast.makeText(getContext(), title+" - Pain level saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(getContext(),
                            "selected number " + numberPicker.getValue(), Toast.LENGTH_SHORT);
                }
            };

}