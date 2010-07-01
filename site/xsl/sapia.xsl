<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:sapia="http://www.sapia-oss.org/2003/XSL/Transform">
<xsl:output method="html" indent="yes" encoding="US-ASCII" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"/>
                
<!-- ========================================= PAGE ========================================= -->
                
  <xsl:template match="/sapia:page">
    <html>
    <head>

      <script Language="JavaScript">
      <xsl:text>&lt;!--
      function popup(url, name, width, height)
      {
      settings=
      "toolbar=no,location=no,directories=no,"+
      "status=no,menubar=no,scrollbars=yes,"+
      "resizable=yes,width="+width+",height="+height;
      
      MyNewWindow=window.open(url,name,settings);
      }
      //--&gt;</xsl:text>
      </script>     
    
    <title><xsl:value-of select="@title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <link rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="@cssPath"/></xsl:attribute></link>
    
    </head>
    <body bgcolor="#F5EEB8" text="#000000">    
    <xsl:apply-templates select="sapia:refreshMenu"/>
    <table width="100%" border="0" cellspacing="0">
      <tr>
        <td width="800">
        <xsl:apply-templates select="sapia:sect1"/>
        </td>
      </tr>
    </table>
    </body>
    </html>    
  </xsl:template>        

  <xsl:template match="sapia:sect1">
    <table width="800" border="0" class="titleTable" bgcolor="#FFFFFF" bordercolor="#FFFFFF">
      <tr> 
        <td class="titleTable" align="left" valign="top">
          <xsl:apply-templates select="sapia:section" />
          <br/>
          <xsl:call-template name="displayAnchor"/>
          <h1><xsl:value-of select="@title"/></h1>
        </td>
      </tr>
    </table>
    <table width="800" border="1" class="textTable" align="left" valign="top" bordercolor="#FFFFFF" cellpadding="20">
      <tr> 
        <td class="textTable"> 
          <xsl:apply-templates select="sapia:sect-desc"/>
          <xsl:if test="toc">
          <ul>
          <xsl:call-template name="toc2"/>
          </ul>
          </xsl:if>
          <xsl:apply-templates select="sapia:sect2"/>
        </td>
      </tr>
    </table>
  </xsl:template>

  <xsl:template match="sapia:sect-desc">
    <xsl:apply-templates/>
  </xsl:template>        
  
  <xsl:template match="sapia:sect2">
    <xsl:call-template name="displayAnchor"/>
    <h2><xsl:value-of select="@title"/></h2>
    <xsl:apply-templates/>    
  </xsl:template>

  <xsl:template match="sapia:sect3">
    <xsl:call-template name="displayAnchor"/>
    <h3><xsl:value-of select="@title"/></h3>
    <xsl:apply-templates/>    
  </xsl:template>

  <xsl:template match="sapia:sect4">
    <xsl:call-template name="displayAnchor"/>
    <h4><xsl:value-of select="@title"/></h4>
    <xsl:apply-templates/>    
  </xsl:template>
  
  <xsl:template match="sapia:sect5">
    <xsl:call-template name="displayAnchor"/>
    <h5><xsl:value-of select="@title"/></h5>
    <xsl:apply-templates/>    
  </xsl:template>

  <xsl:template match="sapia:sect6">
    <xsl:call-template name="displayAnchor" /> 
    <h6><xsl:value-of select="@title"/></h6>
    <xsl:apply-templates/>    
  </xsl:template>
  
  <xsl:template name="displayAnchor">
    <xsl:choose>
    <xsl:when test="@alias">
      <a><xsl:attribute name="name"><xsl:value-of select="@alias"/></xsl:attribute></a>
    </xsl:when>
    <xsl:otherwise>
      <a><xsl:attribute name="name"><xsl:value-of select="@title"/></xsl:attribute></a>    
    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
<!-- =====================================      TOC     ====================================== -->

<xsl:template name="toc2">
  <xsl:for-each select="sapia:sect2">
    <li><xsl:call-template name="displayLink"/>
    <ul>
      <xsl:for-each select="sapia:sect3">
        <xsl:call-template name="toc3"/>
      </xsl:for-each>
    </ul>
    </li>
  </xsl:for-each> 
</xsl:template>

