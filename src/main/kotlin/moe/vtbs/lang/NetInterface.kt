/*
 * Copyright (C) 2021 一七年夏
 * 
 * The part of program is free: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published 
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/
 */
package moe.vtbs.lang

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import moe.vtbs.lang.annotation.Network
import moe.vtbs.lang.exception.NetException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.yaml.snakeyaml.Yaml
import java.io.IOException
import java.io.InputStream
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

/**
 *  网络工具，所有HTTP网络请求的基础接口
 *
 * @author 一七年夏
 * @since 2021-12-26 23:21
 */

open class NetInterface(
    /** OK HTTP 客户端 */
    open val okHttpClient: OkHttpClient = OkHttpClient()
) : CoroutineScope {
    companion object {
        val gson = Gson()
        val yaml = Yaml()

        /**
         * 检查响应，如果出现HTTP异常，返回NetException，否则返回null
         * @param response OK HTTP的响应体
         * @return NetException或null
         */
        fun checkResponse(response: Response): NetException? {
            if (response.code >= 400) return NetException.makeException(response.code, response);
            return null;
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    /**
     * 请求一个返回JSON的URL，返回一个对应的对象
     * @param url 地址
     * @param T 对象类型
     * @return 请求返回的对象
     */
    @Network
    suspend inline fun <reified T> getJObject(url: String): T {
        return gson.fromJson(getString(url), T::class.java)
    }

    /**
     * 请求一个返回YAML的URL，返回一个对应的对象
     * @param url 地址
     * @param T 对象类型
     * @return 请求返回的对象
     */
    @Network
    suspend inline fun <reified T> getYObject(url: String): T {
        return yaml.loadAs(getString(url), typeOf<T>().jvmErasure.java) as T
    }

    /**
     * 请求一个URL，返回文本
     * @param url 地址
     * @return 请求返回的文本
     */
    @Network
    suspend fun getString(url: String): String = get(url) {
        return@get body!!.string()
    }

    /**
     * 请求一个URL，返回字节数组
     * @param url 地址
     * @return 请求返回的字节数组/数据
     */
    @Network
    suspend fun getByteArray(url: String): ByteArray = get(url) {
        return@get body!!.bytes()
    }

    /**
     * 请求一个URL，返回输入流
     * @param url 地址
     * @return 请求返回的输入流
     */
    @Network
    suspend fun getInputStream(url: String): InputStream = get(url) {
        return@get body!!.byteStream()
    }

    /**
     * 请求一个URL，在块级函数中传入响应体，返回块级函数的返回值
     * @param url 地址
     * @param response this 指向 OK HTTP 响应结构体的一段块级函数
     * @param T 返回类型
     * @return 返回块级函数的返回值
     * @see Response OK HTTP的响应体
     */
    @OptIn(Network::class)
    suspend inline fun <reified T> get(url: String, response: Response.() -> T): T {
        return response(get(url))
    }

    /**
     * 请求一个URL，返回一个OK HTTP响应体
     * @param url 地址
     * @return OK HTTP的响应体
     */
    @Network
    suspend fun get(url: String): Response {
        val request = Request.Builder().url(url).get().build()
        val call = okHttpClient.newCall(request)
        return suspendCancellableCoroutine { sc ->
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    sc.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    checkResponse(response)
                        ?.let { sc.resumeWithException(it) }
                        ?: sc.resume(response)
                }
            })
        }
    }

    /**
     * 向指定URL中放置（PUT）一段字节数组，类型是MIME类型
     * @param url 地址
     * @param data 字节数组/数据
     * @param type MIME类型
     * @return OK HTTP响应体
     * @see Response
     */
    @Network
    suspend fun put(url: String, data: ByteArray, type: String): Response {
        val request = Request.Builder().url(url).put(data.toRequestBody(type.toMediaType())).build()
        val call = okHttpClient.newCall(request)
        return suspendCancellableCoroutine { sc ->
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    sc.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    checkResponse(response)
                        ?.let { sc.resumeWithException(it) }
                        ?: sc.resume(response)
                }

            })
        }
    }

    /**
     * 向指定URL中放置（PUT）一段字节数组，类型是MIME类型
     * @param url 地址
     * @param data 字节数组/数据
     * @param type MIME类型
     * @param response this 指向 OK HTTP 响应结构体的一段块级函数
     * @param T 返回类型
     * @return 返回块级函数的返回值
     * @see Response
     */
    @OptIn(Network::class)
    suspend inline fun <reified T> put(
        url: String,
        data: ByteArray,
        type: String,
        response: Response.() -> T
    ): T {
        return response(put(url, data, type))
    }

    /**
     * 向指定URL中放置（PUT）一段JSON数据，返回一个 OK HTTP 响应体
     * @return OK HTTP 响应结构体
     * @see Response
     */
    @Network
    suspend fun putJson(url: String, json: String) =
        put(url, json.toByteArray(), "application/json; charset=utf-8")

}