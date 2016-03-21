package com.spbau.wimag.hw3;

/**
 * Created by Mark on 21.03.2016.
 */
public abstract class Predicate<T> extends Function1<T, Boolean> {
    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object arg) {
            return Boolean.TRUE;
        }
    };

    public static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object arg) {
            return Boolean.FALSE;
        }
    };

    public Predicate<T> not() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return !(Predicate.this.apply(arg));
            }
        };
    }

    public Predicate<T> or(Predicate<? super T> other) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return Predicate.this.apply(arg) || other.apply(arg);
            }
        };
    }

    public Predicate<T> and(Predicate<? super T> other) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return Predicate.this.apply(arg) && other.apply(arg);
            }
        };
    }
}
