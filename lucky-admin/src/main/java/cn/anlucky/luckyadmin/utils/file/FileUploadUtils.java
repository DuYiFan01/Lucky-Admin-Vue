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
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件上传工具类
 */
public class FileUploadUtils {

    /**
     * 是否是删除文件 否则 将文件移入到回收站文件夹
     */
    private static final boolean isDeleteFile = false;

    /**
     * 被删除的文件夹
     */
    private static final String deleteFilesDir = "/deleteFiles";

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
        String absolutePath = getAbsoluteFile(LuckyConfig.getProfile(), fileName).getPath();

        try {
            // LuckyConfig.getFileLocation() 未配置，默认本地文件上传
            if (StringUtils.isBlank(LuckyConfig.getFileLocation()) || Constants.FILE_LOCATION_LOCAL.equalsIgnoreCase(LuckyConfig.getFileLocation())){
                file.transferTo(Paths.get(absolutePath));
            }
            // 七牛文件上传
            if (Constants.FILE_LOCATION_QINIU.equalsIgnoreCase(LuckyConfig.getFileLocation())){
                QiNiuUploadUtils.upload(file,fileName);
            }
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
     * 删除文件
     * @param sysFiles
     */
    public static final void removeFile(SysFiles sysFiles){
        // 删除文件或移动文件
        if (isDeleteFile){
            // 删除文件
            deleteFile(sysFiles);
        }else {
            // 移动文件
            moveFile(sysFiles);
        }
    }

    /**
     * 删除文件
     * @param sysFiles
     */
    private static void deleteFile(SysFiles sysFiles){
        if (Constants.FILE_LOCATION_LOCAL.equalsIgnoreCase(LuckyConfig.getFileLocation())){
            // 本地文件删除
            try {
                Files.delete(Paths.get(LuckyConfig.getProfile() + "/" +sysFiles.getFileName()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Funcation FileUploadUtils.deleteFile() Error,Message:" + e.getMessage());
            }
        } else if (Constants.FILE_LOCATION_QINIU.equalsIgnoreCase(LuckyConfig.getFileLocation())) {
            // 七牛文件删除
        }
    }

    /**
     * 移动文件
     * @param sysFiles
     */
    private static void moveFile(SysFiles sysFiles){
        if (Constants.FILE_LOCATION_LOCAL.equalsIgnoreCase(LuckyConfig.getFileLocation())){
            // 本地文件移动
            // 获取文件完整存储路径
            String absolutePath = getAbsoluteFile(LuckyConfig.getProfile()+"/"+deleteFilesDir, sysFiles.getFileName()).getAbsolutePath();
            try {
                Files.move(Paths.get(LuckyConfig.getProfile() + "/" +sysFiles.getFileName()), Paths.get(absolutePath));
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Funcation FileUploadUtils.moveFile() Error,Message:" + e.getMessage());
            }
        } else if (Constants.FILE_LOCATION_QINIU.equalsIgnoreCase(LuckyConfig.getFileLocation())) {
            // 七牛文件移动
        }
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
        return getPathFilePrefix() + "/" + currentDir;
    }
    public static final String getPathFilePrefix(){
        // 默认文件上传，本地文件上传
        if (StringUtils.isBlank(LuckyConfig.getFileLocation()) || Constants.FILE_LOCATION_LOCAL.equalsIgnoreCase(LuckyConfig.getFileLocation())){
            return Constants.RESOURCE_PREFIX;
        }
        // 七牛上传文件映射前缀
        if (Constants.FILE_LOCATION_QINIU.equalsIgnoreCase(LuckyConfig.getFileLocation())){
            return Constants.RESOURCE_QINIU;
        }
        return Constants.RESOURCE_PREFIX;
    }

}
