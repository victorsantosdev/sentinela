//Events object
function terrenos(){};
//exported through exports.Terrenos
exports.terrenos = terrenos;

//GET ALL EVENTS!
terrenos.prototype.getAllTerrenos = function(callback){
   var allTerrenos = [];
   /*resposta do servidor em arquivo,  para testar sem necessidade da estrutura PostgreSQL*/

var jsonObj = require("./dados_fake.json");
   var allTerrenos = [];

      allTerrenos = jsonObj;
      //return an empty object if no events exist
      if(allTerrenos.length == 0) {
         callback([]);
      }else callback(allTerrenos);
    };
