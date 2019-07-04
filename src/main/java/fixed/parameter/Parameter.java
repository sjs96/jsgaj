package fixed.parameter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    public static @interface Must{ String[] value(); }
}
