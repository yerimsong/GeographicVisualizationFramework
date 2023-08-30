package edu.cmu.cs.cs214.hw6.framework.Geocoding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cmu.cs.cs214.hw6.framework.Coordinate;

/**
 * Class for a geocoder object that makes calls to the OpenWeather Geocoding API
 */
public class Geocoder {

    private String apiKey;

    private URI makeGeocodeUri(String placeName) throws MalformedURLException,UnsupportedEncodingException {
        String encodedParam = URLEncoder.encode(placeName, StandardCharsets.UTF_8.toString());
        return URI.create("http://api.openweathermap.org/geo/1.0/direct?q=" + encodedParam + "&limit=1&appid=" + apiKey);
    }

    /**
     * Creates a new geocoder with a given api key.  NOTE: By default free keys from OpenWeather are rate-limited to 60 calls/min
     * @param apiKey API key to {@link https://openweathermap.org}
     */
    public Geocoder(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Turns a place name into a coordinate
     *
     * @param name Should be at least place name and if there is additional information, that
     * should be in the form of {name},{state},{country}
     * @return a Coord/null of a place's coordinates
     */
    public Coordinate nameToCoord(String name) {

        try {
            URI uri = makeGeocodeUri(name);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).header("accept", "application/json").build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            List<GeocodingResponse> responses = objectMapper.readValue(
                response.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, GeocodingResponse.class)
            );

            if (responses.size() == 0) {
                throw new IllegalArgumentException("No place was found");
            }
            return new Coordinate(responses.get(0).getLon(), responses.get(0).getLat(), responses.get(0).getName());
        } catch (IOException e) {
            System.out.println("API call failed");
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            System.out.println("Geocoding deserialization failed");
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println(name + " was unable to be found");
            return null;
        }
    }

    /**
     * Turns a list of place names into a list of coordinates
     * @param names List of names
     * @param failed List that will be populated with names that fail
     * @return List of coordinates that were successfully converted
     */
    public List<Coordinate> namesToCoords(List<String> names, List<String> failed) {

        List<Coordinate> result = new ArrayList<Coordinate>();
        for (String name : names) {
            Coordinate coord = nameToCoord(name);

            if (coord != null) {
                result.add(coord);
            } else {
                failed.add(name);
            }
        }

        return result;
    }

    /**
     * Turns a list of place names into a list of coordinates
     * @param names List of names
     * @return List of coordinates that were successfully converted
     */
    public List<Coordinate> namesToCoords(List<String> names) {

        List<Coordinate> result = new ArrayList<Coordinate>();
        for (String name : names) {
            Coordinate coord = nameToCoord(name);

            if (coord != null) {
                result.add(coord);
            }
        }

        return result;
    }
}
