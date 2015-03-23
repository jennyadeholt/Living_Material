package com.jd.living.server;

import com.jd.living.model.ListingsResult;
import com.jd.living.model.SoldResult;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Rest(rootUrl = "http://api.booli.se", converters = { GsonHttpMessageConverter.class })
@Accept(MediaType.APPLICATION_JSON)
public interface BooliClient {

    @Get("/listings?q={search}&maxListPrice={maxA}&minListPrice={minA}&minRooms={minR}&maxRooms={maxR}&objectType={objectType}&isNewConstruction={isNew}&&maxRent={rent}&limit={limit}&minPublished={minPublished}&" + BooliServer.common)
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<ListingsResult> getListings(String search, String callerId, long time,
                                               String unique, String hash,
                                               String minR, String maxR,
                                               String objectType,
                                               String minA, String maxA,
                                               String isNew, String rent, int limit, String minPublished);

    @Get("/listings/{booliId}?" + BooliServer.common)
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<ListingsResult> getListing(int booliId, String callerId, long time,
                                              String unique, String hash);

    @Get("/sold?q={search}&maxListPrice={maxA}&minListPrice={minA}&minRooms={minR}&maxRooms={maxR}&objectType={objectType}&isNewConstruction={isNew}&maxRent={rent}&limit={limit}&" + BooliServer.common)
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<SoldResult> getObjectsSold(String search, String callerId, long time,
                                              String unique, String hash,
                                              String minR, String maxR,
                                              String objectType,
                                              String minA, String maxA,
                                              String isNew, String rent, int limit);

    RestTemplate getRestTemplate();

    void setRestTemplate(RestTemplate restTemplate);
}

