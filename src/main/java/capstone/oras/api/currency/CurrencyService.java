package capstone.oras.api.currency;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class CurrencyService {

    public double currencyConverter(String baseCurrency, String inputCurrencyType, double currency) throws Exception {
        String url = "https://currencyapi.net/api/v1/rates?key=hMJVDE4VPEi5DTtaZm4NTWyxi8Wwt1x4YtUf&base=" + baseCurrency;
        URL obj = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod(RequestMethod.POST.name());
        connection.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
//        connection.setRequestProperty("Host", "localhost:8080");
        connection.setRequestProperty("Host", "currencyapi.net");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject jsonObject = new JSONObject(response.toString());
        jsonObject = jsonObject.getJSONObject("rates");
        String output = jsonObject.getString(inputCurrencyType);
        if (output != null) {
            double outputCurrency = Double.parseDouble(output);
            outputCurrency = currency / outputCurrency;
            return outputCurrency;
        } else {
            return 0;
        }
    }
}
