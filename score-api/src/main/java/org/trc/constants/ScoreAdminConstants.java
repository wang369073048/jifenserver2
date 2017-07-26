package org.trc.constants;

/**
 * 积分平台常量
 */
public class ScoreAdminConstants {

    public static final class BaseParam {
        public static final String DEFAULT_PAGEINDEX_STR = "1";
        public static final String DEFAULT_PAGESIZE_STR = "10";
    }

    /**
     * 符号
     */
    public static final class Symbol {
        public static final String COMMA = ",";//逗号
        public static final String MINUS = "-";//减号
        public static final String FILE_NAME_SPLIT = ".";//文件名称分隔符
        public static final String FULL_PATH_SPLIT = "|";//分类路径ID分隔符
    }

    /**
     * 授权
     */
    public static final class Authorization{
        //用户ID
        public static final String USER_ID = "userId";
        //用户授权列表
        public static final String ACL_USER_ACCREDIT_INFO = "aclUserAccreditInfo";

    }

    /**
     * 虚拟货币类型
     */
    public enum CurrencyType {
        TCoin,//T币
        Ecard//E卡
    }

    //系统参数
    public static final class SysParam {
        //积分商城后台管理员
        public static final String ADMIN = "admin";
    }

    public static final class Route {

        public static final class Admin {

            public static final class Auth {
                public static final String ROOT = "auth";
                public static final String BLANKLIST = "blanklist";
            }

            public static final class Flow {
                public static final String ROOT = "flow";
            }
        }

        public static final class Sys {
            public static final String ROOT = "sys";
            public static final String ROLE = "/role";
        }


        public static final class Converter {
            public static final String ROOT = "converter";
        }

        public static final class Goods {
            public static final String ROOT = "goods";
            public static final String ENTITY = "/entity";
            public static final String VIRTUAL = "/virtual";
            public static final String UP = "/up";
            public static final String DOWN = "/down";
            public static final String DETAIL = "/detail";
            public static final String CATEGORY = "/category";
            public static final String ENTITY_UP = ENTITY + UP;
            public static final String ENTITY_DOWN = ENTITY + DOWN;
            public static final String VIRTUAL_UP = VIRTUAL + UP;
            public static final String VIRTUAL_DOWN = VIRTUAL + DOWN;
            public static final String CHECKEID = "/checkeid";
        }

        //推荐
        public static final class Recommand {
            public static final String ROOT = "recommand";
            public static final String GOODS = "/goods";
        }

        //橱窗
        public static final class Shopwindow {
            public static final String ROOT = "shopwindow";
        }

        public static final class Flow {
            public static final String ROOT = "flow";
            public static final String RECORD = "/record";
            public static final String EXPORT = "/export";
        }

        public static final class Banner {
            public static final String ROOT = "banner";
            public static final String SETCONTENT = "/set/content";
            public static final String SETUP = "/set/up";
            public static final String SORT = "/sort";
        }

        public static final class BannerContent {
            public static final String ROOT = "banner/content";
        }

        public static final class Category {
            public static final String ROOT = "category";
        }

        public static final class Shop {
            public static final String ROOT = "shop";
        }

        public static final class Manager {
            public static final String ROOT = "manager";
        }

        public static final class Settlement {
            public static final String ROOT = "settlement";
            public static final String ORDER = "/order";
            public static final String EXPORT = "/export";
        }

        public static final class Order{
            public static final String ROOT = "order";
            public static final String LOGISTICSTRACKING = "/logisticsTracking";
            public static final String PULL = "/pull";
            public static final String LIST = "/list";
        }

        public static final class Financial{
            public static final String ROOT = "/fs";
            public static final String CONSUMPTION_SUMMARY = "/consumptionSummary";
            public static final String MONTH_CONSUMPTION_SUMMARY = CONSUMPTION_SUMMARY + "/month";
            public static final String CONSUMPTION_SUMMARY_EXPORT = CONSUMPTION_SUMMARY + "/export";
            public static final String MONTH_CONSUMPTION_SUMMARY_EXPORT = MONTH_CONSUMPTION_SUMMARY + "/export";
            public static final String MEMBERSHIP_SCORE_DAILY_DETAILS = "/membershipScoreDailyDetails";
            public static final String MEMBERSHIP_SCORE_DAILY_DETAILS_EXPORT = MEMBERSHIP_SCORE_DAILY_DETAILS + "/export";
        }

