package rss.wine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenRequest {
  String client_id;
  String client_secret;
  String grant_type;
}
