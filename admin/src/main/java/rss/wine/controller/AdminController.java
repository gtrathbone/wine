package rss.wine.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public String root() {
/*
    try {
      ECKey jwk = new ECKeyGenerator(Curve.P_256)
        .keyUse(KeyUse.SIGNATURE) // indicate the intended use of the key (optional)
        .keyID(UUID.randomUUID().toString()) // give the key a unique ID (optional)
        .issueTime(new Date()) // issued-at timestamp (optional)
        .generate();

// Output the private and public EC JWK parameters
      System.out.println(jwk);

// Output the public EC JWK parameters only
      System.out.println(jwk.toPublicJWK());

      // https://connect2id.com/products/nimbus-jose-jwt/examples
      RSAKey rsaJWK = new RSAKeyGenerator(2048)
        .keyUse(KeyUse.SIGNATURE)
        .keyID(UUID.randomUUID().toString())
        .generate();
      RSAKey rsaPublicJWK = rsaJWK.toPublicJWK();
//      JWSSigner signer = new RSASSASigner(rsaJWK);

      //var header = new JWSHeader.Builder(JWSAlgorithm.RS256)
      var header = new JWSHeader.Builder(JWSAlgorithm.ES256)
        .keyID(rsaJWK.getKeyID())
        .build();

      Date now = new Date();
      JWTClaimsSet claims = new JWTClaimsSet.Builder()
        .audience("wine")
        .issuer("me")
        .issueTime(now)
        .notBeforeTime(now)
        .expirationTime(Date.from(now.toInstant().plus(5, ChronoUnit.MINUTES)))
        .jwtID(UUID.randomUUID().toString())
        .subject("subject")
        .claim("a_claim", Arrays.asList("a", "b"))
        .build();

      var jwt = new SignedJWT(header, claims);
      //jwt.sign(new RSASSASigner(rsaJWK));
      jwt.sign(new ECDSASigner(jwk));
      String token = jwt.serialize();
      token.toString();

      var keyset = new JWKSet(Arrays.asList(jwk.toPublicJWK(), rsaJWK.toPublicJWK()));

      try {
        var parsedJwt = JWTParser.parse(token);
        parsedJwt.getHeader();

        if (parsedJwt.getHeader() instanceof JWSHeader) {
          JWSHeader h = (JWSHeader) parsedJwt.getHeader();
          List<JWK> matches1 = new JWKSelector(JWKMatcher.forJWSHeader(((SignedJWT) parsedJwt).getHeader())).select(keyset);
          List<JWK> matches2 = new JWKSelector(
            new JWKMatcher.Builder()
              .keyID(jwk.getKeyID())
              .build())
            .select(keyset);

          if (((SignedJWT) parsedJwt).verify(new ECDSAVerifier(jwk.toECPublicKey()))) {
            System.out.println("valid");
          }

        }

        List<JWK> matches1 = new JWKSelector(JWKMatcher.forJWSHeader(((SignedJWT) parsedJwt).getHeader())).select(keyset);
        List<JWK> matches2 = new JWKSelector(
          new JWKMatcher.Builder()
            .keyType(jwk.getKeyType())
            .keyID(jwk.getKeyID())
            .build())
          .select(keyset);

        if (((SignedJWT) parsedJwt).verify(new ECDSAVerifier(jwk.toECPublicKey()))) {
          System.out.println("valid");
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
*/

    return "Admin ok";
  }
}
