package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private ToggleButton theToggleButton;
    private MediaPlayer theMediaPlayer_Music; //OBIEKTY
    private MediaPlayer theMediaPlayer_Sound;

    private final static String LOG_TAG = "PDLog"; //klucz do LOGCAT'a

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theToggleButton = (ToggleButton) findViewById(R.id.toggleButton_MusicOnOff); // rzutujemy na naszego batona w XML'u
    }

    public void onClick_MusicOnOff(View v) { //metoda ta jest ustawiana w OnClicku toggleButton_MusicOnOff
        if (theToggleButton.isChecked()) { // metoda sprawdza czy wcisnieta; tak = gra ... itd
            playMyMusic(R.raw.test);
        } else {
            releaseMediaPlayer_Music(); // metoda tworzona nizej
        }
    }

    public void onClick_SoundA(View v) {
        playMySound(R.raw.sound_ringer_normal);
    }
    public void onClick_SoundB(View v) {
        playMySound(R.raw.sound_screen_off); // przy kliknieciu gra muzyka
    }
    public void onClick_SoundC(View v) {
        playMySound(R.raw.sound_view_clicked);
    }

    protected void releaseMediaPlayer_Music() {
        if (theMediaPlayer_Music != null) {
            theMediaPlayer_Music.release(); //jesli muzyka jest grana zostaje ona zwolniona i obiekt zostaje ustawiony na zero
            theMediaPlayer_Music = null;
        }
    }
    protected void releaseMediaPlayer_Sound() {
        if (theMediaPlayer_Sound != null) {
            theMediaPlayer_Sound.release();
            theMediaPlayer_Sound = null;
        }
    }
    @Override
    protected void onStop(){
        super.onStop();


        releaseMediaPlayer_Music(); //przy minimalozacji apki wylacza dziwieki
        releaseMediaPlayer_Sound();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        releaseMediaPlayer_Music(); //przy niszczzeniu apki wylacza dziwkei
        releaseMediaPlayer_Sound();
    }

    protected void playMyMusic(int anFileID) { //anFileID - lokalizacja do pliku
        releaseMediaPlayer_Music();
        try{ //TRY/CATCH - obluga wyjatkow
            theMediaPlayer_Music = MediaPlayer.create(this, anFileID);
            theMediaPlayer_Music.start();
        } catch (Exception e) { // Exception wyswietla tylko informacje
            Log.e(LOG_TAG, "error: " + e.getMessage(), e);
        }
    }

    // W TRY umieszczamy instrukcje ktore moga spowodowac jakis error, ale normalnie sie je wykonuje
    //

    //W bloku CATCH przechwytujemy wyjatek, tu: Exception, i ustalamy co sie dziac
    // (typ wyjatku, nasza nazwa wyjatku)

    protected void playMySound(int anFileID) {
        releaseMediaPlayer_Sound();
        try{
            theMediaPlayer_Sound = MediaPlayer.create(this,anFileID); //tworzymy nasz MP,  resid -
            theMediaPlayer_Sound.setOnPreparedListener(theOnPreparedListener);
            theMediaPlayer_Sound.setOnErrorListener(theOnErrorListener); // usatwainy nasze obiekty nasluchujace
            theMediaPlayer_Sound.setOnCompletionListener(theOnCompletionListener);
//wymaga wpisu w manifescie : <uses-permission android:name="android.permission.WAKE_LOCK"/>

            theMediaPlayer_Sound.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            //Partail - zapewnienie ze CPU dziala, ekran i klawa moga sie wylaczyc
            //wakemode daje dostep do zarzadzania energia gdy jest niski stan baterii
            //do jego oblugi potzeba flag z clasyy PowerManagmnt
            //getAC - zwraca kontekst aktualnego obiektu
            theMediaPlayer_Sound.start();
        }catch(Exception e){
            Log.e(LOG_TAG, "error: " + e.getMessage(), e);
        }
    }

    //tworzymy obiekt nasluchujacy kiedy ropocznie sie odtwarzanie dzwieku

    //mp - MP ktory bierze udzial w daje czynnosci
    private final MediaPlayer.OnPreparedListener theOnPreparedListener = new MediaPlayer.OnPreparedListener(){
        @Override
        public void onPrepared(MediaPlayer mp){
            Log.i(LOG_TAG, "onPrepared");
        }
    };

    //obiekt nasluchujacy bledy, dy cos bedzie wyskoczy komunikat
    private final MediaPlayer.OnErrorListener theOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) { // what - typ errora, extra - specjalny kod okreslajacy error z what
            Log.i(LOG_TAG, "onError");
            return false;
        }
    };

    //naslchuje kiedy dziwk sie skoczny
    private final MediaPlayer.OnCompletionListener theOnCompletionListener = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(LOG_TAG, "onCompletion");
        }
    };
}
