package cn.anlucky.luckyadmin.utils.file;

import cn.anlucky.luckyadmin.config.LuckyConfig;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 七牛云上传工具类
 */
public class QiNiuUploadUtils {

    /**
     * 上传文件
     * @param file 文件
     * @param fileName 文件名称 不带盘符 如 "/FILE_UPLOAD/2025/04/24/a.jpg" 携带盘符会忽略目录，直接变成 "D:/FILE_UPLOAD/2025/04/24/a.jpg文件"
     * @throws IOException
     */
    public static void upload(MultipartFile file, String fileName) throws IOException {

        // 创建上传token
        Auth auth = Auth.create(LuckyConfig.getQiniuAk(), LuckyConfig.getQiniuSk());
        String upToken = auth.uploadToken(LuckyConfig.getQiniuBucket());

        // 设置上传配置，Region要与存储空间所属的存储区域保持一致
        Configuration cfg = new Configuration(Region.qvmHuabei());

        // 创建上传管理器
        UploadManager uploadManager = new UploadManager(cfg);

        // 上传文件
        Response response = uploadManager.put(file.getInputStream(), fileName, upToken, null, null);

    }

    /**
     * 获取下载地址
     * @param fileName 文件名
     * @param timeOut 下载地址过期时间 默认 3600
     * @return
     */
    public static String getDownloadUrl(String fileName,Long timeOut) {

        // 获取下载地址
        Auth auth = Auth.create(LuckyConfig.getQiniuAk(), LuckyConfig.getQiniuSk());

        // 编码
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        // 组合  url + / + 文件名
        String publicUrl = String.format("%s/%s", LuckyConfig.getQiniuDomain(), encodedFileName);
        // 链接过期时间 默认3600秒
        if (timeOut == null) {
            timeOut = 3600L; // 1小时
        }
        // 获取下载链接
        return auth.privateDownloadUrl(publicUrl, timeOut);
    }


}
