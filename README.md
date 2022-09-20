
This is the source code and data for the Arkham Explorer website, a collection of data and tools for Arkham Horror: The 
Card Game by Fantasy Flight Games. 

This is a fully-static website and Single-Page Application, hosted on GitHub Pages. It requires Java 11+ to build and 
run the site, and uses the following folder structure:

- `content/` contains all data and images being manually curated for this site. The static site generator combines this
  data with data from other sources, which are all listed in the [Credits](#credits) below.
- `site/spa/` contains all the code for manging interactions and displaying content in the front end website. It is 
  written with [Kotlin Compose for Web](https://github.com/JetBrains/compose-jb/tree/master/tutorials/Web) and uses 
  [Bulma](https://bulma.io/) for styling.
- `site/spa/` contains all the code for manging interactions and displaying content in the front end website.
- `site/shared/` contains data models and utility code shared by both the front-end website and the static site generator 

## Single-Page Application

The SPA portion of the website is built with [Kotlin Compose for Web](https://github.com/JetBrains/compose-jb/tree/master/tutorials/Web),
using [Bulma](https://bulma.io/) for styling. State management is provided by [Ballast](https://github.com/copper-leaf/ballast).
Together these work similar to React with Redux, but written in Kotlin instead of JavaScript and JSX.

The SPA application lives in `site/spa/` and is set up so that the site is not intended to run directly from this 
project. Instead, it should run once to generate the application JS files, and those will then be used by the SSG 
project to actually serve the site locally or prepare it for publication to GitHub Pages.

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

## Disclaimer

This is a fan-made project and website, which is in no way produced, endorsed, supported, or affiliated with Fantasy 
Flight Games. All game content in this repo and in the published website, both literal and graphical, is copyrighted by 
Fantasy Flight Games. 

Most content in this site was originally curated by other fans, but those sources are often not in formats that can be
easily exposed over an API. Whenever possible, data exposed through the Arkham Explorer API is fetched and cached 
locally from another API, but when that is not possible, that data is manually copied to the source code in this repo. 
In all circumstances, the original authors will be credited for their hard work, because without them this site would 
not be possible.

## Credits

- [ArkhamDB API](https://arkhamdb.com/api/): Pack and card data
- [Strange Eons Arkaham HorrorLCG Plugin](https://boardgamegeek.com/thread/1688703/strange-eons-plugin): Fonts
- [Arkham Horror LCG Photoshop templates](https://boardgamegeek.com/filepage/140374/custom-card-templates-300dpi-psd): Card Designer Templates
- [Official FFG sources](https://www.fantasyflightgames.com/en/products/arkham-horror-the-card-game/):
  - Expansion, Scenario, and Encounter Set Icons extracted from rulebook PDFs
  - Expansion flavor text from the product page text
  - Some data was transcribed manually from official rulebooks
- Box art is from the respective photo galleries on BoardGameGeek, credited to those original posters
- 

