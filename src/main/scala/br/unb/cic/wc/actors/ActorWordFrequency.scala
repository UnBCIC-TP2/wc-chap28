package br.unb.cic.wc.actors

import akka.actor.Actor

import scala.collection.immutable.ListMap

class ActorWordFrequency extends Actor {

  var frequencies = scala.collection.mutable.Map[String, Integer]()

  override def receive = {
    case ProcessWord(word) =>
      processWord(word)
    case TakeFirst(n) => {
      sender() ! ListMap(frequencies.toSeq.sortWith(_._2 > _._2):_*).take(n)
    }
  }

  def processWord(word: String): Unit = {
    val current : Integer = frequencies.getOrElse(word, 0)
    frequencies(word) = current + 1
  }

}
