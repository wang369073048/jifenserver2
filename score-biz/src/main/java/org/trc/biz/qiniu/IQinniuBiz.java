package org.trc.biz.qiniu;

import org.trc.form.FileUrl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by wzhen
 */
public interface IQinniuBiz {
    /**
     * 上传
     * @param inputStream 文件流
     * @param fileName 文件名称
     * @param module 系统模块
     * @return String 文件路径
     * @throws Exception
     */
    String upload(InputStream inputStream, String fileName, String module) throws Exception;

    /**
     * 下载
     * @param fileName
     * @return
     */
    String download(String fileName) throws Exception;

    /**
     * 获取缩略图地址
     * @param fileName 文件名称
     * @param width 缩略图的宽
     * @param height 缩略图的高
     * @return
     * @throws Exception
     */
    String getThumbnail(String fileName, int width, int height) throws Exception;

    /**
     * 批量获取多个文件的url
     * @param fileNames 文件名称数组
     * @param thumbnail 是否带缩略:0-否,1-是
     * @return
     * @throws Exception
     */
    List<FileUrl> batchGetFileUrl(String[] fileNames, String thumbnail) throws Exception;

    /**
     * 批量删除
     * @param fileNames
     * @param  module 系统模块
     * @return map,{success:成功数,fialure:失败数,msg:错误信息}
     * @throws Exception
     */
    Map<String, Object> batchDelete(String[] fileNames, String module) throws Exception;

}
