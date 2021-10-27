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

import sonia.scm.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class AbstractPluginBackend implements PluginBackend
{

  /**
   *   Method description
   *
   *
   *   @param listener
   */
  @Override
  public void addListener(PluginBackendListener listener)
  {
    listenerSet.add(listener);
  }

  /**
   * Method description
   *
   *
   * @param listeners
   */
  @Override
  public void addListeners(Collection<PluginBackendListener> listeners)
  {
    listenerSet.addAll(listeners);
  }

  /**
   * Method description
   *
   *
   * @param plugins
   */
  @Override
  public void addPlugins(PluginInformation... plugins)
  {
    if (Util.isNotEmpty(plugins))
    {
      addPlugins(Arrays.asList(plugins));
    }
  }

  /**
   * Method description
   *
   *
   * @param scannedFiles
   */
  @Override
  public void addScannedFiles(File... scannedFiles)
  {
    if (Util.isNotEmpty(scannedFiles))
    {
      addScannedFiles(Arrays.asList(scannedFiles));
    }
  }

  /**
   * Method description
   *
   *
   * @param listener
   */
  @Override
  public void removeListener(PluginBackendListener listener)
  {
    listenerSet.remove(listener);
  }

  /**
   * Method description
   *
   *
   *
   * @param plugins
   */
  protected void fireEvent(Collection<PluginInformation> plugins)
  {
    for (PluginBackendListener listener : listenerSet)
    {
      listener.addedNewPlugins(plugins);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Set<PluginBackendListener> listenerSet =
    new HashSet<PluginBackendListener>();
}
