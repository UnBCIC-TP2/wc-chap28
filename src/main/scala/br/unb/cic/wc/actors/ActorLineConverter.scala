package br.unb.cic.wc.actors

import akka.actor.Actor

class ActorLineConverter extends Actor {

  override def receive = {
    case ProcessLine(line)=>
      val words = line.split(" ").map(w => w.replaceAll("[^a-zA-Z]", "").toLowerCase).toList
      sender() ! words
  }

}
