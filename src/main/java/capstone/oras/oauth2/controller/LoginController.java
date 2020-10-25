package capstone.oras.oauth2.controller;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

@RestController
@CrossOrigin(value = "http://localhost:9527")
public class LoginController {

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    @ResponseBody
//    @CrossOrigin(origins = "http://localhost:8088")
    ResponseEntity<String>  login(@RequestParam("username") String email, @RequestParam("password") String password) throws Exception {
//        String url = "http://localhost:8088/oauth/token";
        String url = "https://oras-api.herokuapp.com/";
        URL obj = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod(RequestMethod.POST.name());
//        connection.setRequestProperty("Host", "localhost:8080");
        connection.setRequestProperty("Host", "oras-api.herokuapp.com");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", "Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0");
//        connection.setRequestProperty("Access-Control-Allow-Origin", "http://localhost:9527");
        String urlParameters = "grant_type=password&username="+ email + "&password=" + password;

        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
//        int responseCode = connection.getResponseCode();
        BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null ) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonObject = new JSONObject(response.toString());
        String access_token = jsonObject.getString("access_token");
//        httpHeaders.set("Access-Control-Allow-Origin", "http://localhost:8088");
//        httpHeaders.setAccessControlAllowOrigin("http://localhost:8088");

//        return new ResponseEntity<>(InetAddress.getLoopbackAddress().getHostName(), HttpStatus.OK);
        return new ResponseEntity<>(access_token, HttpStatus.OK);
    }
}
