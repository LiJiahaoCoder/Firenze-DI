package com.jiahao.restful;

import org.javatuples.Quartet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UriTable {

  private final ArrayList<Quartet<String, Class<? extends Annotation>, Method, Class<?>>> table;

  private static UriTable uriTable;

  private UriTable(ArrayList<Quartet<String, Class<? extends Annotation>, Method, Class<?>>> table) {
    this.table = table;
  }

  public static UriTable getUriTable() {
    if (Objects.isNull(uriTable)) {
      uriTable = new UriTable(new ArrayList());
    }
    return uriTable;
  }

  public List<Quartet<String, Class<? extends Annotation>, Method, Class<?>>> getTable() {
    return table;
  }

  public void add(Quartet<String, Class<? extends Annotation>, Method, Class<?>> value) {
    table.add(value);
  }

  public Quartet<String, Class<? extends Annotation>, Method, Class<?>> get(int index) {
    return table.get(index);
  }
}
