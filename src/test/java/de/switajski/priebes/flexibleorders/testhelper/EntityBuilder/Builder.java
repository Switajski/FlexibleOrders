package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

/**
 * Interface for Builder Pattern to construct test fixtures. Build in
 * dependencies should have a visibility over just one node.a
 * 
 * @author Marek Switajski
 * 
 * @param <T>
 *            the entity/transient entity to return for tests
 */
public interface Builder<T> {

	public T build();

}
