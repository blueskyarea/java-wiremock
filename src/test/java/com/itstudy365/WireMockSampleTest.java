package com.itstudy365;

import static org.assertj.core.api.Assertions.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@DisplayName("WireMock Sample Test with JUnit5")
public class WireMockSampleTest {
	private WireMockServer server;

	@BeforeEach
	void setup() {
		server = new WireMockServer(8089);
		server.start();
	}
	
	@AfterEach
	void tearDown() {
		server.stop();
	}
	
	@Test
	@DisplayName("Normal test with wiremock")
	public void NormalTest() throws UnirestException {
		server.stubFor(get(urlEqualTo("/mock/sample"))
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("Normal")));
		
		HttpResponse<String> res = Unirest.get("http://localhost:8089/mock/sample")
				.header("Content-Type", "application/json")
				.asString();
		
		assertThat(res.getStatus()).isEqualTo(200);
		assertThat(res.getBody()).isEqualTo("Normal");
		
		server.verify(getRequestedFor(urlEqualTo("/mock/sample"))
				.withHeader("Content-Type", equalTo("application/json")));
	}
	
	@Test
	@DisplayName("Abnormal test with wiremock")
	public void AbnormalTest() throws UnirestException {
		server.stubFor(get(urlEqualTo("/mock/sample"))
				.willReturn(
						aResponse()
						.withStatus(503)
						.withHeader("Content-Type", "application/json")
						.withBody("Abnormal")));
		
		HttpResponse<String> res = Unirest.get("http://localhost:8089/mock/sample")
				.header("Content-Type", "application/json")
				.asString();
		
		assertThat(res.getStatus()).isEqualTo(503);
		assertThat(res.getBody()).isEqualTo("Abnormal");
		
		server.verify(getRequestedFor(urlEqualTo("/mock/sample"))
				.withHeader("Content-Type", equalTo("application/json")));
	}
	
	@Test
	@DisplayName("Normal test with wiremock via mapping file")
	public void NormalTestViaMapping() throws UnirestException {		
		HttpResponse<String> res = Unirest.get("http://localhost:8089/mock/sample/1")
				.header("Content-Type", "application/json")
				.asString();
		
		assertThat(res.getStatus()).isEqualTo(200);
		assertThat(res.getBody()).isEqualTo("{\"name\": \"Ken\", \"age\": 20}");
		
		server.verify(getRequestedFor(urlEqualTo("/mock/sample/1"))
				.withHeader("Content-Type", equalTo("application/json")));
	}
	
	@Test
	@DisplayName("Normal test with wiremock via bodyFileName")
	public void NormalTestViaBodyFileName() throws UnirestException {		
		HttpResponse<String> res = Unirest.get("http://localhost:8089/mock/sample/2")
				.header("Content-Type", "application/json")
				.asString();
		
		assertThat(res.getStatus()).isEqualTo(200);
		assertThat(res.getBody()).isEqualTo("{\"name\": \"Ami\", \"age\": 18}");
		
		server.verify(getRequestedFor(urlEqualTo("/mock/sample/2"))
				.withHeader("Content-Type", equalTo("application/json")));
	}
}
