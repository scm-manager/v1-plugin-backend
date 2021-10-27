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

import com.google.common.base.Strings;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.ServletModule;

import net.sf.ehcache.CacheManager;

import sonia.scm.ConfigurationException;
import sonia.scm.plugin.rest.api.PluginResource;
import sonia.scm.plugin.scanner.DefaultPluginScannerFactory;
import sonia.scm.plugin.scanner.PluginScannerFactory;
import sonia.scm.plugin.scanner.PluginScannerScheduler;
import sonia.scm.plugin.scanner.TimerPluginScannerScheduler;
import sonia.scm.plugin.url.BitbucketUrlBuilder;
import sonia.scm.plugin.url.GithubUrlBuilder;
import sonia.scm.plugin.url.UrlBuilder;
import sonia.scm.plugin.url.UrlBuilderFactory;
import sonia.scm.web.proxy.ProxyConfigurationProvider;
import sonia.scm.web.proxy.ProxyServlet;

//~--- JDK imports ------------------------------------------------------------

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXB;

/**
 *
 * @author Sebastian Sdorra
 */
public class ScmBackendModule extends ServletModule
{

  /** Field description */
  public static final String CACHE_CONFIG = "/config/ehcache.xml";

  /** Field description */
  public static final String FILE_CONFIG = "config.xml";

  /** Field description */
  public static final String PACKAGE = "sonia.scm.plugin.rest";

  /** Field description */
  public static final String PATTERN_NEWS = "/news*";

  /** Field description */
  public static final String PATTERN_REST_API = "/*";

  /** Field description */
  public static final String PATTERN_REST_EXCLUDE = "/(template/|news).*";

  /** Field description */
  private static final String ENV_CONFIG = "SCMBACKEND_CONFIG";

  //~--- methods --------------------------------------------------------------

  private File findConfigurationFile() {
    File configurationFile;

    String env = System.getenv(ENV_CONFIG);
    if (Strings.isNullOrEmpty(env)) {
      configurationFile = BaseDirectory.getFile(FILE_CONFIG);
    } else {
      configurationFile = new File(env);
    }

    if (!configurationFile.exists())
    {
      throw new ConfigurationException(
              "could not find configuration at ".concat(configurationFile.getPath()));
    }

    return configurationFile;
  }

  /**
   * Method description
   *
   */
  @Override
  protected void configureServlets()
  {
    BackendConfiguration configuration = JAXB.unmarshal(findConfigurationFile(), BackendConfiguration.class);

    bind(BackendConfiguration.class).toInstance(configuration);
    bind(PluginBackend.class).to(DefaultPluginBackend.class);
    bind(PluginScannerFactory.class).to(DefaultPluginScannerFactory.class);
    bind(PluginScannerScheduler.class).to(TimerPluginScannerScheduler.class);

    // cache manager
    CacheManager cacheManager =
      new CacheManager(PluginResource.class.getResource(CACHE_CONFIG));

    bind(CacheManager.class).toInstance(cacheManager);

    // compare url builder
    Multibinder<UrlBuilder> compareUrlBuilderBinder =
      Multibinder.newSetBinder(binder(), UrlBuilder.class);

    compareUrlBuilderBinder.addBinding().to(BitbucketUrlBuilder.class);
    compareUrlBuilderBinder.addBinding().to(GithubUrlBuilder.class);

    // compare url builder factory
    bind(UrlBuilderFactory.class);

    // news proxy
    bind(ProxyConfigurationProvider.class).to(NewsProxyURLProvider.class);
    serve(PATTERN_NEWS).with(ProxyServlet.class);

    // redirect for start page
    filter(PATTERN_REST_API).through(RedirectFilter.class);

    // rest
    Map<String, String> params = new HashMap<String, String>();

    params.put(PackagesResourceConfig.PROPERTY_PACKAGES, PACKAGE);
    params.put(ServletContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX,
      PATTERN_REST_EXCLUDE);
    filter(PATTERN_REST_API).through(GuiceContainer.class, params);
  }
}
