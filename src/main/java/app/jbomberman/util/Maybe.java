package app.jbomberman.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Maybe<T>
{
    private enum MaybeEnum
    {
        Just, Nothing;
    }

    private static final Maybe nothing = new Maybe();

    private final T object;
    private final MaybeEnum maybeEnum;

    private Maybe()
    {
        maybeEnum = MaybeEnum.Nothing;
        object = null;
    }

    private Maybe(T a)
    {
        object = a;
        maybeEnum = MaybeEnum.Just;
    }

    public static <T> Maybe<T> just(T a)
    {
        return new Maybe<>(a);
    }

    @SuppressWarnings("unchecked")
    public static <T> Maybe<T> nothing()
    {
        return nothing;
    }

    public static <T> Maybe<T> something(T a)
    {
        return (a == null) ? Maybe.nothing() : Maybe.just(a);
    }

    public boolean isNothing()
    {
        return maybeEnum == MaybeEnum.Nothing;
    }

    public boolean isJust()
    {
        return maybeEnum == MaybeEnum.Just;
    }

    public <S> S compute(S elsevalue, Function<T, S> func)
    {
        return isNothing() ? elsevalue : func.apply(object);
    }

    public <S> Maybe<S> maybeCompute(Function<T, S> function)
    {
        return isNothing() ? Maybe.nothing() : Maybe.just(function.apply(this.object));
    }

    public T unwrap()
    {
        if (isNothing())
            throw new RuntimeException("Unwrapping a nothing object");
        return object;
    }

    public T unwrapOrThrow(Supplier<? extends RuntimeException> supplier)
    {
        if (isNothing())
            throw supplier.get();
        return object;
    }

    public T orElse(T alternative)
    {
        return isJust() ? object : alternative;
    }

    public void maybeDo(Consumer<T> consumer)
    {
        if (isJust())
            consumer.accept(unwrap());
    }

    public void doOrThrow(Consumer<T> toDoIfJust,
                          Supplier<? extends RuntimeException> toThrowIfNothingSupplier)
    {
        isJustOrThrow(toThrowIfNothingSupplier);
        toDoIfJust.accept(unwrap());
    }

    public void isJustOrThrow(Supplier<? extends RuntimeException> toThrowIfNothing)
    {
        if (isNothing())
            throw toThrowIfNothing.get();
    }


    public Maybe<T> orElseWrapped(T otherwise)
    {
        return (isJust()) ? this : Maybe.just(otherwise);
    }

    public <S> S computeOrThrow(Function<T, S> toApply, Supplier<? extends RuntimeException> toThrowIfNothing)
    {
        return maybeCompute(toApply).unwrapOrThrow(toThrowIfNothing);
    }

    public void doOrDo(Consumer<T> toApplyIfJust, Callback toCallIfNothing)
    {
        if (isJust())
            toApplyIfJust.accept(unwrap());
        else
            toCallIfNothing.run();
    }

    @Override
    public String toString()
    {
        return (isNothing()) ? "Nothing" : "Just(" + object + ")";
    }
}
