# Project Bosphorus

## Background
This project is aimed at providing a custom portal framework for vRealize Automation (vRA) along with a reference implementation. It is 
intended for advanced users/developers of vRealize Automation who need to provide an alternate User Interface or who need to 
integrate vRA into a custom portal.

Bosphorus is written in Java using Spring MVC, Spring Boot, Thymeleaf and jQuery. The reference implementation uses jQuery Mobile as a UI 
framework. The UI framework can very easily be swapped out for another jQuery-based framework. 

The choice of Java/Spring/Thymeleaf/jQuery was deliberate, as it seems to be a combination that's very commonly used for Enterprise 
portals at the time of writing. 

### Why the name?
I wanted a name that was related to the concept of a portal. If you paid attention during geography class, you know that the Bosphorus Strait, located in Turkey is the portal between the Mediterranean Sea and the Black Sea. Plus is sounds cool. 

## Design goals

* Allow web coders to develop portals with no or little knowledge of vRA
* Implement on a robust platform that's likely to be used in an enterprise setting.
* Easy to install.
* Extremely small footprint.
* Extremely fast startup time.
* Avoid cross-platform AJAX issues.

## Features
Bosphorus was designed to be have a very small footprint, start and run very fast. At the same time, Bosphorus offers many advanced 
features such as live updates using long-polling and lazy-loading UI-snippets using AJAX.

## Installation

### Installing from binary

I will try to keep the binaries reasonably up to date, but if you want to be sure you have the latest, I strongly
suggest you build Bospohorus from source. It's easy!

1. Make sure you have java 1.8.x installed. Bosphorus will NOT run on java versions lower than 1.8.0!
1. Download the binary from here: 
https://drive.google.com/file/d/0BymSAYUyWEPuN2xQbVZqQjVweTg/view?usp=sharing
1. Run the downloaded JAR as a self-executing JAR. Specify the URL for your vRA host as the --vra-host parameter:
`java -jar bosphorus-0.0.1.jar --vra-url=https://<your vra host>`
1. The portal should now be accessible on http://yourhost:8080/ 
1. Log in using your vRA credentials. No domain ID is necessary.

### Building from source

If you want the latest and greatest version of Bosphorus, you should build it from source. This guide shows how to build it on a CentOS 
machine, but you can build/run it on any Linux, Windows or OSX.

1. If not already present on your machine, install java, maven and git:<br>
`yum install -y java`<br>
`yum install -y maven`<br>
`yum install -y git`
1. Create a directory for the source files, e.g. "bosphorus".<br>
`mkdir bosphorus; cd bosphorus`
1. Check out the bosphorus source code from git.<br>
`git pull https://github.com/njswede/bosphorus.git`
1. Build the code using Maven:<br>
`mvn package`
1. Run the newly created JAR as a self-executing JAR. Specify the URL for your vRA host as the --vra-host parameter:
`java -jar target/bosphorus-0.0.1.jar --vra-url=https://<your vra host>`
1. The portal should now be accessible on http://yourhost:8080/ 
1. Log in using your vRA credentials. No domain ID is necessary. 

## Known bugs and limitations

* Currently only works for the default tenant.
* Only supports day 2 operations for which there is no form.
* Displays some day 2 operations that won't work outside the native vRA portal (such as Connect via RDP).
* Only allows you to edit basic machine parameters when requesting catalog items. Networks, software components, etc. will be created using their default values.
* In the Requests section, live update doesn't work for some day 2 operations.

## Future updates

I'm currently running Bosphorus as a side project, so updates may be sporadic. However, here is a list of updates I'm likely do post in the somewhat near future:

* Support for tenants other than the default one. 
* More robust live update code.
* Support for "skins" and "themes".
* Basic support for approvals (e.g. an "inbox" where you can do approve/reject)
