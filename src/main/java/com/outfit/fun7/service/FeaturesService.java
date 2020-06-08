package com.outfit.fun7.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.outfit.fun7.model.Features;
import com.outfit.fun7.repository.UserRepository;
import com.outfit.fun7.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class FeaturesService {

    private final UserRepository userRepository;

    @Autowired
    public FeaturesService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Features getFeatures(Long id, String timezone, String cc) {
        Optional<User> users = this.userRepository.findById(id);
        User user;
        Features features = null;
        if (users.isPresent()) {
            user = users.get();
            System.out.println(user.getUsage());
            features = new Features(multiplayer(user.getUsage(), cc), customerSupport(timezone), ads(cc));
            user.incrementUsage();
            this.userRepository.save(user);
        }
        return features;
    }

    private String multiplayer(int usage, String cc) {
        if (usage > 5 && cc.equals("US")) {
            return "enabled";
        }
        return "disabled";
    }

    private String customerSupport(String timezone) {
        return "disabled";
    }

    private String ads(String cc) {
        try {
            URL url = new URL("https://us-central1-o7tools.cloudfunctions.net/fun7-ad-partner?countryCode=" + cc);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(("fun7user:fun7pass").getBytes(StandardCharsets.UTF_8)));
            con.setDoOutput(true);

            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                Gson gson = new Gson();
                JsonObject convertedObject = new Gson().fromJson(content.toString(), JsonObject.class);
                System.out.println(convertedObject.get("ads"));
                if (convertedObject.get("ads").getAsString().equals("sure, why not!")) {
                    return "enabled";
                }
                else {
                    return "disabled";
                }
            }
            else {
                // TODO: Find nicer way to print error.
                System.out.println("error");
            }
        }
        catch (IOException e) {
            // TODO: Find nicer way to print error.
            System.out.println(e);
        }

        return "disabled";
    }
}
