//Events object
function terrenos(){};
//exported through exports.Terrenos
exports.terrenos = terrenos;

//DB client is passed and inititialized from outside...
exports.setAndConnectClient = function(_client){
  client = _client;  //assign it to the module's client var
  client.connect();  //connect to DB...
}

//GET ALL TERRENOSs!
terrenos.prototype.getAllTerrenos = function(callback){
   var allTerrenos = [];
   var query =  client.query('SELECT * FROM terreno', function(err, result){
      allTerrenos = result.rows;
      //return an empty object if no events exist
      if(allTerrenos.length == 0) {
         callback([]);
      }else callback(allTerrenos);
    });
}
