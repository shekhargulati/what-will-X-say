package com.shekhargulati.wwxs;

import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLDocument;
import de.l3s.boilerpipe.sax.HTMLFetcher;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class DatasetGenerator {

    public static void main(String[] args) throws Exception {
        DatasetGenerator.writeTextToOutputDir(
                Paths.get("src", "main", "resources", "urls.txt"),
                Paths.get("src", "main", "resources", "speeches")
        );
    }


    public static void writeTextToOutputDir(Path inputFile, Path outputDir) throws IOException {
        GoogleTranslator translator = new GoogleTranslator();
        List<String> urls = Files.readAllLines(inputFile);
        int totalUrls = urls.size();
        for (int i = 0; i < totalUrls; i++) {
            String url = urls.get(i);
            System.out.println(String.format("Downloading text for url %d - %s", i, url));
            String text = text(url);
            translator.translate(text, "en", "hi");
            Files.write(
                    outputDir.resolve(String.format("%d_%s.txt", i + 1, UUID.randomUUID().toString())),
                    text.getBytes());
        }
    }

    private static String text(String url) {
        try {
            HTMLDocument htmlDocument = HTMLFetcher.fetch(new URL(url));
            final TextDocument doc = new BoilerpipeSAXInput(htmlDocument.toInputSource()).getTextDocument();
            return ArticleExtractor.INSTANCE.getText(doc);
        } catch (Exception e) {
            // skipping exception
            throw new RuntimeException(e);
        }
    }


}
