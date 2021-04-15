# ENSF 409 Final Project - Supply Chain Management (Group 30)

## Contributors
---
- Parbir Lehal
- Abhay Khosla
- Mushtaba Al Yasseen

## About The Project
---
This project is a supply chain management application which reads from inventory.sql database that consists of manufacturers and 4 different kinds of furniture. It then takes in input from the user and provides the user with a order form that displays the cheapest combination of partial furniture which give a complete furniture.

## Demo Video Link
---
[Demo Video](https://www.youtube.com/watch?v=RVE_cPWRPe0&ab_channel=ParbirLehal)
## Usage
---
### Run Application via Terminal
1. Assuming you have extracted zip folder, now open your desired command prompt.
2. Make sure you have the inventory.sql and in the MySQL command line client, create the inventory database using the command "source path-to-inventory-file" or alternatively use SQL workbench.
3. Next to compile, use command,
```bash
javac -cp .;mysql-connector-java-8.0.23.jar;. edu/ucalgary/ensf409/Main.java
```
4. Finally to run, use command,
```bash
java -cp .;mysql-connector-java-8.0.23.jar;. edu/ucalgary/ensf409/Main
```

### Run Unit Tests via Terminal 
#### Make sure you refresh inventory.sql database after every test!!!
1. Again, assuming you have extracted zip folder, now open your desired command prompt.
2. Next to compile, use command (run command from src folder),
```bash
javac -cp .;/junit-4.13.2.jar;/hamcrest-core-1.3.jar;/mysql-connector-java-8.0.23.jar;. edu/ucalgary/ensf409/<TestClassName.java>
```
3. Finally to run, use command (run command from src folder),
```bash
java -cp .;/hamcrest-core-1.3.jar;/junit-4.13.2.jar;/mysql-connector-java-8.0.23.jar;. org.junit.runner.JUnitCore edu.ucalgary.ensf409.<TestClassName>
```
##### Note: Replace ; with : if using linux/mac.

## UML Diagram
Available as FinalProjectUML.drawio in Group30 folder

