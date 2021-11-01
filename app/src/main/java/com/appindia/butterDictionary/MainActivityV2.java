package com.appindia.butterDictionary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static com.appindia.butterDictionary.JsonToText.audiofiles;
import static com.appindia.butterDictionary.JsonToText.dialects;
import static com.appindia.butterDictionary.JsonToText.etymologies;
import static com.appindia.butterDictionary.JsonToText.phoneticSpellings;

public class MainActivityV2 extends AppCompatActivity {

    int today;
    int lastDate;
    int lastWord = 0;
    private FirebaseAnalytics mFirebaseAnalytics;
    LottieAnimationView loadingAnimationViewV2;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Context context;
    boolean exitBool = false;
    String wordSeached = null;
    MaterialToolbar searchAndTabToolV2;
    DrawerLayout mainDrawerV2;
    ActionBarDrawerToggle mainDrawerToggle;
    NavigationView mainNavigationViewV2;
    MaterialTextView etymologyTextViewV2;
    MaterialCardView etymologyCardViewV2;
    MaterialTextView pageTextViewV2;
    MaterialButton speakPronunciationButton;
    String savedAudioFile = null;
    MaterialTextView dialectTextMain;
    RelativeLayout.LayoutParams pageViewLayoutParams;
    RelativeLayout.LayoutParams pronunciationButtonLayoutParams;
    SwipeRefreshLayout swipeToRefreshLayout;
    private InterstitialAd mInterstitialAd;
    private InterstitialAdLoadCallback interstitialAdLoadCallback;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = settings.edit();

        if (settings.getBoolean("MODE_NIGHT", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        context = this;

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        loadingAnimationViewV2 = findViewById(R.id.loading_animation_v2);

        if (!isNetworkAvailable()) {
            showAlertBar(getResources().getString(R.string.inf), getResources().getString(R.string.messageToSwitchOnNetConnection));
            loadingAnimationViewV2.pauseAnimation();
            loadingAnimationViewV2.setVisibility(View.INVISIBLE);
        }

        searchAndTabToolV2 = findViewById(R.id.search_and_tab_tool_v2);
        setSupportActionBar(searchAndTabToolV2);

        loadingAnimationViewV2.setVisibility(View.VISIBLE);
        loadingAnimationViewV2.playAnimation();

        etymologyCardViewV2 = findViewById(R.id.etymology_card_v2);
        etymologyTextViewV2 = findViewById(R.id.etymology_text_view_v2);
        pageTextViewV2 = findViewById(R.id.page_text_view_v2);
        speakPronunciationButton = findViewById(R.id.speak_pronunciation_button);
        dialectTextMain = findViewById(R.id.dialect_text_view_main);
        swipeToRefreshLayout = findViewById(R.id.swipe_to_refresh_layout);

        pageTextViewV2.setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getInt("TEXTSIZE", 20));

        pageViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pageViewLayoutParams.setMargins(12, 12, 12, 12);
        pageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        pageViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.speak_pronunciation_button);
        pageTextViewV2.setLayoutParams(pageViewLayoutParams);

        pronunciationButtonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pronunciationButtonLayoutParams.setMargins(20, 12, 0,12 );
        pronunciationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        pronunciationButtonLayoutParams.addRule(RelativeLayout.BELOW, R.id.etymology_card_v2);
        speakPronunciationButton.setLayoutParams(pronunciationButtonLayoutParams);

        savedAudioFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wotdAudio.mp3";

        //setting navigation drawer.

        mainDrawerV2 = findViewById(R.id.main_drawer_v2);

        mainDrawerToggle = new ActionBarDrawerToggle(this, mainDrawerV2, searchAndTabToolV2, R.string.navigation_drawer_enabled, R.string.navigation_drawer_disabled);
        mainDrawerV2.addDrawerListener(mainDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mainNavigationViewV2 = findViewById(R.id.main_nav_view_v2);
        mainNavigationViewV2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.settings) {
                    startActivity(new Intent(context, SettingsActivity.class));
                } else if (item.getItemId() == R.id.share) {
                    showShareButterDialog();
                } else if (item.getItemId() == R.id.rate_the_app) {
                    showRateButterDialog();
                }

