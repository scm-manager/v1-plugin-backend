/**
 * Copyright (c) 2010, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of SCM-Manager; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://bitbucket.org/sdorra/scm-manager
 *
 */



package sonia.scm.plugin;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import org.apache.shiro.guice.web.ShiroWebModule;

import sonia.scm.plugin.scanner.PluginScannerScheduler;
import sonia.scm.plugin.security.SecurityModule;

//~--- JDK imports ------------------------------------------------------------

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 *
 * @author Sebastian Sdorra
 */
public class BackendContextListener extends GuiceServletContextListener
{

  /**
   * Method description
   *
   *
   * @param servletContextEvent
   */
  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent)
  {
    scheduler.shutdown();
    super.contextDestroyed(servletContextEvent);
  }

  /**
   * Method description
   *
   *
   * @param servletContextEvent
   */
  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent)
  {
    this.servletContext = servletContextEvent.getServletContext();
    super.contextInitialized(servletContextEvent);
    scheduler = injector.getInstance(PluginScannerScheduler.class);
    scheduler.start();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Injector getInjector()
  {
    injector = Guice.createInjector(ShiroWebModule.guiceFilterModule(),
      new SecurityModule(servletContext), new ScmBackendModule());

    return injector;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Injector injector;

  /** Field description */
  private PluginScannerScheduler scheduler;

  /** Field description */
  private ServletContext servletContext;
}
