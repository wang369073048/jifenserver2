package org.trc.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trc.domain.goods.CardItemDO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class XlsProcessing {

    private Logger logger = LoggerFactory.getLogger(XlsProcessing.class);

    private String basePath = "E://upload/";

    private boolean flag = false;

    public void setBasePath(String basePath) {
        if(flag){
            return;
        }else {
            this.basePath = basePath;
            flag = true;
        }
    }

    private static XlsProcessing instance = new XlsProcessing();

    private XlsProcessing(){

    }

    public static XlsProcessing getInstance(){
        return instance;
    }

    public List<CardItemDO> dealFile(String batchNumber, Long shopId, String dirName, String fileName, InputStream fileInputStream) {
        //目录处理
        String destDirName = basePath + dirName;
        System.out.println(destDirName);
        File dir = new File(destDirName);
        if(dir.exists()){
            logger.info("目录:"+dirName+"已存在!");
        }else{
            if (dir.mkdirs()) {
                logger.info("创建目录" + destDirName + "成功！");
            }
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        String destFileName = destDirName + fileName;
        File file = new File(destFileName);
        //文件备份
        try {
            FileUtils.copyInputStreamToFile(fileInputStream, file);
        } catch (IOException e) {
            logger.error("文件名为:"+fileInputStream+"备份失败!");
            e.printStackTrace();
        }
        //读取excel内容
        ExcelReader excelReader = ExcelReader.init(destFileName);
        //String[] titles = excelReader.readExcelTitle();
        Map<Integer, String> contextMap = excelReader.readExcelContent();
        //处理成卡券明细数据
        Date now = Calendar.getInstance().getTime();
        List<CardItemDO> cardItemList = new ArrayList<>();
        for(Map.Entry<Integer, String> entry : contextMap.entrySet()){
            String code = entry.getValue();
            CardItemDO cardItem = new CardItemDO();
            cardItem.setShopId(shopId);
            cardItem.setBatchNumber(batchNumber);
            cardItem.setCode(code);
            cardItem.setState(0);
            cardItem.setCreateTime(now);
            cardItemList.add(cardItem);
        }
        return cardItemList;
    }

}
