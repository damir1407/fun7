package com.outfit.fun7;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.outfit.fun7.model.Features;
import com.outfit.fun7.model.User;
import com.outfit.fun7.repository.UserRepository;
import com.outfit.fun7.service.FeaturesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

public class Fun7UnitTests {

    @InjectMocks
    private FeaturesService featuresService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFeaturesService() {
        Long id = 5634161670881280L;
        String timezone = "America/New_York";
        String countryCode = "US";

        when(userRepository.findById(id)).thenReturn(Optional.of(new User(id, 10)));

        Features features = featuresService.getFeatures(id, timezone, countryCode);
        JsonObject convertedObject = new Gson().fromJson(features.toString(), JsonObject.class);
        assertEquals("enabled", convertedObject.get("multiplayer").getAsString());
        verify(userRepository).findById(id);
    }
}
