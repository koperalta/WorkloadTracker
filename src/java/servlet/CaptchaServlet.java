package servlet;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class CaptchaServlet {
    
    public static final String SECRET_KEY = "6LdUXrwsAAAAAMOru-Ex8z6Kiis2so4PMiq3Q9lU";
    
    public static boolean verify(String gCaptchaResponse) {
        if (gCaptchaResponse == null || "".equals(gCaptchaResponse)) {
            return false;
        }
        
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";
            String params = "secret=" + SECRET_KEY + "&response=" + gCaptchaResponse;
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(params.getBytes());
            wr.flush();
            wr.close();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String currLine;
            while ((currLine = in.readLine()) != null) {
                response.append(currLine);
            }
            in.close();
            
            JSONObject responseJSON = new JSONObject(response.toString());
            return responseJSON.getBoolean("success");
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
