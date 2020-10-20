package com.goumang.core.base;

import java.util.List;

/**
 * By huang.rb on 2019/7/23
 */
public interface PageFunc<P,R> {
    List<R> excute(P p);
}
