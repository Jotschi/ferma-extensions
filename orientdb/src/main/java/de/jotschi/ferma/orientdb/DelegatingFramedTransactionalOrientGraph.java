package de.jotschi.ferma.orientdb;

import com.syncleus.ferma.WrapperFramedTransactionalGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class DelegatingFramedTransactionalOrientGraph<G extends OrientGraph> extends AbstractDelegatingFramedOrientGraph<G>
		implements WrapperFramedTransactionalGraph<G> {

	public DelegatingFramedTransactionalOrientGraph(final G delegate, final boolean typeResolution, final boolean annotationsSupported) {
		super(delegate, new OrientDBTypeResolver());
	}

	@Override
	public void stopTransaction(Conclusion conclusion) {
		getBaseGraph().stopTransaction(conclusion);
	}

	@Override
	public void commit() {
		getBaseGraph().commit();
	}

	@Override
	public void rollback() {
		getBaseGraph().rollback();
	}

}
