package code.lib

import org.apache.shiro.SecurityUtils
import net.liftweb.common.Box


/**
 * MOCK USER OBJECT !!  In real project the user is an entity class - loaded from the db !
 * @param name
 */
case class User(name: String)

object AuthSubject {

  /**
   * refactored for this example... !!
   * @return
   */
  def user : Option[User] = {
    val s = SecurityUtils.getSubject
    if (s.isAuthenticated) Some(User(s.getPrincipal.asInstanceOf[String]))
    else None
  }

  def userBox = Box(user)

}
