package app.jbomberman.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Maybe is my java counterpart for the Haskell Maybe Monad. It's essentially
 * an enhanced extensible Optional-like class providing many more functional
 * methods
 *
 * @param <T> the type that Maybe contains
 * @author gp
 */
public class Maybe<T>
{
    /**
     * Maybe.nothing is a constant representing a nothing. IntelliJ says that
     * we are using a raw parametrized maybe but in the end it's ok. When we
     * got a nothing, we don't have to access to the object field and nothing
     * bad can happen
     */
    private static final Maybe nothing = new Maybe();

    /**
     * field containing the object if this is not nothing
     */
    private final T object;

    /**
     * private constructor meant to build nothing
     */
    private Maybe()
    {
        object = null;
    }

    /**
     * private constructor meant to build just
     *
     * @param a the element to store in maybe
     */
    private Maybe(T a)
    {
        assert a != null;
        object = a;
    }

    /**
     * public function to get a just
     *
     * @param a   the element that must be places in the Maybe
     * @param <T> the type of the element to be stored in the Maybe
     * @return the maybe
     */
    public static <T> Maybe<T> just(T a)
    {
        return new Maybe<>(a);
    }

    /**
     * get the nothing
     *
     * @param <T> the type of the elements that the Maybe might contain in the
     *            future
     * @return a maybe that is nothing
     */
    @SuppressWarnings("unchecked")
    public static <T> Maybe<T> nothing()
    {
        return nothing;
    }

    /**
     * Create a maybe depending on the value of {@param a}. If a is null, then nothing
     * gets return otherwise it returns a just
     *
     * @param a   the element that the maybe might in the end contain if it not null
     * @param <T> the type of the element that must be wrapped in the maybe
     * @return the maybe built
     */
    public static <T> Maybe<T> something(T a)
    {
        return returnM(a);
    }

    /**
     * Check whether the maybe doesn't contain any element
     *
     * @return a boolean saying whether the maybe doesn't contain an element
     */
    public boolean isNothing()
    {
        return object == null;
    }

    /**
     * Check whether the maybe is a just
     *
     * @return a boolean saying whether the maybe contains an element
     */
    public boolean isJust()
    {
        return object != null;
    }

    /**
     * bind monadic operation
     *
     * @param function operation to be applied
     * @param <S>      type of the result of the computation by function
     * @return nothing if the this was nothing or function applied to this
     */
    public static <T, S> Maybe<? extends S> bindM(Maybe<? extends T> m, Function<? super T, Maybe<? extends S>> function)
    {
        return m.isNothing() ? Maybe.nothing() : function.apply(m.fromJust());
    }

    /**
     * Monadic return operation implemented to return just a
     *
     * @param a   the value to be monadically returned
     * @param <S> the type of a
     * @return a Just containing a
     */
    public static <S> Maybe<S> returnM(S a)
    {
        return (a == null) ? Maybe.nothing() : Maybe.just(a);
    }

    /**
     * compute() applies func to this.object if this is just or it returns the
     * elsevalue
     *
     * @param elsevalue alternative to return
     * @param func      function to apply if this is just
     * @param <S>       return type of func and of the elsevalue
     * @return the elsevalue or the result of applying func to this if this is
     * just
     */
    public <S> S compute(S elsevalue, Function<? super T, ? extends S> func)
    {
        return isNothing() ? elsevalue : func.apply(object);
    }

    /**
     * maybeCompute conditionally applies function of this.fromJust() depending on
     * whether this is a maybe or a just
     *
     * @param function the function to be applied if this is just
     * @param <S>      the type returned by function
     * @return nothing if this is nothing or just the wrapped return value of
     * function
     */
    public <S> Maybe<S> maybeCompute(Function<? super T, ? extends S> function)
    {
        return isNothing() ? Maybe.nothing() : Maybe.just(function.apply(this.object));
    }

    /**
     * Brutally unwrap maybe
     *
     * @return the value contained if this was a just
     * @throws RuntimeException if this is nothing
     */
    public T fromJust()
    {
        if (isNothing())
            throw new RuntimeException("Unwrapping a nothing object");
        return object;
    }

