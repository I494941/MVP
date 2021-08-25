package com.mike.base.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.SPUtils
import com.mike.base.http.RxApiManager

/**
 * Author:  andy.xwt
 * Date:    2020-01-14 14:44
 * Description: Androidx 下支持栏加载的fragment
 * https://github.com/AndyJennifer/AndroidxLazyLoad
 */
abstract class LazyFragment : LogFragment() {

    var isLoaded = false
    var rootView : View? = null

    protected var sp = SPUtils.getInstance()
    var rxApiManager = RxApiManager()

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (rootView == null) {
            rootView = inflater.inflate(initLayout(), container, false)
        }
        initView(rootView!!)
        initData()
        return rootView
    }

    abstract fun initLayout() : Int

    open fun initView(rootView : View) {
    }

    open fun initData() {
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
        }
    }

    open fun lazyInit() {
        Log.e(TAG, "lazyInit:!!!!!!!")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
        if (rxApiManager != null) {
            rxApiManager.clear()
        }
    }

    /**
     * startActivity
     */
    open fun startActivity(clazz : Class<*>?) {
        startActivity(clazz, null)
    }

    /**
     * startActivity with bundle
     */
    open fun startActivity(clazz : Class<*>?, bundle : Bundle?) {
        val intent = Intent(activity, clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * startActivityForResult
     */
    protected open fun startActivityForResult(clazz : Class<*>?, requestCode : Int) {
        startActivityForResult(clazz, requestCode, null)
    }

    /**
     * startActivityForResult with bundle
     */
    protected open fun startActivityForResult(clazz : Class<*>?, requestCode : Int, bundle : Bundle?) {
        val intent = Intent(activity, clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }
}