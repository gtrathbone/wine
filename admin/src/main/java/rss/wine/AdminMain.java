package rss.wine;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Admin", version = "1.0",
                                description = "Documentation API Admin v1.0"),
                                servers = {@Server(url="https://localhost:8081")})
public class AdminMain {
  public static void main(String[] args) {
    SpringApplication.run(AdminMain.class, args);
  }

  //@Bean
  public RestTemplate restTemplate(@Value("${client.keystore:}") String keystore,
                                   @Value("${client.keystore_password:}") String keystorePassword) {
    try {
      SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
      File f = new File(keystore);
      sslContextBuilder.loadKeyMaterial(f, keystorePassword.toCharArray(), keystorePassword.toCharArray());
      sslContextBuilder.build();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}