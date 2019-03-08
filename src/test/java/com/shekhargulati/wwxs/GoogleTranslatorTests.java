package com.shekhargulati.wwxs;

import org.junit.Test;

public class GoogleTranslatorTests {

    @Test
    public void should_convert_hindi_to_english() throws Exception {

        GoogleTranslator translator = new GoogleTranslator();
        String translated = translator.translate(
                "美丽优于丑陋",
                "zh-CN",
                "en"
        );

        System.out.println(translated);

    }
}