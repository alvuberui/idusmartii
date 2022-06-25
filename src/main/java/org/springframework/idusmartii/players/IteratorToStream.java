package org.springframework.idusmartii.players;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IteratorToStream {
	
	public static <T> Stream<T> iterableToStream(Iterable<T> iterable){
		Spliterator<T> spliterator = iterable.spliterator();
		return StreamSupport.stream(spliterator, false);
	}

}
