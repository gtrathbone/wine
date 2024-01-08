package rss.wine;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.ui.SpringDocUIException;
import org.springdoc.webflux.ui.SwaggerConfigResource;
import org.springdoc.webflux.ui.SwaggerIndexPageTransformer;
import org.springdoc.webflux.ui.SwaggerIndexTransformer;
import org.springdoc.webflux.ui.SwaggerWelcomeCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.resource.ResourceTransformerChain;
import org.springframework.web.reactive.resource.TransformedResource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "1.0", description = "Documentation API Gateway v1.0"))
public class GatewayMain {
  @Autowired
  SwaggerConfigResource swaggerConfigResource;

  public static void main(String[] args) {
    SpringApplication.run(GatewayMain.class, args);
  }

  @Bean
  public SwaggerIndexTransformer swaggerIndexTransformer(
    SwaggerUiConfigProperties a,
    SwaggerUiOAuthProperties b,
    SwaggerUiConfigParameters c,
    SwaggerWelcomeCommon d,
    ObjectMapperProvider e)
  {
    return new SwaggerIndexPageTransformer(a, b, c, d, e) {
      @Override
      public Mono<Resource> transform(ServerWebExchange serverWebExchange, Resource resource, ResourceTransformerChain resourceTransformerChain) {
        try {
          if (resource.toString().contains("swagger-ui.css")) {
            try (InputStream is = resource.getInputStream();
                 final InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader br = new BufferedReader(isr)) {
              final String css = br.lines().collect(Collectors.joining());
              final byte[] transformedContent = css.replace("old", "new").getBytes();

              return Mono.just(new TransformedResource(resource, transformedContent));
            } // AutoCloseable br > isr > is
          }
        }
        catch (Exception e) {
          throw new SpringDocUIException("Failed to transform Index", e);
        }
        return super.transform(serverWebExchange, resource, resourceTransformerChain);
      }
    };
  }

  @Bean
  public CommandLineRunner openApiGroups1(
    RouteDefinitionLocator locator,
    SwaggerUiConfigParameters swaggerUiParameters,
    SwaggerUiConfigProperties properties)
  {
    return args -> Objects.requireNonNull(locator.getRouteDefinitions().collectList().block())
      .stream()
      .filter(rd -> rd.getId().matches(".*-swagger"))
      .forEach(rd -> {
        var name = rd.getId().replace("-swagger", "");
        var path = name.toLowerCase(Locale.getDefault()) + "/v3/api-docs";
//        var path = rd.getUri().toString() + "/v3/api-docs";
//        swaggerUiParameters.addGroup(name);
//        var url = new AbstractSwaggerUiConfigProperties.SwaggerUrl(name, path, name);
        swaggerUiParameters.getUrls().add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(name, path, name));
        // Seems silly we need to do this, but the behaviour does not work without it.
        properties.setUrls(swaggerUiParameters.getUrls());
      });
  }

}