<xsl:template name="toc3">
  <li><xsl:call-template name="displayLink"/>
    <ul>
      <xsl:for-each select="sapia:sect4">    
         <xsl:call-template name="toc4"/>
      </xsl:for-each>      
    </ul>
    </li>
</xsl:template>

<xsl:template name="toc4">
  <li><xsl:call-template name="displayLink"/>
    <ul>
      <xsl:for-each select="sapia:sect5">    
         <xsl:call-template name="toc5"/>
      </xsl:for-each>      
    </ul>
    </li>
</xsl:template>

<xsl:template name="toc5">
  <li><xsl:call-template name="displayLink"/></li>
</xsl:template>

<xsl:template name="displayLink">
 <font size="1">
  <xsl:choose>
  <xsl:when test="@alias">
    <a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="@alias"/></xsl:attribute><xsl:value-of select="@title"/></a>
  </xsl:when>
  <xsl:otherwise>
    <a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="@title"/></xsl:attribute><xsl:value-of select="@title"/></a>    
  </xsl:otherwise>
  </xsl:choose>
  </font>
</xsl:template>

  
<!-- =====================================      SECTION PATH     ====================================== -->  
  
  <xsl:template match="sapia:section">
    <xsl:for-each select="sapia:path">
      <xsl:choose>
      <xsl:when test="@href">
        <a><xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
          <xsl:attribute name="target"><xsl:text>main</xsl:text></xsl:attribute>        
          <xsl:text>/</xsl:text><xsl:value-of select="@name"/></a>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>/</xsl:text><xsl:value-of select="@name"/>
      </xsl:otherwise>
      </xsl:choose>      
    </xsl:for-each>
  </xsl:template>          
  
<!-- ======================================     REFRESH MENU    ====================================== -->

  <xsl:template match="sapia:refreshMenu">
    <xsl:attribute name="onLoad">
    <xsl:text>javascript:top.sidebar.location.href='</xsl:text>
    <xsl:value-of select="@page"/>
    <xsl:text>'</xsl:text>
    </xsl:attribute>
  
  </xsl:template>

<!-- ======================================        ANCHOR      ====================================== -->

  <xsl:template match="a">
    <a>
      <xsl:apply-templates select="@*"/>
      <xsl:choose>
      <xsl:when test="@href">
        <b><font color="#0066CC"><xsl:value-of select="."/></font></b>      
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="."/>      
      </xsl:otherwise>
      </xsl:choose>
    </a>
  </xsl:template>
  
<!-- ======================================        POP UP      ====================================== -->

  <xsl:template match="sapia:popup">
    <a href="#">
    <xsl:attribute name="onClick">
    <xsl:text>popup('</xsl:text>
    <xsl:value-of select="@href"/><xsl:text>','</xsl:text>
    <xsl:value-of select="@title"/><xsl:text>',</xsl:text>
    <xsl:value-of select="@width"/><xsl:text>,</xsl:text>
    <xsl:value-of select="@height"/><xsl:text>); return false</xsl:text>    
    </xsl:attribute><b><font color="#0066CC"><xsl:value-of select="."/></font></b>
    </a>
  </xsl:template>  
  
<!-- =========================================      NOTE     ========================================= -->

   <xsl:template match="sapia:note">    
     <table width="100%" border="0" bgcolor="#FFFFCC" cellpadding="20">
       <tr><td><b><font size="2">
       <xsl:apply-templates/>       
       </font></b></td></tr>
     </table>    
   </xsl:template>
   
<!-- =========================================      NOTE     ========================================= -->

   <xsl:template match="sapia:command">    
       <font size="1" face="courier, courier new">
       <xsl:apply-templates/>       
       </font>
   </xsl:template>   

<!-- =========================================     CLASS     ========================================= -->

   <xsl:template match="sapia:class">    
       <b><font size="1" face="courier, courier new">
       <xsl:apply-templates/>       
       </font></b>
   </xsl:template>   

<!-- =========================================      CODE     ========================================= -->

   <xsl:template match="sapia:code">    
     <table width="100%" border="0" bgcolor="#FFFFCC" cellpadding="20">
       <tr><td><font size="3"><pre>
<xsl:apply-templates/>  
       </pre></font></td></tr>
     </table> 
   </xsl:template>

