package com.shekhargulati.wwxs;

import org.junit.Test;

public class TranslatorTests {

    @Test
    public void should_convert_hindi_to_english() throws Exception {

        Translator translator = new Translator();
        String translated = translator.translate(
                "美丽优于丑陋",
                "zh-CN",
                "en"
        );

        System.out.println(translated);

    }
}