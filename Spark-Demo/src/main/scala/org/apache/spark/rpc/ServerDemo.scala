package org.apache.spark.rpc

import org.apache.spark.{SecurityManager, SparkConf}

object ServerDemo {
  def main(args: Array[String]) {
    // 初始化RpcEnv环境
    val conf = new SparkConf
    val rpcEnv: RpcEnv = RpcEnv.create("hello-server", "localhost", 52345, conf, new SecurityManager(conf))

    // 当前RpcEnv设置后端服务
    val helloEndpoint: RpcEndpoint = new EndpointDemo(rpcEnv)
    rpcEnv.setupEndpoint("hello-service", helloEndpoint)

    // 等待客户端访问该后端服务
    rpcEnv.awaitTermination()
  }
}

class EndpointDemo(override val rpcEnv: RpcEnv) extends RpcEndpoint {
  override def onStart(): Unit = {
    val millis = java.lang.System.currentTimeMillis
    println(s"$millis \tstart hello endpoint")
  }

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case SayHi(msg) =>
      val millis = java.lang.System.currentTimeMillis
      println(s"$millis receive $msg")
      context.reply(s"hi, $msg")
    case SayBye(msg) =>
      val millis = java.lang.System.currentTimeMillis
      println(s"$millis receive $msg")
      context.reply(s"bye, $msg")
  }

  override def onStop(): Unit = {
    val millis = java.lang.System.currentTimeMillis
    println(s"$millis stop hello endpoint")
  }
}

case class SayHi(msg: String)

case class SayBye(msg: String)