        public static final class CardCoupuns{
            public static final String ROOT = "coupuns";
            public static final String ITEM = "/item";
        }

        public static final class LuckyDraw{
            public static final String ROOT = "luckyDraw";
            public static final String LIST = "/list";
            public static final String WINNING_RECORD_LIST = "/winningRecord/list";
            public static final String WINNING_RECORD_EXPORT = "/winningRecord/list/export";
            public static final String ACTIVITY_DETAIL_LIST = "/activityDetail/list";
            public static final String ACTIVITY_DETAIL_EXPORT = "/activityDetail/list/export";
            public static final String WINNING_RECORD = "/winningRecord";
            public static final String SHIP = "/ship";
        }

        public static final class ActivityPrizes {
            public static final String ROOT = "activityPrizes";
            public static final String ADD = "/add";
        }
        public static final class Liumi{
            public static final String ROOT = "/liumi";
            public static final String ORDER_NOTICE = "/orderNotice";
        }
        /**
         * 授权
         */
        public static final class Authorization{
            //用户ID
            public static final String USER_ID = "userId";
            //用户授权列表
            public static final String ACL_USER_ACCREDIT_INFO = "aclUserAccreditInfo";

        }
        /**
         * 资源（权限）
         */
        public static final class Jurisdiction {
            //根路径
            public static final String ROOT = "accredit";
            //全局资源资源查询
            public static final String JURISDICTION_WHOLE = "jurisdictionWhole";
            //渠道资源查询
            public static final String JURISDICTION_CHANNEL = "jurisdictionChannel";
            //查询资源加载树
            public static final String JURISDICTION_TREE = "jurisdictionTree";
            //新增资源
            public static final String JURISDICTION_SAVE = "jurisdictionSave";
            //编辑资源
            public static final String JURISDICTION_EDIT = "jurisdictionEdit";
            //页面资源
            public static final String JURISDICTION_HTML = "jurisdictionHtml";

        }

        /**
         * 角色信息
         */
        public static final class Role {
            //根路径
            public static final String ROOT = "accredit";
            //角色信息分页查询
            public static final String ROLE_PAGE = "rolePage";
            //授权的用户使用名查询
            public static final String ROLE = "role";
            //角色用户授权入口
            public static final String ROLE_ACCREDITINFO = "roleAccreditInfo";
            //状态的修改
            public static final String UPDATE_STATE = "role/updateState";
        }

        /**
         * 授权信息
         */
        public static final class UserAccreditInfo {

            //根路径
            public static final String ROOT = "accredit";

            //授权信息分页查询
            public static final String ACCREDIT_PAGE = "accreditInfoPage";

            //授权的用户使用名查询
            public static final String ACCREDIT = "accreditInfo";

            //状态的修改
            public static final String UPDATE_STATE = "accreditInfo/updateState";

            //查询已启用的渠道
            public static final String CHANNEL = "select/channel";

            //查询拥有采购员角色的用户
            public static final String PURCHASE = "purchase";

            //查询选择用户对应角色
            public static final String ROLE = "rolelist";

            //新增授权
            public static final String SAVE_ACCREDIT = "saveaccredit";

            //用户修改
            public static final String UPDATE_ACCREDIT = "updateaccredit";

            //查询用户名是否已被使用
            public static final String CHECK = "check";

            //查询手机号是否已经注册
            public static final String CHECK_PHONE = "checkPhone";

            //用户采购组状态查询
            public static final String CHECK_PURCHASE = "checkPurchase";

            //编辑用户之前,查询是否有角色被停用
            public static final String ROLE_VALID = "rolevalid";

        }

    }

}
