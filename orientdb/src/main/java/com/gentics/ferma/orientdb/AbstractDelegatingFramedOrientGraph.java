package com.gentics.ferma.orientdb;

import com.syncleus.ferma.ClassInitializer;
import com.syncleus.ferma.DefaultClassInitializer;
import com.syncleus.ferma.DelegatingFramedGraph;
import com.syncleus.ferma.VertexFrame;
import com.syncleus.ferma.typeresolvers.TypeResolver;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;

public abstract class AbstractDelegatingFramedOrientGraph<G extends OrientBaseGraph> extends DelegatingFramedGraph<G> {

	public AbstractDelegatingFramedOrientGraph(G delegate, TypeResolver typeResolver) {
		super(delegate, typeResolver);
	}

	@Override
	public <T> T addFramedVertex(Object id, final ClassInitializer<T> initializer) {
		return frameNewElement(this.getBaseGraph().addVertex(id), initializer);
	}

	@Override
	public <T> T addFramedEdge(Object id, VertexFrame source, VertexFrame destination, String label, ClassInitializer<T> initializer) {
		return frameNewElement(this.getBaseGraph().addEdge(id, source.getElement(), destination.getElement(), label), initializer);
	}

	@Override
	public <T> T addFramedVertex(final Class<T> kind) {
		return this.addFramedVertex("class:" + kind.getSimpleName(), new DefaultClassInitializer<>(kind));
	}

	@Override
	public <T> T addFramedEdge(VertexFrame source, VertexFrame destination, String label, Class<T> kind) {
		return super.addFramedEdge(source, destination, label, kind);
	}
}
