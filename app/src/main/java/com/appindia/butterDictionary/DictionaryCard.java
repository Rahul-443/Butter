package com.appindia.butterDictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import static com.appindia.butterDictionary.JsonToText.audiofiles;
import static com.appindia.butterDictionary.JsonToText.dialects;
import static com.appindia.butterDictionary.JsonToText.phoneticSpellings;

public class DictionaryCard extends AppCompatActivity {

    public MaterialTextView dictionaryTextView;
    public MaterialButton wordSpeakerButton;
    MaterialTextView dialectTextView;
    String word;
    Context context;
    private FirebaseAnalytics mFirebaseAnalytics;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    private InterstitialAd mInterstitialAd;

    boolean connected = false;
    static boolean workingInDictionaryCard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        if (height > width) {
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }

        setContentView(R.layout.activity_dictionary_card);

        getWindow().setGravity(Gravity.BOTTOM);

        RelativeLayout dcRelativeLayout = findViewById(R.id.dictionary_card_relative_layout);
        final ViewGroup.LayoutParams dcLayoutParams = dcRelativeLayout.getLayoutParams();

        if (height > width) {

            dcLayoutParams.height = height/3;
            dcLayoutParams.width = 4 * (width/5);

        } else {

            dcLayoutParams.height = height;
            dcLayoutParams.width = width/2;

        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = settings.edit();

        if (settings.getBoolean("MODE_NIGHT", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        dcRelativeLayout.setLayoutParams(dcLayoutParams);

        final RelativeLayout.LayoutParams dcTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dcTextParams.setMargins(30, 10, 30, 20);
        dcTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

        workingInDictionaryCard = true;
        context = this;

        dictionaryTextView = findViewById(R.id.dictionary_text);
        dialectTextView = findViewById(R.id.dialect_text_view);
        wordSpeakerButton = findViewById(R.id.word_speaker_button);

        //step 1 - getting text
        CharSequence text = null;
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        final ProgressBar progressBar = findViewById(R.id.progress_circular_bar);
        progressBar.setVisibility(View.VISIBLE);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type))
            text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        } else if (Intent.ACTION_PROCESS_TEXT.equals(action) && type != null) {
            if ("text/plain".equals(type)) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                text = getIntent().getCharSequenceExtra("android.intent.extra.PROCESS_TEXT");
            }
        }

        word = String.valueOf(text);

        if (connected) {
            //step-4 displaying meaning
            Runnable dictionaryCardRunnable = new Runnable() {
                @Override
                public void run() {

                    //if (!Oxford.networkError) {
//
//
                    //} else {
//
                    //    wordSpeakerButton.setVisibility(View.INVISIBLE);
                    //    dialectTextView.setVisibility(View.INVISIBLE);
                    //    dictionaryTextView.setVisibility(View.INVISIBLE);
//
                    //}

                    JsonToText.refreshAllLists();
                    Word.refreshAllLists();

                    GetDefinition getDefinition = new GetDefinition();
                    final Spannable text = getDefinition.define(word, null, context);

                    boolean isAudioDownloaded = false;

                    try {

                        if (Oxford.queryType.equals("entry") && !audiofiles.isEmpty()) {
                            isAudioDownloaded = new AudioFileDownloader().execute(audiofiles.get(0), "dictionaryCardAudioFile.mp3").get();
                        } else if (Oxford.queryType.equals("word") && !Word.audioFileList.isEmpty()) {
                            isAudioDownloaded = new AudioFileDownloader().execute(Word.audioFileList.get(0), "dictionaryCardAudioFile.mp3").get();
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final boolean finalIsAudioDownloaded = isAudioDownloaded;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.INVISIBLE);
                            dictionaryTextView.setText(text);

                            if (Oxford.queryType.equals("entry")) {

                                if (!dialects.isEmpty()) {

                                    String dialectText = dialects.get(0);
                                    dialectText = "(" + dialectText + ")";
                                    dialectTextView.setText(dialectText);

                                } else {
                                    dialectTextView.setVisibility(View.INVISIBLE);
                                }

                                if (!phoneticSpellings.isEmpty()) {
                                    wordSpeakerButton.setText(phoneticSpellings.get(0));
                                }

                                if (!audiofiles.isEmpty()) {

                                    if (finalIsAudioDownloaded) {

                                        wordSpeakerButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                MediaPlayer mediaPlayer = new MediaPlayer();

                                                try {
                                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                    mediaPlayer.setDataSource(String.valueOf(getFileStreamPath("dictionaryCardAudioFile.mp3")));
                                                    mediaPlayer.prepare();
                                                    mediaPlayer.start();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });

                                    }

                                } else {
                                    wordSpeakerButton.setVisibility(View.INVISIBLE);
                                    dialectTextView.setVisibility(View.INVISIBLE);
                                    dcTextParams.setMargins(30, 16, 30, 20);
                                    dcTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                    dictionaryTextView.setLayoutParams(dcTextParams);
                                }

                            } else if (Oxford.queryType.equals("word")) {

                                if (!Word.dialectsList.isEmpty()) {

                                    String dialectText = dialects.get(0);
                                    dialectText = "(" + dialectText + ")";
                                    dialectTextView.setText(dialectText);

                                } else {
                                    dialectTextView.setVisibility(View.INVISIBLE);
                                }

                                if (!Word.phoneticSpellingList.isEmpty()) {
                                    wordSpeakerButton.setText(phoneticSpellings.get(0));
                                }

                                if (!Word.audioFileList.isEmpty()) {

                                    if (finalIsAudioDownloaded) {

                                        wordSpeakerButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                MediaPlayer mediaPlayer = new MediaPlayer();

                                                try {
                                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                    mediaPlayer.setDataSource(Word.audioFileList.get(0));
                                                    mediaPlayer.prepare();
                                                    mediaPlayer.start();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });

                                    }

                                } else {
                                    wordSpeakerButton.setVisibility(View.INVISIBLE);
                                    dialectTextView.setVisibility(View.INVISIBLE);
                                    dcTextParams.setMargins(30, 16, 30, 20);
                                    dcTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                    dictionaryTextView.setLayoutParams(dcTextParams);
                                }

                            }

                        }
                    });

                }
            };
            Thread dictionaryCardThread = new Thread(dictionaryCardRunnable);
            dictionaryCardThread.start();

        } else {
            dictionaryTextView.setText(getResources().getString(R.string.inf));
        }

        //attaching ad.

        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.sample_admob_ad_id));
        mInterstitialAd.loadAd(getAdRequest());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    @Override
    protected void onPause() {
        super.onPause();

        int i = settings.getInt("dictionary_card_ad_count", 0);

        if (i == 2 && mInterstitialAd.isLoaded()) {

            editor.putInt("dictionary_card_ad_count", 0);
            editor.apply();

            mInterstitialAd.show();
        } else {

            editor.putInt("dictionary_card_ad_count", i + 1);
            editor.apply();

            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        finish();
    }

    public class AudioFileDownloader extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            int count;
            boolean isFileSaved = false;

            String audioFileDestination = strings[1];

            try {

                URL url = new URL(strings[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();

                int lengthOfFile = urlConnection.getContentLength(); //size of file (useful for showing progress in UI)

                FileOutputStream fileOutputStream = openFileOutput(audioFileDestination, Context.MODE_PRIVATE);

                //Download the file
                InputStream inputStream = new BufferedInputStream(url.openStream());

                byte[] data = new byte[1024];

                long total = 0;

                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    //publish the progress here e.g. -> total/length of file * 100 = download progress.
                    ((OutputStream) fileOutputStream).write(data, 0, count); //saving the file.
                }

                fileOutputStream.flush();
                ((OutputStream) fileOutputStream).close();
                inputStream.close();

                isFileSaved = true;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return isFileSaved;
        }

    }

}