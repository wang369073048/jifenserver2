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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.trc.biz.pagehome.IBannerBiz;
import org.trc.domain.pagehome.Banner;

import java.util.Calendar;

/**
 * Created by hzwzhen on 2017/6/20.
 */
public class BannerBizTest extends BaseTestCase{

    @Autowired
    private IBannerBiz bannerBiz;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("");
    };

    @Test
    public void test() throws Exception {
        //删除原数据
        execSql(conn,"delete from banner");
        //从xml文件读取数据并插入数据库中
        prepareData(conn,"MyTest.xml");
        // 从xml文件读取期望结果
        ReplacementDataSet dataload_result = createDataSet(Thread.currentThread().getContextClassLoader().getResourceAsStream("MyTest_Result.xml"));
        //从数据库中查出数据与期望结果作比较
        assertDataSet("banner","select * from banner",dataload_result,conn);
    }

    /**
     * 测试正常情况
     */
    @Test
    public void testSave() {

        Banner banner = buildBanner();
        int result = bannerBiz.saveBanner(banner);
        Assert.assertEquals(1,result);
    }

    /**
     * 测试异常情况
     */
    @Test
    public void testSave1() {
        int result = -1;
        Banner banner = new Banner();
        //banner.set
        try{
            result = bannerBiz.saveBanner(banner);
        }catch (Exception e) {
            Assert.assertEquals("BannerException",e.getClass().getSimpleName());
            Assert.assertEquals(1,result);
        }
    }

    @Test
    public void testUpdate() throws Exception {

        execSql(conn,"delete from banner");
        prepareData(conn,"MyTest.xml");
        //更新
        Banner banner = new Banner();
        banner.setId(28L);
        banner.setShopId(2L);
        banner.setName("618活动");
        banner.setIsUp(false);
        bannerBiz.updateBanner(banner);

        // 从xml文件读取期望结果
        ReplacementDataSet dataload_result = createDataSet(Thread.currentThread().getContextClassLoader().getResourceAsStream("Update_Banner_Result.xml"));
        //从数据库中查出数据与期望结果作比较
        assertDataSet("banner","select * from banner",dataload_result,conn);
    }

    @Test
    public void testSelectOne () throws Exception {
        //删除原数据
        execSql(conn,"delete from banner");
        //从xml文件读取数据并插入数据库中
        prepareData(conn,"MyTest.xml");

        Banner banner = new Banner();
        banner.setName("518");
        banner.setSort(28);
        banner = bannerBiz.selectOne(banner);
        Assert.assertNotNull(banner);
        Assert.assertEquals((Long) 28L,(Long)banner.getId());
    }



    private Banner buildBanner(){
        Banner banner = new Banner();
        banner.setShopId(1L);
        banner.setName("518");
        banner.setContentId(1123L);
        banner.setIsUp(true);
        banner.setSort(99);
        banner.setDescription("测试");
        banner.setOperatorUserId("userId-2");
        banner.setIsDeleted(false);
        banner.setCreateTime(Calendar.getInstance().getTime());
        banner.setUpdateTime(Calendar.getInstance().getTime());
        return banner;
    }

    protected void assertDataSet(String tableName, String sql, IDataSet expectedDataSet, IDatabaseConnection conn) throws Exception {
        //printDataAsXml(conn, tableName, sql);
        QueryDataSet loadedDataSet = new QueryDataSet(conn);
        loadedDataSet.addTable(tableName, sql);
        ITable table1 = loadedDataSet.getTable(tableName);
        ITable table2 = expectedDataSet.getTable(tableName);
        Assert.assertEquals(table2.getRowCount(), table1.getRowCount());
        table2 = DefaultColumnFilter.excludedColumnsTable(table2,new String[] {"updateTime"});
        table1 = DefaultColumnFilter.includedColumnsTable(table1, table2.getTableMetaData().getColumns());
        Assertion.assertEquals(table2, table1);
    }
}
