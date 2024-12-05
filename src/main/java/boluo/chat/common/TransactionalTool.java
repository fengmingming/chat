package boluo.chat.common;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionalTool {

    public static void afterCommit(Runnable runnable) {
        afterCommit(runnable, false);
    }

    /**
     * @param runnable 执行方法
     * @param doContinue 如果不在十五内是否继续执行
     * */
    public static void afterCommit(Runnable runnable, boolean doContinue) {
        if(TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        }else if(doContinue) {
            runnable.run();
        }
    }

}
