package com.spbau.wimag.hw3;

/**
 * Created by Mark on 21.03.2016.
 */
public abstract class Function1<T, R> {
    public abstract R apply(T arg);

    public <R1> Function1<T, R1> compose(Function1<? super R, R1> g) {
        return new Function1<T, R1>() {

            @Override
            public R1 apply(T arg) {
                return g.apply(Function1.this.apply(arg));
            }
        };
    }
}