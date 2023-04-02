# E-voting

This is voting app for country elections in Bosnia and Herzegowina. It is written in JavaFx.
To run apps you need to have JRE/JDK 17 or newer installed on your PC.


# E-voting app

This is GUI app for vote casting. It is built to enable secure voting on local machines. 
This application is intended to be installed on pre-built virtual machines that will be used at polling stations.
These machines should not be connected to the Internet for security reasons.
The application is cross platform and can be run on Windows, GNU/Linux and MacOS.
App  requests password when closing it or opening new clear ballot.

[//]: # (SHA256 for checking application integrity is: ...)

## Instructions for running app:

- App uses arguments that will be read from environment variables or can be passed to it when running as system property.
  If environment variables are set, they will be used, else system properties will be used.
* If environment variables are used, the following environment variables must be added to the PC where app is started:
1. key={generated secret for MAC hash and ByCript password hasing. Must be in form of ByCript salt}
2. systemPassword={ByCript hash of password}
3. resourcePath={path to existing directory that ends with \} i.e. resourcePath=C:\Users\User\Desktop\ballots\
   In this case E-voting app can be run by double clicking it or by running in terminal: java -jar E-voting.jar
* If system properties are used then app must be started from terminal with arguments like:
  java -jar E-voting.jar -Dkey={key} -DsystemPassword={pass} -DresourcePath={path}

# VoteCounting app

This is console app that counts votes stored by E-voting app.
The application is intended to be used by the Central Election Commission (CIK).

App will ask user:
1. "Unesite putanju do .json datoteke u kojoj se nalaze glasovi:" (full path to .json file with votes generated by E-voting app)
2. "Unesite putanju i naziv .txt datoteke gdje želite da se spase prebrojani glasovi:" (full path with file name where counted votes will be stored)

After path is provided app will output counted votes or will print that vote integrity is compromised if at least one vote is changed and mac hashes do not match.
If eather mac hash or vote changed or key is not the same as key used for E-voting app, exception will be thrown: "MAC hash se ne podudara. Integritet glasova je kompromitovan!".

[//]: # (SHA256 for checking application integrity is: ...)

App can be run from terminal by typing: 
If environment variable for key is set: java -jar VoteCounter.jar
else: java -jar VoteCounter.jar -Dkey={key}
key must be same as key used for E-voting app or else exception will be thrown.

# BallotGenerator app

This is console app that generates ballots for E-voting app.
The application is intended to be used by the Central Election Commission (CIK).

App will ask user to provide:
1. "Unesite naslov glasačkog listića:" 
(Ballot title, i.e. "OPĆINSKO VIJEĆE")
2. "Unesite broj kolona za prikaz na glasačkom listiću:" 
(Number of columns on ballot, i.e. 3)
3. "Unesite putanju do .json datoteke u kojoj se nalaze stranke i kandidati:"
(Full path to .json file with candidates and parties, i.e. "C:\Users\User\Desktop\candidates.json")
4. "Unesite putanju do .fxml datoteke u koju želite da se upišu podaci:"
(Full path with file name where ballots will be stored, i.e. "C:\Users\User\Desktop\ballots\page1.fxml") 
File names must be in form of "page" + i + ".fxml": page1.json, page2.json, page3.json, ...
Directory must exist. Names must not skip numbers.

[//]: # (SHA256 for checking application integrity is: ...)


# PasswordGenerator app

This is console app that generates secret key used by other applications and hashes input password for E-voting app.
The application is intended to be used by the Central Election Commission (CIK).

App will ask user to provide:
1. "Unesite lozinku:" (asking user to enter password that will be used for E-voting app)
2. "Unesite broj iteracija za heširanje:" (asking user to enter number of iterations for password hashing)
This number should be at least 10, the larger the number the more secure the hash is, but it takes exponentially more time to generate

App will output:
1. "Ključ (key): " + key (key used by other apps)
2. "Heširana lozinka (systemPassword): " + systemPassword (ByCript hash of password used for E-voting app)

[//]: # (SHA256 for checking application integrity is: ...)


