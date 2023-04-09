package com.phosphorus.extension

import com.lark.oapi.core.response.BaseResponse
import java.lang.RuntimeException

private val DEFAULT_THROWABLE: (resp: BaseResponse<*>) -> Throwable = { RuntimeException(it.msg) }

fun <DATA> BaseResponse<DATA>.ensureOk(throwable: (resp: BaseResponse<DATA>) -> Throwable = DEFAULT_THROWABLE) {
    if (!this.success()) {
        throw throwable.invoke(this)
    }
}

fun <DATA> BaseResponse<DATA>.ensureData(throwable: (resp: BaseResponse<DATA>) -> Throwable = DEFAULT_THROWABLE): DATA {
    ensureOk(throwable)
    return this.data
}
