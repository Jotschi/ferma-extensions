package de.jotschi.ferma;

import com.syncleus.ferma.FramedTransactionalGraph;

/**
 * A {@link Trx} is an interface for autoclosable transactions.
 */
public interface Trx extends AutoCloseable {

	/**
	 * Mark the transaction as succeeded. The autoclosable will invoke a commit when completing.
	 */
	void success();

	/**
	 * Mark the transaction as failed. The autoclosable will invoke a rollback when completing.
	 */
	void failure();

	/**
	 * Return the framed graph that is bound to the transaction.
	 * 
	 * @return
	 */
	FramedTransactionalGraph getGraph();

	/**
	 * Invoke rollback or commit when closing the autoclosable. By default a rollback will be invoked.
	 */
	@Override
	void close();

}
