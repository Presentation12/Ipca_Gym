using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Npgsql;
using System.Data;
using System.Data.SqlClient;
using System.Numerics;
using System.Runtime.Intrinsics.Arm;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ClassificacaoController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public ClassificacaoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Classificacao";
            List<Classificacao> classificacoes = new List<Classificacao>();

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
                        Classificacao classificacao = new Classificacao();

                        classificacao.id_avaliacao = Convert.ToInt32(dataReader["id_avaliacao"]);
                        classificacao.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        classificacao.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                        classificacao.avaliacao = Convert.ToInt32(dataReader["avaliacao"]);
                        classificacao.comentario = dataReader["comentario"].ToString();
                        classificacao.data_avaliacao = Convert.ToDateTime(dataReader["data_avaliacao"]);

                        classificacoes.Add(classificacao);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(classificacoes);
        }
    }
}
