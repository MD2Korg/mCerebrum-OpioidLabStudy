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
    private static final String evening_diary="evening_diary";
    private static final String event_contingent_entry="event_contingent_entry";
    private static final String morning_diary="morning_diary";
    private static final String random_prompt="random_prompt";
    private static final String medication="medication";
    public static void create(Context context, ArrayList<String> medications){
        create(context, medications, evening_diary);
        create(context, medications, event_contingent_entry);
        create(context, medications, morning_diary);
        create(context, medications, random_prompt);
    }
    private static void create(Context context, ArrayList<String> medications, String filePrefix){
        ArrayList<Question> start = readQuestion(context, filePrefix+"_start.json");
        ArrayList<Question> end = readQuestion(context, filePrefix+"_end.json");
        Question med = readQuestion(context, medication+".json").get(0);
        for(int i = 0;i<medications.size();i++){
            String qText = med.getQuestion_text().replace("<OPIOID>", medications.get(i));
            Question q = new Question(start.size(), qText, med.getQuestion_type(), med.getResponse_option(), null);
            start.add(q);
        }
        for(int i =0;i<end.size();i++){
            Question eQ = end.get(i);
            eQ.setQuestion_id(start.size());
            start.add(eQ);
        }
        try {
            Storage.writeJsonArray(Constants.CONFIG_DIRECTORY+filePrefix+".json", start);
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
