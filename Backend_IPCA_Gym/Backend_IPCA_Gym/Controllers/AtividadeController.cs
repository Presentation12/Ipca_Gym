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
    public class AtividadeController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public AtividadeController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Atividade";
            List<Atividade> atividades = new List<Atividade>();

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
                        Atividade atividade = new Atividade();

                        atividade.id_atividade = Convert.ToInt32(dataReader["id_atividade"]);
                        atividade.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        atividade.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                        atividade.data_entrada = Convert.ToDateTime(dataReader["data_entrada"]);
                        atividade.data_saida = Convert.ToDateTime(dataReader["data_saida"]);

                        atividades.Add(atividade);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(atividades);
        }
    }
}
