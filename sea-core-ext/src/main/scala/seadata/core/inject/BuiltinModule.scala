/*
 * Copyright (c) Yangbajing 2018
 *
 * This is the custom License of Yangbajing
 */

//package seadata.core.inject
//
//import javax.inject.{Provider, Singleton}
//
//import akka.actor.ActorSystem
//import akka.stream.{ActorMaterializer, Materializer}
//import com.typesafe.config.Config
//import helloscala.common.{AppLifecycle, Configuration}
//import seadata.core.server.{DefaultAppLifecycle, SeaAbstractModule, SeaServer}
//
//import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
//
//@Singleton
//class ActorSystemProvider extends Provider[ActorSystem] {
//  override def get(): ActorSystem = SeaServer.actorSystem
//}
//
//@Singleton
//class ActorMaterializerProvider() extends Provider[ActorMaterializer] {
//  override def get(): ActorMaterializer = SeaServer.actorMaterializer
//}
//
//@Singleton
//class ExecutionContextExecutorProvider() extends Provider[ExecutionContextExecutor] {
//  override def get(): ExecutionContextExecutor = SeaServer.actorSystem.dispatcher
//}
//
//@Singleton
//class ConfigurationProvider() extends Provider[Configuration] {
//  override def get(): Configuration = SeaServer.configuration
//}
//
//@Singleton
//class ConfigProvider() extends Provider[Config] {
//  override def get(): Config = SeaServer.config
//}
//
//class BuiltinModule extends SeaAbstractModule {
//
//  override def setup(): Unit = {
//    bind(classOf[ActorSystem]).toProvider(classOf[ActorSystemProvider])
//
//    bind(classOf[Config]).toProvider(classOf[ConfigProvider])
//    bind(classOf[Configuration]).toProvider(classOf[ConfigurationProvider])
//
//    bind(classOf[AppLifecycle]).to(classOf[DefaultAppLifecycle])
//    bind(classOf[ActorMaterializer]).toProvider(classOf[ActorMaterializerProvider])
//    bind(classOf[Materializer]).to(classOf[ActorMaterializer])
//
//    bind(classOf[ExecutionContextExecutor]).toProvider(classOf[ExecutionContextExecutorProvider])
//    bind(classOf[ExecutionContext]).to(classOf[ExecutionContextExecutor])
//  }
//
//}
