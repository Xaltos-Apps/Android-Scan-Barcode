package com.github.wrdlbrnft.primitivecollections.bytes;

import androidx.annotation.Keep;

import com.github.wrdlbrnft.primitivecollections.PrimitiveCollection;

/**
 * Created with Android Studio<br>
 * User: Xaver<br>
 * Date: 09/12/2016
 */
@Keep
public interface ByteCollection extends PrimitiveCollection {
    boolean contains(byte value);

    boolean add(byte value);

    byte get(int index);

    byte[] toArray();
}
