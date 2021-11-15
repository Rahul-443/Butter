package com.appindia.butterDictionary;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AudioFileDownloaderV2 extends AsyncTask<String, Integer, Integer> {

    @Override
    protected Integer doInBackground(String... strings) {

        int count = 0;
        boolean isFileSaved = false;

        String audioFileDestination = strings[1];

        try {

            URL url = new URL(strings[0]);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            int lengthOfFile = urlConnection.getContentLength(); //size of file (useful for showing progress in UI)

            FileOutputStream fileOutputStream = App.openFileOut(audioFileDestination);

            //Download the file
            InputStream inputStream = new BufferedInputStream(url.openStream());

            byte[] data = new byte[1024];

            long total = 0;

            while ((count = inputStream.read(data)) != -1) {
                total += count;
                //publish the progress here e.g. -> total/length of file * 100 = download progress.
                //((OutputStream) fileOutputStream).write(data, 0, count); //saving the file.
            }

            //fileOutputStream.flush();
            //((OutputStream) fileOutputStream).close();
            inputStream.close();

            isFileSaved = true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }

}
