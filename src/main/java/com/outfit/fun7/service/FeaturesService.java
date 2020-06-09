package com.outfit.fun7.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.outfit.fun7.model.Features;
import com.outfit.fun7.repository.UserRepository;
import com.outfit.fun7.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class FeaturesService {

    private final UserRepository userRepository;
    private final Logger logger = LogManager.getLogger(FeaturesService.class);

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
        if (Arrays.asList(TimeZone.getAvailableIDs()).contains(timezone)) {
            LocalDateTime timeLj= LocalDateTime.now(ZoneId.of("Europe/Ljubljana"));
            LocalDateTime timeUser = LocalDateTime.now(ZoneId.of(timezone));

            LocalTime startHour = LocalTime.parse("09:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime endHour = LocalTime.parse("15:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));

            int hourDifference = (int) Duration.between(timeLj, timeUser).toHours();
            if (startHour.isBefore(LocalTime.from(timeUser.minusHours(hourDifference))) && endHour.isAfter(LocalTime.from(timeUser.minusHours(hourDifference)))) {
                return "enabled";
            }
            else {
                return "disabled";
            }
        }
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
                if (convertedObject.get("ads").getAsString().equals("sure, why not!")) {
                    return "enabled";
                }
                else {
                    return "disabled";
                }
            }
            else {
                logger.info("External ads API response code: " + status + ", and message: " + con.getResponseMessage());
            }
        }
        catch (IOException e) {
            logger.error("Error while getting ads feature.", e);
        }

        return "disabled";
    }
}
