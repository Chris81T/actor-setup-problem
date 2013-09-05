package code.snippet

import org.slf4j.LoggerFactory
import net.liftweb.http.S
import net.liftweb.util.ClearNodes
import net.liftweb.util.Helpers._
import net.liftweb.common.Full
import code.lib.{Tools, AuthSubject}
import code.comet.SetupSessionUserActor

object TheSnippet extends SetupSessionUserCometActor {

  def render = {
    println("THE_SNIPPET :: render snippet - setup the-actor for session = " + S.session.map(_.uniqueId))
    renderActorMarkup("TheActor", "the-actor")
  }

}

/**
 * can be used by snippets to perform rendering markup for @see AbstractSessionUserActor instances...
 */
trait SetupSessionUserCometActor {

  private val logger = LoggerFactory.getLogger(classOf[SetupSessionUserCometActor])

  def renderActorMarkup(actorClassName: String, actorMarkupId: String) = {
    logger.trace("render actor markup with given actor-class-name = {} and actor-markup-id = {}",
      actorClassName, actorMarkupId)
    (for {
      session <- S.session
      user <- AuthSubject.userBox
    } yield {
      val sessionId = session.uniqueId
      val actorName = Tools.getUniqueActorId(sessionId, user)
      println("THE_SNIPPET >> SETUP_SESSION_USER_COMET_ACTOR :: start to setup comet-actor = {} with actor-name = {}", actorClassName, actorName)
      session.setupComet(actorClassName, Full(actorName), SetupSessionUserActor(user, sessionId))

      s"#${actorMarkupId} [data-lift]" #> s"comet?type=${actorClassName};name=${actorName}"
    }) getOrElse {
      println("THE_SNIPPET >> SETUP_SESSION_USER_COMET_ACTOR :: No session is given or no user is logged in. Clear nodes, that will prevent creating the desired " +
        "actor = {}", actorClassName)
      if (S.session isEmpty) logger.warn("NO session exists! Could not setup actor class = {}", actorClassName)
      ClearNodes
    }
  }

}
