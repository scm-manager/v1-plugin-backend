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

import sonia.scm.web.proxy.ProxyConfiguration;
import sonia.scm.xml.XmlIntervalAdapter;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Sebastian Sdorra
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "configuration")
public class BackendConfiguration
{

  /**
   * Method description
   *
   *
   * @return
   */
  public AdminAccountConfiguration getAdminAccount()
  {
    return adminAccount;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<File> getDirectories()
  {
    return directories;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<String> getExcludes()
  {
    return excludes;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ProxyConfiguration getNewsConfiguration()
  {
    return newsConfiguration;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<PluginRepository> getRepositories()
  {
    return repositories;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getScannInterval()
  {
    return scannInterval;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTrackingCode()
  {
    return trackingCode;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isMultithreaded()
  {
    return multithreaded;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @XmlElement(name = "admin-account")
  private AdminAccountConfiguration adminAccount;

  /** Field description */
  @XmlElement(name = "directory")
  @XmlElementWrapper(name = "directories")
  private Set<File> directories;

  /** Field description */
  @XmlElement(name = "exclude")
  @XmlElementWrapper(name = "excludes")
  private Set<String> excludes;

  /** Field description */
  private boolean multithreaded = true;

  /** Field description */
  @XmlElement(name = "news-settings")
  private ProxyConfiguration newsConfiguration;

  /** Field description */
  @XmlElement(name = "repository")
  @XmlElementWrapper(name = "repositories")
  private Set<PluginRepository> repositories;

  /** Field description */
  @XmlElement(name = "scann-interval")
  @XmlJavaTypeAdapter(XmlIntervalAdapter.class)
  private Long scannInterval;

  /** Field description */
  @XmlElement(name = "tracking-code")
  private String trackingCode;
}
