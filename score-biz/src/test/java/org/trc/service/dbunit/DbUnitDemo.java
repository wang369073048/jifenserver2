package org.trc.service.dbunit;

import org.dbunit.Assertion;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.trc.domain.pagehome.Banner;
import org.trc.domain.pagehome.BannerContent;
import org.trc.service.pagehome.IBannerContentService;
import org.trc.service.pagehome.IBannerService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * DbUnitDemo
 * Created by hzwzhen on 2017/6/19.
 */
public class DbUnitDemo extends BaseTestCase {

    @Autowired
    private IBannerContentService bannerContentService;

    @Autowired
    private IBannerService bannerService;

    private static final String TABLE_BANNER_CONTENT = "banner_content";

    private static final String TABLE_BANNER = "banner";

    @BeforeClass
    public static void beforeClass() {
        System.out.println("this is @BeforeClass");
    };



    /**
     * 准备数据插入数据库并与
     * 期望结果作比较
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        //删除原数据
        execSql(conn,"delete from banner");
        //从xml文件读取数据并插入数据库中
        prepareData(conn,"MyTest.xml");
        // 从xml文件读取期望结果
        ReplacementDataSet dataload_result = createDataSet(Thread.currentThread().getContextClassLoader().getResourceAsStream("MyTest_Result.xml"));
        //空元素的字段需要一个"[null]"占位符，然后用 replacementDataSet.addReplacementObject("[null]", null) 替换成null
        dataload_result.addReplacementObject("[null]", null);
        //从数据库中查出数据与期望结果作比较
        assertDataSet(TABLE_BANNER,"select * from banner",dataload_result,conn);
    }

    /**
     *从数据库中导出数据到
     * xml文件中
     * @throws Exception
     */
    @Test
    public void testExport() throws Exception {
        List<String> tableNameList = new ArrayList<>();
        tableNameList.add("banner");
        exportData(tableNameList,"banner.xml");
    }

    /**
     * 关于FlatXmlDataSet:从已准备好的xml文件读取期望结果
     * 与从DB里取实际数据比较断言
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {

        //get actual tableInfo from DB 从DB里取实际数据
        IDataSet dbDataSet = getDBDataSet();
        ITable dbTable = dbDataSet.getTable(TABLE_BANNER_CONTENT);

        //get expect Information from xml file 从已准备好的xml文件读取期望结果
        IDataSet xmlDataSet = getXmlDataSet("expect_demo.xml");
        ITable xmlTable = xmlDataSet.getTable(TABLE_BANNER_CONTENT);

        //exclude some columns which don't want to compare result 将指定字段给排除在比较范围之外
        dbTable = DefaultColumnFilter.excludedColumnsTable(dbTable,
                new String[]{"id","shopId","imgUrl","targetUrl","description","isDeleted","operatorUserId","createTime","updateTime"});
        xmlTable = DefaultColumnFilter.excludedColumnsTable(xmlTable, new String[]{""});
        System.out.println(dbTable.getRowCount());
        Assertion.assertEquals(dbTable,xmlTable);
    }


    /**
     *关于QueryDataSet:通过自己的query语句查到的结果
     * 作为期望结果与调用Dao层取得的实际结果比较断言
     * @throws Exception
     */
    @Ignore
    @Test
    public void test2() throws Exception {
        //调用Service层取得的实际结果
        List<BannerContent> bannerContentList = bannerContentService.select(new BannerContent());
        //通过自己的query语句查到的结果作为期望结果
        QueryDataSet queryDataSet = getQueryDataSet();
        queryDataSet.addTable("banner_content", "select * from banner_content where id = '1'");
        ITable dbTable = queryDataSet.getTable("banner_content");
        Assert.assertEquals(dbTable.getValue(0,"imgUrl"),bannerContentList.get(0).getImgUrl());
    }

    /**
     * 关于ReplacementDataSet:当从数据库取出的实际数据第一行的字段值有null时,
     * FlatXmlDataSet所对应的XML文件里的数据第一行的定义,用"[null]"占位符
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {

        //get actual tableInfo from DB 从DB里取实际数据
        IDataSet dbDataSet = getDBDataSet();
        ITable dbTable = dbDataSet.getTable(TABLE_BANNER);

        //get expect Information from xml file 从已准备好的xml文件读取期望结果
        IDataSet xmlDataSet = getXmlDataSet("expect_demo.xml");
        //空元素的字段需要一个"[null]"占位符，然后用 replacementDataSet.addReplacementObject("[null]", null) 替换成null
        ReplacementDataSet replacementDataSet = createReplacementDataSet(xmlDataSet);
        ITable xmlTable = replacementDataSet.getTable(TABLE_BANNER);

        //exclude some columns which don't want to compare result 将指定字段给排除在比较范围之外
        dbTable = DefaultColumnFilter.excludedColumnsTable(dbTable,
                new String[]{"shopId","name","isUp","sort","description","operatorUserId","isDeleted","createTime","updateTime"});
        xmlTable = DefaultColumnFilter.excludedColumnsTable(xmlTable, new String[]{""});
        System.out.println(dbTable.getRowCount());
        Assertion.assertEquals(dbTable,xmlTable);

    }



    @Ignore
    @Test
    public void testDelete() throws Exception{
        //默认自动回滚,不用备份
        //backupCustom(TABLE_BANNER_CONTENT);
        List<BannerContent> list1 = bannerContentService.select(new BannerContent());
        Assert.assertEquals(5,list1.size());
        System.out.println("list1 size ==> " + list1.size());
        bannerContentService.deleteByPrimaryKey(1L);
        List<BannerContent> list2 = bannerContentService.select(new BannerContent());
        Assert.assertEquals(4,list2.size());
        System.out.println("list2 size ==> " + list2.size());
        //rollback();
    }




    private Banner buildBanner(){
        Banner banner = new Banner();
        banner.setShopId(1L);
        banner.setName("518");
        banner.setContentId(1123L);
        banner.setIsUp(true);
        banner.setSort(98);
        banner.setDescription("测试");
        banner.setOperatorUserId("userId-1");
        banner.setIsDeleted(false);
        banner.setCreateTime(Calendar.getInstance().getTime());
        banner.setUpdateTime(Calendar.getInstance().getTime());
        return banner;
    }


}
