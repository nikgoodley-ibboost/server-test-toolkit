package com.googlecode.test.toolkit.util;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 *  <code>
     Condition condition=new ConditionWaitUtil.Condition() {

            @Override
            protected boolean checkCondition() {
                  return false;
            }
        };

    ConditionWaitUtil.wait(condition, 2*1000, 10*1000);
    </code>
 * @author pholen.wang jiafu
 *
 */
public final class ConditionWaitUtil {

    private final static Logger LOGGER = Logger.getLogger(ConditionWaitUtil.class);

    private ConditionWaitUtil(){
        //no instance;
    }

    public static abstract class Condition{
        protected abstract boolean checkCondition();
    }

    public static class WaitTimeoutException extends RuntimeException{

        private static final long serialVersionUID = 1L;

        public WaitTimeoutException() {
        }

        public WaitTimeoutException(String arg0) {
            super(arg0);
        }

        public WaitTimeoutException(Throwable arg0) {
            super(arg0);
        }

        public WaitTimeoutException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

    }


    public static void wait(Condition condition,long intervalInMilliseconds, long timeoutInMilliseconds) {
        LOGGER.info("[util][wait][start]");
        long start = System.currentTimeMillis();
        long end = start + timeoutInMilliseconds;

        while (System.currentTimeMillis() < end) {
            if (condition.checkCondition()) {
                LOGGER.info("[util][wait][end][success]");
                return;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(intervalInMilliseconds);
            } catch (InterruptedException e) {
                LOGGER.info("[util][wait][end][fail]");
                throw new RuntimeException(e);
            }
        }

        LOGGER.info("[util][wait][end][fail]after(ms): "+timeoutInMilliseconds);
        throw new WaitTimeoutException("[util][wait][fail] Fail to wait condition after (ms): "+timeoutInMilliseconds);
    }

}
