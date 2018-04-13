# Cardinal
Cardinal is a basic peer-to-peer drawing program written in Java. With it, users can create a drawing either alone or together with other users. Four drawing tools exist; the line tool, the ellipse tool, the rectangle tool and the fill tool. Users may change the color used with each of these tools as well as the width of the line, ellipse and rectangle tools. Drawings can be saved in the common formats (JPG, PNG & GIF).

## Building
Before building, verify that you have [Gradle](http://gradle.org/) installed on your system. Gradle 4.6 or later will suffice. To compile and package the application, navigate with your terminal to the repository root and run `gradle jar`. If the compilation is successful the application will be output to the `app` directory located under the repository root. The application can now be launched by double-clicking the JAR file located within the `app` directory. While this method of building and testing the system is quick and simple an IDE is recommended for an increased development speed during prolonged periods of development.
