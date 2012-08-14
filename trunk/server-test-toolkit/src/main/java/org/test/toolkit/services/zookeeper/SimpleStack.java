package org.test.toolkit.services.zookeeper;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class SimpleStack<T> {
	private List<T> container = new ArrayList<T>();

	public final boolean empty() {
		return container.isEmpty();
	}

	public final T peek() {
		if (empty())
			throw new EmptyStackException();
		return container.get(container.size() - 1);
	}

	public final T pop() {
		return container.remove(container.size() - 1);
	}

	public final T push(T item) {
		container.add(item);
		return item;
	}
}