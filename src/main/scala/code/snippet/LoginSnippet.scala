package code.snippet

import net.liftweb._
import http._
import js._
import JsCmds._
import common._
import util.Helpers._
import scala.xml.NodeSeq

import org.apache.shiro._
import org.apache.shiro.authc._
import org.apache.shiro.subject._;

import shiro._;

object LoginSnippet {

  def render = {
    var user = ""
    var password = ""

    def execLogin() {
	    def redirect = S.redirectTo(LoginRedirect.is.openOr("/"))

        var subject = SecurityUtils.getSubject()
	    
	    if (!subject.isAuthenticated) {
	      println("Subject is not authenticated. Try to login with given info's")
	      
	      var token = new UsernamePasswordToken(user, password)
	      token.setRememberMe(true)
	      
	      tryo(subject.login(token)) match {
	        case Failure(_,Full(err),_) => err match {
	          case x: UnknownAccountException => 
	            println("Unkown user account")
	          case x: IncorrectCredentialsException => 
	            println("Invalid username or password")
	          case x: LockedAccountException => 
	            println("Your account has been locked")
	          case x: ExcessiveAttemptsException => 
	            println("You have exceeded the number of login attempts")
	          case e: Exception => 
	            println("Unexpected login error: " + e)
	        }
	        case _ => {
	          println("UNKNOWN CASE CALL ?????! Redirect... :: " + redirect)
	          redirect 
	        }
	      }
	    } else {
	      println("User is already authenticated! Redirect... :: " + redirect)
	      redirect 
	    }
    }    
    
    "@user" #> SHtml.text(user, user = _) &
    "@password" #> SHtml.password(password, password = _) &
    "@login-button" #> SHtml.onSubmitUnit(execLogin)       
  }


}

object NameSnippet {

  def render = {
    val s = SecurityUtils.getSubject
    def sessionLabel = S.session map (" :: with session id = " + _.uniqueId) getOrElse ("no session available : (")
    var name : String = "guest"
    if (s.isAuthenticated) name = s.getPrincipal.asInstanceOf[String]
    "#uname" #> (name + sessionLabel)
  }

}