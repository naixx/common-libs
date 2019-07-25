package common;

import androidx.annotation.NonNull;

public class K {

    public static <T extends Enum<T>> T enumValueOf(Class<T> klass, @NonNull String value) {
        return Enum.valueOf(klass, value);
    }
}
