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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DatasetGenerator {


    public static void donwloadUrls(Path inputFile, Path outputDir) throws IOException {

        List<String> urls = Files.readAllLines(inputFile);
        int totalUrls = urls.size();

        List<Path> files = IntStream.rangeClosed(1, totalUrls)
                .peek(idx -> System.out.println(String.format("Downloading text for url %d - %s", idx, urls.get(idx - 1))))
                .mapToObj(idx -> new Pair<>(text(urls.get(idx - 1)), idx))
                .map(p -> {
                    try {
                        return Files.write(outputDir.resolve(String.format("%d_%s.txt", p.getSecond(), UUID.randomUUID().toString())), p.getFirst().getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        System.out.println("Done downloading all files");


    }

    public static String text(String url) {
        try {
            HTMLDocument htmlDocument = HTMLFetcher.fetch(new URL(url));
            final TextDocument doc = new BoilerpipeSAXInput(htmlDocument.toInputSource()).getTextDocument();
            return ArticleExtractor.INSTANCE.getText(doc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
