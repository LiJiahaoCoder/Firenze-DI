package com.jiahao.di.utils;

import com.jiahao.di.exceptions.CreateException;

import java.util.Stack;
import java.util.stream.IntStream;

public class CircularDependencyGuard {
  private final Stack<Class<?>> stack = new Stack<>();

  public void start(Class<?> clazz) {
    check(clazz);
    stack.push(clazz);
  }

  public void end() {
    stack.pop();
  }

  private void check(Class<?> clazz) {
    IntStream.range(0, stack.size())
            .filter(index -> stack.get(index).equals(clazz))
            .findFirst()
            .ifPresent(
                    index -> {
                      throw new CreateException("Existing circular dependency while creating " + stack.get(index).getSimpleName());
                    }
            );
  }
}
