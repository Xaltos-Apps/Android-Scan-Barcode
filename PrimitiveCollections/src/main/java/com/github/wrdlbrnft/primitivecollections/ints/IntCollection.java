package com.github.wrdlbrnft.primitivecollections.ints;

import com.github.wrdlbrnft.primitivecollections.PrimitiveCollection;
import androidx.annotation.Keep;

/**
 * Created with Android Studio<br>
 * User: Xaver<br>
 * Date: 09/12/2016
 */
@Keep
public interface IntCollection extends PrimitiveCollection {
    boolean contains(int value);
    boolean add(int value);
    int get(int index);
    int[] toArray();
}
