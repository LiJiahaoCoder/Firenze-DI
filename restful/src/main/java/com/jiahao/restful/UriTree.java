package com.jiahao.restful;

import io.netty.handler.codec.http.HttpMethod;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UriTree {

  private final ArrayList<Triplet<String, HttpMethod, Class<?>>> table;

  private static UriTree uriTree;

  private UriTree(ArrayList<Triplet<String, HttpMethod, Class<?>>> table) {
    this.table = table;
  }

  public static UriTree getInstance() {
    if (Objects.isNull(uriTree)) {
      uriTree = new UriTree(new ArrayList());
    }
    return uriTree;
  }

  public List<Triplet<String, HttpMethod, Class<?>>> getTable() {
    return table;
  }

  public void add(Triplet<String, HttpMethod, Class<?>> value) {
    table.add(value);
  }

  public Triplet<String, HttpMethod, Class<?>> get(int index) {
    return table.get(index);
  }
}
