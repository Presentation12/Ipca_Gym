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

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Atividade where id_atividade = @id_atividade";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_atividade", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Atividade targetAtividade = new Atividade();
                        targetAtividade.id_atividade = reader.GetInt32(0);
                        targetAtividade.id_ginasio = reader.GetInt32(1);
                        targetAtividade.id_cliente = reader.GetInt32(2);
                        targetAtividade.data_entrada = reader.GetDateTime(3);
                        targetAtividade.data_saida = reader.GetDateTime(4);

                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetAtividade);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Atividade newAtividade)
        {
            string query = @"
                            insert into dbo.Atividade (id_ginasio, id_cliente, data_entrada, data_saida)
                            values (@id_ginasio, @id_cliente, @data_entrada, @data_saida)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", newAtividade.id_ginasio);
                    myCommand.Parameters.AddWithValue("id_cliente", newAtividade.id_cliente);
                    myCommand.Parameters.AddWithValue("data_entrada", Convert.ToDateTime(newAtividade.data_entrada));
                    myCommand.Parameters.AddWithValue("data_saida", Convert.ToDateTime(newAtividade.data_saida));
                    dataReader = myCommand.ExecuteReader();
                    
                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Atividade adicionada com sucesso");
        }

        [HttpPatch]
        public IActionResult Update(Atividade atividade)
        {
            string query = @"
                            update dbo.Atividade 
                            set id_ginasio = @id_ginasio, 
                            id_cliente = @id_cliente, 
                            data_entrada = @data_entrada, 
                            data_saida = @data_saida
                            where id_atividade = @id_atividade";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", atividade.id_ginasio);
                    myCommand.Parameters.AddWithValue("id_cliente", atividade.id_cliente);
                    myCommand.Parameters.AddWithValue("data_entrada", Convert.ToDateTime(atividade.data_entrada));
                    myCommand.Parameters.AddWithValue("data_saida", Convert.ToDateTime(atividade.data_saida));

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Atividade atualizada com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Atividade 
                            where id_atividade = @id_atividade";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_atividade", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Atividade apagada com sucesso");
        }
    }
}
