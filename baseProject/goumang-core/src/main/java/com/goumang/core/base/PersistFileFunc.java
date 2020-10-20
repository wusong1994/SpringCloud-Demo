package com.goumang.core.base;

public interface PersistFileFunc<T, U> {

    int applyAsInt(T t, U... u);
}
