package org.trc.resource.pagehome;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.qiniu.IQinniuBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.pagehome.Banner;
import org.trc.form.FileUrl;
import org.trc.form.qiniu.UploadResponse;
import org.trc.util.AppResult;
import org.trc.util.AssertUtil;
import org.trc.util.ResultUtil;
import org.trc.util.TxJerseyTools;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;

/**
 * Created by hzwdx on 2017/5/3.
 */
@Component
@Path(ScoreAdminConstants.Route.QinNiu.ROOT)
public class QiniuResource {

    private Logger  log = LoggerFactory.getLogger(QiniuResource.class);

    //逗号
    private static final String DOU_HAO = ",";
    //缩略图宽度
    private static final int WIDTH = 150;
    //缩略图高度
    private static final int HEIGHT = 150;

    @Autowired
    private IQinniuBiz qinniuBiz;

    /**
     * 文件上传
     * @param fileInputStream
     * @param disposition
     * @return
     */
    @POST
    @Path(ScoreAdminConstants.Route.QinNiu.UPLOAD+"/{module}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormDataParam("Filedata") InputStream fileInputStream,
                                 @FormDataParam("Filedata") FormDataContentDisposition disposition,
                                 @PathParam("module") String module, @FormDataParam("fileName") String fileName) throws Exception {
        UploadResponse uploadResponse = new UploadResponse(true);
        try{
            AssertUtil.notBlank(disposition.getFileName(), "上传文件名称不能为空");
            AssertUtil.notBlank(fileName, "上传文件名称不能为空");
            String fileExt = fileName.split("\\"+ScoreAdminConstants.Symbol.FILE_NAME_SPLIT)[1];
            String newFileName = String.format("%s%s%s", String.valueOf(System.nanoTime()), ScoreAdminConstants.Symbol.FILE_NAME_SPLIT, fileExt);
            String key = qinniuBiz.upload(fileInputStream, newFileName, module);
            uploadResponse.setKey(key);
            uploadResponse.setFileName(fileName);
            //获取图片缩略图url
            //String url = qinniuBiz.getThumbnail(key, WIDTH, HEIGHT);
            uploadResponse.setUrl(key);
        }catch (Exception e){
            String msg = String.format("%s%s%s%s", "上传文件", fileName, "异常,异常信息：",e.getMessage());
            log.error(msg, e);
            uploadResponse.setSuccess(false);
            uploadResponse.setErrorMsg(msg);
        }
//        return uploadResponse;
        return TxJerseyTools.returnSuccess(JSON.toJSONString(uploadResponse));
    }

    @GET
    @Path(ScoreAdminConstants.Route.QinNiu.DOWNLOAD)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response download(@QueryParam("fileName") String fileName) throws Exception {
//       return ResultUtil.createSucssAppResult("下载成功", qinniuBiz.download(fileName));
       String result = qinniuBiz.download(fileName);
       if(result!=null){
    	   return TxJerseyTools.returnSuccess(JSON.toJSONString(result));
       }
       return TxJerseyTools.returnSuccess();
    }

    /**
     * 缩略图
     * @return
     * @throws Exception
     */
    @GET
    @Path(ScoreAdminConstants.Route.QinNiu.THUMBNAIL)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response thumbnail(@QueryParam("fileName") String fileName, @QueryParam("width") Integer width, @QueryParam("height") Integer height) throws Exception {
//        return ResultUtil.createSucssAppResult("获取缩略图成功", qinniuBiz.getThumbnail(fileName, width, height));
        String result = qinniuBiz.getThumbnail(fileName, width, height);
        if(result!=null){
     	   return TxJerseyTools.returnSuccess(JSON.toJSONString(result));
        }
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 批量获取多个文件的url
     * @return
     * @throws Exception
     */
    @GET
    @Path(ScoreAdminConstants.Route.QinNiu.URLS)
    @Produces(MediaType.APPLICATION_JSON)
    public Response urls(@QueryParam("fileNames") String fileNames, @QueryParam("thumbnail") String thumbnail) throws Exception {
        String[] fileNames2 = fileNames.split(DOU_HAO);
//        return ResultUtil.createSucssAppResult("批量获取url成功",qinniuBiz.batchGetFileUrl(fileNames2, thumbnail));
        List<FileUrl> fileUrls = qinniuBiz.batchGetFileUrl(fileNames2, thumbnail);
        if(fileUrls!=null){
     	   return TxJerseyTools.returnSuccess(JSON.toJSONString(fileUrls));
        }
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 批量删除
     * @return
     * @throws Exception
     */
    @GET
    @Path(ScoreAdminConstants.Route.QinNiu.DELETE+"/{module}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("fileNames") String fileNames, @PathParam("module") String module) throws Exception {
        String[] fileNames2 = fileNames.split(DOU_HAO);
//        return ResultUtil.createSucssAppResult("删除成功",qinniuBiz.batchDelete(fileNames2, module));
        Map<String, Object> result = qinniuBiz.batchDelete(fileNames2, module);
        if(result!=null){
     	   return TxJerseyTools.returnSuccess(JSON.toJSONString(result));
        }
        return TxJerseyTools.returnSuccess();
        
    }


}
