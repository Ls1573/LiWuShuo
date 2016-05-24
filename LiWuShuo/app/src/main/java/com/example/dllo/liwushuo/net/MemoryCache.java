package com.example.dllo.liwushuo.net;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by dllo on 16/5/23.
 */
public class MemoryCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> lruCache;


    public MemoryCache() {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8 / 1024);
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getHeight() * value.getRowBytes() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {

        return lruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }
}
