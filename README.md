
This is the source code and data for the Arkham Explorer website. This is a fully-static website and Single-Page 
Application, hosted on GitHub Pages. 

- Java 11+

## Single-Page Application

The SPA portion of the website is built with [Kotlin Compose for Web](https://github.com/JetBrains/compose-jb/tree/master/tutorials/Web),
using [Bulma](https://bulma.io/) for styling. The SPA application lives in `site/spa/` and is set up so that the site
is not intended to run directly from this project. Instead, it should run once to generate the application JS files, and 
those will then be used by the SSG project to actually test the site.

- Build locally: `./gradlew :app:build`
  - This will build the debug version of the site, but you will need to run the static website to actually test the app.
- Build continually: `./gradlew -t :app:build`
  - This will be useful for developing locally. While the SSG is running in one terminal window/tab, you can open 
    another to run this command, so that any changes made to the SPA code will automatically recompile and subsequently
    rebuild the static website.
- Build for production: `./gradlew :app:build -Prelease`
  - This will generate all the files needed to deploy into `app/build/distributions`. These outputs are not intended to
    be deployed on their own, but instead used as inputs to the static website fron the `:site:ssg` project (see below)

## Static Website

The static website portion is a custom Kotlin CLI application which copies files from multiple directories into a single
one, suitable for deploying to GitHub Pages. It is optimized for local development, such that changes to input files 
will only re-render the output files that actually use those inputs. 

You must make sure to build the SPA first or have it building continuously before running the static site.

- Run locally: `./gradlew :site:run`
  - This will analyze the inputs and serve the site locally on http://localhost:8080 without actually writing any files
    to disk. The site will repeatedly check for changes while serving, and always serve you the latest version of any 
    requested output file.
- Build for production: `./gradlew :site:run -Prelease`
  - This will generate all the files needed to deploy into `build/distributions`. This entire folder should be
    deployed to the webserver.
