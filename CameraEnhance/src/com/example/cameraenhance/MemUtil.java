package com.example.cameraenhance;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
 
public class MemUtil {
    public static FloatBuffer makeFloatBufferFromArray(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }
 
    /**
     * creates a floatbuffer of the given size.
     * 
     * @param size
     * @return
     */
    public static FloatBuffer makeFloatBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.position(0);
        return fb;
    }
 
    public static FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }
 
    public static IntBuffer makeIntBuffer(int[] buff) {
        ByteBuffer bb = ByteBuffer.allocateDirect(buff.length * 4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer sb = bb.asIntBuffer();
        sb.put(buff);
        sb.position(0);
        return sb;
    }
 
}