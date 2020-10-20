package com.goumang.core.template;

import java.util.Date;

/**
 * By huang.rb on 2020/1/2
 */
public class OssPo {

    /** 名称 */
    private String name;
    /** 相对路径 */
    private String path;
    /** 最后修改时间 */
    private Date lastModified;
    /** 是否目录 */
    private boolean directory;
    /** 是否叶子节点 */
    private boolean leaf;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
