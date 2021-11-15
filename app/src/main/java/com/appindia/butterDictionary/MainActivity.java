package com.appindia.butterDictionary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static com.appindia.butterDictionary.JsonToText.etymologies;

public class MainActivity extends AppCompatActivity {

    int today;
    int lastDate;
    int lastWord = 0;
    private FirebaseAnalytics mFirebaseAnalytics;
    LottieAnimationView loadingAnimationView;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Context context;
    private AdView adView;
    boolean connected = false;
    boolean workingInWotd = false;
    boolean setWhichWord = false;
    boolean exitBool = false;
    MaterialToolbar searchAndTabTool;
    ViewPager2 meaningsViewPager;
    TabLayout wordIdTablayout;
    SectionsStateAdapter wotdSectionsStateAdapter;
    SectionsStateAdapter searchSectionsStateAdapter;
    DrawerLayout mainDrawer;
    ActionBarDrawerToggle mainDrawerToggle;
    NavigationView mainNavigationView;
    ArrayList<String> tabNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        context = this;

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        loadingAnimationView = findViewById(R.id.loading_animation);

        loadingAnimationView.setVisibility(View.VISIBLE);
        loadingAnimationView.playAnimation();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) {
            showAlertBar("Internet not found", "Please switch on your Internet. This application cannot work without an Internet connection.");
            loadingAnimationView.pauseAnimation();
            loadingAnimationView.setVisibility(View.INVISIBLE);
        }

        tabNames = new ArrayList<>();

        searchSectionsStateAdapter = new SectionsStateAdapter(this);
        wotdSectionsStateAdapter = new SectionsStateAdapter(this);

        meaningsViewPager = findViewById(R.id.meanings_view_pager);
        meaningsViewPager.setAdapter(wotdSectionsStateAdapter);

        wordIdTablayout = findViewById(R.id.word_id_tab_layout);

        searchAndTabTool = findViewById(R.id.search_and_tab_tool);
        setSupportActionBar(searchAndTabTool);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(wordIdTablayout, meaningsViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            }
        });
        tabLayoutMediator.attach();

        //////////////

        //setting navigation drawer.

        mainDrawer = findViewById(R.id.main_drawer);

        mainDrawerToggle = new ActionBarDrawerToggle(this, mainDrawer, searchAndTabTool, R.string.navigation_drawer_enabled, R.string.navigation_drawer_disabled);
        mainDrawer.addDrawerListener(mainDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mainNavigationView = findViewById(R.id.main_nav_view);
        mainNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.Home) {

                } else if (item.getItemId() == R.id.settings) {
                    startActivity(new Intent(context, SettingsActivity.class));
                } else if (item.getItemId() == R.id.share) {
                    showShareButterDialog();
                } else if (item.getItemId() == R.id.rate_the_app) {
                    showRateButterDialog();
                }

                mainDrawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        ////////////

        /////////////////

        //if user opens app the same day the same word is called.
        //the day is on the basis of -- if the user has connected to internet that day -- then it will be a new day.
        //if the user hasn't connected then he/she will see the same word as the day will not be increased to new day.

        workingInWotd = true;
        setWhichWord = true;

        if (!checkNewDay()) {

            Spannable text = getWotdText();

            if (text != null) {
                wotdSectionsStateAdapter.addPage(text);
                wotdSectionsStateAdapter.notifyDataSetChanged();
            }

            if (wordIdTablayout.getTabAt(0) != null) {
                wordIdTablayout.getTabAt(0).setText("Word of the day");
                tabNames.add("Word of the day");
            }

            loadingAnimationView.pauseAnimation();
            loadingAnimationView.setVisibility(View.INVISIBLE);

        }

        //////////////////////

        ////////////////

        //attaching ad.

        AudienceNetworkAds.initialize(this);

        adView = new AdView(this, "IMG_16_9_APP_INSTALL#4016175595081057_4028848430480440", AdSize.BANNER_HEIGHT_50);

        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(adView);

        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("FACEBOOK_AD_ERROR", "onError: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());

        ////////////////

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkNewDay()) {

            workingInWotd = true;
            setWhichWord = true;

            //new day is the user opening app for the first time in a new day.

            if (connected) {
                final int newWord = lastWord + 1;

                editor = settings.edit();
                editor.putInt("last_time_started", today);
                if (lastDate != 3000) {
                    editor.putInt("last_word", newWord);
                } else {
                    editor.putInt("last_word", -1);
                }
                editor.apply();

                GetDefinition getDefinition = new GetDefinition();
                Spannable text = getDefinition.define(getWotdFromJson(newWord), "Word of the day");
                wotdSectionsStateAdapter.addPage(text);
                wotdSectionsStateAdapter.notifyDataSetChanged();

                if (wordIdTablayout.getTabAt(0) != null) {
                    wordIdTablayout.getTabAt(0).setText("Word of the day");
                    tabNames.add("Word of the day");
                }

                loadingAnimationView.pauseAnimation();
                loadingAnimationView.setVisibility(View.INVISIBLE);

            } else {
                showAlertBar("Internet not found", "Please switch on your Internet. This application cannot work without an Internet connection.");
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mainMenuInflater = getMenuInflater();
        mainMenuInflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        /////////////////

        //setting search view.

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search a word");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {

                searchView.clearFocus();

                workingInWotd = false;
                setWhichWord = false;

                if (connected) {

                    loadingAnimationView.playAnimation();
                    loadingAnimationView.setVisibility(View.VISIBLE);

                    searchSectionsStateAdapter.removeAllPages();
                    searchSectionsStateAdapter.notifyDataSetChanged();

                    wotdSectionsStateAdapter.removeAllPages();
                    wotdSectionsStateAdapter.notifyDataSetChanged();

                    tabNames.clear();

                    meaningsViewPager.setAdapter(searchSectionsStateAdapter);

                    GetDefinition getDefinition = new GetDefinition();
                    Spannable text = getDefinition.define(query, null);
                    searchSectionsStateAdapter.addPage(text);
                    searchSectionsStateAdapter.notifyDataSetChanged();

                    if (wordIdTablayout.getTabAt(0) != null) {
                        wordIdTablayout.getTabAt(0).setText(query);
                        tabNames.add(query);
                    }

                    loadingAnimationView.pauseAnimation();
                    loadingAnimationView.setVisibility(View.INVISIBLE);

                } else {
                    showAlertBar("Internet not found", "Please switch on your Internet. This application cannot work without an Internet connection.");
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ///////////////

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

        Log.d("ONBACKPRESSED", "onBackPressed: pressed");

        if (exitBool) {

            wotdSectionsStateAdapter.removeAllPages();
            wotdSectionsStateAdapter.notifyDataSetChanged();

            searchSectionsStateAdapter.removeAllPages();
            searchSectionsStateAdapter.notifyDataSetChanged();

            super.onBackPressed();
            return;

        }

        if (mainDrawer.isDrawerOpen(GravityCompat.START)) {

            mainDrawer.closeDrawer(GravityCompat.START);

        } else {

            if (!workingInWotd) {

                searchSectionsStateAdapter.removeLastPage();
                searchSectionsStateAdapter.notifyDataSetChanged();

                if (searchSectionsStateAdapter.isEmpty()) {

                    tabNames.remove(tabNames.size() - 1);

                    for (int i = 0; i < tabNames.size(); i++) {

                        if (wordIdTablayout.getTabAt(i) != null) {
                            wordIdTablayout.getTabAt(i).setText(tabNames.get(i));
                        }

                    }

                } else {

                    meaningsViewPager.setAdapter(wotdSectionsStateAdapter);

                    Spannable text = getWotdText();

                    if (text != null) {

                        wotdSectionsStateAdapter.addPage(text);
                        wotdSectionsStateAdapter.notifyDataSetChanged();

                        workingInWotd = true;

                        if (wordIdTablayout.getTabAt(0) != null) {
                            wordIdTablayout.getTabAt(0).setText("Word of the day");
                            tabNames.add("word of the day");
                        }

                    }

                }

            } else {

                wotdSectionsStateAdapter.removeLastPage();
                wotdSectionsStateAdapter.notifyDataSetChanged();

                if (wotdSectionsStateAdapter.isEmpty()) {

                    tabNames.remove(tabNames.size() - 1);

                    for (int i = 0; i < wordIdTablayout.getTabCount(); i++) {

                        if (wordIdTablayout.getTabAt(i) != null) {
                            wordIdTablayout.getTabAt(i).setText(tabNames.get(i));
                        }

                    }

                } else {

                    super.onBackPressed();

                }

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

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    public void showAlertBar(String notification, String message) {

        new MaterialAlertDialogBuilder(this)
                .setTitle(notification)
                .setMessage(message)
                .create()
                .show();

    }

    public String getWotdFromJson(int index) {

        String wordFromJson = null;

        try {
            InputStream wotdJsonIS = getAssets().open("src/English/assets/3000_most_general_english_words.json");

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

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        lastDate = settings.getInt("last_time_started", -1);
        lastWord = settings.getInt("last_word", -1);
        Calendar calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_YEAR);

        boolean newDay = false;

        if (today != lastDate) newDay = true;

        return newDay;
    }

    public Spannable getWotdText() {

        String textData = settings.getString("WOTD", "error");
        String[] textDataArray = textData.split(",,,");
        ArrayList<String> textList = new ArrayList<>(Arrays.asList(textDataArray));

        return new FormatText().getformattedText("Word of the day", textData, context);
    }

    public void showRateButterDialog() {

        MaterialAlertDialogBuilder rateButterDialog = new MaterialAlertDialogBuilder(this);
        rateButterDialog.setTitle("Love Butter " + getEmojiByUniCode("two Hearts"));
        rateButterDialog.setMessage("Please take some time to rate us on Google Play.");

        rateButterDialog.show();

    }

    public void showShareButterDialog() {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String shareMessage = "\nGet definitions of hard words on your screen on just a click. Download Butter ";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose any"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getEmojiByUniCode(String string) {
//TODO is it not working on android 4.4
        String emoji = "";

        switch (string) {

            case "two Hearts" :
                emoji = new String(Character.toChars(0x1F495));

        }

        return emoji;
    }

    public void showFriendlyAdviceDialog(int i) {

        switch (i) {

            case 0 :
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

    public class SectionsHolder extends RecyclerView.ViewHolder {

        MaterialTextView pageTextView;
        MaterialTextView etymologyText;

        public SectionsHolder(@NonNull View itemView) {
            super(itemView);
            pageTextView = itemView.findViewById(R.id.page_text_view);
            etymologyText = itemView.findViewById(R.id.etymology_text);

            pageTextView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    menu.add(0, 1299, 0, "Define");
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    menu.removeItem(android.R.id.selectAll);
                    menu.removeItem(android.R.id.cut);
                    menu.removeItem(android.R.id.paste);
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case 1299:

                            int min = 0;
                            int max = pageTextView.getText().length();

                            if (pageTextView.isFocused()) {

                                final int selStart = pageTextView.getSelectionStart();
                                final int selEnd = pageTextView.getSelectionEnd();

                                min = Math.max(0, Math.min(selStart, selEnd));
                                max = Math.max(0, Math.max(selStart, selEnd));

                                final CharSequence selectedText = pageTextView.getText().subSequence(min, max);

                                String word = selectedText.toString();

                                loadingAnimationView.playAnimation();
                                loadingAnimationView.setVisibility(View.VISIBLE);

                                Spannable text;
                                GetDefinition getDefinition = new GetDefinition();

                                if (workingInWotd) {

                                    setWhichWord = false;
                                    text = getDefinition.define(word, null);
                                    wotdSectionsStateAdapter.addPage(text);
                                    wotdSectionsStateAdapter.notifyDataSetChanged();

                                } else {

                                    text = getDefinition.define(word, null);
                                    searchSectionsStateAdapter.addPage(text);
                                    searchSectionsStateAdapter.notifyDataSetChanged();

                                }

                                tabNames.add(String.valueOf(selectedText));

                                wordIdTablayout.selectTab(wordIdTablayout.getTabAt(wordIdTablayout.getTabCount() - 1), true);

                                for (int i = 0; i < wordIdTablayout.getTabCount(); i++) {

                                    if (wordIdTablayout.getTabAt(i) != null) {
                                        wordIdTablayout.getTabAt(i).setText(tabNames.get(i));
                                    }

                                }

                                loadingAnimationView.pauseAnimation();
                                loadingAnimationView.setVisibility(View.INVISIBLE);

                                actionMode.finish();
                            }
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });
        }

    }

    public class SectionsStateAdapter extends RecyclerView.Adapter<SectionsHolder> {

        private final Context context;
        private final ArrayList<Spannable> pagesData;

        public SectionsStateAdapter(Context context) {
            this.context = context;
            pagesData = new ArrayList<>();
        }

        @NonNull
        @Override
        public SectionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SectionsHolder(LayoutInflater.from(context).inflate(R.layout.page_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SectionsHolder holder, int position) {
            holder.pageTextView.setText(pagesData.get(position));

            if (context != null) {

                if (workingInWotd && setWhichWord) {

                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                    String etymString = settings.getString("WOTDETYMOLOGIES", "Etymology not found");

                    if (!(etymString.equals("Etymology not found") || etymString.equals(""))) {
                        String[] etymologies = etymString.split(",,,");
                        for (int i = 0; i < etymologies.length; i++) {
                            holder.etymologyText.setText("\n" + i + ". " + etymologies[i] + "\n");
                        }
                    } else {
                        holder.etymologyText.setVisibility(View.INVISIBLE);
                    }

                } else if (etymologies != null) {

                    StringBuilder etymText = new StringBuilder();
                    for (int i = 0; i < etymologies.size(); i++) {
                        etymText.append("\n").append(i + 1).append(". ").append(etymologies.get(i)).append("\n");
                    }

                    holder.etymologyText.setText(etymText.toString());

                } else {

                    holder.etymologyText.setVisibility(View.INVISIBLE);
                }

            }

        }

        @Override
        public int getItemCount() {
            return pagesData.size();
        }

        public void addPage(Spannable spannable) {
            pagesData.add(spannable);
        }

        public void removeAllPages() {
            pagesData.clear();
        }

        public void removeLastPage() {
            pagesData.remove(pagesData.size() - 1);
        }

        public boolean isEmpty() {

            boolean bool = false;

            if (pagesData.isEmpty()) bool = true;

            return !bool;
        }

    }

}