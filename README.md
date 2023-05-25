# demo
This application  expose two REST endpoints:

GET /api/players - returns the list of all players.
GET /api/players/{playerID} - returns a single player by ID.

In the application startup, the app loads csv file, using csv service, after that, you can launch and send API.
The application contains unit tests and configuration to H2 db. 
most of the edge cases took into accont - in level of existance.
validations were done also.
I assume there is no relationship between id and retrooID and bbrefID, because as I checked,each one was not related to other.
(Maybe ther a need to add uniqe to fields, but I didn't know if it' required..)
optimization was done to the code by implement implements CommandLineRunneron csv class, add transactional,
save in batch ,add flush mode and use with temp hashes for cities..
