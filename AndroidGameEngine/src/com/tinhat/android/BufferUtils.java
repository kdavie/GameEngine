package com.tinhat.android;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class BufferUtils
{
	static
	{
		System.loadLibrary("Utils"); // Notice lack of lib prefix
	}
   /**
    * Copies the full src array to the given ByteBuffer. The
    * ByteBuffer is assumed to be a direct buffer. The method
    * will crash if that is not the case! The position of the
    * buffer is set to 0 and the limit is set according to numFloats
    * depending on the buffer type, e.g. for FloatBuffer it is numFloats
    * for a ByteBuffer it is numFloats * 4; This will only work with
    * ByteBuffers or FloatBuffers!
    * 
    * @param src the source array
    * @param dst the buffer, must be either a ByteBuffer or a FloatBuffer
    * @param numFloats the number of floats to copy from src
    * @param offset the offset in floats from which to start copying from src
    */
   public static void copy( float[] src, Buffer dst, int numFloats, int offset )
   {		
      copyJni( src, dst, numFloats, offset );
      dst.position(0);
 
      if( dst instanceof ByteBuffer )
         dst.limit(numFloats << 2);
      else
      if( dst instanceof FloatBuffer )
         dst.limit(numFloats);		
	   
 
   }
   
   private native static void copyJni (float[] src, Buffer dst, int numFloats, int offset); /*
   memcpy(dst, src + offset, numFloats << 2 );
*/
}