<!-- =========================================     TABLE     ========================================= -->

   <xsl:template match="sapia:table">    
     <table class="sapiaTable" cellspacing="5">
        <xsl:apply-templates select="@*"/>
        <xsl:apply-templates/>
     </table>    
   </xsl:template>
   
   <xsl:template match="sapia:th">
      <th bgcolor="#FFFFCC">
        <xsl:apply-templates select="@*"/>      
        <xsl:apply-templates/>
      </th>
   </xsl:template>   

<!-- ========================================= VERTICAL MENU ========================================= -->

  <xsl:template match="/sapia:vmenu">
    <html>
    <head>
      <title>Sidebar</title>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
      <link rel="stylesheet" type="text/css"><xsl:attribute name="href"><xsl:value-of select="@cssPath"/></xsl:attribute></link>
    </head>
    <body bgcolor="#F5EEB8" text="#000000">
    <table width="170" border="0" cellpadding="5">
    <xsl:apply-templates select="sapia:vsection"/>
      <tr>
        <td height="100" valign="bottom" align="left">
          <xsl:apply-templates select="sapia:sflogo"/>
        </td>
      </tr>
    </table>
    </body>
    </html>
  </xsl:template>
  
  <xsl:template match="sapia:vsection">
    <tr>
      <xsl:choose>
      <xsl:when test="@href">
        <td class="menuTable" bgcolor="#FFFFCC"><a>      
        <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
        <xsl:attribute name="target"><xsl:text>main</xsl:text></xsl:attribute>        
        <xsl:value-of select="@name"/>
        </a></td>
      </xsl:when>
      <xsl:otherwise>
        <td class="menuTable" bgcolor="#FFFFCC">
          <b><xsl:value-of select="@name"/></b>
        </td>
      </xsl:otherwise>
      </xsl:choose>      
    </tr>   
    <xsl:for-each select="sapia:vitem">
      <tr>    
      <td class="menuTable">
        <a><xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
          <xsl:attribute name="target"><xsl:text>main</xsl:text></xsl:attribute>      
          <font size="1"><xsl:value-of select="@name"/></font>
          
            <xsl:for-each select="sapia:vsubitem">
            <tr class="menuSubItem">
                <td><a><xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
                   <xsl:attribute name="target"><xsl:text>main</xsl:text></xsl:attribute>      
                   <xsl:value-of select="@name"/>
                </a>
                </td>
            </tr>
            </xsl:for-each>
        </a>
      </td>
     </tr>
    </xsl:for-each>
  </xsl:template>
  
 <!-- --> 
 
  <xsl:template match="sapia:sflogo">
<a href="http://sourceforge.net"><img src="http://sourceforge.net/sflogo.php?group_id=68099&amp;type=3" width="125" height="37" border="0" alt="SourceForge.net Logo" /></a>
  
  </xsl:template>
  
 <!-- ========================================= LICENSE ========================================= -->  

  <xsl:template match="sapia:license">
<p>
This license is based on the Apache Software License, Version 1.1
</p>
<p>
 Copyright (c) 2002, 2003 Sapia Open Source Software.  All rights
 reserved.
