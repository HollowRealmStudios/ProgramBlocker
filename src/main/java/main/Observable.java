package main;

import java.util.ArrayList;

public class Observable<T> {

	private final ArrayList<Lambda<T>> lambdas = new ArrayList<>();
	private T t;

	public Observable(T t) {
		this.t = t;
	}

	public void onChange(Lambda<T> lambda) {
		lambdas.add(lambda);
	}

	public T get() {
		return t;
	}

	public void set(T t) {
		this.t = t;
		lambdas.forEach(lambda -> lambda.run(this.t));
	}
}
