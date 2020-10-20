package com.goumang.core.template;

import java.text.DecimalFormat;

/**
 * By huang.rb on 2020/1/2
 */
public class OssFilePo extends OssPo{

    /** 文件类型 */
    private String contentType;
    /** 文件大小 */
    private long size;
    /** 格式化大小 */
    private String formatSize;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFormatSize() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (size < 1024) {
            formatSize = df.format((double) size) + "B";
        } else if (size < 1048576) {
            formatSize = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            formatSize = df.format((double) size / 1048576) + "M";
        } else {
            formatSize = df.format((double) size / 1073741824) + "G";
        }
        return formatSize;
    }

}
