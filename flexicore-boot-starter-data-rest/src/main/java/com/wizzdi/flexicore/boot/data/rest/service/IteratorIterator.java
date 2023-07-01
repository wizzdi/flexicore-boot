package com.wizzdi.flexicore.boot.data.rest.service;

import java.util.Collection;
import java.util.Iterator;

public class IteratorIterator<T> implements Iterator<T> {
	private final Iterator<T>[] is;
	private int current;

	public IteratorIterator(Collection<Iterator<T>> iterators) {
		this(iterators.toArray(Iterator[]::new));

	}

	public IteratorIterator(Iterator<T>... iterators) {
		is = iterators;
		current = 0;
	}

	public boolean hasNext() {
		while (current < is.length && !is[current].hasNext())
			current++;

		return current < is.length;
	}

	public T next() {
		while (current < is.length && !is[current].hasNext())
			current++;

		return is[current].next();
	}

	public void remove() { /* not implemented */ }
}

