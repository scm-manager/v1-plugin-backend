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



package sonia.scm.plugin.scanner;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultFileFilter implements FileFilter
{

  /** Field description */
  public static final String PLUGIN_EXTENSION = ".jar";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DefaultFileFilter()
  {
    this(null);
  }

  /**
   * Constructs ...
   *
   *
   * @param excludes
   */
  public DefaultFileFilter(Collection<String> excludes)
  {
    if (excludes == null)
    {
      this.excludes = Collections.emptySet();
    }
    else
    {
      this.excludes = excludes;
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   */
  @Override
  public boolean accept(File file)
  {
    boolean accepted = false;

    if (file.isDirectory())
    {
      accepted = true;
    }
    else
    {
      String name = file.getName();

      if (name.endsWith(PLUGIN_EXTENSION))
      {
        accepted = true;

        for (String exclude : excludes)
        {
          if (name.matches(exclude))
          {
            accepted = false;

            break;
          }
        }
      }
    }

    return accepted;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Collection<String> excludes;
}
