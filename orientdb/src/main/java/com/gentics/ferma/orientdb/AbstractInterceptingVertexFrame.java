package com.gentics.ferma.orientdb;

import java.util.HashMap;
import java.util.Map;

import com.gentics.ferma.annotation.GraphElement;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.DelegatingFramedGraph;
import com.syncleus.ferma.FramedGraph;
import com.syncleus.ferma.VertexFrame;
import com.syncleus.ferma.typeresolvers.PolymorphicTypeResolver;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.wrapped.WrappedElement;
import com.tinkerpop.blueprints.util.wrappers.wrapped.WrappedVertex;

public class AbstractInterceptingVertexFrame extends AbstractVertexFrame {

	private Object id;
	public ThreadLocal<Element> threadLocalElement = ThreadLocal.withInitial(() -> ((WrappedVertex) getGraph().getVertex(id)).getBaseElement());

	@Override
	protected void init() {
		super.init();
	}

	@Override
	protected void init(FramedGraph graph, Element element) {
		super.init(graph, element);
		this.id = element.getId();
	}

	/**
	 * Return the properties which are prefixed using the given key.
	 * 
	 * @param prefix
	 * @return
	 */
	public Map<String, String> getProperties(String prefix) {
		Map<String, String> properties = new HashMap<>();

		for (String key : getPropertyKeys()) {
			if (key.startsWith(prefix)) {
				properties.put(key, getProperty(key));
			}
		}
		return properties;
	}

	@SuppressWarnings("unchecked")
	public Object getId() {
		return id;
	}

	public void setLinkInTo(VertexFrame vertex, String... labels) {
		// Unlink all edges between both objects with the given label
		unlinkIn(vertex, labels);
		// Create a new edge with the given label
		linkIn(vertex, labels);
	}

	public void setLinkOutTo(VertexFrame vertex, String... labels) {
		// Unlink all edges between both objects with the given label
		unlinkOut(vertex, labels);
		// Create a new edge with the given label
		linkOut(vertex, labels);
	}

	public String getUuid() {
		return getProperty("uuid");
	}

	public void setUuid(String uuid) {
		setProperty("uuid", uuid);
	}

	public Vertex getVertex() {
		return getElement();
	}

	public String getFermaType() {
		return getProperty(PolymorphicTypeResolver.TYPE_RESOLUTION_KEY);
	}

	@Override
	public FramedGraph getGraph() {
		return new DelegatingFramedGraph<>(OrientDBTrxFactory.getThreadLocalGraph(), true, false);
	}

	@Override
	public Vertex getElement() {
		Element vertex = threadLocalElement.get();

		// Unwrap wrapped vertex
		if (vertex instanceof WrappedElement) {
			vertex = (Vertex) ((WrappedElement) vertex).getBaseElement();
		}
		return (Vertex) vertex;
	}

}
