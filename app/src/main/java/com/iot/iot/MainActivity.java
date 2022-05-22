package com.iot.iot;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String buttonCond = "switch1";
    Animation animationSuccess;

    private ImageButton buttonLampu_;
    private ImageView imageViewLampu_;
    private ProgressBar progressBarLampu;
    private TextView textLampu_;
    int count;
    boolean clicked = false;

    boolean state = false;


    private Calendar saatIni;
    private TimeZone zonaWaktu;
    private int stampTahun, stampBulan, stampTanggal, stampJam, stampMenit, stampDetik;
    private SimpleDateFormat formatWaktu, formatTanggal;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference root_;

    ProgressDialog TempDialog;
    CountDownTimer mCountDownTimer;
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        cekDataAda();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DAOkondisiLampu daoLampu = new DAOkondisiLampu();

        buttonLampu_ = (ImageButton) findViewById(R.id.buttonLampu);
        textLampu_ = (TextView) findViewById(R.id.textLampu);
        imageViewLampu_ = (ImageView) findViewById(R.id.imageViewLampu);
        progressBarLampu = (ProgressBar) findViewById(R.id.progressBarLampu);




        animationSuccess = AnimationUtils.loadAnimation(this, R.anim.animation_succes);
        animationSuccess.setInterpolator(new AccelerateDecelerateInterpolator());


        saatIni = new GregorianCalendar();
        zonaWaktu = saatIni.getTimeZone();




        buttonLampu_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateAwal();

                stampAmbil();
                kondisiLampu kondisi = new kondisiLampu(!buttonLampu_.isSelected(), stampTahun, stampBulan, stampTanggal, stampJam, stampMenit, stampDetik);
                daoLampu.add(kondisi).addOnSuccessListener(suc -> {
                    float height = buttonLampu_.getHeight();
                    float width = buttonLampu_.getWidth();

                    ubahState();
                    state = buttonLampu_.isSelected();
                    stateLampu(state, height, width);

                    Toast.makeText(MainActivity.this, "Berhasil Diubah", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(err -> {
                    Toast.makeText(MainActivity.this, "Tidak Berhasil Diubah", Toast.LENGTH_SHORT).show();
                });

            }
        });

        buttonLampu_.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogFragment suhuFragment = new fragmentSuhu();
                suhuFragment.show(getSupportFragmentManager(), "My  Fragment");
                return true;
            }
        });
    }

    public void stateLampu(boolean state, float height, float width){
        Bitmap bmapOn = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_on);
        Bitmap bmapOff = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_off);

        if (state) {
            textLampu_.setText("Hidup");
            textLampu_.setTextColor(Color.BLACK);
            buttonLampu_.setImageBitmap(resizeImage(bmapOn, height, width));
        } else {
            textLampu_.setText("Mati");
            textLampu_.setTextColor(Color.WHITE);
            buttonLampu_.setImageBitmap(resizeImage(bmapOff, height, width));
        }
    }

    public void stateAwal(){
        progressBarLampu.setVisibility(View.VISIBLE);
        textLampu_.setVisibility(View.GONE);
        buttonLampu_.setImageResource(0);
        buttonLampu_.setEnabled(!buttonLampu_.isEnabled());
    }

    public void ubahState(){
        buttonLampu_.setEnabled(!buttonLampu_.isEnabled());
        buttonLampu_.setSelected(!buttonLampu_.isSelected());
        clicked = buttonLampu_.isSelected();
        progressBarLampu.setVisibility(View.INVISIBLE);
        textLampu_.setVisibility(View.VISIBLE);
        imageViewLampu_.startAnimation(animationSuccess);
    }


    public void content(boolean ya) {
        if (ya) {
            count++;
            textLampu_.setText("ref " + count);
            refresh(1000);
        }

    }

    public void refresh(int miliseconds) {
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content(clicked);
            }
        };
        handler.postDelayed(runnable, miliseconds);
    }

    public void stampAmbil() {
        Date trialTime = new Date();
        saatIni.setTime(trialTime);
        stampTahun = saatIni.get(Calendar.YEAR);
        stampBulan = saatIni.get(Calendar.MONTH);
        stampTanggal = saatIni.get(Calendar.DATE);

        stampJam = saatIni.get(Calendar.HOUR_OF_DAY);
        stampMenit = saatIni.get(Calendar.MINUTE);
        stampDetik = saatIni.get(Calendar.SECOND);
    }


    public static Bitmap resizeImage(Bitmap realImage, float width_, float height_) {
        int width = Math.round((float) (width_/2));
        int height = Math.round((float) (height_/2));

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, true);
        return newBitmap;
    }

    public int[] Hitung(int tahunAwal, int tahunAkhir, int bulanAwal, int bulanAkhir, int tanggalAwal, int tanggalAkhir){
        GregorianCalendar Now_ = new GregorianCalendar();
        Now_.set(Calendar.YEAR, tahunAkhir);
        Now_.add(Calendar.YEAR, tahunAwal*-1);
        Now_.add(Calendar.MONTH, (bulanAwal*-1));
        Now_.add(Calendar.DATE, (tanggalAwal*-1));
        int tahunSekarang = 1;
        int bulanSekarang = 1;
        int tanggalSekarang = 1;

        int[] waktu_ = {tahunSekarang, bulanSekarang, tanggalSekarang};
        return waktu_;
    };

    private int[] ambilData(){
        root_ = FirebaseDatabase.getInstance().getReference().child("kondisiLampu");
        int[] aa = {1};
        return aa;
    };

    public Boolean cekDataAda(){
//        CountDownLatch done = new CountDownLatch(1);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        final Boolean[] status = new Boolean[1];

        rootRef.child("kondisiLampu");
        rootRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    status[0] = true;
                    String status = (String) snapshot.child("statusLampu").getValue();
                    Boolean status_ = Boolean.valueOf(status);
                    System.out.println("asdasdasdasdasdasdas");
                    System.out.println(status);
                }
                else{
                    status[0] = false;
                    System.out.println("false12add");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        done.await();
        return status[0];
    }
}
