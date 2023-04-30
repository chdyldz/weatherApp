package com.weather.weatherApp.router;

import com.weather.weatherApp.model.WeatherModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class WeatherRouther extends RouteBuilder {
    @Override
    public void configure() {

        restConfiguration()
                .component("servlet").contextPath("/")
                .host("0.0.0.0").port("4200").bindingMode(RestBindingMode.json)
                .enableCORS(true) // <-- Important
                .corsHeaderProperty("Access-Control-Allow-Origin","*")
                .corsHeaderProperty("Access-Control-Allow-Headers","Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, city, country");

        rest("/api").description("Weather api")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .get("/weather").description("Get weather by city")
                .param().name("city").type(RestParamType.header).description("The city name").dataType("string").endParam()
                .param().name("country").type(RestParamType.header).description("The country name").dataType("string").endParam()
                .to("direct:weather");

        from("direct:weather")
                .log("Get weather by city")
                .toD("weather:foo?location=${header.city},${header.country}&appid=8bb3bc2e4ef5ee975fb92992ed3e7190&units=metric").process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        JSONParser parser = new JSONParser();
                        JSONObject json = (JSONObject) parser.parse(exchange.getIn().getBody(String.class));
                        JSONObject main = (JSONObject) json.get("main");
                        JSONObject wind = (JSONObject) json.get("wind");
                        JSONObject sys = (JSONObject) json.get("sys");
                        WeatherModel weatherModel = new WeatherModel();
                        weatherModel.setHumidity(main.get("humidity").toString());
                        weatherModel.setPressure(main.get("pressure").toString());
                        weatherModel.setTemperature(main.get("temp").toString());
                        weatherModel.setFeelsLike(main.get("feels_like").toString());
                        weatherModel.setTempMax(main.get("temp_max").toString());
                        weatherModel.setTempMin(main.get("temp_min").toString());
                        weatherModel.setWindspeed(wind.get("speed").toString());
                        weatherModel.setWinddeg(wind.get("deg").toString());
                        weatherModel.setCity(json.get("name").toString());
                        weatherModel.setCountry(sys.get("country").toString());
                        exchange.getMessage().setBody(weatherModel);
                        System.out.println("body = " + json);
                    }
                })
                .log("Weather: ${body}");
    }
}

