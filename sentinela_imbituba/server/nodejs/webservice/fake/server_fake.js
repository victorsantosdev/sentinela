//Define the port to listen to
var PORT = process.env.PORT || 30000;
//Include retify.js framework
var restify = require('restify');

var options = {
  serverName: 'v_server',
  accept: [ 'application/json' ]
}

//Create the server
var server = restify.createServer(options);

//Use bodyParser to read the body of incoming requests
server.use(restify.bodyParser({ mapParams: false }));

server.listen(PORT, '0.0.0.0');
console.log("listening "+PORT);


//IMPORT RESOURCES
var terrenosResource = require('./terrenos_fake');

//DEFINE THE URIs THE SERVER IS RESPONDING TO
server.get('/terrenos_fake', function(req, res) {

  var terrenos = new terrenosResource.terrenos() ;

  //Get all events from DB
  terrenos.getAllTerrenos(function(result){

    var allTerrenos = result;

    //If no events exist return 200 and and empty JSON
    if(allTerrenos.length == 0) {
      //res.send(200, []);
      res.send([]);
      return;
    //}else res.send(200, res_jsonObj);
    }else res.send(allTerrenos);
  });

});
