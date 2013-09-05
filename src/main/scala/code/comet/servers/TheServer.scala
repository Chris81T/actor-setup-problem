package code.comet.servers

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import org.slf4j.LoggerFactory

case object WelcomeActor

object TheServer extends LiftActor with ListenerManager {

    private val logger = LoggerFactory.getLogger(TheServer.getClass)

    def createUpdate = WelcomeActor

    override def lowPriority = {
      case _ =>
    }

}