                mainDrawerV2.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        //if user opens app the same day the same word is called.
        //the day is on the basis of -- if the user has connected to internet that day -- then it will be a new day.
        //if the user hasn't connected then he/she will see the same word as the day will not be increased to new day.

        if (checkNewDay()) {
            setNewDayWotd();  //setNewDayWotd();
        } else {
            setSamedayWotd();
        }

        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshPage();

            }
        });

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

    public void refreshPage() {

        boolean refreshForNewDay = false;
        String pageTextViewV2Text = pageTextViewV2.getText().toString();

        if (checkNewDay()) refreshForNewDay = true;

        if (pageTextViewV2Text.equals(getString(R.string.requestToCheckNetConnection)) || pageTextViewV2Text.equals(getString(R.string.requestToRefreshPage)) || refreshForNewDay || pageTextViewV2Text.equals(getString(R.string.nswf))) {

            if (wordSeached != null) {
                searchWord();
            } else {
                setNewDayWotd();
            }

        } else {
            swipeToRefreshLayout.setRefreshing(false);
        }

    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkNewDay()) setNewDayWotd();

        pageTextViewV2.setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getInt("TEXTSIZE", 20));

    }

    private void setSamedayWotd() {

        refreshViewsAlignment();

        Runnable setSameDayWotdRunnable = new Runnable() {
            @Override
            public void run() {

                final Spannable text = (Spannable) Html.fromHtml(settings.getString("WOTDV2", "Text not Found."));

                final String etymologies = settings.getString("WOTDETYMOLOGIES", "Etymologies not found.");
                final String audio = settings.getString("AUDIO", "Audio not found.");

                if (!audio.equals("Audio not found.")) {
                    setAudioPlayer(audio, "wotdAudioFile.mp3", true);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!etymologies.equals("no etymologies.")) {
                            etymologyTextViewV2.setText(etymologies);
                        }

                        if (!audio.equals("Audio not found.")) {
                            speakPronunciationButton.setText(settings.getString("PHONETICSPELLING", "Pronunciation not found."));
                            dialectTextMain.setText(settings.getString("DIALECT", "(Dialect not found)"));
                        }

                        if (!text.equals(new SpannableString("no wotd."))) {
                            pageTextViewV2.setText(text);
                        } else {
                            pageTextViewV2.setText(new SpannableString(getString(R.string.requestToRefreshPage)));
                        }

                        if (etymologies.equals("no etymologies.") && audio.equals("Audio not found.")) {

                            etymologyCardViewV2.setVisibility(View.INVISIBLE);
                            speakPronunciationButton.setVisibility(View.INVISIBLE);
                            dialectTextMain.setVisibility(View.INVISIBLE);

                            pageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            pageViewLayoutParams.setMargins(12, 20, 12, 12);
                            pageTextViewV2.setLayoutParams(pageViewLayoutParams);

                        } else if (audio.equals("Audio not found.") || etymologies.equals("no etymologies.")) {

                            if (audio.equals("Audio not found.")) {

                                dialectTextMain.setVisibility(View.INVISIBLE );
                                speakPronunciationButton.setVisibility(View.INVISIBLE);

                                pageViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.etymology_card_v2);
                                pageTextViewV2.setLayoutParams(pageViewLayoutParams);

                            }

                            if (etymologies.equals("no etymologies.")) {

                                etymologyCardViewV2.setVisibility(View.INVISIBLE);

                                pronunciationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                pronunciationButtonLayoutParams.setMargins(20, 20, 0, 12);
                                speakPronunciationButton.setLayoutParams(pronunciationButtonLayoutParams);

                            }

                        }

                        loadingAnimationViewV2.pauseAnimation();
                        loadingAnimationViewV2.setVisibility(View.INVISIBLE);

                    }
                });

            }
        };

        Thread setSameDayWotdThread = new Thread(setSameDayWotdRunnable);
        setSameDayWotdThread.start();

    }

    private void setNewDayWotd() {

        if (isNetworkAvailable()) {

            final int[] wordInt = new int[1];

            JsonToText.refreshAllLists();
            Word.refreshAllLists();

            refreshViewsAlignment();

            if (checkNewDay()) {
                wordInt[0] = getNewWotdInt();
            } else {
                wordInt[0] = settings.getInt("last_word", 1);
            }


            Runnable setNewWotdRunnable = new Runnable() {
                @Override
                public void run() {

                    GetDefinition getDefinition = new GetDefinition();
                    Spannable text = getDefinition.define(getWotdFromJson(wordInt[0]), getString(R.string.wtd), context);

                    while (text.toString().equals(getString(R.string.nswf))) {
                        wordInt[0] = getNewWotdInt();
                        text = getDefinition.define(getWotdFromJson(wordInt[0]), getString(R.string.wtd), context);
                    }

                    getNewWotd(text);

                }

            };

            Thread setNewWotdThread = new Thread(setNewWotdRunnable);
            setNewWotdThread.start();

        } else {

            showAlertBar(getResources().getString(R.string.inf), getResources().getString(R.string.messageToSwitchOnNetConnection));
            loadingAnimationViewV2.pauseAnimation();
            loadingAnimationViewV2.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mainMenuInflater = getMenuInflater();
        mainMenuInflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        //setting search view.

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.saw));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {

                wordSeached = query;

                searchView.clearFocus();

                JsonToText.refreshAllLists();
                Word.refreshAllLists();

                refreshViewsAlignment();

                if (i == 0 && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                i++;

                if (i == 3) {
                    i = 0;
                }

                if (isNetworkAvailable()) {

                    loadingAnimationViewV2.playAnimation();
                    loadingAnimationViewV2.setVisibility(View.VISIBLE);

                    searchWord();

                } else {

                    showAlertBar(getResources().getString(R.string.inf), getResources().getString(R.string.messageToSwitchOnNetConnection));
                    loadingAnimationViewV2.pauseAnimation();
                    loadingAnimationViewV2.setVisibility(View.INVISIBLE);

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem refreshItem = menu.findItem(R.id.refresh);
        refreshItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                swipeToRefreshLayout.setRefreshing(true);
                refreshPage();

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mainDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mainDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mainDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        pageTextViewV2.setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getInt("TEXTSIZE", 20));
        wordSeached = null;

        if (exitBool) {

            super.onBackPressed();
            return;

        }

        if (mainDrawerV2.isDrawerOpen(GravityCompat.START)) {

            mainDrawerV2.closeDrawer(GravityCompat.START);

        } else {

            if (checkNewDay()) {
                setNewDayWotd();
            } else {
                setSamedayWotd();
            }

        }

        exitBool = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exitBool = false;
            }
        }, 1500);

    }

    public void refreshViewsAlignment() {

        etymologyCardViewV2.setVisibility(View.VISIBLE);
        speakPronunciationButton.setVisibility(View.VISIBLE);
        dialectTextMain.setVisibility(View.VISIBLE);
        pageTextViewV2.setVisibility(View.VISIBLE);

        pageViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.speak_pronunciation_button);
        pageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        pageViewLayoutParams.setMargins(12, 12, 12, 12);
        pageTextViewV2.setLayoutParams(pageViewLayoutParams);

        pronunciationButtonLayoutParams.addRule(RelativeLayout.BELOW, R.id.etymology_card_v2);
        pronunciationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        pronunciationButtonLayoutParams.setMargins(20, 12, 0, 12);
        speakPronunciationButton.setLayoutParams(pronunciationButtonLayoutParams);

    }

    public void setViewsAlignment(String setType) {

        if (setType.equals("entry")) {

            if (audiofiles.isEmpty() && etymologies.isEmpty()) {

                etymologyCardViewV2.setVisibility(View.INVISIBLE);
                speakPronunciationButton.setVisibility(View.INVISIBLE);
                dialectTextMain.setVisibility(View.INVISIBLE);

                pageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                pageViewLayoutParams.setMargins(12, 20, 12, 12);
                pageTextViewV2.setLayoutParams(pageViewLayoutParams);

            } else if (audiofiles.isEmpty() || etymologies.isEmpty()) {

                if (audiofiles.isEmpty()) {

                    speakPronunciationButton.setVisibility(View.INVISIBLE);
                    dialectTextMain.setVisibility(View.INVISIBLE);

                    pageViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.etymology_card_v2);
                    pageTextViewV2.setLayoutParams(pageViewLayoutParams);

                }

                if (etymologies.isEmpty()) {

                    etymologyCardViewV2.setVisibility(View.INVISIBLE);

                    pronunciationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    pronunciationButtonLayoutParams.setMargins(20, 20, 0, 12);
                    speakPronunciationButton.setLayoutParams(pronunciationButtonLayoutParams);

                }

            }

        } else if (setType.equals("word")) {

            if (Word.etymologiesList.isEmpty() && Word.audioFileList.isEmpty()) {

                etymologyCardViewV2.setVisibility(View.INVISIBLE);
                speakPronunciationButton.setVisibility(View.INVISIBLE);
                dialectTextMain.setVisibility(View.INVISIBLE);

                pageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                pageTextViewV2.setLayoutParams(pageViewLayoutParams);

            } else if (Word.audioFileList.isEmpty() || Word.etymologiesList.isEmpty()) {

                if (Word.audioFileList.isEmpty()) {

                    speakPronunciationButton.setVisibility(View.INVISIBLE);
                    dialectTextMain.setVisibility(View.INVISIBLE);

                    pageViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.etymology_card_v2);
                    pageViewLayoutParams.setMargins(12, 20, 12, 12);
                    pageTextViewV2.setLayoutParams(pageViewLayoutParams);

                }

                if (Word.etymologiesList.isEmpty()) {

                    etymologyCardViewV2.setVisibility(View.INVISIBLE);

                    pronunciationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    pronunciationButtonLayoutParams.setMargins(20, 20, 0, 12);
                    speakPronunciationButton.setLayoutParams(pronunciationButtonLayoutParams);

                }

            }

        }

    }

    public void searchWord() {

        Runnable setQueryTextRunnable = new Runnable() {
            @Override
            public void run() {

                GetDefinition getDefinition = new GetDefinition();
                final Spannable text = getDefinition.define(wordSeached, null, context);

                if (Oxford.queryType.equals("entry") && !audiofiles.isEmpty()) {
                    setAudioPlayer(audiofiles.get(0), "audioFile.mp3", false);
                } else if (Oxford.queryType.equals("word") && !Word.audioFileList.isEmpty()) {
                    setAudioPlayer(Word.audioFileList.get(0), "audioFile.mp3", false);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (Oxford.queryType.equals("entry")) {

                            if (!etymologies.isEmpty()) {

                                StringBuilder etymologiesText = new StringBuilder();
                                for (int i = 0; i < etymologies.size(); i++) {
                                    etymologiesText.append("\n").append(i + 1).append(". ").append(etymologies.get(i)).append("\n");
                                }
                                etymologyTextViewV2.setText(etymologiesText);

                            }

                            if (!phoneticSpellings.isEmpty()) {
                                speakPronunciationButton.setText(phoneticSpellings.get(0));
                            }

                            if (!dialects.isEmpty()) {
                                String dialectText = dialects.get(0);
                                dialectText = "(" + dialectText + ")";
                                dialectTextMain.setText(dialectText);
                            }

                            setViewsAlignment("entry");

                        } else if (Oxford.queryType.equals("word")) {

                            if (!Word.etymologiesList.isEmpty()) {

                                StringBuilder etymologiesText = new StringBuilder();
                                for (int i = 0; i < Word.etymologiesList.size(); i++) {
                                    etymologiesText.append("\n").append(i + 1).append(". ").append(Word.etymologiesList.get(i)).append("\n");
                                }
                                etymologyTextViewV2.setText(etymologiesText);

                            }

                            if (!Word.phoneticSpellingList.isEmpty()) {
                                speakPronunciationButton.setText(Word.phoneticSpellingList.get(0));
                            }

                            if (!Word.dialectsList.isEmpty()) {
                                String dialectText = Word.dialectsList.get(0);
                                dialectText = "(" + dialectText + ")";
                                dialectTextMain.setText(dialectText);
                            }

                            setViewsAlignment("word");

                        }

                        if (text != null) {
                            pageTextViewV2.setText(text);
                        }

                        loadingAnimationViewV2.pauseAnimation();
                        loadingAnimationViewV2.setVisibility(View.INVISIBLE);
                        swipeToRefreshLayout.setRefreshing(false);

                    }
                });

            }
        };

        Thread setQueryTextThread = new Thread(setQueryTextRunnable);
        setQueryTextThread.start();

    }

    public void getNewWotd(Spannable text) {

        if (Oxford.queryType.equals("entry") && !audiofiles.isEmpty()) {
            setAudioPlayer(audiofiles.get(0), "wotdAudioFile.mp3", false);
        } else if (Oxford.queryType.equals("word") && !Word.audioFileList.isEmpty()) {
            setAudioPlayer(Word.audioFileList.get(0), "wotdAudioFile.mp3", false);
        }

        final Spannable finalText = text;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!finalText.equals(new SpannableString(getString(R.string.requestToCheckNetConnection)))) {

                    editor.putString("WOTDV2", Html.toHtml(finalText));

                } else {

                    editor.putString("WOTDV2", "no wotd.");

                }
                editor.apply();

                StringBuilder wotdEtymology = new StringBuilder();

                if (Oxford.queryType.equals("entry")) {

                    if (!etymologies.isEmpty()) {

                        for (int i = 0; i < etymologies.size(); i++) {
                            wotdEtymology.append("\n").append(i + 1).append(". ").append(etymologies.get(i)).append("\n");
                        }

                        editor.putString("WOTDETYMOLOGIES", wotdEtymology.toString());
                        editor.apply();

                        etymologyTextViewV2.setText(wotdEtymology);

                    } else {
                        editor.putString("WOTDETYMOLOGIES", "no etymologies.");
                        editor.apply();
                    }

                    if (!phoneticSpellings.isEmpty()) {
                        speakPronunciationButton.setText(phoneticSpellings.get(0));
                        editor.putString("PHONETICSPELLING", phoneticSpellings.get(0));
                    } else {
                        editor.putString("PHONETICSPELLING", "Phonetic Spelling not found.");
                    }
                    editor.apply();

                    if (!dialects.isEmpty()) {
                        String dialectText = dialects.get(0);
                        dialectText = "(" + dialectText + ")";
                        dialectTextMain.setText(dialectText);
                        editor.putString("DIALECT", dialectText);
                    } else {
                        editor.putString("DIALECT", "Dialect not found.");
                    }
                    editor.apply();

                    if (!audiofiles.isEmpty()) {
                        editor.putString("AUDIO", audiofiles.get(0));
                    } else {
                        editor.putString("AUDIO", "Audio not found.");
                    }
                    editor.apply();

                    setViewsAlignment("entry");

                } else if (Oxford.queryType.equals("word")) {

                    if (!Word.etymologiesList.isEmpty()) {

                        for (int i = 0; i < Word.etymologiesList.size(); i++) {
                            wotdEtymology.append("\n").append(i + 1).append(". ").append(Word.etymologiesList.get(i)).append("\n");
                        }

                        editor.putString("WOTDETYMOLOGIES", wotdEtymology.toString());
                        editor.apply();

                        etymologyTextViewV2.setText(wotdEtymology);

                    } else {
                        editor.putString("WOTDETYMOLOGIES", "no etymologies.");
                        editor.apply();
                    }

                    if (!Word.phoneticSpellingList.isEmpty()) {
                        speakPronunciationButton.setText(Word.phoneticSpellingList.get(0));
                        editor.putString("PHONETICSPELLING", Word.phoneticSpellingList.get(0));
                    } else {
                        editor.putString("PHONETICSPELLING", "Phonetic Spelling not found.");
                    }
                    editor.apply();

                    if (!Word.dialectsList.isEmpty()) {
                        String dialectText = Word.dialectsList.get(0);
                        dialectText = "(" + dialectText + ")";
                        dialectTextMain.setText(dialectText);
                        editor.putString("DIALECT", dialectText);
                    } else {
                        editor.putString("DIALECT", "Dialect not found.");
                    }
                    editor.apply();

                    if (!Word.audioFileList.isEmpty()) {
                        editor.putString("AUDIO", Word.audioFileList.get(0));
                    } else {
                        editor.putString("AUDIO", "Audio not found.");
                    }
                    editor.apply();

                    setViewsAlignment("word");

                }

                pageTextViewV2.setText(finalText);

                loadingAnimationViewV2.pauseAnimation();
                loadingAnimationViewV2.setVisibility(View.INVISIBLE);
                swipeToRefreshLayout.setRefreshing(false);

            }
        });

    }

    public void showAlertBar(String notification, String message) {

        new MaterialAlertDialogBuilder(this)
                .setTitle(notification)
                .setMessage(message)
                .create()
                .show();

    }

    public int getNewWotdInt() {

        final int newWord = lastWord + 1;

        editor.putInt("last_time_started", today);
        if (lastDate != 3000) {
            editor.putInt("last_word", newWord);
        } else {
            editor.putInt("last_word", -1);
        }
        editor.apply();

        return newWord;
    }

    public String getWotdFromJson(int index) {

        String wordFromJson = null;

        try {
            InputStream wotdJsonIS = getAssets().open(getResources().getString(R.string.wotdFileName));

            int size = wotdJsonIS.available();
            byte[] buffer = new byte[size];
            wotdJsonIS.read(buffer);
            wotdJsonIS.close();

            String wotdJsonText = new String(buffer, "UTF-8");
            JSONArray wotdJsonArray = new JSONArray(wotdJsonText);

            JSONObject sn = wotdJsonArray.getJSONObject(index);
            if (!sn.isNull("serial_number")) wordFromJson = sn.getString("word");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return wordFromJson;
    }

    public boolean checkNewDay() {

        //new day is the user opening app for the first time in a new day.

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lastDate = settings.getInt("last_time_started", -1);
        lastWord = settings.getInt("last_word", -1);
        Calendar calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_YEAR);

        boolean newDay = false;

        if (today != lastDate) newDay = true;

        return newDay;
    }

    public void showRateButterDialog() {

        final MaterialAlertDialogBuilder rateButterDialog = new MaterialAlertDialogBuilder(this);
        rateButterDialog.setTitle(getResources().getString(R.string.loveButter) + " " + getEmojiByUniCode("two Hearts"));
        rateButterDialog.setMessage(getResources().getString(R.string.requestMessageToRate));
        rateButterDialog.setPositiveButton(getResources().getString(R.string.ratingRequestAccepted), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
                Intent goToMarketIntent = new Intent(Intent.ACTION_VIEW, uri);

                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.

                int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;

                if (Build.VERSION.SDK_INT >= 21) {
                    flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
                } else {
                    flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
                }

                goToMarketIntent.addFlags(flags);

                try {
                    startActivity(goToMarketIntent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                }

            }
        });
        rateButterDialog.setNegativeButton(getResources().getString(R.string.ratingRequestDeniedForNow), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                rateButterDialog.setCancelable(true);

            }
        });

        rateButterDialog.show();

    }

    public void showShareButterDialog() {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String shareMessage = "\n" + getResources().getString(R.string.messageShare);
            shareMessage = shareMessage + " " + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.ca)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getEmojiByUniCode(String string) {
//TODO is it not working on android 4.4
        String emoji = "";

        switch (string) {

            case "two Hearts":
                emoji = new String(Character.toChars(0x1F495));

        }

        return emoji;
    }

    public void showFriendlyAdviceDialog(int i) {

        switch (i) {

            case 0:
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Friendly Advice")
                        .setMessage("You can learn more by writing difficult words and their definitions in a notebook and memorising them.")
                        .create()
                        .show();

            case 1:
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Covid Advice")
                        .setMessage("Wash your hands and wear your masks regularly. In such uncertain times we are all together.")
                        .create()
                        .show();

        }

    }

    public void setAudioPlayer(final String audioFileURL, final String audioFileDestination, final boolean playOfflineFile)  {

        boolean isAudioSaved = false;

        if (!playOfflineFile) {
            try {
                isAudioSaved = new AudioFileDownloader().execute(audioFileURL, audioFileDestination).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final boolean finalIsAudioSaved = isAudioSaved;
        speakPronunciationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaPlayer mediaPlayer = new MediaPlayer();

                if (finalIsAudioSaved && audioFileDestination.equals("audioFile.mp3")) {

                    try {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(String.valueOf(getFileStreamPath(audioFileDestination)));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (finalIsAudioSaved && audioFileDestination.equals("wotdAudioFile.mp3")) {

                    try {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(String.valueOf(getFileStreamPath(audioFileDestination)));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (audioFileDestination.equals("wotdAudioFile.mp3")) {

                    try {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(String.valueOf(getFileStreamPath(audioFileDestination)));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    public boolean isNetworkAvailable() {

        boolean connected;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
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