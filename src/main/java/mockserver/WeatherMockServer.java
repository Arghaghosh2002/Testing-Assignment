package mockserver;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WeatherMockServer {

    private HttpServer server;

    private static final int    PORT     = 8080;
    private static final String ENDPOINT = "/api/weather";

    private static final String JSON_RESPONSE =
            "{\"city\": \"Kolkata\", \"temperature\": 30, \"condition\": \"Humid\"}";

    public void start() throws IOException {

        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(ENDPOINT, new WeatherHandler());
        server.setExecutor(null);
        server.start();

        //System.out.println("Mock server started  : http://localhost:" + PORT + ENDPOINT);
    }
    public void stop() {
        if (server != null) {
            server.stop(0);
            //System.out.println("Mock server stopped.");
        }
    }

    private static class WeatherHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {

                byte[] responseBytes = JSON_RESPONSE.getBytes("UTF-8");

                exchange.getResponseHeaders().set("Content-Type", "application/json");

                exchange.sendResponseHeaders(200, responseBytes.length);

                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(responseBytes);
                outputStream.close();

                //System.out.println("Mock server responded with: " + JSON_RESPONSE);

            } else {
                exchange.sendResponseHeaders(405, -1);
                exchange.getResponseBody().close();
            }
        }
    }
}