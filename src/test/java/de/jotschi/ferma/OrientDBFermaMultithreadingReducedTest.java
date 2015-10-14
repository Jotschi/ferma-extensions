package de.jotschi.ferma;

import static de.jotschi.ferma.util.TestUtils.runAndWait;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.jotschi.ferma.model.Person;

public class OrientDBFermaMultithreadingReducedTest extends AbstractOrientDBTest {

	private Person p;

	@Before
	public void setup() {
		setupData();
	}

	private void setupData() {
		try (Trx tx = graph.trx()) {
			String name = "SomeName";
			p = addPersonWithFriends(tx.getGraph(), name);
			// tx.getGraph().commit();
			tx.success();
			runAndWait(() -> {
				try (Trx tx2 = graph.trx()) {
					readPerson(p);
					manipulatePerson(tx2.getGraph(), p);
				}
			});
		}

		runAndWait(() -> {
			try (Trx tx2 = graph.trx()) {
				readPerson(p);
				manipulatePerson(tx2.getGraph(), p);
			}
		});

	}

	@Test
	public void testMultithreading() {

		// fg.commit();
		runAndWait(() -> {
			Person reloaded;
			try (Trx tx = graph.trx()) {
				manipulatePerson(tx.getGraph(), p);
				String name = "newName";
				p.setName(name);
				reloaded = tx.getGraph().v().has(Person.class).has("name", name).nextOrDefaultExplicit(Person.class, null);
				System.out.println(reloaded.getName());
				assertNotNull(reloaded);
				manipulatePerson(tx.getGraph(), reloaded);
				tx.success();
			}
			runAndWait(() -> {
				try (Trx tx2 = graph.trx()) {
					readPerson(reloaded);
				}
			});
		});
	}

	private void readPerson(Person person) {
		person.getName();
		for (Person p : person.getFriends()) {
			p.getName();
			for (Person p2 : person.getFriends()) {
				p2.getName();
				for (Person p3 : p2.getFriends()) {
					p3.getName();
				}
			}
		}
	}

}
