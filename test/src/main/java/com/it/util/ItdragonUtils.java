package com.it.util;

import com.it.config.ShiroSpringConfig;
import com.it.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 工具类
 *
 * @author itdragon
 */
@Component
public class ItdragonUtils {

    private static final String ALGORITHM_NAME = "MD5";
    private static final Integer HASH_ITERATIONS = 1024;

    /**
     * 加盐加密的策略非常多,根据实际业务来
     */
    public void entryptPassword(User user) {
        String salt = UUID.randomUUID().toString();
        String temPassword = user.getPlainPassword();
        Object md5Password = new SimpleHash(ALGORITHM_NAME, temPassword, ByteSource.Util.bytes(salt), HASH_ITERATIONS);
        user.setSalt(salt);
        user.setPassword(md5Password.toString());
    }

    /**
     * 加盐加密的策略非常多,根据实际业务来
     */
    public static boolean isEncrypted() {
        ShiroSpringConfig shiroSpringConfig = new ShiroSpringConfig();
        CustomConfiguration customConfiguration = shiroSpringConfig.setIsEncrypted();
        if ("yes".equals(customConfiguration.getIsEncrypted())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取ShiroSession
     *
     * @return
     */
    public Session getShiroSession() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return session;
    }

    /**
     * 获取Session用户信息
     *
     * @return
     */
    public User getSessionUser() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user;
    }

    public String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) { //有可能是负数
            hashCodeV = -hashCodeV;
        }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    /**
     * 判断用户是否登录
     */
    public boolean isGogin() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isAuthenticated();
    }

    public static boolean stringIsNotBlack(String string) {
        if (string != null && !"".equals(string)) {
            return true;
        }
        return false;
    }

    private int compare(String str, String target) {
        int d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) {                       // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {                       // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) {                       // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    private int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     */

    public float getSimilarityRatio(String str, String target) {
        return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());
    }


}
