package rss.wine.controller;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rss.wine.dto.OpenIdConnectConfig;
import rss.wine.dto.TokenRequest;
import rss.wine.dto.TokenRequestResponse;
import rss.wine.service.TokenManager;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
  @Autowired
  TokenManager tokenManager;

  @GetMapping("/.well-known/openid-configuration")
  @ResponseStatus(HttpStatus.OK)
  public OpenIdConnectConfig getConfig() {
    return tokenManager.getWellKnownConfig();
  }

  @GetMapping("/oauth2/protocol/openid-connect/certs")
  @ResponseStatus(HttpStatus.OK)
  public Map<String, Object> getKeys() {
    return tokenManager.getKeys();
  }

  @PostMapping("/oauth2/protocol/openid-connect/token")
  @ResponseStatus(HttpStatus.OK)
  public TokenRequestResponse getToken(@RequestBody TokenRequest request) {
    return tokenManager.getToken(request);
  }
}
