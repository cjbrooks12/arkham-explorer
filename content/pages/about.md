# About Arkham Explorer

Arkham Explorer exists to provide high-quality assets, structured data, community content, and custom tools for 
_Arkham Horror: The Card Game_ by _Fantasy Flight Games_. 

This website can be installed onto your mobile device's home screen and accessed offline, allowing you to browse all
assets and use its tools without needing an internet connection.

## Official Content

The main feature of this website is a curated API and web app browser for content related to official 
_Arkham Horror: The Card Game_ content, created and published by _Fantasy Flight Games_. This includes box art, icons 
for expansions and encounter sets, and metadata for all released products. 

Much of this data is fetched from [ArkhamDB](https://arkhamdb.com/) and reformatted for better developer access, along 
with some manually-curated info that is not provided through that API. Wherever possible, all content and metadata is
sourced directly from Fantasy Flight Games (either their website, or extracted through its officially published PDFS).

All content, both literal and graphical, is copyrighted by Fantasy Flight Games. This website is not produced, endorsed,
supported, or affiliated with Fantasy Flight Games. 

## Community Content

This website also curates links to content creators, useful resources, and other things that are commonly endorsed by 
the Arkham Horror: LCG community. This content is currently curated on [this page]({{baseUrl}}/pages/resources).

## Custom Tools

There are some great tools produced by the community, but they are often in mobile apps, Excel docs, or other 
programs/formats that are difficult to casually access. So I'm using my own software development skills to help the 
community by recreating some of these tools directly on this website, so they can be accessed from anywhere.

See the list of available tools [here]({{baseUrl}}/tools).

## Offline Access

this entire website is served to you as a 
[Progressive Web App](https://en.wikipedia.org/wiki/Progressive_web_application), which means it can be installed onto 
your mobile device and accessed offline. Any content that is downloaded will be cached, so that any page you've visted
before will remain accessible, even without an internet connection.

This is especially useful for the custom [here]({{baseUrl}}/tools).

Note that as this site's content and code is still under active development and may need to be refreshed from 
time-to-time, so it is advisable that you preload the pages you need to access if you know you are going somewhere 
without internet access, to ensure those pages have the most up-to-date content and will still be available.

## API

In addition to hosting a website where non-technical users can browser and download game assets, this site provides an 
[API]({{baseUrl}}/pages/api) to allow programs to dynamically fetch this data. In fact, this entire site is served to 
you as a [Progressive Web App](https://en.wikipedia.org/wiki/Progressive_web_application), using these same APIs for 
itself. This means anything you see on this website can also be included in your application.

See more about how to interact with the API [here]({{baseUrl}}/pages/api), and Swagger documentation for the API can be
found [here]({{baseUrl}}/api/doc/).
