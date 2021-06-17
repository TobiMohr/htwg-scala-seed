package controller


import de.htwg.se.Skip_Bo.controller.Controller
import de.htwg.se.Skip_Bo.controller.PushCardHandCommand
import de.htwg.se.Skip_Bo.model.{Card, Game, Player, Value}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PushCardHandCommandSpec extends AnyWordSpec with Matchers {
  "A PushCardHandCommand" when {
    "new" should {
      val player1 = new Player("A",
        List(Card(Value.Seven), Card(Value.Eleven), Card(Value.Five), Card(Value.Twelve), Card(Value.Two)),
        List(List(Card(Value.Three)), List(Card(Value.Eleven)), List(Card(Value.Three)), List(Card(Value.Five))),
        List(Card(Value.Five), Card(Value.Joker), Card(Value.Twelve), Card(Value.Two), Card(Value.Four)))
      val player2 = new Player("B",
        List(Card(Value.Four), Card(Value.Ten), Card(Value.Eight), Card(Value.Seven), Card(Value.Seven)),
        List(List(Card(Value.Four),Card(Value.Six)), List(Card(Value.Eleven)), List(Card(Value.Ten)), List(Card(Value.Eight))),
        List(Card(Value.One), Card(Value.Joker), Card(Value.Joker), Card(Value.Two), Card(Value.Seven)))

      val game = Game(List(List(Card(Value.Three), Card(Value.Two), Card(Value.One)),
        List(Card(Value.Two), Card(Value.Joker)),
        List(Card(Value.One)),
        List(Card(Value.Eleven), Card(Value.Ten), Card(Value.Nine), Card(Value.Eight), Card(Value.Seven), Card(Value.Six)
          , Card(Value.Five), Card(Value.Four), Card(Value.Joker), Card(Value.Two), Card(Value.One))),
        List(player1, player2),
        List(Card(Value.Three), Card(Value.Seven), Card(Value.Eight), Card(Value.Twelve), Card(Value.Joker),
          Card(Value.Five), Card(Value.Six), Card(Value.Nine), Card(Value.Eight)))
      val controller = new Controller(game)
      val command1 = new PushCardHandCommand(0, 0, 1, false, controller)
      val command2 = new PushCardHandCommand(3, 3, 0, false, controller)
      val command3 = new PushCardHandCommand(0, 3, 0, true, controller)

      "newGame" in {
        command1.doStep
        controller.game.stack(0).size should be(4)
        controller.game.player(1).cards.size should be(4)
      }
      "back to old" in {
        command1.undoStep
        controller.game.stack(0).size should be(3)
        controller.game.player(1).cards.size should be(5)
      }
      "go back to newGame again" in {
        command1.redoStep
        controller.game.stack(0).size should be(4)
        controller.game.player(1).cards.size should be(4)
      }
      "refill?" in {
        command1.undoStep
        command2.doStep
        controller.game.stack(3).size should be(0)
        controller.game.cardsCovered.size should be(21)
      }
      "undo refill" in {
        command2.undoStep
        controller.game.stack(3).size should be(11)
        controller.game.cardsCovered.size should be(9)
      }
      "redo refill" in {
        command2.redoStep
        controller.game.stack(3).size should be(0)
        controller.game.cardsCovered.size should be(21)
      }
      "command3" in {
        command2.undoStep
        command3.doStep
        controller.game.player(0).cards.size should be(4)
      }
    }
  }


}
