package com.jiahao.restful.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UriHelperTest {

  @Test
  void should_get_correct_uri_when_request_uri_start_with_slash() {
    String resolve = UriHelper.normalize("/request");

    assertEquals("request", resolve);
  }

  @Test
  void should_get_correct_uri_when_request_uri_end_with_slash() {
    String resolve = UriHelper.normalize("request/id/");

    assertEquals("request/id", resolve);
  }

  @Test
  void should_get_correct_uri_when_request_uri_both_start_and_end_with_slash() {
    String resolve = UriHelper.normalize("/request/id/");

    assertEquals("request/id", resolve);
  }

  @Test
  void should_get_true_when_match_given_single_param_in_path() {
    boolean match = UriHelper.match("/students/1", "/students/{id}");

    assertTrue(match);
  }

  @Test
  void should_get_true_when_match_given_two_params_in_path() {
    boolean match = UriHelper.match("/students/1/details/2", "/students/{id}/details/{chapter}");

    assertTrue(match);
  }

  @Test
  void should_get_false_when_match_given_different_uri_and_target() {
    boolean match = UriHelper.match("/students/1", "/students/{id}/details/{chapter}");

    assertFalse(match);
  }

  @Test
  void should_get_uri_when_parse_given_single_param_in_path() {
    Object[] parse = UriHelper.parse("/students/1", "/students/{id}");

    assertEquals("1", parse[0]);
  }

  @Test
  void should_get_uri_when_parse_given_two_params_in_path() {
    Object[] parse = UriHelper.parse("/students/1/details/2", "/students/{id}/details/{chapter}");

    assertEquals("1", parse[0]);
    assertEquals("2", parse[1]);
  }
}
