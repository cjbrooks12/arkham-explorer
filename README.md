
This is the source code and data for the Arkham Explorer website. This is a fully-static website and Single-Page 
Application, hosted on GitHub Pages. 

- Java 11+

## Single-Page Application

The SPA portion of the website is built with [Kotlin Compose for Web](https://github.com/JetBrains/compose-jb/tree/master/tutorials/Web),
using [Bulma](https://bulma.io/) for styling.

- Serve locally: `./gradlew :app:jsBrowserDevelopentRun`
  - This will run the site via WebPack on `http://localhost:8080`
- Build for production: `./gradlew :app:build -Prelease`
  - This will generate all the files needed to deploy into `app/build/distributions`. This entire folder should be 
    deployed to the webserver

## Static Website

The static website portion is a custom Kotlin CLI application which copies files from multiple directories into a single
one, suitable for deploying to GitHub Pages.

- Build for production: `./gradlew :site:run -Prelease`
  - This will generate all the files needed to deploy into `build/dist`. This entire folder should be
    deployed to the webserver



- 11am: Get to office
- 10am: leave house
- 9-10am: get brisket out of smoker
- 5am: wrap brisket
- 10:30-11pm: put brisket on smoker
- 9:30: start trimming and seasoning brisket

- 9:30am: get ribs out of smoker
- 7:30am: wrap ribs (brown sugar, butter, honey, seasoning, apple juice)
- 5am: put ribs on smoker (just rub, spritz with apple juice every hour)
