package org.trc.biz.impl.order;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Date;


/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/17
 */
public class Test {
    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        //1.访问root对象属性
        Date date = new Date();
        StandardEvaluationContext context = new StandardEvaluationContext(date);
        int result1 = parser.parseExpression("year").getValue(context, int.class);
        System.out.println(result1);
    }
}
