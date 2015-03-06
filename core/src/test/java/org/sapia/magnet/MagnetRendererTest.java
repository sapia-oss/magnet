package org.sapia.magnet;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.sapia.magnet.domain.Launcher;
import org.sapia.magnet.domain.Magnet;
import org.sapia.magnet.domain.Param;
import org.sapia.magnet.domain.Parameters;
import org.sapia.magnet.domain.Profile;
import org.sapia.magnet.domain.system.Environment;
import org.sapia.magnet.domain.system.SystemLauncher;
import org.sapia.magnet.domain.system.Variable;


/**
 * @author Jean-Cedric Desrochers
 *
 * <dl>
 * <dt><b>Copyright:</b><dd>Copyright &#169; 2002-2003 <a href="http://www.sapia-oss.org">Sapia Open Source Software</a>. All Rights Reserved.</dd></dt>
 * <dt><b>License:</b><dd>Read the license.txt file of the jar or visit the
 *        <a href="http://www.sapia-oss.org/license.html" target="sapia-license">license page</a> at the Sapia OSS web site</dd></dt>
 * </dl>
 */
public class MagnetRendererTest extends BaseMagnetTestCase {

  @Before
  public void setUp() throws Exception {
    super.baseSetUp();
  }

  @Test
  public void testSimpleMagnet() throws Exception {
    Magnet aMagnet = createSimpleMagnet();
    ArrayList<Magnet> aList = new ArrayList<Magnet>(1);
    aList.add(aMagnet);
    new MagnetRenderer().render(aList, "dev");
    System.out.println(aMagnet);

    for (Launcher l: aMagnet.getLaunchers()) {
      l.execute("dev");
    }
    Thread.sleep(5000l);
  }


  private Magnet createSimpleMagnet() throws Exception {
    Magnet aMagnet = new Magnet();
    aMagnet.setName("simpleMagnet");
    aMagnet.setDescription("A test magnet");

    Parameters aParameters = new Parameters();
    aMagnet.addParameters(aParameters);
    aParameters.addParam(new Param("protocol", "http"));
    aParameters.addParam(new Param("host", "www.opensap.org"));
    aParameters.addParam(new Param("key", "${protocol}-key"));

    aParameters = new Parameters("test");
    aMagnet.addParameters(aParameters);
    aParameters.addParam(new Param("host", "test.sapia.org"));
    aParameters.addParam(new Param("value", "${protocol}-value"));
    aParameters.addParam(new Param("base", "${host}/index.html"));

    aMagnet.addLauncher(createWindowsLauncher());
    aMagnet.addLauncher(createMacLauncher());

    return aMagnet;
  }
  
  private Launcher createWindowsLauncher() throws Exception {
    Launcher aLauncher = new Launcher();
    aLauncher.setType("system");
    SystemLauncher aLauncherHandler = (SystemLauncher) aLauncher.getLaunchHandler();
    aLauncherHandler.setOs("windows");
    aLauncherHandler.setName("SapiaHomePage");
    aLauncherHandler.setCommand("cmd /C call explorer.exe D:\\");
    aLauncherHandler.setWorkingDirectory("C:\\");

    Profile aProfile = new Profile();
    aProfile.setName("dev");
    Parameters aParameters = new Parameters();
    aProfile.setParameters(aParameters);
    aParameters.addParam(new Param("url", "${protocol}://${host}"));
    Environment anEnv = new Environment();
    Variable aVar = new Variable();
    aVar.setName("path");
    aVar.setValue("C:\\Windows");
    anEnv.addVariable(aVar);
    aProfile.handleObject("Environment", anEnv);
    aLauncherHandler.addProfile(aProfile);

    aProfile = new Profile();
    aProfile.setName("test");
    aParameters = new Parameters();
    aProfile.setParameters(aParameters);
    aParameters.addParam(new Param("url", "${protocol}://${base}?query=${value}"));
    aLauncherHandler.addProfile(aProfile);
    
    return aLauncher; 
  }
  
  private Launcher createMacLauncher() throws Exception {
    Launcher aLauncher = new Launcher();
    aLauncher.setType("system");
    SystemLauncher aLauncherHandler = (SystemLauncher) aLauncher.getLaunchHandler();
    aLauncherHandler.setOs("mac");
    aLauncherHandler.setName("SapiaHomePage");
    aLauncherHandler.setCommand("./Safari.app/Contents/MacOS/Safari");
    aLauncherHandler.setWorkingDirectory("/Applications");

    Profile aProfile = new Profile();
    aProfile.setName("dev");
    Parameters aParameters = new Parameters();
    aProfile.setParameters(aParameters);
    aParameters.addParam(new Param("url", "${protocol}://${host}"));
    Environment anEnv = new Environment();
    Variable aVar = new Variable();
    aVar.setName("path");
    aVar.setValue("/Applications");
    anEnv.addVariable(aVar);
    aProfile.handleObject("Environment", anEnv);
    aLauncherHandler.addProfile(aProfile);

    aProfile = new Profile();
    aProfile.setName("test");
    aParameters = new Parameters();
    aProfile.setParameters(aParameters);
    aParameters.addParam(new Param("url", "${protocol}://${base}?query=${value}"));
    aLauncherHandler.addProfile(aProfile);
    
    return aLauncher; 
  }
  
}
