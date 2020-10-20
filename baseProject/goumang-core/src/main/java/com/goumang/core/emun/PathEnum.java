package com.goumang.core.emun;

/**
 * By huang.rb on 2019/9/19
 */
public enum PathEnum {

    VISITOR,
    PERM,
    TEMP;

    public String getPath() {
        return this.name().toLowerCase();
    }

}

