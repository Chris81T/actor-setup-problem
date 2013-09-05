package code.lib

/**
 * TODO document this part
 *
 * User: christian
 * Date: 8/21/13
 * Time: 10:39 AM
 */
object Tools {

  /**
   * Is used to create unique actor id's according to current logged in user. If no user is given, simply use the given
   * someId as unique actor id. If an user is given, simply append the unique id to someId.
   */
  def getUniqueActorId(someId: String, user: Option[User]): String = {
    user match {
      case Some(user) => getUniqueActorId(someId, user)
      case None => someId
    }
  }
  def getUniqueActorId(someId: String, user: User): String = someId + "#" + user.name


}
