package br.unb.cic.wc.actors

trait Message

case class ReadFile(path: String) extends Message
case object StartExecution extends Message
case class ProcessLine(line: String) extends Message
case class ProcessWord(word: String) extends Message
case class TakeFirst(n: Integer) extends Message