</p>
<p>
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
</p>
<ul>
 <li>Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.</li>
 
 <li>Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the
     distribution.</li>
 
 <li>The end-user documentation included with the redistribution,
     if any, must include the following acknowledgment:
     "This product includes software developed by 
      Sapia Open Source Software Organization (http://www.sapia-oss.org/)."
      Alternately, this acknowledgment may appear in the software itself,
      if and wherever such third-party acknowledgments normally appear.</li> 
 
 <li>The names "Sapia", "Sapia Open Source Software" and "Sapia OSS" must
     not be used to endorse or promote products derived from this
     software without prior written permission. For written
     permission, please contact info@sapia-oss.org.</li>
 
 <li>Products derived from this software may not be called "Sapia",
     nor may "Sapia" appear in their name, without prior written
     permission of Sapia OSS.</li>
 
</ul>
<p>
 THIS SOFTWARE IS PROVIDED ''AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE SAPIA OSS ORGANIZATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
</p>
<p>
 This software consists of voluntary contributions made by many
 individuals on behalf of the Sapia Open Source Software Organization.  
 For more information on Sapia OSS, please see
 <a href="http://www.sapia-oss.org/">http://www.sapia-oss.org/</a>
</p>  
  </xsl:template>
  
  <xsl:template match="sapia:license-apache2">
<p align="center">
Apache License<br />
Version 2.0, January 2004<br />
<a href="http://www.apache.org/licenses/">http://www.apache.org/licenses/</a>
</p>
<p>
TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
</p>
<p><b><a name="definitions">1. Definitions</a></b>.</p>
<p>
      "License" shall mean the terms and conditions for use, reproduction,
      and distribution as defined by Sections 1 through 9 of this document.
</p>
<p>
      "Licensor" shall mean the copyright owner or entity authorized by
      the copyright owner that is granting the License.
</p>
<p>
      "Legal Entity" shall mean the union of the acting entity and all
      other entities that control, are controlled by, or are under common
      control with that entity. For the purposes of this definition,
      "control" means (i) the power, direct or indirect, to cause the
      direction or management of such entity, whether by contract or
      otherwise, or (ii) ownership of fifty percent (50%) or more of the
      outstanding shares, or (iii) beneficial ownership of such entity.
</p>
<p>
      "You" (or "Your") shall mean an individual or Legal Entity
      exercising permissions granted by this License.
</p>
<p>
      "Source" form shall mean the preferred form for making modifications,
      including but not limited to software source code, documentation
      source, and configuration files.
</p>
<p>
      "Object" form shall mean any form resulting from mechanical
      transformation or translation of a Source form, including but
      not limited to compiled object code, generated documentation,
      and conversions to other media types.
</p>
<p>
      "Work" shall mean the work of authorship, whether in Source or
      Object form, made available under the License, as indicated by a
      copyright notice that is included in or attached to the work
      (an example is provided in the Appendix below).
</p>
<p>
      "Derivative Works" shall mean any work, whether in Source or Object
      form, that is based on (or derived from) the Work and for which the
      editorial revisions, annotations, elaborations, or other modifications
      represent, as a whole, an original work of authorship. For the purposes
      of this License, Derivative Works shall not include works that remain
      separable from, or merely link (or bind by name) to the interfaces of,
      the Work and Derivative Works thereof.
</p>
<p>
      "Contribution" shall mean any work of authorship, including
      the original version of the Work and any modifications or additions
      to that Work or Derivative Works thereof, that is intentionally
      submitted to Licensor for inclusion in the Work by the copyright owner
      or by an individual or Legal Entity authorized to submit on behalf of
      the copyright owner. For the purposes of this definition, "submitted"
      means any form of electronic, verbal, or written communication sent
      to the Licensor or its representatives, including but not limited to
      communication on electronic mailing lists, source code control systems,
      and issue tracking systems that are managed by, or on behalf of, the
      Licensor for the purpose of discussing and improving the Work, but
      excluding communication that is conspicuously marked or otherwise
      designated in writing by the copyright owner as "Not a Contribution."
</p>
<p>
      "Contributor" shall mean Licensor and any individual or Legal Entity
      on behalf of whom a Contribution has been received by Licensor and
      subsequently incorporated within the Work.
</p>
<p><b><a name="copyright">2. Grant of Copyright License</a></b>.
Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      copyright license to reproduce, prepare Derivative Works of,
      publicly display, publicly perform, sublicense, and distribute the
      Work and such Derivative Works in Source or Object form.
</p>
<p><b><a name="patent">3. Grant of Patent License</a></b>.
Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      (except as stated in this section) patent license to make, have made,
      use, offer to sell, sell, import, and otherwise transfer the Work,
      where such license applies only to those patent claims licensable
      by such Contributor that are necessarily infringed by their
      Contribution(s) alone or by combination of their Contribution(s)
      with the Work to which such Contribution(s) was submitted. If You
      institute patent litigation against any entity (including a
      cross-claim or counterclaim in a lawsuit) alleging that the Work
      or a Contribution incorporated within the Work constitutes direct
      or contributory patent infringement, then any patent licenses
      granted to You under this License for that Work shall terminate
      as of the date such litigation is filed.
</p>
<p><b><a name="redistribution">4. Redistribution</a></b>.
You may reproduce and distribute copies of the
      Work or Derivative Works thereof in any medium, with or without
      modifications, and in Source or Object form, provided that You
      meet the following conditions:
<ol type="a">
<li>You must give any other recipients of the Work or
          Derivative Works a copy of this License; and
<br /> <br /></li>

<li>You must cause any modified files to carry prominent notices
          stating that You changed the files; and
<br /> <br /></li>

<li>You must retain, in the Source form of any Derivative Works
          that You distribute, all copyright, patent, trademark, and
          attribution notices from the Source form of the Work,
          excluding those notices that do not pertain to any part of
          the Derivative Works; and
<br /> <br /></li>

<li>If the Work includes a "NOTICE" text file as part of its
          distribution, then any Derivative Works that You distribute must
          include a readable copy of the attribution notices contained
          within such NOTICE file, excluding those notices that do not
          pertain to any part of the Derivative Works, in at least one
          of the following places: within a NOTICE text file distributed
          as part of the Derivative Works; within the Source form or
          documentation, if provided along with the Derivative Works; or,
          within a display generated by the Derivative Works, if and
          wherever such third-party notices normally appear. The contents
          of the NOTICE file are for informational purposes only and
          do not modify the License. You may add Your own attribution
          notices within Derivative Works that You distribute, alongside
          or as an addendum to the NOTICE text from the Work, provided
          that such additional attribution notices cannot be construed
          as modifying the License.</li>
</ol>
      You may add Your own copyright statement to Your modifications and
      may provide additional or different license terms and conditions
      for use, reproduction, or distribution of Your modifications, or
      for any such Derivative Works as a whole, provided Your use,
      reproduction, and distribution of the Work otherwise complies with
      the conditions stated in this License.
</p>
<p><b><a name="contributions">5. Submission of Contributions</a></b>.
Unless You explicitly state otherwise,
      any Contribution intentionally submitted for inclusion in the Work
      by You to the Licensor shall be under the terms and conditions of
      this License, without any additional terms or conditions.
      Notwithstanding the above, nothing herein shall supersede or modify
      the terms of any separate license agreement you may have executed
      with Licensor regarding such Contributions.
</p>
<p><b><a name="trademarks">6. Trademarks</a></b>.
This License does not grant permission to use the trade
      names, trademarks, service marks, or product names of the Licensor,
      except as required for reasonable and customary use in describing the
      origin of the Work and reproducing the content of the NOTICE file.
</p>
<p><b><a name="no-warranty">7. Disclaimer of Warranty</a></b>.
Unless required by applicable law or
      agreed to in writing, Licensor provides the Work (and each
      Contributor provides its Contributions) on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
      implied, including, without limitation, any warranties or conditions
      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
      PARTICULAR PURPOSE. You are solely responsible for determining the
      appropriateness of using or redistributing the Work and assume any
      risks associated with Your exercise of permissions under this License.
</p>
<p><b><a name="no-liability">8. Limitation of Liability</a></b>.
In no event and under no legal theory,
      whether in tort (including negligence), contract, or otherwise,
      unless required by applicable law (such as deliberate and grossly
      negligent acts) or agreed to in writing, shall any Contributor be
      liable to You for damages, including any direct, indirect, special,
      incidental, or consequential damages of any character arising as a
      result of this License or out of the use or inability to use the
      Work (including but not limited to damages for loss of goodwill,
      work stoppage, computer failure or malfunction, or any and all
      other commercial damages or losses), even if such Contributor
      has been advised of the possibility of such damages.
</p>
<p><b><a name="additional">9. Accepting Warranty or Additional Liability</a></b>.
While redistributing
      the Work or Derivative Works thereof, You may choose to offer,
      and charge a fee for, acceptance of support, warranty, indemnity,
      or other liability obligations and/or rights consistent with this
      License. However, in accepting such obligations, You may act only
      on Your own behalf and on Your sole responsibility, not on behalf
      of any other Contributor, and only if You agree to indemnify,
      defend, and hold each Contributor harmless for any liability
      incurred by, or claims asserted against, such Contributor by reason
      of your accepting any such warranty or additional liability.
</p>
<p>
END OF TERMS AND CONDITIONS
</p>
  </xsl:template>
  
  <xsl:template match="@*|node()">
    <xsl:copy>
       <xsl:apply-templates select="@*"/>
       <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>  