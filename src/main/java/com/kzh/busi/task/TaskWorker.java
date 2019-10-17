package com.kzh.busi.task;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created with IntelliJ IDEA.
 * 目前不支持分布式  依赖  fixedDelay
 * "0 0 12 * * ?"    每天中午十二点触发
 * "0 15 10 ? * *"    每天早上10：15触发
 * "0 15 10 * * ?"    每天早上10：15触发
 * "0 15 10 * * ? *"    每天早上10：15触发
 * "0 15 10 * * ? 2005"    2005年的每天早上10：15触发
 * "0 * 14 * * ?"    每天从下午2点开始到2点59分每分钟一次触发
 * "0 0/5 14 * * ?"    每天从下午2点开始到2：55分结束每5分钟一次触发
 * "0 0/5 14,18 * * ?"    每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
 * "0 0-5 14 * * ?"    每天14:00至14:05每分钟一次触发
 * "0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发
 * "0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发
 * （1）fixedRate：每隔多少毫秒执行一次该方法
 * （2）fixedDelay：当一次方法执行完毕之后，延迟多少毫秒再执行该方法。
 * （3）cron：详细配置了该方法在什么时候执行。cron值是一个cron表达式.
 */

public class TaskWorker {
    private static final Logger logger = Logger.getLogger(TaskWorker.class);

    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 10 * 60 * 1000)
    public void runTask() {

    }

    //每天0点触发
    /*@Scheduled(cron = "0 0 0 * * *")
    public void zero() {
        
    }*/

    //每天23点触发
    /*@Scheduled(cron = "0 0 23 * * *")
    public void archive() {
        logger.info("job归档任务开始=============================");
//        jobService.archive();
        logger.info("job归档任务结束=============================");
    }*/
}