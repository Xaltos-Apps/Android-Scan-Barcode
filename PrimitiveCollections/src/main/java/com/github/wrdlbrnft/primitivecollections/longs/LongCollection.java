package com.github.wrdlbrnft.primitivecollections.longs;

import com.github.wrdlbrnft.primitivecollections.PrimitiveCollection;
import androidx.annotation.Keep;

/**
 * Created with Android Studio<br>
 * User: Xaver<br>
 * Date: 09/12/2016
 */
@Keep
public interface LongCollection extends PrimitiveCollection {
    boolean contains(long value);
    boolean add(long value);
    long get(int index);
    long[] toArray();
}
