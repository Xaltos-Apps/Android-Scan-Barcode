package com.github.wrdlbrnft.primitivecollections.doubles;

import androidx.annotation.Keep;

import com.github.wrdlbrnft.primitivecollections.PrimitiveCollection;

/**
 * Created with Android Studio<br>
 * User: Xaver<br>
 * Date: 09/12/2016
 */
@Keep
public interface DoubleCollection extends PrimitiveCollection {
    boolean contains(double value);

    boolean add(double value);

    double get(int index);

    double[] toArray();
}
