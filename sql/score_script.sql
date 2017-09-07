#[membership_score_daily_details]lotteryConsumeNum、consumeCorrectNum
ALTER TABLE membership_score_daily_details ADD lotteryConsumeNum BIGINT (20) NOT NULL DEFAULT 0 COMMENT '抽奖消费积分数量',
 ADD consumeCorrectNum BIGINT (20) NOT NULL DEFAULT 0 COMMENT '消费冲正积分数量';

#[consumption_summary]lotteryConsumeNum、consumeCorrectNum
ALTER TABLE consumption_summary ADD lotteryConsumeNum BIGINT (20) NOT NULL DEFAULT 0 COMMENT '抽奖消费积分数量',
 ADD consumeCorrectNum BIGINT (20) NOT NULL DEFAULT 0 COMMENT '消费冲正积分数量';

 #表[shop_classification]添加字段selectPicUrl
 ALTER TABLE `shop_classification`
ADD COLUMN `selectPicUrl`  varchar(255) NULL COMMENT '分类图片(选中)' AFTER `pictureUrl`;