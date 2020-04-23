package com.it.util;

import lombok.Data;

/**
 * 〈自定义配置项〉<br>
 *
 * @author
 * @create2020/10/6 18:30
 * @since 1.0.0
 */
@Data
public class CustomConfiguration {
    /**
     * 用户密码是否加密属性
     */
    private String isEncrypted;
}
