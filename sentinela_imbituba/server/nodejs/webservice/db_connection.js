//require postgre driver
var pg = require('pg');   
//config file contains DB credentials, schema...
var config = require('./db_config.json');  
 
//var conString = "postgres://"+config.db.username+":"+config.db.password+
//"@"+config.db.host+":"+config.db.port+"/"+config.db.name;

//var conString = "postgres://postgres:postgres@localhost:5432/sentinela";

var conString = process.env.DATABASE_URL || 'postgres://postgres:postgres@localhost:5432/sentinela'; //postgres://YourUserName:YourPassword@localhost:5432/YourDatabase";

var client = new pg.Client(conString);
 
if(!client){
  console.log("Starting client to DB "+conString+ " failed")
}else{
  console.log("Started client to  DB"+client.host+"/"+client.database);
}
 
exports.client = client;
