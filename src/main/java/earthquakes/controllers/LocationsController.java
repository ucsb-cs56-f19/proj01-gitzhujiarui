package earthquakes.controllers;

import earthquakes.osm.Place;
// import earthquakes.osm.PlaceCollection;
import earthquakes.services.EarthquakeQueryService;
import earthquakes.services.LocationQueryService;
// import earthquakes.searches.EqSearch;
import earthquakes.searches.LocSearch;

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

import earthquakes.geojson.FeatureCollection;

import com.nimbusds.oauth2.sdk.client.ClientReadRequest;

@Controller
public class LocationsController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    // @GetMapping("/")
    // public String getHomepage(Model model, OAuth2AuthenticationToken
    // oAuth2AuthenticationToken) {
    // return "index";
    // }

    // @GetMapping("/login")
    // public String getLoginPage(Model model, OAuth2AuthenticationToken
    // oAuth2AuthenticationToken) {

    // Map<String, String> urls = new HashMap<>();

    // // get around an unfortunate limitation of the API
    // @SuppressWarnings("unchecked") Iterable<ClientRegistration> iterable =
    // ((Iterable<ClientRegistration>) clientRegistrationRepository);
    // iterable.forEach(clientRegistration ->
    // urls.put(clientRegistration.getClientName(),
    // "/oauth2/authorization/" + clientRegistration.getRegistrationId()));

    // model.addAttribute("urls", urls);
    // return "login";
    // }

    @GetMapping("/locations/search")
    public String getLocationSearch(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken,
            LocSearch locSearch) {
        return "locations/search";
    }

    // @GetMapping("/locations/results")
    // public String getLocationResult(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken,
    //         LocSearch locSearch) {
    //     LocationQueryService e = new LocationQueryService();
    //     model.addAttribute("locSearch", locSearch);
    //     String json = e.getJSON(locSearch.getLocation());
    //     model.addAttribute("json", json);
    //     List<PlaceCollection> placeCollection = PlaceCollection.listFromJSON(json);
    //     model.addAttribute("placeCollection",placeCollection);
    //     return "locations/results";
    // }

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


}
