THIS PROJECT IS MADE PUBLIC UNDER  THE SAPIA-OSS
LICENSE. SEE LICENSE.txt THAT COMES  PACKAGED IN 
THE ROOT OF THE DISTRIBUTION OR GO TO 

http://www.sapia-oss.org/license.html 


SAPIA MAGNET
============

Sapia Magnet is a tool to start java or system process using an XML notation.
See the Magnet home page (http://www.sapia-oss.org/projects/magnet/home.html)
for more information.


FILES
=====

./LICENSE.txt                Sapia license file

./*.license                  Third party license file

./bin/*                      Scripts to execute magnet

./etc/*                      Magnet examples

./dist/sapia_magnet.jar      The sapia Magnet jar files (classes only)

./dist/sapia_magnet_src.jar  The sapia Magnet jar files (classes and source)
 
./docs/index.html            Sapia Magnet documentation home page

./docs/api/index.html        Sapia Magnet Javadoc home page

./lib/bootstrap/*.jar        Bootstaop libraries needed to startup magnet

./lib/magnet/*.jar           Core and third party libraries to execute magnet


CREDITS
=======

This product includes software developed  by the  
Apache Software Foundation  (http://www.apache.org/).


DEPENDENCIES
============

Ant 1.5.1                (http://ant.apache.org)

Log4j 1.2.5              (http://jakarta.apache.org/log4j)

Piccolo XML parser 1.0.3 (http://piccolo.sourceforge.net)

Junit 3.7.               (http://www.junit.org/index.htm)

JDOM 1.0 beta 9          (http://www.jdom.org)

BeanShell                (http://www.beanshell.org)


SUPPORT
=======

Mailing List: sapia-magnet_users@lists.sourceforge.net

Email: info@sapia-oss.org


INSTALLATION
============

1. Download

Download the distribution from SourceForge 
(http://www.sourceforge.net/projects/magnet). The download
link appears in the "Files" section; the latest "magnet"
release should be downloaded.


2. Extract

Extract the distribution under a directory of your choice
on the target computer.


3. Set-up Environment

Make that directory the MAGNET_HOME (define the MAGNET_HOME
environment variable as the path to that directory). Add the
MAGNET_HOME/bin directory to your PATH environment variable.


4. Running Magnet

Execute magnet by running the script magnet.bat (on Windows) or
magnet.sh (on Unix/Linux) from the /bin directory. Use the -help
option to see the usage of the magnet scripts.


5. Magnet Usage

Usage: magnet [vm options] [options] [profile] [args...]
VM Options:
    -javahome <path>    to define the home of the java runtime
                        this option overrides the JAVA_HOME environement variable
    -client             to start java with the "client" VM
    -server             to start java with the "server" VM
    -X<option>          to start java with non-standard options
    -D<name>=<value>    to set a system property

Options:
    -help, -h           print this message
    -version            print the version information and exit
    -logfile    <file>  use the given file to log
      -log      <file>            ''
    -debug              print debugging information
    -info               print information that can help to diagnose
    -warn               print warning and error information
    -magnetfile <file>  use the given magnet configuration file
      -file     <file>                ''
      
Example: magnet -server -magnetfile TimeServer.xml test
Example: magnet -javahome /opt/jdk1.4 -magnetfile TimeServer.xml test
Example: magnet -Xms8m -Xmx256m -Dfoo=bar -magnetfile TransactionServer.xml prod


BUILD
=====

You must have Ant installed (see http://ant.apache.org).

In this directory, type the "ant" command, followed by the
name of the target you want to execute (example: to compile
the sources, type "ant compile".

The following targets are available:

- init

 Creates the directories (under this directory) necessary
 to build this project.

 THIS TARGET MUST BE CALLED THE FIRST TIME THIS PROJECT 
 IS BUILT.

- compile

 Compiles the sources.

- test

 Runs the unit tests.

- dist
 
 Creates the library for this project (in the ./dist
 directory).

