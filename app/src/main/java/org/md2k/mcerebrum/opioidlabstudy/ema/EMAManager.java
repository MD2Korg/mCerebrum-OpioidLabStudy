package org.md2k.mcerebrum.opioidlabstudy.ema;

import android.content.Context;

import org.md2k.mcerebrum.commons.storage.Storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class EMAManager {
    private static final String evening_diary="evening_diary.json";
    private static final String event_contingent_entry="event_contingent_entry.json";
    private static final String morning_diary="morning_diary.json";
    private static final String random_prompt="random_prompt.json";
    public static void create(Context context, ArrayList<String> medications){
        ArrayList<String> conditionRandom = new ArrayList<>();conditionRandom.add("4:Yes");
        ArrayList<String> conditionEvening = new ArrayList<>();conditionEvening.add("21:Yes");
        ArrayList<String> conditionMorning = new ArrayList<>();conditionMorning.add("11:Yes");
        create(context, medications, evening_diary, conditionEvening);
        create(context, medications, morning_diary, conditionMorning);
        create(context, medications, random_prompt, conditionRandom);
        create(context, medications, event_contingent_entry, null);
    }
    private static void create(Context context, ArrayList<String> medications, String fileName, ArrayList<String> condition){
        ArrayList<String> defaultCondition = new ArrayList<>();
        defaultCondition.add("0:null");
        ArrayList<Question> start = readQuestion(context, fileName);
        for(int i =0;i<start.size();i++){
            if(start.get(i).getQuestion_text().startsWith("How many pills of") && start.get(i).getQuestion_text().endsWith("did you take?")) {
                start.get(i).setCondition(defaultCondition);
                for (int j = 0; j < medications.size(); j++) {
                    if (start.get(i).getQuestion_text().contains("<i>"+medications.get(j)+"</i>")) {
                        start.get(i).setCondition(condition);
                    }
                }
            }
        }
        try {
            Storage.writeJsonArray(Constants.CONFIG_DIRECTORY+fileName, start);
        } catch (IOException e) {

        }
    }
    private static ArrayList<Question> readQuestion(Context context, String fileName){
        ArrayList<Question> questions=new ArrayList<>();
        String filePath = Constants.CONFIG_DIRECTORY+fileName;
        try {
            questions = Storage.readJsonArrayList(filePath, Question.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
