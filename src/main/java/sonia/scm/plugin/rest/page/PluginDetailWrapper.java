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

import sonia.scm.plugin.PluginInformation;
import sonia.scm.plugin.PluginUtil;

/**
 *
 * @author Sebastian Sdorra
 */
public class PluginDetailWrapper
{

  /**
   * Constructs ...
   *
   *
   * @param plugin
   */
  public PluginDetailWrapper(PluginInformation plugin)
  {
    this(plugin, null);
  }

  /**
   * Constructs ...
   *
   *
   * @param plugin
   * @param compareUrl
   */
  public PluginDetailWrapper(PluginInformation plugin, String compareUrl)
  {
    this.plugin = PluginUtil.prepareForView(plugin);
    this.compareUrl = compareUrl;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getCompareUrl()
  {
    return compareUrl;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public PluginInformation getPlugin()
  {
    return plugin;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param compareUrl
   */
  public void setCompareUrl(String compareUrl)
  {
    this.compareUrl = compareUrl;
  }

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void setPlugin(PluginInformation plugin)
  {
    this.plugin = plugin;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String compareUrl;

  /** Field description */
  private PluginInformation plugin;
}
