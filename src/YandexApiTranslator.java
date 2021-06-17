import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class YandexApiTranslator {
    private static final String fileInput = "input.txt";

    public static void main(String[] args) {
        new YandexApiTranslator();
    }
    private YandexApiTranslator() {
        try {
            Scanner scanner = new Scanner(new File(fileInput));
            String input = scanner.nextLine();
            System.out.println(translate(input, "en-ru"));
        }catch(Exception e){
            System.out.println("Error");
        }

    }
    public String translate(String text, String lang) {
        byte out[] = ("text=" + text).getBytes();
        if (out.length>10000) {
            System.out.println("Error");
        }
        String key = "trnsl.1.1.20190115T093726Z.65e1460d8d95bd06.р45ор345о3р4о53р45о345р3о";
        String baseUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate";
        String finalUrl = baseUrl + "?lang=" + lang + "&key=" + key;
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", out.length + "");
            connection.setRequestProperty("Accept", "*/*");
            connection.getOutputStream().write(out);
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr = jobj.get("text").getAsJsonArray();
                return jarr.get(0).getAsString();
            } else {
                return "Error";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}