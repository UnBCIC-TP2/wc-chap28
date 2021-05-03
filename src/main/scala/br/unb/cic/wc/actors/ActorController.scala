package br.unb.cic.wc.actors

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.immutable.ListMap
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.Source
import scala.language.postfixOps

class ActorController(val lcm: ActorRef, val swm : ActorRef, val wfm : ActorRef) extends Actor {
  var lines: Iterator[String] = _

  override def receive = {
    case ReadFile(path) =>
      lines = processFile(path)
    case StartExecution =>
      implicit val timeout = Timeout(5 seconds)
      for(line <- lines) {
        var future = lcm ? ProcessLine(line)
        val words = Await.result(future, timeout.duration).asInstanceOf[List[String]]
        for(word <- words) {
          future = swm ? ProcessWord(word)
          val isStopWord = Await.result(future, timeout.duration).asInstanceOf[Boolean]
          if (!isStopWord) {
            wfm ! ProcessWord(word)
          }
        }
      }
      val future = wfm ? TakeFirst(20)
      val frequentWords = Await.result(future, timeout.duration).asInstanceOf[ListMap[String, Int]]
      frequentWords.foreach(r => println(r))

      context.system.terminate()
  }

  def processFile(path: String): Iterator[String] = Source.fromFile(path).getLines()
}
