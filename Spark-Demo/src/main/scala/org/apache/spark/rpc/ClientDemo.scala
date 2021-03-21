package org.apache.spark.rpc

import org.apache.spark.{SecurityManager, SparkConf}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
/**
 * ClassName: HelloWorldClient 
 * Description:
 * date: 2021/3/19 11:54
 *
 * @author zhy
 * @version
 * @since JDK 1.8
 */
object ClientDemo {
  def main(args: Array[String]) {
    // 初始化RpcEnv环境
    val conf = new SparkConf
    // 这里的rpc环境主机需要指定本机，端口号可以任意指定
    val rpcEnv = RpcEnv.create("hello-client", "localhost", 52346, conf, new SecurityManager(conf))

    // 根据Server端IP + Port获取后端服务的引用,得到的是RpcEndpointRef类型对象
    val endpointRef = rpcEnv.setupEndpointRef(RpcAddress("localhost", 52345), "hello-service")

    // 1、客户端异步请求
    // 客户端通过RpcEndpointRef#ask方法异步访问服务端,服务端通过RpcEndpoint#receiveAndReply方法获取到该请求后处理
    val future = endpointRef.ask[String](SayBye("neo"))


    // 客户端请求成功/失败时的处理方法
    future.onComplete {
      case scala.util.Success(value) ⇒ println(s"Got the result = $value")
      case scala.util.Failure(e) => println(s"Got error: $e")
    }
    // 客户端等待超时时间
    Await.result(future, Duration("5s"))

    // 2、客户端同步请求
//    val resp = endpointRef.askWithRetry[String](SayHi("hehe"))
    val resp = endpointRef.ask[String](SayHi("hehe"))
    print(resp)
  }
}

