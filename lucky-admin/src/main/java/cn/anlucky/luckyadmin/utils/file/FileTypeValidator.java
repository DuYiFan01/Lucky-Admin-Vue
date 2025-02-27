package cn.anlucky.luckyadmin.utils.file;

import cn.anlucky.luckyadmin.exception.CustomException;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Set;

public class FileTypeValidator {
    private static final DefaultDetector DETECTOR = new DefaultDetector();

    // 预定义常用文件类型MIME集合
    public static final Set<String> IMAGE_MIME_TYPES = Set.of(
        "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp"
    );

    public static final Set<String> DOCUMENT_MIME_TYPES = Set.of(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    public static final Set<String> VIDEO_MIME_TYPES = Set.of(
        "video/mp4", "video/quicktime", "video/x-msvideo", "video/x-matroska"
    );

    public static final Set<String> AUDIO_MIME_TYPES = Set.of(
        "audio/mpeg", "audio/wav", "audio/ogg", "audio/webm"
    );

    /**
     * 通用文件类型校验方法
     * @param file 上传的文件
     * @param allowedMimeTypes 允许的MIME类型集合
     * @return 是否符合要求
     */
    public static boolean validateFileType(MultipartFile file, Set<String> allowedMimeTypes) {
        if (file == null || file.isEmpty()) return false;
        try (TikaInputStream tikaStream = TikaInputStream.get(file.getInputStream())) {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getOriginalFilename());
            MediaType mediaType = DETECTOR.detect(tikaStream, metadata);
            return allowedMimeTypes.contains(mediaType.toString());
        } catch (IOException e) {
            throw new CustomException("调用方法validateFileType失败，原因："+e.getMessage());
        }
    }

    /**
     * 快速校验方法 - 图片类型
     */
    public static boolean isImage(MultipartFile file) {
        boolean b = validateFileType(file, IMAGE_MIME_TYPES);
        if (!b){
            throw new CustomException("上传文件类型错误，请上传图片");
        }
        return true;
    }

    /**
     * 快速校验方法 - 文档类型
     */
    public static boolean isDocument(MultipartFile file) throws IOException {
        return validateFileType(file, DOCUMENT_MIME_TYPES);
    }

    /**
     * 快速校验方法 - 视频类型
     */
    public static boolean isVideo(MultipartFile file) throws IOException {
        return validateFileType(file, VIDEO_MIME_TYPES);
    }

    /**
     * 基于扩展名的快速校验（辅助方法）
     */
    public static boolean validateByExtension(MultipartFile file, Set<String> allowedExtensions) {
        if (file == null || file.isEmpty()) return false;

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) return false;

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1)
                                   .toLowerCase();
        return allowedExtensions.contains(extension);
    }
}
