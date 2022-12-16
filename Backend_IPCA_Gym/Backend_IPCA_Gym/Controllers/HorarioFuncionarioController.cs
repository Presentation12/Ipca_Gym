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
    public class HorarioFuncionarioController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public HorarioFuncionarioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Horario_Funcionario";
            List<HorarioFuncionario> horario = new List<HorarioFuncionario>();

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
                        HorarioFuncionario dia = new HorarioFuncionario();

                        dia.id_funcionario_horario = Convert.ToInt32(dataReader["id_funcionario_horario"]);
                        dia.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                        dia.hora_entrada = (TimeSpan)dataReader["hora_entrada"];
                        dia.hora_saida = (TimeSpan)dataReader["hora_saida"];
                        dia.dia_semana = dataReader["dia_semana"].ToString();

                        horario.Add(dia);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(horario);
        }
    }
}
