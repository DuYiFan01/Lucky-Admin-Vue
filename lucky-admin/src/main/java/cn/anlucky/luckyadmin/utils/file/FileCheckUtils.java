package cn.anlucky.luckyadmin.utils.file;

import cn.anlucky.luckyadmin.config.LuckyConfig;
import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.constant.Constants;
import cn.anlucky.luckyadmin.system.enums.FileBusinessType;
import cn.anlucky.luckyadmin.utils.DateUtils;
import cn.anlucky.luckyadmin.utils.StringUtils;
import cn.anlucky.luckyadmin.utils.uuid.Seq;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Objects;

public class FileCheckUtils {

    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    /**
     * 校验文件名长度
     *
     * @param file
     */
    public static void checkFileNameLength(MultipartFile file) {
        // 文件名长度获取，若不存在文件名 则抛出 NullPointerException
        int fileNamelength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNamelength > LuckyConfig.getFileNameMaxLength()) {
            throw new CustomException("文件名长度不能超过" + LuckyConfig.getFileNameMaxLength() + "个字符");
        }
    }

    /**
     * 校验文件大小
     *
     * @param file
     */
    public static void checkFileSize(MultipartFile file) {
        // 获取文件大小
        long size = file.getSize();
        if (size > LuckyConfig.getFileMaxSize()) {
            throw new CustomException("文件上传最大不可超过" + LuckyConfig.getFileMaxSize() / 1024 / 1024 + "MB");
        }
    }


    /**
     * 文件名格式化
     *
     * @param file
     * @param fileBusinessType
     * @return
     */
    public static final String formatFileName(MultipartFile file, FileBusinessType fileBusinessType) {
        String fileName = StringUtils.format("{}/{}/{}_{}.{}", fileBusinessType.getBusinessType(), DateUtils.datePath(),
                FilenameUtils.getBaseName(file.getOriginalFilename()), Seq.getId(Seq.uploadSeqType), getFileLastName(file));
        return fileName;
    }


    /**
     * 获取文件后缀
     *
     * @param file
     * @return
     */
    public static final String getFileLastName(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

    /**
     * 根据文件类型获取后缀
     *
     * @param prefix
     * @return
     */
    private static String getExtension(String prefix) {
        switch (prefix) {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }

    /**
     * 计算文件的哈希值 SHA256
     * @param file
     * @return
     */
    public static String getFileHash(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            MessageDigest digest = MessageDigest.getInstance(Constants.SHA256);

            byte[] buffer = new byte[8192]; // 使用8KB缓冲区
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            return bytesToHex(digest.digest());
        } catch (Exception e) {
            throw new CustomException("计算哈希值失败"+e.getMessage());
        }
    }

    // 字节数组转十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
