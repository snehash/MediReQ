package com.example.sneha.medireq;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by Sneha on 7/25/2015.
 */
public class Profile{
    public String name, address, phone, email, emergencyContact, emergencyNumber, gender,
            height, docname, docno,currMedi,vitamins, pastConddetails, surgicalHistory,
            allergiesOther,motherIllness, fatherIllness, sibilingIllness,otherFamilyHistory,
            lastVisit, birthday, momLiving, dadLiving;
    public Time lastUpdate;
    public int age, weight, momAge, dadAge;
    public boolean hospitalized, heptest, hepB, hepA, std, heartDisease,highcholest,highbp,
            lowbp, hearburn, anemia,swollenankles, shortbreath, asthma, lungprobs,sinus,
            seasonalAllergies, otherAllergies, tonsillitis, earprob, eye, seizure, stroke,headache,
            neuro, depression, psych, diabetes, kidney, liver, arthritis, cancer, ulcer, thyroid,
            penicillin, latex, tobacco, alcohol, coffee, exercise, fam_anemia, fam_cancer,
            fam_diabetes, fam_glaucoma, fam_hd, fam_hbp, fam_hiv, fam_depression, fam_stroke;
    public final String filename;


    public Profile(String name){
        this.name = name;
        filename = "/system/bin/MediReQ_" + name;
        address = "";
        phone = "";
        email = "";
        emergencyContact = "";
        emergencyNumber="";
        gender = "";
        height = "";
        docname = "";
        docno = "";
        currMedi = "";
        vitamins = "";
        pastConddetails= "";
        surgicalHistory = "";
        allergiesOther = "";
        motherIllness = "";
        fatherIllness = "";
        sibilingIllness = "";
        otherFamilyHistory = "";
        birthday = "";
        headache = false;
        lastUpdate = new Time(Time.getCurrentTimezone());
        lastUpdate.setToNow();
        lastVisit = "";
        age = -1;
        weight = -1;
        momAge = -1;
        dadAge = -1;
        hospitalized = false;
        heptest = false;
        hepB = true;
        hepA = true;
        std = false;
        heartDisease = false;
        highcholest = false;
        highbp = false;
        lowbp = false;
        hearburn = false;
        anemia = false;
        swollenankles = false;
        shortbreath = false;
        asthma = false;
        lungprobs = false;
        sinus = false;
        seasonalAllergies = false;
        otherAllergies = false;
        tonsillitis = false;
        earprob = false;
        eye = false;
        seizure = false;
        stroke = false;
        neuro = false;
        depression = false;
        psych = false;
        diabetes = false;
        kidney = false;
        liver = false;
        arthritis = false;
        cancer = false;
        ulcer = false;
        thyroid = false;
        penicillin = false;
        latex = false;
        tobacco = false;
        alcohol = false;
        coffee = false;
        exercise = false;
        momLiving = "";
        dadLiving = "";
        fam_anemia = false;
        fam_cancer = false;
        fam_diabetes = false;
        fam_glaucoma = false;
        fam_hd = false;
        fam_hbp = false;
        fam_hiv = false;
        fam_depression = false;
        fam_stroke = false;

    }


    //Todo: Might need to override equals

}
