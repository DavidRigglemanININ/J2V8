package com.eclipsesource.v8;

abstract public class V8Value {

    public static final int UNDEFINED = 0;
    public static final int VOID = 0;
    public static final int INTEGER = 1;
    public static final int DOUBLE = 2;
    public static final int BOOLEAN = 3;
    public static final int STRING = 4;
    public static final int V8_ARRAY = 5;
    public static final int V8_OBJECT = 6;

    protected static int v8ObjectInstanceCounter = 1;
    protected V8 v8;
    protected int objectHandle;
    protected boolean released = true;

    public V8Value() {
        super();
    }

    protected void initialize(final int runtimeHandle, final int objectHandle) {
        v8._initNewV8Object(runtimeHandle, objectHandle);
        v8.addObjRef();
        released = false;
    }

    public int getHandle() {
        return objectHandle;
    }

    public void release() {
        V8.checkThread();
        if ( !released ) {
            released = true;
            v8._release(v8.getV8RuntimeHandle(), objectHandle);
            v8.releaseObjRef();
        }
    }

    @Override
    public boolean equals(final Object that) {
        V8.checkThread();
        checkReleaesd();
        if ((that instanceof V8Object)) {
            return v8._equals(v8.getV8RuntimeHandle(), getHandle(), ((V8Object) that).getHandle());
        }
        return false;
    }

    @Override
    public int hashCode() {
        V8.checkThread();
        checkReleaesd();
        return v8._identityHash(v8.getV8RuntimeHandle(), getHandle());
    }

    public boolean isReleased() {
        return released;
    }

    @Override
    public abstract String toString();

    protected void checkReleaesd() {
        if (released) {
            throw new IllegalStateException("Object released");
        }
    }

}