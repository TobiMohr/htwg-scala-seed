package de.htwg.se.Skip_Bo.model

import de.htwg.se.Skip_Bo.Skip_Bo

class TUI {
  while(true) {
    val scanner = new java.util.Scanner(System.in)
    val line = scanner.nextLine
    line match {
      case "p1" => println("legt Karte auf 1. Spieler Stack")
      case "p2" => println("legt Karte auf 2. Spieler Stack")
      case "p3" => println("legt Karte auf 3. Spieler Stack")
      case "p4" => println("legt Karte auf 4. Spieler Stack")
      case "m1" => println("legt Karte auf 1. Mittel Stack")
      case "m2" => println("legt Karte auf 2. Mittel Stack")
      case "m3" => println("legt Karte auf 3. Mittel Stack")
      case "m4" => println("legt Karte auf 4. Mittel Stack")
      case "end" => Beenden
    }

    def Beenden: Unit = {
      println("Der Zug ist beendet")
      println("Nächster Spieler ist am Zug")
    }

  }
}
