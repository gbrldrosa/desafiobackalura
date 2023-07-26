import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ConversorMoedas {

    private static final String API_KEY = "SUA_CHAVE_DE_API_AQUI";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Digite a moeda de origem (exemplo: USD): ");
            String moedaOrigem = reader.readLine().toUpperCase();

            System.out.print("Digite a moeda de destino (exemplo: EUR): ");
            String moedaDestino = reader.readLine().toUpperCase();

            System.out.print("Digite o valor a ser convertido: ");
            double valor = Double.parseDouble(reader.readLine());

            double taxaDeCambio = getTaxaDeCambio(moedaOrigem, moedaDestino);
            if (taxaDeCambio != -1) {
                double valorConvertido = valor * taxaDeCambio;
                System.out.printf("%.2f %s equivalem a %.2f %s%n", valor, moedaOrigem, valorConvertido, moedaDestino);
            } else {
                System.out.println("Não foi possível obter a taxa de câmbio.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double getTaxaDeCambio(String moedaOrigem, String moedaDestino) {
        try {
            URL url = new URL(BASE_URL + moedaOrigem);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject data = new JSONObject(response.toString());
                return data.getJSONObject("conversion_rates").getDouble(moedaDestino);
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
}


