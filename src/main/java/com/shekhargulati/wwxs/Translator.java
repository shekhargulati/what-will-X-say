package com.shekhargulati.wwxs;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Translator {

    private final Map<String, Object> headers;
    private String url;
    private OkHttpClient client = new OkHttpClient();

    public Translator() {
        this.url = "http://translate.google.com/translate_a/t?client=webapp&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&dt=at&ie=UTF-8&oe=UTF-8&otf=2&ssel=0&tsel=0&kc=1";
        this.headers = new HashMap<>();
        this.headers.put("Accept", "*/*");
        this.headers.put("Connection", "keep-alive");
        this.headers.put("User-Agent", Arrays.asList("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) ", "AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.168 Safari/535.19"));
    }

    public String translate(String source, String fromLang, String toLang) throws IOException {
//        String updatedUrl = String.format(
//                "%s&sl=%s&tl=%s&hl=%s&tk=%s",
//                this.url,
//                fromLang,
//                toLang,
//                toLang,
//                calculateTk(source)
//        );

//        String encoded = URLEncoder.encode(source, "UTF-8");
//
//        RequestBody body = RequestBody.create(JSON, json);
//
//        Request request = new Request.Builder()
//                .url(updatedUrl)
//                .post(Reokh)
//                .header("Accept", "*/*")
//                .header("Connection", "keep-alive")
//                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) ")
//                .addHeader("User-Agent", "AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.168 Safari/535.19)")
//                .build();


//
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        }

        CloseableHttpClient client = HttpClients.createMinimal();

        String updatedUrl = String.format(
                "%s&sl=%s&tl=%s&hl=%s&tk=%s",
                this.url,
                fromLang,
                toLang,
                toLang,
                calculateTk(source)
        );

        HttpPost httpPost = new HttpPost(updatedUrl);
        httpPost.setHeader("User-Agent", "Apache-HttpClient/4.5.2 (Java/1.8.0_91)");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String postParam = "q=" + URLEncoder.encode(source, "UTF-8");
        httpPost.setEntity(new StringEntity(postParam));

        try (CloseableHttpResponse response = client.execute(httpPost)) {
            return EntityUtils.toString(response.getEntity());
        }

    }

    private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    private static Invocable invocable;

    static {
        String script = "function tk(a) {"
                + "var TKK = ((function() {var a = 561666268;var b = 1526272306;return 406398 + '.' + (a + b); })());\n"
                + "function b(a, b) { for (var d = 0; d < b.length - 2; d += 3) { var c = b.charAt(d + 2), c = 'a' <= c ? c.charCodeAt(0) - 87 : Number(c), c = '+' == b.charAt(d + 1) ? a >>> c : a << c; a = '+' == b.charAt(d) ? a + c & 4294967295 : a ^ c } return a }\n"
                + "for (var e = TKK.split('.'), h = Number(e[0]) || 0, g = [], d = 0, f = 0; f < a.length; f++) {"
                + "var c = a.charCodeAt(f);"
                + "128 > c ? g[d++] = c : (2048 > c ? g[d++] = c >> 6 | 192 : (55296 == (c & 64512) && f + 1 < a.length && 56320 == (a.charCodeAt(f + 1) & 64512) ? (c = 65536 + ((c & 1023) << 10) + (a.charCodeAt(++f) & 1023), g[d++] = c >> 18 | 240, g[d++] = c >> 12 & 63 | 128) : g[d++] = c >> 12 | 224, g[d++] = c >> 6 & 63 | 128), g[d++] = c & 63 | 128)"
                + "}"
                + "a = h;"
                + "for (d = 0; d < g.length; d++) a += g[d], a = b(a, '+-a^+6');"
                + "a = b(a, '+-3^+b+-f');"
                + "a ^= Number(e[1]) || 0;"
                + "0 > a && (a = (a & 2147483647) + 2147483648);"
                + "a %= 1E6;"
                + "return a.toString() + '.' + (a ^ h)\n"
                + "}";
        try {
            engine.eval(script);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        invocable = (Invocable) engine;
    }

    private static String calculateTk(String str) {
        String tk = null;
        try {
            tk = (String) invocable.invokeFunction("tk", str);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return tk;
    }

}
