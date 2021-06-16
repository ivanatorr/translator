import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class YandexApiTranslator {

    public static void main(String[] args) {
        new YandexApiTranslator();
    }
    private YandexApiTranslator() {
        System.out.println(translate("apple core?", "en-ru"));
    }
    public String translate(String text, String lang) {
        byte out[] = ("text=" + text).getBytes();
        if (out.length>10000) {
            return "Error. Text too long";
        }
        String key = "trnsl.1.1.20170315T212456Z.4a15b1b7d9902867.271a33f600908be40a04905acfa8d35ed564f787"; //Подставьте сюда свой полученный ключ между кавычек
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
                return "Error. Site response non 200";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}