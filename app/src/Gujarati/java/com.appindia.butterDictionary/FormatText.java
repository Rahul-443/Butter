package com.appindia.butterDictionary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

import static com.appindia.butterDictionary.JsonToText.lexicalCategoryTexts;
import static com.appindia.butterDictionary.JsonToText.types;

public class FormatText {

    public Spannable getformattedText(String whichWord, String queryType, String textData, Context context) {

        String word = null;

        if (queryType.equals("entry")) {
            word = JsonToText.word;
        } else if (queryType.equals("word")) {
            word = Word.query;
        }

        Spannable s = new SpannableString(textData);
        if (textData != null) {

            if (whichWord != null && word != null) {

                int wwStartIndex = 0;
                int wwEndIndex = whichWord.length() + 1;

                s.setSpan(new TypefaceSpan("sans-serif"), wwStartIndex, wwEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), wwStartIndex, wwEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new RelativeSizeSpan(1.5f), wwStartIndex, wwEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWhichWord)), wwStartIndex, wwEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                int wordStartIndex = whichWord.length() + 1;
                int wordEndIndex = whichWord.length() + word.length() + 3;

                s.setSpan(new TypefaceSpan("sans-serif"), wordStartIndex, wordEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), wordStartIndex, wordEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new RelativeSizeSpan(1.5f), wordStartIndex, wordEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWord)), wordStartIndex, wordEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                for (int i = whichWord.length(); (i = textData.indexOf(word, i + 1)) != -1; i++ ) {
                    s.setSpan(new UnderlineSpan(), i, i + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                String type = null;

                if (queryType.equals("entry")) {
                    type = "(" + types.get(0) + ")";
                } else if (queryType.equals("word")) {
                    type = "(" + Word.wordTypeList.get(0) + ")";
                }

                int typeStartIndex = whichWord.length() + word.length() + 4;

                if (type != null) {

                    int typeEndIndex = typeStartIndex + type.length() + 1;
                    s.setSpan(new RelativeSizeSpan(0.8f), typeStartIndex, typeEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

            } else if (word != null) {

                s.setSpan(new TypefaceSpan("sans-serif"), 0, word.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), 0, word.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new RelativeSizeSpan(1.5f), 0, word.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWord)), 0, word.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                for (int i = -1; (i = textData.indexOf(word, i + 1)) != -1; i++ ) {
                    s.setSpan(new UnderlineSpan(), i, i + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                String type = null;

                if (queryType.equals("entry")) {
                    type = "(" + types.get(0) + ")";
                } else if (queryType.equals("word")) {
                    type = "(" + Word.wordTypeList.get(0) + ")";
                }

                if (type != null) {
                    s.setSpan(new RelativeSizeSpan(0.8f), word.length() + 1, word.length() + type.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }

            if (queryType.equals("entry")) {

                for (int i = 0; i < lexicalCategoryTexts.size(); i++) {
                    int startIndex = textData.indexOf((i + 1) + ". " + lexicalCategoryTexts.get(i));
                    int endIndex = startIndex + lexicalCategoryTexts.get(i).length() + 3;
                    for (int i1 = -1; (i1 = textData.indexOf((i + 1) + ". " + lexicalCategoryTexts.get(i), i1 + 1)) != -1; i1++ ) {
                        s.setSpan(new TypefaceSpan("sans-serif"), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorLexicalCategory)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s.setSpan(new StyleSpan(Typeface.ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //s.setSpan(new RelativeSizeSpan(1.1f), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

            } else if (queryType.equals("word")) {

                for (int i = 0; i < Word.lexicalCategoryList.size(); i++) {
                    int startIndex = textData.indexOf((i + 1) + ". " + Word.lexicalCategoryList.get(i));
                    int endIndex = startIndex + Word.lexicalCategoryList.get(i).length() + 3;
                    for (int i1 = -1; (i1 = textData.indexOf((i + 1) + ". " + Word.lexicalCategoryList.get(i), i1 + 1)) != -1; i1++ ) {
                        s.setSpan(new TypefaceSpan("sans-serif"), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorLexicalCategory)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s.setSpan(new StyleSpan(Typeface.ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //s.setSpan(new RelativeSizeSpan(1.1f), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

            }



            for (int i = -1; (i = textData.indexOf(context.getResources().getString(R.string.shortDefinitions), i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorShortDefinitions)), i, i + 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int i = -1; (i = textData.indexOf(context.getResources().getString(R.string.definitions), i + 1)) != -1; i++ ) {
                if (textData.substring(i - 2, i).equals(". ")) {
                    s.setSpan(new TypefaceSpan("sans-serif"), i - 3, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    s.setSpan(new StyleSpan(Typeface.BOLD), i - 3, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //s.setSpan(new UnderlineSpan(), i - 3, i + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorDefinition)), i - 3, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            for (int i = -1; (i = textData.indexOf(context.getResources().getString(R.string.examples), i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorExample)), i, i + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int i = -1; (i = textData.indexOf(context.getResources().getString(R.string.synonyms), i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSynonym)), i, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int i = -1; (i = textData.indexOf("Etymologies", i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorEtymologies)), i, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int i = -1; (i = textData.indexOf(context.getResources().getString(R.string.grammaticalFeatures), i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorGrammaticalFeatures)), i, i + 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int i = -1; (i = textData.indexOf(context.getResources().getString(R.string.phrases), i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPhrases)), i, i + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int i = -1; (i = textData.indexOf(context.getResources().getString(R.string.semanticClasses), i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSemanticClasses)), i, i + 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int i = -1; (i = textData.indexOf(context.getResources().getString(R.string.domainClasses), i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorDomainClasses)), i, i + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int i = -1; (i = textData.indexOf("Derivatives", i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorDerivatives)), i, i + 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int i = -1; (i = textData.indexOf(context.getResources().getString(R.string.notes), i + 1)) != -1; i++ ) {
                s.setSpan(new TypefaceSpan("sans-serif"), i, i + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new StyleSpan(Typeface.BOLD), i, i + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorNotes)), i, i + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }

        return s;
    }


}
