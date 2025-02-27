package cn.anlucky.luckyadmin.utils.file;

import cn.anlucky.luckyadmin.config.LuckyConfig;
import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.constant.Constants;
import cn.anlucky.luckyadmin.system.enums.FileBusinessType;
import cn.anlucky.luckyadmin.system.pojo.SysFiles;
import cn.anlucky.luckyadmin.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * 文件上传工具类
 */
public class FileUploadUtils {


    /**
     * 文件上传
     *
     * @param file             文件对象
     * @param fileBusinessType 枚举类业务类型
     * @return 文件路径
     * @throws IOException 异常
     */
    public static final SysFiles uploadFile(MultipartFile file, FileBusinessType fileBusinessType) {
        // 校验文件名长度
        FileCheckUtils.checkFileNameLength(file);

        // 校验文件大小
        FileCheckUtils.checkFileSize(file);

        // 格式化文件名
        String fileName = FileCheckUtils.formatFileName(file, fileBusinessType);

        // 获取文件完整存储路径
        String absolutePath = getAbsoluteFile(LuckyConfig.getProfile(), fileName).getAbsolutePath();

        try {
            // 本地文件上传
            if (StringUtils.isBlank(LuckyConfig.getFileLocation()) || Constants.FILE_LOCATION_LOCAL.equalsIgnoreCase(LuckyConfig.getFileLocation())){
                file.transferTo(Paths.get(absolutePath));
            }
            // 七牛文件上传
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("Funcation FileUploadUtils.uploadFile() Error,Message:" + e.getMessage());
        }
        SysFiles sysFiles = new SysFiles();
        // 原始文件名称
        sysFiles.setOriginalName(file.getOriginalFilename());
        // 现在文件名称
        sysFiles.setFileName(fileName);
        // 完整路径
        sysFiles.setStoragePath(getPathFileName(absolutePath));
        // 文件hash值
        sysFiles.setFileHash(FileCheckUtils.getFileHash(file));
        // 文件类型
        sysFiles.setFileType(FileCheckUtils.getFileLastName(file));
        // 业务类型
        sysFiles.setFileBusinessType(fileBusinessType.getBusinessType());
        // 文件大小
        sysFiles.setFileSize(file.getSize());
        return sysFiles;
    }


    /**
     * 生成文件
     *
     * @param uploadDir
     * @param fileName
     * @return
     * @throws IOException
     */
    private static final File getAbsoluteFile(String uploadDir, String fileName){
        File desc = new File(uploadDir + File.separator + fileName);
        if (!desc.exists()) {
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    public static final String getPathFileName(String uploadDir){
        int dirLastIndex = LuckyConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        return Constants.RESOURCE_PREFIX + "/" + currentDir;
    }


}
