package org.buildATrip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        return "index"; // Returns "index.html" from the templates folder
    }


    @GetMapping("/itinerary/")
    public String showItineraryPage() {
        return "itinerary"; // Returns the Thymeleaf template (itinerary.html)
    }
}
