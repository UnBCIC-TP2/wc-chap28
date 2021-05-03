package br.unb.cic.wc.actors

import akka.actor.{ActorSystem, PoisonPill, Props}

object Main extends App {
  val system = ActorSystem("WordCount")

  // register all actors into the WordCount system.
  val alc = system.actorOf(Props[ActorLineConverter])
  val asw = system.actorOf(Props[ActorStopWord])
  val awf = system.actorOf(Props[ActorWordFrequency])

  // the controller actor has references to the other
  // actors.
  val controller = system.actorOf(Props(classOf[ActorController],alc, asw, awf))

  controller ! ReadFile("/Users/rbonifacio/tmp/gutenberg/republic.txt")
  controller ! StartExecution
}
