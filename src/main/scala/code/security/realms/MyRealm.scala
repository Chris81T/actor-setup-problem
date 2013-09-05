package code.security.realms

import org.apache.shiro.realm._
import org.apache.shiro.subject._
import org.apache.shiro.crypto.hash._

import org.apache.shiro.authc._
import org.apache.shiro.authc.credential._
import org.apache.shiro.authz._

/**
 * 
 * 
  source: http://shiro.apache.org/authorization.html
  	
  Potentially Brittle Security
  While the simpler and most common approach, implicit roles potentially impose a 
  lot of software maintenance and management problems.

  For example, what if you just want to add or remove a role, or redefine a role's
  behavior later? You'll have to go back into your source code and change all your
  role checks to reflect the change in your security model, every time such a 
  change is required! Not to mention the operational costs this would incur 
  (re-test, go through QA, shut down the app, upgrade the software with the new 
  role checks, restart the app, etc).

  This is probably ok for very simple applications (e.g. maybe there is an 'admin'
  role and 'everyone else'). But for more complicated or configurable applications,
  this can be a major major problem throughout the life of your application and
  drive a large maintenance cost for your software.

	IMPORTANT:
	http://shiro.apache.org/session-management.html#SessionManagement-SessionClustering
	http://shiro.apache.org/session-management.html#SessionManagement-sessionstorage

 * 
 */
class MyRealm extends AuthorizingRealm {
  
//    val credentialsMatcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME)
//    	credentialsMatcher.setStoredCredentialsHexEncoded(false)
//    	setCredentialsMatcher(credentialsMatcher)  
  
  	override def doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo = {
  		println("### MY-REALM --> doGetAuthorizationInfo <-- principals: " + principals);
//  		val email = principals.fromRealm(getName()).iterator().next().asInstanceOf[String]
//  		val user = UserDao.getByEmail(email)
//  		if(user.isDefined) user.get else null
  		
  		println("### MY-REALM --> fromRealm " + principals.fromRealm(getName))
  		println("### MY-REALM --> getPrimaryPrincipal " + principals.getPrimaryPrincipal)
  		println("### MY-REALM --> isEmpty " + principals.isEmpty)
  		println("### MY-REALM --> asList " + principals.asList)
  		println("### MY-REALM --> asSet " + principals.asSet)
  		
  		val userName = principals.getPrimaryPrincipal.asInstanceOf[String]
  		UserManagement.doGetAuthorizationInfo(userName)
  	}

  	override def supports(token: AuthenticationToken): Boolean = {
  	  println("### MY-REALM --> CHECK, IF GIVEN TOKEN IS SUPPORTED")
  	  token match {
  		  case userToken: UsernamePasswordToken => {
  			println("### MY-REALM --> USERTOKEN IS SUPPORTED")
  		    true  		      		   
  		  }
  		  case _ => {  		    
  			println("### MY-REALM --> ATTENTION: USERNAMEPASSWORDTOKEN IS REQUIRED !!")
  			false
  		  }
  	  }
  	} 
  	
	override def doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo = {
  		println("### MY-REALM --> doGetAuthenticationInfo <-- token: " + token);
  		val userToken = token.asInstanceOf[UsernamePasswordToken]
	    println("### MY-REALM --> getUsername " + userToken.getUsername())
	    println("### MY-REALM --> getCredentials " + userToken.getCredentials())
	    println("### MY-REALM --> getPassword " + userToken.getPassword())
	    println("### MY-REALM --> getHost " + userToken.getHost())
	    
	    UserManagement.validate(userToken.getUsername, getName).getOrElse(null)  

//	    var userSubject = UserManagement.validate(userToken.getUsername, getName)
//	    if (userSubject.isEmpty) throw new UnknownAccountException("username " + userToken.getUsername + " unknown") 
//	    userSubject.get
		
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// TODO FIXME if null will be returned, an unexpected exception is thrown !!! Check it..
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	} 
}

case class User(name: String, password: String, role: String)

object UserManagement {
	
  val users = User("master", "123", "admin") :: Nil
  
  def validate(username: String, realmname: String) : Option[AuthenticationInfo] = {
    println(":: validate username= " + username + " ::")
    for (user <- users) {
      if (user.name.equals(username)) {
    	val info = new SimpleAuthenticationInfo(user.name, user.password, realmname)
        return Some(info) 
      }
    }
    println(":: IS NONE ::")
    None
  }
  
  def doGetAuthorizationInfo(username: String) : AuthorizationInfo = {
	val authInfo = new SimpleAuthorizationInfo  		
	for (user <- users) {
      if (user.name.equals(username)) {
        authInfo.addRole(user.role)        
        // also here permission should also be set -- see above the class documentation!!!
      }
	}	
    authInfo
  }
    
}

