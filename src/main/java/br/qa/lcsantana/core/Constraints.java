package br.qa.lcsantana.core;

import io.restassured.http.ContentType;

public interface Constraints {

    String APP_BASE_URL = "https://serverest.dev";
    ContentType CONTENT_TYPE = ContentType.JSON;
    Long MAX_TIMEOUT = 5000L;
}
