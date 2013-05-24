package io.ifar.skidroad.jersey.single;

import com.google.common.base.Function;
import io.ifar.goodies.Triple;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import javax.annotation.Nullable;

/**
 * Support for use case of using Skid Road to record (request id, tag, data) triples.
 */
public class IDTagTripleTransformFactory {

    /**
     * Generates a transformer that wraps data in a Triple of (request id, tag, data).
     */
    public static <T> Function<T,Triple<String,String,T>> passThrough(final String typeTag) {
        return new Function<T, Triple<String, String, T>>() {
            @Nullable
            @Override
            public Triple<String, String, T> apply(@Nullable T input) {
                return new Triple<>(
                        UUIDGeneratorFilter.getID(),
                        typeTag,
                        input
                );
            }
        };
    }

    /**
     * Generates a transformer that wraps data in a Triple of (request id, tag, transformed data).
     */
    public static <F,T> Function<F,Triple<String,String,T>> transform(final String typeTag, final Function<F, T> dataTransform) {
        return new Function<F, Triple<String, String, T>>() {
            @Nullable
            @Override
            public Triple<String, String, T> apply(@Nullable F input) {
                return new Triple<>(
                        UUIDGeneratorFilter.getID(),
                        typeTag,
                        dataTransform.apply(input)
                );
            }
        };
    }

    /**
     * Generates a transform that wraps DateTime data in a Triple of (request id, data type tag, ISO datetime representation)
     */
    public static Function<DateTime,Triple<String,String,String>> isoDateTime(final String typeTag) {
        return transform(typeTag, new Function<DateTime, String>() {
            @Nullable
            @Override
            public String apply(@Nullable DateTime input) {
                return ISODateTimeFormat.basicDateTime().print(input);
            }
        });
    }

    /**
     * Generates a transform that toString's Object data into a Triple of (request id, data type tag, string representation)
     */
    public static <T extends Object> Function<T,Triple<String,String,String>> string(final String typeTag) {
        return transform(typeTag, new Function<T, String>() {
            @Nullable
            @Override
            public String apply(@Nullable T input) {
                return input == null ? null : input.toString();
            }
        });
    }
}
