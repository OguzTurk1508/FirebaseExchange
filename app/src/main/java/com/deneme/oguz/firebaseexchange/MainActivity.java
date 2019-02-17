package com.deneme.oguz.firebaseexchange;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText textAd,textSifre;
    Button buttonKayit,buttonGiris;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;

    private Kullanici kullanici;

    String giris_pw;
    String giris_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textAd=findViewById(R.id.textAd);
        textSifre=findViewById(R.id.textSifre);

        buttonKayit=findViewById(R.id.buttonKayit);
        buttonKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonKayit_Click();
            }
        });

        buttonGiris=findViewById(R.id.buttonGiris);
        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonGiris_Click();
            }
        });

        kullanici=new Kullanici();

        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference().child("kullanicilar");

    }

    public void buttonKayit_Click(){
        kullanici.setAd(textAd.getText().toString());
        kullanici.setSifre(textSifre.getText().toString());

        ref.child(kullanici.getAd()).setValue(kullanici).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(MainActivity.this,"Kayit basarili!",Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(MainActivity.this,"Kayit basarisiz!",Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void buttonGiris_Click(){
        giris_id = textAd.getText().toString();
        giris_pw = textSifre.getText().toString();

        ref.child(giris_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici= dataSnapshot.getValue(Kullanici.class);

                if(giris_pw.equals(kullanici.getSifre())){

                    Toast.makeText(MainActivity.this,"Giris basarili!",Toast.LENGTH_LONG).show();

                    Intent start = new Intent(MainActivity.this,AnaClass.class);
                    start.putExtra("isim",kullanici.getAd());
                    start.putExtra("sifre",kullanici.getSifre());
                    startActivity(start);

                }else{

                    Toast.makeText(MainActivity.this,"Giris basarisiz! Kullanici adi veya Sifre hatali",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(MainActivity.this,"Kayit Bulunamadi!",Toast.LENGTH_LONG).show();

                    Intent start = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(start);

            }
        });

    }

}
