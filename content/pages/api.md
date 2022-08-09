# Arkham Explorer API

One of the biggest motivations to create this site was to provide the Arkham Horror LCG community with a centralized`` 
location for game data and assets. Typically, one owuld have to scour through forums on BoardGameGeek, Reddit, or 
the Mythos Busters Discord server to find the assets they're looking for, and even then they're typically not in a 
format that is suitable for programming applications.

Arkham Explorer offers a fully-documented API to enable programmers and software developers to fetch and display data
for their applications, without needing to gather and format all that info themselves.

The API can be accessed at the base URL of `{{baseUrl}}/api`, and Swagger documentation can be found 
[here]({{baseUrl}}/api/doc/).

All data offered through the API can be used free-of-charge with no restrictions on usage limits. However, the site is
being generously served for free as static JSON files and other assets via 
[GitHub Pages](https://docs.github.com/en/pages/getting-started-with-github-pages/about-github-pages), but it has soft
limits on monthly bandwidth and potential rate-limits if you are not careful with how you access the API. Please cache 
all content fetched from this API to ensure it will always be available to all developers and users of your application.

Additionally, since the content may be updated over time, it is advisable to fetch these assets at runtime from your
application when possible, rather than downloading and hardcoding them in your app's source code. This will ensure your
app always has the most up-to-date data available, frees you from the burden to managing those assets yourself, and will 
help make Arkham Explorer remain the central location on the for this data.

All information and assets provided by this API is copyrighted by Fantasy Flight Games. This website and API is not 
produced, endorsed, supported, or affiliated with Fantasy Flight Games. Additionally, some content has been taken from
the [ArkhamDB API](https://arkhamdb.com/api/), and reformatted to be more easily discoverable and fix some issues 
with the formatting of data presented by that API. 
