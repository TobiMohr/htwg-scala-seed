package de.htwg.se.Skip_Bo.util

import de.htwg.se.Skip_Bo.model.{InvalidHandCard, InvalidMove}

class TestObject extends Observer {
  def update:Unit = println("ping")

  override def error(throwable: Throwable): Unit = throwable match{
    case InvalidHandCard(i) => println("Falscher Index: " + i)
    case InvalidMove => println("Dieser Zug geht nicht!")
  }
}
object ObserverPattern {
  val observable = new Observable                 //> observable  : de.htwg.util.Observable = de.htwg.util.Observable@23394894
  val observer1 = new TestObject                  //> observer1  : TestObject = TestObject@630045eb
  val observer2 = new TestObject                  //> observer2  : TestObject = TestObject@26ee7a14
  observable.add(observer1)
  observable.add(observer2)
  observable.notifyObservers                      //> Ping
  //| Ping
  observable.remove(observer1)
  observable.notifyObservers                      //> Ping
  observable.remove(observer2)
  observable.notifyObservers
}