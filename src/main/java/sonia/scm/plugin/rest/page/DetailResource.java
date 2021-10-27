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



package sonia.scm.plugin.rest.page;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sonia.scm.plugin.BackendConfiguration;
import sonia.scm.plugin.PluginBackend;
import sonia.scm.plugin.PluginInformation;
import sonia.scm.plugin.PluginInformationVersionComparator;
import sonia.scm.plugin.PluginUtil;
import sonia.scm.plugin.rest.CachedViewableResource;
import sonia.scm.plugin.url.UrlBuilder;
import sonia.scm.plugin.url.UrlBuilderFactory;
import sonia.scm.util.Util;

//~--- JDK imports ------------------------------------------------------------

import com.sun.jersey.api.view.Viewable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Sebastian Sdorra
 */
@Singleton
@Path("/page/detail/{groupId}/{artifactId}.html")
public class DetailResource extends CachedViewableResource
{

  /** Field description */
  public static final String CACHE = "sonia.cache.plugin-detail";

  /** the logger for DetailResource */
  private static final Logger logger =
    LoggerFactory.getLogger(DetailResource.class);

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   *
   * @param context
   * @param backend
   * @param configuration
   * @param urlFactory
   * @param cacheManager
   */
  @Inject
  public DetailResource(ServletContext context, PluginBackend backend,
    BackendConfiguration configuration, UrlBuilderFactory urlFactory,
    CacheManager cacheManager)
  {
    super(context, backend, configuration, cacheManager, CACHE);
    this.backend = backend;
    this.urlFactory = urlFactory;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param groupId
   * @param artifactId
   * @param snapshot
   *
   * @return
   */
  @GET
  public Viewable getPluginDetails(@PathParam("groupId") String groupId,
    @PathParam("artifactId") String artifactId, @DefaultValue("false")
  @QueryParam("snapshot") boolean snapshot)
  {
    String cacheKey = createCacheKey(groupId, artifactId, snapshot);
    Viewable viewable = getFromCache(cacheKey);

    if (viewable == null)
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("create viewable from backend");
      }

      List<PluginInformation> pluginVersions =
        PluginUtil.getFilteredPluginVersions(backend, groupId, artifactId);

      if (Util.isEmpty(pluginVersions))
      {
        throw new WebApplicationException(Status.NOT_FOUND);
      }

      PluginInformation latest = pluginVersions.get(0);

      if (!snapshot)
      {
        pluginVersions = PluginUtil.filterSnapshots(pluginVersions);
      }

      List<PluginDetailWrapper> detailList = createDetailList(latest,
                                               pluginVersions);
      Map<String, Object> vars = createVarMap(latest.getName());

      vars.put("latest", PluginUtil.prepareForView(latest));
      vars.put("versions", detailList);
      viewable = new Viewable("/page/detail", vars);
      putToCache(cacheKey, viewable);
    }
    else if (logger.isDebugEnabled())
    {
      logger.debug("create viewable from cache");
    }

    return viewable;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param groupId
   * @param artifactId
   * @param snapshot
   *
   * @return
   */
  private String createCacheKey(String groupId, String artifactId,
    boolean snapshot)
  {
    return new StringBuilder(Util.nonNull(groupId)).append(
      Util.nonNull(artifactId)).append(Boolean.toString(snapshot)).toString();
  }

  /**
   * Method description
   *
   *
   * @param latest
   * @param pluginVersions
   *
   * @return
   */
  private List<PluginDetailWrapper> createDetailList(PluginInformation latest,
    List<PluginInformation> pluginVersions)
  {
    List<PluginDetailWrapper> detailList = new ArrayList<PluginDetailWrapper>();

    Collections.sort(pluginVersions,
      PluginInformationVersionComparator.INSTANCE);

    Iterator<PluginInformation> pluginIterator = pluginVersions.iterator();
    UrlBuilder urlBuilder = urlFactory.createCompareUrlBuilder(latest);
    PluginInformation last = null;

    while (pluginIterator.hasNext())
    {
      PluginInformation current = pluginIterator.next();

      if (last != null)
      {
        String url = null;

        if (urlBuilder != null)
        {
          url = urlBuilder.createCompareUrl(latest, last, current);
        }

        detailList.add(new PluginDetailWrapper(last, url));
      }

      last = current;
    }

    if (last != null)
    {
      detailList.add(new PluginDetailWrapper(last));
    }

    return detailList;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private PluginBackend backend;

  /** Field description */
  private UrlBuilderFactory urlFactory;
}
