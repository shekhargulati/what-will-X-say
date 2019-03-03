package com.shekhargulati.wwxs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class DatasetGeneratorTests {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void should_download_content_from_file() throws Exception {
        Path outputDir = folder.getRoot().toPath();
        DatasetGenerator.donwloadUrls(
                Paths.get("src", "test", "resources", "urls.txt"),
                outputDir
        );

        assertThat(Files.list(outputDir).count()).isEqualTo(10);
    }
}