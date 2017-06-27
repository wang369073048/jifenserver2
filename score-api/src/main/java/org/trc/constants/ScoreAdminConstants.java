package org.trc.constants;

/**
 * 积分平台常量
 * Created by huyan on 2016/11/21.
 */
public class ScoreAdminConstants {

    public static final class BaseParam {
        public static final String DEFAULT_PAGEINDEX_STR = "1";
        public static final String DEFAULT_PAGESIZE_STR = "10";
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
    }

}
