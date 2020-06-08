package com.outfit.fun7.api;

import com.outfit.fun7.model.Features;
import com.outfit.fun7.model.User;
import com.outfit.fun7.service.FeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1")
@RestController
public class MainController {

    private final FeaturesService featuresService;

    @Autowired
    public MainController(FeaturesService featuresService) {
        this.featuresService = featuresService;
    }

    @GetMapping(path="/features")
    @ResponseBody
    public String getFeatures(@RequestParam("userId") Long id,
                            @RequestParam("timezone") String timezone,
                            @RequestParam("cc") String cc) {
        Features feature = this.featuresService.getFeatures(id, timezone, cc);
        if(feature != null) {
            return feature.toString();
        }
        else {
            return "[]";
        }

    }
}
