# HowDoICraftGW2

## What is HowDoICraftGW2
HowDoICraftGW2 is a tool created to assist in crafting generic items in the massively multiplayer online roleplaying game (MMORPG) Guild Wars 2. Within the game, you have the ability to craft items. Unfortunately, the system can be somewhat cumbersome and difficult, with few tools that can help you determine how to craft specific items. At this point in the game's lifespan, approaching six years since its start, there are thousands upon thousands of each item on the trading post, the player-run market. As a result, most people rarely, if ever, seek out to craft items manually. Many believe that crafting is simply reserved for items that cannot be purchased, but I believe it is an interesting system that can be used to craft whatever you like, and makes achieving items feel more meaningful. 

As a result, this program will allow you to determine what base (or uncraftable) ingredients you need to craft a specific item and all its prerequiste components. After entering an item and a quantity to craft, you will be presented with a list of ingredients that you must either gather or purchase off of the trading post. Then, you'll be presented with a crafting order that focuses on crafting prerequistes as you go.

## How does HowDoICraftGW2 work?
Simply follow the prompts presented to you within the program and you'll be all set. There are some limitations, though. Legendary item crafting is not supported as the recipes are convoluted and difficult to retrieve from the Guild Wars 2 API.
## Technologies used in HowDoICraftGW2
At the core of the program, I use Retrofit, Gson, Xerial's SQLite JDBC, and for testing I use Junit and OkHttp's MockWebServer. The design pattern followed within the project is MVP with some simplifications, as our view is so simple (just System out/in). 

Retrofit is used to make GET requests to both GW2 and Spidy APIs. The GW2 API is the official API provided by the game developers, but much to my dismay, is quite lacking is some fields. For example, all items have item IDs, but the provided API does not have a system for looking up item IDs. As a result, the Spidy API is used. It is a fan-created API that fills in some of the gaps of the main API. 

Gson is then used to parse the responses provided by the APIs. Gson is also used to serialize recipes into the database.

Xerial's SQLite JDBC is used to connect to our SQLite database. This database is a custom database that matches recipe outputs with the recipes themselves (something the provided APIs do not provide...), and is only updated when new builds of the game are released, minimizing the number of API calls made. There are a total of 11800 recipes or so, with a limit of 200 recipe requests per GET request, meaning that many calls are needed to get all the recipes, so storing them in this database for use is the best way to handle this.

For testing, the MVP design architecture works great with Junit and MockWebServer. With all three of these technologies, creating mock web servers and mock responses to API calls are a breeze, as well as testing the underlying code regarding recipe trees.
