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

import java.util.Comparator;

/**
 *
 * @author Sebastian Sdorra
 */
public class PluginInformationVersionComparator
  implements Comparator<PluginInformation>
{

  /** Field description */
  public static final PluginInformationVersionComparator INSTANCE =
    new PluginInformationVersionComparator();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param p1
   * @param p2
   *
   * @return
   */
  @Override
  public int compare(PluginInformation p1, PluginInformation p2)
  {
    int result = 0;
    String v1 = p1.getVersion();
    String v2 = p2.getVersion();

    if (Util.isNotEmpty(v1) && Util.isNotEmpty(v2))
    {
      if (PluginVersion.createVersion(v1).isNewer(
        PluginVersion.createVersion(v2)))
      {
        result = -1;
      }
      else
      {
        result = 1;
      }
    }
    else if (Util.isEmpty(v1) && Util.isNotEmpty(v2))
    {
      result = 1;
    }
    else if (Util.isNotEmpty(v1) && Util.isEmpty(v2))
    {
      result = -1;
    }

    return result;
  }
}
