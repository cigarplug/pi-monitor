// package PiMonitor


import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.RaspiPin

import sys.process._

import akka.actor.{Actor, Props, ActorSystem}
import scala.concurrent.duration._


object sysUser {
  val who: String = "who".!!

}

class Blinker extends Actor {

  private val gpio = GpioFactory.getInstance
  private val led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08)

  def receive = {
    case x if x.toString.contains("pi")  => {
      led.pulse(220)
      // led.low()
    }

    case _ => {
      led.pulse(500)
      println("nobody home")
      // led.low()
    }
  }
}

object ledStrip  {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem()

    val blinker = system.actorOf(Props[Blinker], "blinker")
    import system.dispatcher

    val cancellable =
      system.scheduler.schedule(
        50 milliseconds,
        3000 milliseconds,
        blinker,
        sysUser.who)

    // cancellable.cancel()
  }
}