package me.bscal.bettershields.bettershields.common.util;

import java.util.function.Supplier;

public class BakedArray
{

    public float[] Array;
    public float MinValue;
    public float MaxValue;
    public Supplier<Float> ValueSupplier;

    public BakedArray(int resolution, Supplier<Float> valueSupplier)
    {
        Array = new float[resolution];
        ValueSupplier = valueSupplier;
    }

    public void Bake()
    {
        for (int i = 0; i < Array.length; ++i)
        {
            Array[i] = ValueSupplier.get();
        }
    }

    public float GetValue(float normalizedValue)
    {
        int index = Math.round(normalizedValue * Array.length);
        return Array[index];
    }

}
