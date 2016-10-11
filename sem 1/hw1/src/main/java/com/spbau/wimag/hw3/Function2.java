package com.spbau.wimag.hw3;

/**
 * Created by Mark on 21.03.2016.
 */
public abstract class Function2<T, U, R> {
    public abstract R apply(T arg1, U arg2);

    public <R1> Function2<T, U, R1> compose(Function1<? super R, R1> function1) {
        return new Function2<T, U, R1>() {

            @Override
            public R1 apply(T arg1, U arg2) {
                return function1.apply(Function2.this.apply(arg1, arg2));
            }
        };
    }

    public Function1<U, R> bind1(T arg1) {
        return new Function1<U, R>() {
            @Override
            public R apply(U arg2) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    public Function1<T, R> bind2(U arg2) {
        return new Function1<T, R>() {
            @Override
            public R apply(T arg1) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    public Function1<T, Function1<U, R>> curry() {
        return new Function1<T, Function1<U, R>>() {
            @Override
            public Function1<U, R> apply(T arg) {
                // works same without Function2.this
                return Function2.this.bind1(arg);
            }
        };
    }
}
