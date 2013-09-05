package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._

import shiro.Shiro
import shiro.sitemap.Locs._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
		// initialize shiro
		Shiro.init()

		// where to search snippet
    LiftRules.addToPackages("code")
    
    // Build SiteMap
    LiftRules.setSiteMap(Pages.siteMap)

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQueryArtifacts

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))
  }
}

object Pages {

	val welcome = Menu.i("Welcome") / "index"
	val login = Menu.i("Login") / "login" >> RequireNoAuthentication >> DefaultLogin 
  val pageOne = Menu.i("Page One") / "pageone" >> RequireAuthentication >> HasRole("admin")
	val pageTwo = Menu.i("Page Two") / "pagetwo" >> RequireAuthentication >> HasRole("admin")

	def siteMap = SiteMap(List(welcome, pageOne, pageTwo, login) ::: Shiro.menus : _*)

}