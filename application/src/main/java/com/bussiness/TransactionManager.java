package com.bussiness;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class TransactionManager {
	private static final TransactionManager instance = new TransactionManager();
	
	public static TransactionManager getInstance() {
		return instance;
	}
	
	public static DataSourceTransactionManager txManager;

	/**
	 * transaction 시작
	 *
	 * @return txStatus
	 */
    @Value("${txManager}")
	public static TransactionStatus start(DataSourceTransactionManager txManager) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus txStatus = txManager.getTransaction(def);
		
		TransactionManager.txManager = txManager;
		
		return txStatus;
	}

	/**
	 * transaction commit
	 *
	 * @param txStatus
	 */
	public static void commit(TransactionStatus txStatus) {
		TransactionManager.txManager.commit(txStatus);
	}

	/**
	 * transaction rollback
	 *
	 * @param txStatus
	 */
	public static void rollback(TransactionStatus txStatus) {
		TransactionManager.txManager.rollback(txStatus);
	}
}
