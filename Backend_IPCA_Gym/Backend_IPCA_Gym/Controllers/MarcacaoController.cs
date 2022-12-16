using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MarcacaoController : ControllerBase
    {

        private readonly IConfiguration _configuration;
        public MarcacaoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Marcacao";
            List<Marcacao> marcacoes = new List<Marcacao>();

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
                        Marcacao marcacao = new Marcacao();

                        marcacao.id_marcacao = Convert.ToInt32(dataReader["id_marcacao"]);
                        marcacao.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                        marcacao.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                        marcacao.data_marcacao = Convert.ToDateTime(dataReader["data_marcacao"]);
                        marcacao.descricao = dataReader["descricao"].ToString();
                        marcacao.estado = dataReader["estado"].ToString();
                        marcacoes.Add(marcacao);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(marcacoes);
        }
    }
}
