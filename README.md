# UNIK Designer [![Build Status](https://travis-ci.org/ngx-translate/core.svg?branch=master)](https://travis-ci.org/ngx-translate/core) [![npm version](https://badge.fury.io/js/%40ngx-translate%2Fcore.svg)](https://badge.fury.io/js/%40ngx-translate%2Fcore)

UNIK is an automated code generation platform for developing quick user interfaces in React and Angular. This platform has two parts a designer and a code generation service. This code [https://code.cognizant.com/111651/unikDesginer] repository is for the designer and the code generation service repository is available here [https://code.cognizant.com/238209/ui-code-generator].

UNIK tool is also available in AWS. Click here [https://code.cognizant.com/238209/ui-code-generator].

Follow the below steps to setup and configure UNIK in local environment.

## Table of Contents 
* [Pre Requisites](#pre-requisites)
* [Setup](#setup)
* [Usage](#usage)
* [Output](#output)

## Pre Requisites

The steps mentioned are specific to a windows based environment. Do follow the equivalent steps for linux or other operating systems.

Ensure you have the following environment variables set properly. Open a command prompt and type the commands given below to ensure it is setup properly and the correct version numbers are displayed.

 Tools        | Recommended Versions | Command 
 ------------ | -------------------- | --------------------------
 Java         | 1.8+                 | java -version
 Maven        | 3.x+                 | mvn --version
 Node         | 10.0.x               | node --version
 Npm          | 5.6.x                | npm --version
 Angular CLI  | 7.3.8                | ng --version
 Mongo DB     | 3.4.5                | mongo --version
 GIT          | 2.x+                 | git --version
 Python       | 3.x+                 | python --version (This was used by Angular CLI)

Once you have the node installed, install Angular CLI using the below command.

```sh
npm install @angular/cli@7.3.8 -g
```

If you are setting this in Cognizant environment, use this maven [settings.xml](https://code.cognizant.com/111651/unikDesginer/tree/master/src/main/resources/config/settings.xml). Edit the settings.xml file with the path of the local repository, cognizant network credentials and place the file in "%MAVEN_HOME%\conf" folder.

## Setup

1. Navigate to GIT repository [https://code.cognizant.com/111651/unikDesginer/tree/develop]. Download the source code as zip file and extract it to a path in local hard drive.
2. Open command prompt and navigate to the folder in which the source code was extracted.
3. Restore the mongo db base required data setup. Ensure to change the host and port number in the batch file, if it is different from the default settings.

```sh
cd <UNIK_DESIGNER_EXTRACTED_PATH>
unik-mongo-restore.bat
```

4. Start the UNIK designer with the below command.

```sh
cd <UNIK_DESIGNER_EXTRACTED_PATH>
package-ui-modeller.bat
```

5. Follow the steps in UNIK Codge generator [README] (https://code.cognizant.com/238209/ui-code-generator) to start the code gen service.
6. Launch the app using the url [http://localhost:8080/tree.html]
7. Sample login credentials, use test/test (username / password) 

## Usage

### Design
To be updated

### Generate

1. Click the "Generate" link and navigate to the code generate screen. Follow the below steps for the respective technologies.

#### React
Below are the information required to generate the React code. Provide the information and click on "Generate" button to generate the code.

	a. Code Generation Path - Provide an existing folder in the local drive, where you need the code to be generated. This is an optional parameter for the UNIK accessed from AWS cloud. The code generation path will be assigned by the code generator.
	b. Project Name - Project Name under which the code will be generated. Code Generator will create a folder by this name under the Code Generation Path provided in option (a).
	c. Config Name - Configuration name that defines the styles, templates, folder path, code generation free marker templates, javascripts etc. This should be customized for your project / application needs. Refer to [Usage of Code Generator] (https://code.cognizant.com/111651/unikDesginer/tree/master) for more details on the configuraiton.
	d. Generate Login 	- TRUE  --> Generate a login screen along with the application screen.
							- FALSE --> No login screen will be generated.

#### Angular 
Below are the information required to generate the Angular code. Provide the information and click on "Generate" button to generate the code.

	a. Code Generation Path - Provide an existing folder in the local drive, where you need the code to be generated. This is an optional parameter for the UNIK accessed from AWS cloud. The code generation path will be assigned by the code generator.
	b. Project Name - Project Name under which the code will be generated. Code Generator will create a folder by this name under the Code Generation Path provided in option (a).
	c. Module Name - Module name of the screen.
	d. Component Name - Component name of the screen.
	e. Config Name - Configuration name that defines the styles, templates, folder path, code generation free marker templates, javascripts etc. This should be customized for your project / application needs. Refer to [Usage of Code Generator] (https://code.cognizant.com/111651/unikDesginer/tree/master) for more details on the configuraiton.
	d. User Defined Path - Customized Path in the module need to be created. This is in WIP. The documentation will be updated with more details once the code supports it. 

2. Once the code is generated, the click on "Download" button to download the generated code. This is applicable for users of UNIK deployed in AWS cloud. For users running UNIK in local environment, please skip it as the code is already generated in the folder path specified by you.
3. Navigate to [Output] (#output) to start the generated code. 

## Output

#### React & Angular

1. Navigate the folder where the code is generated.
2. For the first time use, install the necessary npm package. Run the below command to install the necessary npm dependencies. (This takes few minutes to install all the required dependencies. Ensure your configurations are done properly, if you are behind the corporate proxy server)

```sh
npm install
```

3.  To start application, execute the below command.

```sh
npm start
```

4. To run unit test cases, execute the below command.

```sh
npm test
```
