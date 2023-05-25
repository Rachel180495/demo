# demo
This application  expose two REST endpoints:

GET /api/players - returns the list of all players.
GET /api/players/{playerID} - returns a single player by ID.

In the application startup, the app loads csv file, using csv service, after that, you can launch and send API.
The application contains unit tests and configuration to H2 db. 
most of the edge cases took into accont - in level of existance.
What wasn't done ,and should be exist also is validation to the content of each field, like valid height ,valid month etc.
