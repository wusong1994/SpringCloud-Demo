package com.goumang.oss.factory;

import com.goumang.oss.config.OssProperties;
import com.goumang.oss.template.impl.OssAliTemplateImpl;
import com.goumang.oss.template.impl.OssLocalTemplateImpl;
import com.goumang.core.template.OssTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Create by huangzy  on 2019/9/4
 */
@Component
public class OssTemplateFactory {

    @Autowired
    private OssProperties ossProperties;

    private OssTemplate ossTemplate;

    public OssTemplate getOssTemplate() {
        if (ossTemplate != null) return ossTemplate;
        switch (ossProperties.getType()) {
            case ali:
                ossTemplate = new OssAliTemplateImpl(ossProperties);
                break;
            case local:
                ossTemplate = new OssLocalTemplateImpl(ossProperties);
                break;
        }
        return ossTemplate;
    }
}
