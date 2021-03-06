package capstone.oras.oauth2.controller;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.entity.AccountEntity;
import capstone.oras.model.custom.LoginModel;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static capstone.oras.common.Constant.ORAS_HOST;

@RestController
@CrossOrigin(value = "http://localhost:9527")
public class LoginController {

    @Autowired
    private IAccountService accountService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:8088")
    ResponseEntity<String> login(@RequestBody LoginModel model) throws Exception {
        String email = model.getUsername();
        String password = model.getPassword();
        AccountEntity accountEntity = accountService.findAccountByEmail(email);
        if (accountEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is not registered");
        } else if (!accountEntity.getConfirmMail()) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Please confirm your email to complete the registration");
        } else if (!accountEntity.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your company is not verified. Please wait for " +
                    "email from admin.");
        }

        String url = ORAS_HOST + "/oauth/token";
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
//        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
        connection.setRequestMethod(RequestMethod.POST.name());
        connection.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        connection.setRequestProperty("Host", ORAS_HOST);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", "Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0");
        String urlParameters = "grant_type=password&username=" + email + "&password=" + password;
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            String access_token = jsonObject.getString("access_token");
            return new ResponseEntity<>(access_token, HttpStatus.OK);
        } catch (IOException error) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is incorrect");

        }

    }

}
