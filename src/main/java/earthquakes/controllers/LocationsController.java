package earthquakes.controllers;

import earthquakes.osm.Place;
import earthquakes.services.EarthquakeQueryService;
import earthquakes.services.LocationQueryService;
import earthquakes.searches.LocSearch;

import earthquakes.entities.Location;
import earthquakes.repositories.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import earthquakes.geojson.FeatureCollection;

import com.nimbusds.oauth2.sdk.client.ClientReadRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;


@Controller
public class LocationsController {
    
    private LocationRepository locationRepository;

    @Autowired
    public LocationsController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;   
    }

    @GetMapping("/locations/search")
    public String getLocationSearch(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken,
            LocSearch locSearch) {
        return "locations/search";
    }

    @GetMapping("/locations/results")
    public String getLocationsResults(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken,
            LocSearch locSearch) {
        LocationQueryService e = new LocationQueryService();
        model.addAttribute("locSearch", locSearch);
        // TODO: Actually do the search here and add results to the model
        String json = e.getJSON(locSearch.getLocation());
        model.addAttribute("json", json);
        List<Place> place = Place.listFromJSON(json);
        model.addAttribute("place",place);
        return "locations/results";
    }

   @GetMapping("/locations")
    public String index(Model model, OAuth2AuthenticationToken token) {
        String uid = getUid(token);
        Iterable<Location> locations = locationRepository.findByUid(uid);
        model.addAttribute("locations", locations);
        return "locations/index";
    }

    @PostMapping("/locations/add")
    public String add(Location location, Model model, OAuth2AuthenticationToken token) {
      location.setUid(getUid(token));
      locationRepository.save(location);
      model.addAttribute("locations", locationRepository.findByUid(getUid(token)));
      return "locations/index";
    }

    @DeleteMapping("/locations/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
    Location location = locationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid courseoffering Id:" + id));
    locationRepository.delete(location);
    model.addAttribute("locations", locationRepository.findAll());
    return "locations/index";
    }

    public String getUid(OAuth2AuthenticationToken token){
        if (token == null) return "";
        String uid = token.getPrincipal().getAttributes().get("id").toString();
        return uid;
    }

    @GetMapping("/Admin")
    public String Admin(Model model, OAuth2AuthenticationToken token) {
        String uid = getUid(token);
        Iterable<Location> locations = locationRepository.findAll();
        model.addAttribute("locations", locations);
        return "locations/Admin";
    }



}
