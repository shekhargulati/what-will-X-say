package com.shekhargulati.wwxs;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SpeechUrlFinder {

    public static void main(String[] args) throws Exception {
        List<String> urls = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            Connection connect = Jsoup.connect(String.format("https://www.narendramodi.in/speech/loadspeeche?page=%d&language=en", i));
            Document doc = connect.get();
            Elements elements = doc.select(".speeches .speechesBox .speechesItemLink a");
            for (Element element : elements) {
                urls.add(element.attr("href"));
            }
        }

        Path file = Files.write(Paths.get("urls.txt"), urls);
        System.out.println(file.toAbsolutePath());

    }
}
