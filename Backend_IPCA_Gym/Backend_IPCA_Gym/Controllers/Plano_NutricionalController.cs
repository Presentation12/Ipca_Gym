using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class Plano_NutricionalController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public Plano_NutricionalController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Plano_Nutricional";
            List<PlanoNutricional> planosnutricionais = new List<PlanoNutricional>();

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
                        PlanoNutricional planoNutricional = new PlanoNutricional();

                        planoNutricional.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                        planoNutricional.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        planoNutricional.tipo = dataReader["tipo"].ToString();
                        planoNutricional.calorias = Convert.ToInt32(dataReader["calorias"]);

                        planosnutricionais.Add(planoNutricional);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(planosnutricionais);
        }
    }
}
