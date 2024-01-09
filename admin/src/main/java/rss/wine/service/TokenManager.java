package rss.wine.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rss.wine.dto.OpenIdConnectConfig;
import rss.wine.dto.TokenRequest;
import rss.wine.dto.TokenRequestResponse;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenManager {
  @Value("${openid.base_path}")
  String baseUri;
  @Value("${openid.token_duration:0}")
  Duration tokenDuration;
  ECKey jwk;

  public TokenManager() {
    try {
      jwk = new ECKeyGenerator(Curve.P_256)
        .keyUse(KeyUse.SIGNATURE) // indicate the intended use of the key (optional)
        .keyID(UUID.randomUUID().toString()) // give the key a unique ID (optional)
        .issueTime(new Date()) // issued-at timestamp (optional)
        .generate();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  public OpenIdConnectConfig getWellKnownConfig() {
    OpenIdConnectConfig config = new OpenIdConnectConfig();
    config.setIssuer(baseUri);
    //config.setAuthorizationEndpoint(baseUri + "/oauth2/protocol/openid-connect/auth");
    config.setTokenEndpoint(baseUri + "/oauth2/protocol/openid-connect/token");
    //config.setUserInfoEndpoint(baseUri + "/oauth2/protocol/openid-connect/userinfo");
    config.setJwksUri(baseUri + "/oauth2/protocol/openid-connect/certs");
    return  config;
  }

  public TokenRequestResponse getToken(TokenRequest request) {
    JWT accessToken = createToken(request);
    TokenRequestResponse tokens = new TokenRequestResponse();
    tokens.setAccess_token(accessToken.serialize());
    tokens.setToken_type("Bearer");
    tokens.setExpires_in(tokenDuration.getSeconds());
    return tokens;
  }

  JWTClaimsSet getClaims(TokenRequest request) {
    Date now = new Date();
    return new JWTClaimsSet.Builder()
      .audience("wine")
      .issuer(baseUri)
      .issueTime(now)
      .notBeforeTime(now)
      .expirationTime(Date.from(now.toInstant().plus(tokenDuration)))
      .jwtID(UUID.randomUUID().toString())
      .subject("subject")
      .claim("a_claim", Arrays.asList("a", "b"))
      .build();
  }

  JWT createToken(TokenRequest request) {
    SignedJWT token = null;
    try {
      var header = new JWSHeader.Builder(JWSAlgorithm.ES256)
        .keyID(jwk.getKeyID())
        .build();

      token = new SignedJWT(header, getClaims(request));
      token.sign(new ECDSASigner(jwk));
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
    return token;
  }

  public Map<String, Object> getKeys() {
    var keyset = new JWKSet(Collections.singletonList(jwk));
    return keyset.toJSONObject(true);
  }
}
