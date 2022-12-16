using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RefeicaoController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public RefeicaoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Refeicao";
            List<Refeicao> refeicoes = new List<Refeicao>();

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
                        Refeicao refeicao = new Refeicao();

                        refeicao.id_refeicao = Convert.ToInt32(dataReader["id_refeicao"]);
                        refeicao.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                        refeicao.descricao = dataReader["descricao"].ToString();
                        refeicao.hora = (TimeSpan)dataReader["hora_saida"];

                        refeicoes.Add(refeicao);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(refeicoes);
        }
    }
}
