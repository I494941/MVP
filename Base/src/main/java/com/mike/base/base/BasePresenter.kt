package com.mike.base.base

import com.blankj.utilcode.util.SPUtils

/** created by  wjf  at 2021/8/2 15:40 */
interface BasePresenter {

    var sp : SPUtils
        get() = SPUtils.getInstance()
        set(value) = TODO()
}