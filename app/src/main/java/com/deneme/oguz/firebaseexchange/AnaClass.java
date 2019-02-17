package com.deneme.oguz.firebaseexchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AnaClass extends AppCompatActivity {

    DatabaseReference myRef;
    FirebaseDatabase firebaseDatabase;

    TextView textBakiyeTRY,textBakiyeUSD,textBakiyeEUR,textBakiyeGBP,textUSD,textEUR,textGBP;
    EditText editText;
    Button buttonSatinal,buttonBozdur,buttonSirala;
    RadioGroup radioGroup;
    RadioButton radioEUR,radioUSD,radioGBP,RB;

    String edittextS;
    int girilentutar;

    int control;
    String s1,s2,s3;
    static float bakiyeTL,bakiyeDOLAR,bakiyeEURO,bakiyeGBP;

    SharedPreferences sharedPre;
    SharedPreferences.Editor editor;

    Kullanici k1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_class);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("kullanicilar");

        Intent intent=getIntent();
        String isim=intent.getStringExtra("isim");
        String sifre=intent.getStringExtra("sifre");
        k1=new Kullanici(isim,sifre);

        sharedPre=getPreferences(MODE_PRIVATE);
        editor=sharedPre.edit();

        Query sorgu = myRef.orderByChild("ad").equalTo(isim);
        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){ //for-each

                    //d -> o anki döngüdeki veri'dir!!
                    Kullanici kullanici=d.getValue(Kullanici.class); //kişi nesnesini DB den almak

                    String key = d.getKey();//d verisinin key ini alıp tutacağız, ilerde silmek ve güncellemek için lazım oluyor...
                    bakiyeTL=kullanici.getTRY();
                    bakiyeDOLAR=kullanici.getUSD();
                    bakiyeEURO=kullanici.getEUR();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textBakiyeTRY=findViewById(R.id.textBakiyeTRY);
        textBakiyeUSD=findViewById(R.id.textBakiyeUSD);
        textBakiyeEUR=findViewById(R.id.textBakiyeEUR);
        textBakiyeGBP=findViewById(R.id.textBakiyeGBP);


        textBakiyeTRY.setText(" "+bakiyeTL+" ₺");
        textBakiyeUSD.setText(" "+bakiyeDOLAR+ " $");
        textBakiyeEUR.setText(" "+bakiyeEURO+ " €");
        textBakiyeGBP.setText(" "+bakiyeGBP+ " £");

        textUSD=findViewById(R.id.textUSD);
        textEUR=findViewById(R.id.textEUR);
        textGBP=findViewById(R.id.textGBP);

        s1=sharedPre.getString("s1","USD - ALIŞ=0, SATIŞ=0");
        s2=sharedPre.getString("s2","EUR - ALIŞ=0, SATIŞ=0");
        s3=sharedPre.getString("s3","GBP - ALIŞ=0, SATIŞ=0");

        textUSD.setText(s1);
        textEUR.setText(s2);
        textGBP.setText(s3);

        editText=findViewById(R.id.editText);

        radioGroup=findViewById(R.id.radioGroup);

        radioEUR=findViewById(R.id.radioEUR);
        radioUSD=findViewById(R.id.radioUSD);
        radioGBP=findViewById(R.id.radioGBP);

        buttonSatinal=findViewById(R.id.buttonSatinal);
        buttonSatinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                control=0;
                new ArkaPlan().execute("https://www.doviz.gen.tr/doviz_json.asp?version=1.0.4");

            }
        });

        buttonBozdur=findViewById(R.id.buttonBozdur);
        buttonBozdur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                control=1;
                new ArkaPlan().execute("https://www.doviz.gen.tr/doviz_json.asp?version=1.0.4");

            }
        });

        buttonSirala=findViewById(R.id.buttonSirala);
        buttonSirala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(AnaClass.this,SiralamaClass.class);
                startActivity(i);

            }
        });
    }

    class ArkaPlan extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection;
            BufferedReader buf;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                buf = new BufferedReader(new InputStreamReader(is));

                String satir, dosya ="";

                while ((satir = buf.readLine()) != null) {
                    //Log.d("satir", satir);
                    dosya += satir;  // WHİLE BİTTİGİNDE SUNUCUDAKİ TÜM SATİRLARI ELDE ETMİŞ OLUCAĞIM

                }
                return dosya;

            } catch (Exception e) {
                e.printStackTrace();

            }

            return "sorun";


        }
        @Override
        protected void onPostExecute(String s) {

            int selectedRB= radioGroup.getCheckedRadioButtonId(); //********************
            RB =findViewById(selectedRB);                   //SEÇİLMİŞ RB Yİ BELİRLEDİK

            edittextS=editText.getText().toString();         //EditText teki sayı ---editInt---
            girilentutar=Integer.parseInt(edittextS);

            try{
                JSONObject json =new JSONObject(s);

                if(control==0){ //SATIN AL BUTONUNA BASILDIYSA

                    if(RB==radioUSD){

                        double kur=json.getDouble("dolar2");
                        double a=kur*girilentutar;
                        bakiyeTL-=a;
                        textBakiyeTRY.setText(" "+ bakiyeTL +" ₺");
                        bakiyeDOLAR +=girilentutar;
                        textBakiyeUSD.setText(" "+ bakiyeDOLAR + " $");

                    }
                    if(RB==radioEUR){

                        double kur=json.getDouble("euro2");
                        double a =kur*girilentutar;
                        bakiyeTL -=a;
                        textBakiyeTRY.setText(" "+bakiyeTL+" TL");
                        bakiyeEURO+=girilentutar;
                        textBakiyeEUR.setText(" "+bakiyeEURO + " €");

                    }
                    if(RB==radioGBP){

                        double kur=json.getDouble("sterlin2");
                        double a=kur*girilentutar;
                        bakiyeTL-=a;
                        textBakiyeTRY.setText(" "+ bakiyeTL +" ₺");
                        bakiyeDOLAR +=girilentutar;
                        textBakiyeGBP.setText(" "+ bakiyeGBP + " £");

                    }

                }
                if(control==1){ //BOZDUR BUTONUNA TIKLANDIYSA
                    if(RB==radioUSD){

                        double kur=json.getDouble("dolar");
                        double a=kur*girilentutar;
                        bakiyeTL+=a;
                        textBakiyeTRY.setText(" "+bakiyeTL+" ₺");
                        bakiyeDOLAR-=girilentutar;
                        textBakiyeUSD.setText(" "+bakiyeDOLAR+" $");

                    }
                    if(RB==radioEUR){

                        double kur=json.getDouble("euro");
                        double a=kur*girilentutar;
                        bakiyeTL+=a;
                        textBakiyeTRY.setText(" "+bakiyeTL+" ₺");
                        bakiyeEURO-=girilentutar;
                        textBakiyeEUR.setText(" "+bakiyeEURO+" €");

                    }
                    if(RB==radioGBP){

                        double kur=json.getDouble("sterlin");
                        double a=kur*girilentutar;
                        bakiyeTL+=a;
                        textBakiyeTRY.setText(" "+bakiyeTL+" ₺");
                        bakiyeGBP-=girilentutar;
                        textBakiyeGBP.setText(" "+bakiyeGBP+" €");

                    }
                }
                double a,b,c,d,e,f;
                a=json.getDouble("dolar");
                b=json.getDouble("dolar2");
                c=json.getDouble("euro");
                d=json.getDouble("euro2");
                e=json.getDouble("sterlin");
                f=json.getDouble("sterlin2");

                String str1,str2,str3;
                str1= "DOLAR - ALIŞ="+a+", SATIŞ="+b;
                str2= "EURO - ALIŞ="+c+", SATIŞ="+d;
                str3= "STERLİN - ALIŞ="+e+", SATIŞ="+f;

                textUSD.setText(str1);
                textEUR.setText(str2);
                textGBP.setText(str3);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,Object> bilgiler =new HashMap<>();
                        bilgiler.put("TRY",bakiyeTL);
                        bilgiler.put("EUR",bakiyeEURO);
                        bilgiler.put("USD",bakiyeDOLAR);
                        bilgiler.put("GBP",bakiyeGBP);
                        myRef.child(k1.getAd()).updateChildren(bilgiler);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