    /**
     * Brutally unwrap maybe specifying a supplier of RuntimeExceptions
     * to be thrown if this was a nothing
     *
     * @return the value contained if this was a just
     * @throws RuntimeException if this is nothing
     */
    public T unwrapOrThrow(Supplier<? extends RuntimeException> supplier)
    {
        if (isNothing())
            throw supplier.get();
        return object;
    }

    /**
     * Unwrap this if this is just or return the result of calling the alternative
     *
     * @param alternative a supplier that returns the alternative if this is
     *                    nothing. We choose to use a supplier and not a value
     *                    directly because we want to be lazy and compute the
     *                    alternative only if it's really needed
     * @return the alternative or the value contained in this depending
     * on whether or not
     */
    public T orElse(Supplier<? extends T> alternative)
    {
        return isJust() ? object : alternative.get();
    }

    /**
     * conditionally call the consumer depending on whether this is a just or not
     *
     * @param consumer the consumer of the element if this is just
     */
    public void maybeDo(Consumer<? super T> consumer)
    {
        if (isJust())
            consumer.accept(fromJust());
    }

    /**
     * doOrThrow calls toDoIfJust if this is just or it raises the exception supplied
     * by toThrowIfNothingSupplier
     *
     * @param toDoIfJust               the consumer that must be called is this is just
     * @param toThrowIfNothingSupplier a supplier of an exception to throw if this
     *                                 is nothing
     * @throws RuntimeException is this is nothing
     */
    public void doOrThrow(Consumer<? super T> toDoIfJust,
                          Supplier<? extends RuntimeException> toThrowIfNothingSupplier)
    {
        isJustOrThrow(toThrowIfNothingSupplier);
        toDoIfJust.accept(fromJust());
    }

    /**
     * isJustOrThrow conditionally throws the exception supplied by toThrowIfNothing
     * depending on whether this is just or not
     *
     * @param toThrowIfNothing supplier of the exception to throw
     * @throws RuntimeException is this is nothing
     */
    public void isJustOrThrow(Supplier<? extends RuntimeException> toThrowIfNothing)
    {
        if (isNothing())
            throw toThrowIfNothing.get();
    }

    /**
     * orElseWrapped specifies an alternative if this is nothing that is wrapped
     * and returned
     *
     * @param otherwise the alternative to wrap and return
     * @return the alternative wrapped or this depending on whether this is just
     * of nothing
     */
    public Maybe<T> orElseWrapped(T otherwise)
    {
        return (isJust()) ? this : Maybe.something(otherwise);
    }

    /**
     * computeOfThrow applies function toApply if this is something otherwise
     * it throws the exception supplied by toThrowIfNothing
     *
     * @param toApply          the function to call if this is just
     * @param toThrowIfNothing the supplier that gives the exception if this is
     *                         nothing
     * @param <S>              type parameter specifying the type of the return function of
     *                         to apply
     * @return the result of toApply applied to this if this is just
     * @throws RuntimeException is this is nothing
     */
    public <S> S computeOrThrow(Function<? super T, ? extends S> toApply,
                                Supplier<? extends RuntimeException> toThrowIfNothing)
    {
        return maybeCompute(toApply).unwrapOrThrow(toThrowIfNothing);
    }

    /**
     * doOrDo applies toApplyIfJust is this is just or toCallIfNothing is this
     * is nothing
     *
     * @param toApplyIfJust   a consumer that receives the object contained in
     *                        this if this is something
     * @param toCallIfNothing a callback to be called if this is nothing
     */
    public void doOrDo(Consumer<T> toApplyIfJust, Callback toCallIfNothing)
    {
        if (isJust())
            toApplyIfJust.accept(fromJust());
        else
            toCallIfNothing.run();
    }

    @Override
    public String toString()
    {
        return (isNothing()) ? "Nothing" : "Just(" + object + ")";
    }

    public T unwrap()
    {
        return unwrapOrThrow(() -> new RuntimeException("Error! Unwrapping nothing"));
    }
}
