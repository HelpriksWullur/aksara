package com.dreamforest.aksara;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

@SuppressWarnings("SpellCheckingInspection")
public class MainActivity extends AppCompatActivity {
    // memanggil class Random
    final Random rand = new Random();
    // array untuk aksara
    static final String[] letter = {"의", "이", "와", "워", "왜", "우", "위", "웨", "에", "외", "야", "요", "애", "오", "아", "으", "얘", "어"};
    // array untuk pengucapan
    static final String[] pronounce = {"ui", "i", "wa", "wo", "wae", "u",  "wi", "we", "e", "oe", "ya", "yo", "ae", "o", "a", "eu", "yae", "eo"};
    // array untuk status favorite
    static final boolean[] is_favorite = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

    TextView txt_card, txt_card_back, txt_pronounce, txt_unknown;
    Button btn_next, btn_play, btn_flip;
    LinearLayout ll_answer;
    CheckBox cb_is_favorite;

    AnimatorSet animFront, animBack;

    int result;
    boolean is_fliped = false;

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animBack = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flip_back);
        animFront = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flip_front);

        txt_card = findViewById(R.id.txt_card);
        txt_card_back = findViewById(R.id.txt_card_back);
        txt_pronounce = findViewById(R.id.txt_pronounce);
        txt_unknown = findViewById(R.id.txt_unknown);

        btn_next = findViewById(R.id.btn_next);
        btn_play = findViewById(R.id.btn_play);
        btn_flip = findViewById(R.id.btn_flip);

        result = rand.nextInt(letter.length);
        txt_card.setText(letter[result]);
        txt_pronounce.setText(pronounce[result]);

        cb_is_favorite = findViewById(R.id.cb_is_favorite);

        ll_answer = findViewById(R.id.ll_answer);

        // jika tombol acak diklik
        btn_next.setOnClickListener( view -> randAksara());

        // jika tanda tanya diklik
        txt_unknown.setOnClickListener( view -> showPronounce());

        // jika tombol suara diklik
        btn_play.setOnClickListener( view -> playSound(pronounce[result]));

        cb_is_favorite.setOnCheckedChangeListener((cb, is_true) -> {
            if (is_true) {
                cb.setBackgroundResource(R.drawable.ic_favorite_fill);
                if (!is_favorite[result]) {
                    is_favorite[result] = true;
                    Toast.makeText(MainActivity.this, "Ditambahkan ke favorite", Toast.LENGTH_SHORT).show();
                }
            } else {
                cb.setBackgroundResource(R.drawable.ic_favorite);
                if (is_favorite[result]) {
                    is_favorite[result] = false;
                    Toast.makeText(MainActivity.this, "Dihapus dari favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_flip.setOnClickListener( view -> flipCard());

        changeCameraDistance();
    }

    // fungsi untuk mengecek status favorite
    // jika status favorite true maka akan mengubah icon favorite
    private void checkFavorite() {
        if (is_favorite[result]){
            cb_is_favorite.setBackgroundResource(R.drawable.ic_favorite_fill);
        } else {
            cb_is_favorite.setBackgroundResource(R.drawable.ic_favorite);
        }
    }

    // fungsi untuk menampilkan pronounce
    private void showPronounce() {
        txt_unknown.setVisibility(View.GONE);
        ll_answer.setVisibility(View.VISIBLE);
    }

    // memutar suara berdasarkan card
    public void playSound(@NonNull String card) {
        switch (card) {
            case "a":
                setEnable(false);
                MediaPlayer a = MediaPlayer.create(getApplicationContext(), R.raw.a);
                a.start();
                delayPlaying(a);
                break;
            case "i":
                setEnable(false);
                MediaPlayer i = MediaPlayer.create(getApplicationContext(), R.raw.i);
                i.start();
                delayPlaying(i);
                break;
            case "u":
                setEnable(false);
                MediaPlayer u = MediaPlayer.create(getApplicationContext(), R.raw.u);
                u.start();
                delayPlaying(u);
                break;
            case "e":
                setEnable(false);
                MediaPlayer e = MediaPlayer.create(getApplicationContext(), R.raw.e);
                e.start();
                delayPlaying(e);
                break;
            case "o":
                setEnable(false);
                MediaPlayer o = MediaPlayer.create(getApplicationContext(), R.raw.o);
                o.start();
                delayPlaying(o);
                break;
        }
    }

    // fungsi untuk memanggil setEnable parameter true dalam durasi tertentu
    public void delayPlaying(@NonNull MediaPlayer sound) {
        new Handler().postDelayed(() -> setEnable(true), sound.getDuration());
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void randAksara() {
        normalizeFlip();
        result = rand.nextInt(letter.length);
        txt_card.setText(letter[result]);
        txt_pronounce.setText(pronounce[result]);

        txt_unknown.setVisibility(View.VISIBLE);
        ll_answer.setVisibility(View.GONE);

        checkFavorite();
    }

    // fungsi untuk mengubah status favorite
    @SuppressLint("UseCompatLoadingForColorStateLists")
    public void setEnable(@NonNull Boolean v) {
        // jika parameter bernilai true
        // maka akan mengubah status menjadi true dan mengubah backgound
        if (v) {
            btn_play.setEnabled(true);
            btn_play.setBackgroundTintList(getResources().getColorStateList(R.color.btn_enabled));
        } else { // jika tidak, maka akan mengubah status menjadi false dan mengubah background
            btn_play.setEnabled(false);
            btn_play.setBackgroundTintList(getResources().getColorStateList(R.color.btn_disabled));
        }
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        txt_card.setCameraDistance(scale);
        txt_card_back.setCameraDistance(scale);
    }

    private void flipCard() {
        if (is_fliped) {
            animBack.setTarget(txt_card_back);
            animFront.setTarget(txt_card);
            animBack.start();
            animFront.start();
            is_fliped = false;
        } else {
            animBack.setTarget(txt_card);
            animFront.setTarget(txt_card_back);
            animBack.start();
            animFront.start();
            is_fliped = true;
        }
    }

    private void normalizeFlip() {
        if (is_fliped) {
            animBack.setTarget(txt_card_back);
            animFront.setTarget(txt_card);
            animBack.start();
            animFront.start();
            is_fliped = false;
        }
    }
}