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
    public class ExercicioController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public ExercicioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Exercicio";
            List<Exercicio> exercicios = new List<Exercicio>();

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
                        Exercicio exercicio = new Exercicio();

                        exercicio.id_exercicio = Convert.ToInt32(dataReader["id_exercicio"]);
                        exercicio.id_plano_treino = Convert.ToInt32(dataReader["id_plano_treino"]);
                        exercicio.nome = dataReader["nome"].ToString();
                        exercicio.descricao = dataReader["descricao"].ToString();
                        exercicio.tipo = dataReader["tipo"].ToString();
                        exercicio.series = Convert.ToInt32(dataReader["series"]);
                        exercicio.tempo = (TimeSpan?)dataReader["tempo"];
                        exercicio.repeticoes = Convert.ToInt32(dataReader["repeticoes"]);

                        exercicios.Add(exercicio);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(exercicios);
        }
    }
}
