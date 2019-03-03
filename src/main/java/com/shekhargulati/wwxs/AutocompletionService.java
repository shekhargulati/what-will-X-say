package com.shekhargulati.wwxs;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class AutocompletionService {

    Map<String, Map<String, Integer>> first = new HashMap<>();
    Map<String, Map<String, Integer>> second = new HashMap<>();
    Map<String, Map<String, Integer>> third = new HashMap<>();
    Map<String, Map<String, Integer>> fourth = new HashMap<>();
    Map<String, Map<String, Integer>> fifth = new HashMap<>();
    Map<String, Map<String, Integer>> sixth = new HashMap<>();
    Map<String, Map<String, Integer>> seventh = new HashMap<>();
    Map<String, Map<String, Integer>> eigth = new HashMap<>();
    Map<String, Map<String, Integer>> ninth = new HashMap<>();
    Map<String, Map<String, Integer>> tenth = new HashMap<>();

    public AutocompletionService() throws Exception {
        init("");
    }

    private void init(String dataDir) throws Exception {
        Stream<Path> files = Files.list(Paths.get(dataDir));

        String data = files.flatMap(file -> {
            try {
                return Files.lines(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining(" "));


        String[] sentences = data.toLowerCase()
                .replace("\r", " ")
                .replace("\n", " ")
                .replace("?", ".")
                .replace("!", ".")
                .replace("?", ".")
                .replace("“", ".")
                .replace("”", ".")
                .replace("\"", ".")
                .replace("‘", " ")
                .replace("-", " ")
                .replace("’", " ")
                .replace("\"", " ")
                .split("\\.");

        for (String sentence : sentences) {
            String[] wordsInSent = sentence.replace(",", " ").split(" ");
            String[] words = Arrays.stream(wordsInSent).filter(word -> !word.trim().isEmpty()).toArray(String[]::new);
            if (words.length == 0) {
                continue;
            }
            for (int i = 0; i < words.length; i++) {
                if (i >= 1) {
                    String seq = toSeq(words, i, 1);
                    if (seq != null) {
                        updateOcc(first, seq, words[i]);
                    }
                }
                if (i >= 2) {
                    String seq = toSeq(words, i, 2);
                    if (seq != null) {
                        updateOcc(second, seq, words[i]);
                    }
                }
                if (i >= 3) {
                    String seq = toSeq(words, i, 3);
                    if (seq != null) {
                        updateOcc(third, seq, words[i]);
                    }
                }
                if (i >= 4) {
                    String seq = toSeq(words, i, 4);
                    if (seq != null) {
                        updateOcc(fourth, seq, words[i]);
                    }
                }
                if (i >= 5) {
                    String seq = toSeq(words, i, 5);
                    if (seq != null) {
                        updateOcc(fifth, seq, words[i]);
                    }
                }
                if (i >= 6) {
                    String seq = toSeq(words, i, 6);
                    if (seq != null) {
                        updateOcc(sixth, seq, words[i]);
                    }
                }
                if (i >= 7) {
                    String seq = toSeq(words, i, 7);
                    if (seq != null) {
                        updateOcc(seventh, seq, words[i]);
                    }
                }
                if (i >= 8) {
                    String seq = toSeq(words, i, 8);
                    if (seq != null) {
                        updateOcc(eigth, seq, words[i]);
                    }
                }

                if (i >= 9) {
                    String seq = toSeq(words, i, 9);
                    if (seq != null) {
                        updateOcc(ninth, seq, words[i]);
                    }
                }

                if (i >= 10) {
                    String seq = toSeq(words, i, 10);
                    if (seq != null) {
                        updateOcc(tenth, seq, words[i]);
                    }
                }
            }

        }
    }

    private String toSeq(String[] words, int i, int max) {
        if (i <= words.length) {
            return IntStream.range(i - max, i).mapToObj(j -> words[j]).collect(Collectors.joining(" "));
        }
        return null;
    }


    private void updateOcc(Map<String, Map<String, Integer>> dict, String seq, String w) {
        if (!dict.containsKey(seq)) {
            dict.put(seq, new HashMap<>());
        }
        Map<String, Integer> map = dict.get(seq);
        map.put(w, map.getOrDefault(w, 0) + 1);
    }

    private Map<String, Map<String, Integer>> getMap(int testLen) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        if (testLen == 10) {
            map = tenth;
        } else if (testLen == 9) {
            map = ninth;
        } else if (testLen == 8) {
            map = eigth;
        } else if (testLen == 7) {
            map = seventh;
        } else if (testLen == 6) {
            map = sixth;
        } else if (testLen == 5) {
            map = fifth;
        } else if (testLen == 4) {
            map = fourth;
        } else if (testLen == 3) {
            map = third;
        } else if (testLen == 2) {
            map = second;
        } else if (testLen == 1) {
            map = first;
        }
        return map;
    }

    public List<Pair> autocomplete(final String input) {
        String[] words = input.split(" ");
        int length = words.length;
        int lastIdx = length - 1;
        String seq = toSeq(words, lastIdx + 1, length);
        Map<String, Map<String, Integer>> map = getMap(length);
        if (!map.containsKey(seq)) {
            return Collections.emptyList();
        }
        Map<String, Integer> t = map.get(seq);
        return possibleResults(t);
    }


    private List<Pair> possibleResults(Map<String, Integer> t) {
        int total = t.values().stream().mapToInt(i -> i).sum();
        Comparator<Map.Entry<String, Integer>> comparator = Comparator.comparingInt(Map.Entry::getValue);
        List<Map.Entry<String, Integer>> entries = t.entrySet().stream()
                .sorted(comparator.reversed())
                .limit(10)
                .collect(toList());
        entries.forEach(e -> System.out.println(String.format("%s %d", e.getKey(), Double.valueOf(((double) e.getValue() / total) * 100).intValue())));

        return entries.stream()
                .map(e -> new Pair(e.getKey(), Double.valueOf(((double) e.getValue() / total) * 100).intValue()))
                .collect(toList());
    }
}

