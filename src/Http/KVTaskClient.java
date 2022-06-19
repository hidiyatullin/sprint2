package Http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private final HttpClient client;
    String token;
    KVServer kvServer = new KVServer();

    public KVTaskClient() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8078/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        kvServer.start();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        token = response.body();
    }

    public String load(String key) {
        URI uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + token);
        String responseString = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                responseString = response.body();
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return responseString;
    }

    public void put(String key, String json) {
        URI uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .uri(uri)
                .build();
        try {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            client.send(request, handler);
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}
