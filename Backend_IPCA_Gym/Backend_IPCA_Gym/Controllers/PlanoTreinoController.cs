using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PlanoTreinoController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public PlanoTreinoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Plano_Treino";
            List<PlanoTreino> planostreino = new List<PlanoTreino>();

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        PlanoTreino planotreino = new PlanoTreino();

                        planotreino.id_plano_treino = Convert.ToInt32(dataReader["id_plano_treino"]);
                        planotreino.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        planotreino.tipo = dataReader["tipo"].ToString();

                        planostreino.Add(planotreino);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(planostreino);
        }
    }
}
