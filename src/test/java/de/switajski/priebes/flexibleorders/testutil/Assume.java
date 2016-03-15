package de.switajski.priebes.flexibleorders.testutil;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class Assume {

    public static <T> void assumeThat(
            T actual,
            Predicate<T> expected) {
        assumeThat(actual, expected, "Test failed");
    }

    public static <T> void assumeThat(
            T actual,
            Predicate<T> expected,
            String message) {
        assumeThat(() -> actual, expected, message);
    }

    public static <T> void assumeThat(
            Supplier<T> actual,
            Predicate<T> expected) {
        assumeThat(actual, expected, "Test failed");
    }

    public static <T> void assumeThat(
            Supplier<T> actual,
            Predicate<T> expected,
            String message) {
        if (!expected.test(actual.get())) throw new AssertionError(message);
    }

}
