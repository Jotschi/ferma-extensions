package com.gentics.ferma.model;

import java.util.List;

import com.gentics.ferma.annotation.GraphElement;
import com.gentics.ferma.orientdb.AbstractInterceptingVertexFrame;

@GraphElement
public class Group extends AbstractInterceptingVertexFrame {

	public List<? extends Person> getMembers() {
		return out("HAS_MEMBER").has(Person.class).toListExplicit(Person.class);
	}

	public void addMember(Person person) {
		linkOut(person, "HAS_MEMBER");
	}

	public void setName(String name) {
		setProperty("name", name);
	}

	public String getName() {
		return getProperty("name");
	}

}
