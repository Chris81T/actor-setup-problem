package code.comet

import net.liftweb.http.{SHtml, CometListener, CometActor}
import net.liftweb.common._
import net.liftweb.http.js.JsCmds._
import code.comet.servers._
import net.liftweb.common.Full
import scala.Some
import code.lib.{User, Tools}
import net.liftweb.http.js.JE


case class SetupSessionUserActor(user: User, sessionId: String)

/**
 * is used by different actors, that have a name, that includes information about the current session and the logged in
 * user.
 */
abstract class AbstractSessionUserActor extends CometActor with CometListener {

  protected var sessionId: Box[String] = Empty
  protected var user: Option[User] = None

  override def name = sessionId map { id => Tools.getUniqueActorId(id, user) }

  /**
   * will setup the actor
   */
  def setupActor(user: User, sessionId: String) {
    println ("setup session-user-actor type with user = {} and session-id = {}", user, sessionId)

    // it is important to set first the user
    this.user = Some(user)
    this.sessionId = Full(sessionId)
  }

}

class TheActor extends AbstractSessionUserActor with SomeFunctionalMethods {

  def registerWith = TheServer

  override def lowPriority = {
    case WelcomeActor                           =>
      println("THE_ACTOR :: RECEIVED WELCOME_ACTOR MESSAGE FROM THE SERVER -- CHECK THAT THIS IS AFTER THE SETUP MECHANISM .. .. ..")

      println("THE_ACTOR :: TRY TO MAP USER = %s GIVEN FROM SETUP PART" format (user))

      user.map {user =>
        println ("THE_ACTOR :: check, if already a compose-actor is known for given user = {}, " +
          "that should also be set in this actor = {} INIT CONTINUE COMPOSING METHOD WITH USER = " , user, toString)
      }

      println ("THE_ACTOR :: FINISHED PART - MAP USER TO INITIALIZE CONTINUE COMPOSING OPTION :: :: :: :: :: :: :: :: ::")

    case SetupSessionUserActor(user, sessionId) =>

      println ("[][][][][][][][][][][][][][] [][][][][][][][][][][][][][] THE_ACTOR __ BEGIN __ SETUP SESSION USER ACTOR [][][][][][][][][][][][][][] [][][][][][][][][][][][][][]")
      setupActor(user, sessionId)
      partialUpdate(JE.Call("ContinueComposingAPI.showOption") cmd)
      println ("[][][][][][][][][][][][][][] [][][][][][][][][][][][][][] THE_ACTOR __ FIN __ SETUP SESSION USER ACTOR [][][][][][][][][][][][][][] [][][][][][][][][][][][][][]")
  }

  def render = {
    println ("THE_ACTOR :: R E N D E R : : : : : . . . . . .")

    if (user isEmpty) println ("THE ACTOR :: (( ACTUALLY THE USER IS EMPTY / NO LOGGED IN USER ))")

    "#some-script-functions" #> Script {
      someFunction
    }
  }

}

// mock ....
sealed trait SomeFunctionalMethods {

  def trigger = println ("SomeFunctionalMethods :: :: TRIGGER CALL")

  // mock ....
  def someFunction = Function("someFunction", Nil, SHtml.ajaxInvoke(() => trigger)._2.cmd)

}