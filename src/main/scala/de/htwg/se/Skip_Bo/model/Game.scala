package de.htwg.se.Skip_Bo.model


import de.htwg.se.Skip_Bo.controller.{PlayerA, PlayerState}
import de.htwg.se.Skip_Bo.model.Value

import scala.util.{Failure, Random, Success, Try}

case class Game(stack: List[List[Card]] = (0 until 4).map(_ => List.empty).toList,
                player: List[Player] = List.empty,
                cardsCovered: List[Card] = List.empty
               ) {

  //baut Grundspiel auf
  def startGame(numOfPlayerCards: Int): Game = {
    //erstellt Kartendeck
    val c = Random.shuffle(Value.values.toList.flatMap(v => {
      val count = v match {
        case Value.Joker => 18
        case _ => 12
      }
      (1 to count).map(_ => Card(v))
    }))

    // erstellt Handkarten und Spielerstapel von den Spielern
    val (cards, player) = List("A", "B").foldLeft((c, List.empty[Player]))((t, plname) => {
      val (plcards, cards) = t._1.splitAt(numOfPlayerCards)
      val (plstack, cards2) = cards.splitAt(30)
      val p = Player(name = plname, cards = plcards, stack = plstack)
      (cards2, t._2 :+ p)
    })

    copy(cardsCovered = cards, player = player)
  }

  //legt Handkarte auf Ablegestapel oder Hilfstapel von Spieler
  //i=Welcher Hilfs- oder Ablagestapel (Index), j=Index Handkarten, n=Spieler, helpst=(true=Hilfsstapel),(false=Ablegestapel)
  def pushCardHand(i: Int, j: Int, n: Int, helpst: Boolean): Try[Game] = {
    val p = player(n)
    val s = stack(i)
    val c = p.getCard(j)
    c match {
      case Failure(exception) => Failure(exception)
      case Success((card, newpl)) =>
        if (helpst) {
          p.putInHelp(i, card)
          Success(copy(player = player.updated(n, newpl)))
        } else {
          if (!checkCardHand(card, s)) {
            Failure(InvalidMove)
          } else {
            val s2 = card +: s
            Success(copy(stack = stack.updated(i, s2), player = player.updated(n, newpl)))
          }
        }
    }
  }

  //legt Karte vom Hilfsstapel auf Ablegestapel
  def pushCardHelp(i: Int, j: Int, n: Int): Try[Game] = {
    val s = stack(j)
    val p = player(n)
    p.helpCard(i) match {
      case Failure(exception) => Failure(exception)
      case Success((card, newpl)) =>
        if (!checkCardHand(card, s)) {
          Failure(InvalidMove)
        } else {
          val s2 = card +: s
          Success(copy(stack = stack.updated(j, s2), player = player.updated(n, newpl)))
        }
    }
  }

  //legt Karte vom Spielerstapel auf Ablegestapel
  def pushCardPlayer(i: Int, n: Int): Try[Game] = {
    val s = stack(i)
    val p = player(n)
    p.stackCard() match {
      case Failure(exception) => Failure(exception)
      case Success((card, newpl)) =>
        if (!checkCardHand(card, s)) {
          Failure(InvalidMove)
        } else {
          val s2 = card +: s
          Success(copy(stack = stack.updated(i, s2), player = player.updated(n, newpl)))
        }
    }
  }

  def pull(n: Int): Game = {
    val p = player(n)
    while (p.cards.length < 5) {
      p.cards +: Card(cardsCovered.head.value).toString
      cardsCovered.drop(1)
    }
    this
  }

  def checkCardHand(card: Card, stack: List[Card]): Boolean = {
    if (stack.isEmpty) {
      if (card.toString == "1" || card.toString == "J") {
        return true
      }
      return false
    } else {
      if (card.toString != "J") {
        if (stack.head.toString != "J") {
          if ((card.toString.toInt) - 1 == stack.head.toString.toInt) {
            return true
          }
        } else {
          if (card.toString.toInt - 2 == stack(1).toString.toInt) { //geht nicht wenn zwei Joker übereinander liegen
            return true
          }
        }
      }
      if (card.toString == "J") {
        if (stack.head.toString == "J") {
          return false
        } else {
          return true
        }
      }
    }
    false
  }

  def toString(n: Int): String = {

    val l = for (i <- 1 to player(n).cards.length) yield
      ("| " + player(n).cards(i - 1).toString + " | ")
    val b = if (player(n).helpstack(0).isEmpty) {
      ("| leer | ")
    } else {
      ("| " + player(n).helpstack(0).head.toString + " | ")
    }
    val c = if (player(n).helpstack(1).isEmpty) {
      ("| leer | ")
    } else {
      ("| " + player(n).helpstack(1).head.toString + " | ")
    }
    val d = if (player(n).helpstack(2).isEmpty) {
      ("| leer | ")
    } else {
      ("| " + player(n).helpstack(2).head.toString + " | ")
    }
    val e = if (player(n).helpstack(3).isEmpty) {
      ("| leer | ")
    } else {
      ("| " + player(n).helpstack(3).head.toString + " | ")
    }
    val f = ("| " + player(n).stack.head.toString + " | ")
    val m = ("| " + player(n).stack.length + " |")
    val g = if (stack(0).isEmpty) {
      ("| leer | ")
    } else {
      ("| " + stack(0).head.toString + " | ")
    }
    val h = if (stack(1).isEmpty) {
      ("| leer | ")
    } else {
      ("| " + stack(1).head.toString + " | ")
    }
    val j = if (stack(2).isEmpty) {
      ("| leer | ")
    } else {
      ("| " + stack(2).head.toString + " | ")
    }
    val k = if (stack(3).isEmpty) {
      ("| leer | ")
    } else {
      ("| " + stack(3).head.toString + " | ")
    }

    val playField = "Handkarten: " + l + "\n\n" + "Hilfsstapel: " + b + "\t" + c + "\t" + d + "\t" + e +
      "\t" + "Spielerstapel: " + f + "\t" + m + "\n\n" + "Ablagestapel: " + g + "\t" +
      h + "\t" + j + "\t" + k + "\t"

    playField

  }
}
