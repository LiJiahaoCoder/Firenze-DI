package com.jiahao.restful.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UriResolverTest {

  @Test
  void should_get_correct_uri_when_request_uri_start_with_slash() {
    String resolve = UriResolver.resolve("/request");

    assertEquals("request", resolve);
  }

  @Test
  void should_get_correct_uri_when_request_uri_end_with_slash() {
    String resolve = UriResolver.resolve("request/id/");

    assertEquals("request/id", resolve);
  }

  @Test
  void should_get_correct_uri_when_request_uri_both_start_and_end_with_slash() {
    String resolve = UriResolver.resolve("/request/id/");

    assertEquals("request/id", resolve);
  }
}
