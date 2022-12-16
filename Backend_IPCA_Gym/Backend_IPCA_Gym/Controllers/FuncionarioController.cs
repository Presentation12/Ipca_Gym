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
    public class FuncionarioController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public FuncionarioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Funcionario";
            List<Funcionario> funcionarios = new List<Funcionario>();

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
                        Funcionario funcionario = new Funcionario();

                        funcionario.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                        funcionario.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        funcionario.nome = dataReader["nome"].ToString();
                        funcionario.is_admin = Convert.ToByte(dataReader["is_admin"]);
                        funcionario.codigo = Convert.ToInt32(dataReader["codigo"]);
                        funcionario.pass_salt = dataReader["pass_salt"].ToString();
                        funcionario.pass_hash = dataReader["pass_hash"].ToString();
                        funcionario.estado = dataReader["estado"].ToString();

                        funcionarios.Add(funcionario);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(funcionarios);
        }
    }
}
