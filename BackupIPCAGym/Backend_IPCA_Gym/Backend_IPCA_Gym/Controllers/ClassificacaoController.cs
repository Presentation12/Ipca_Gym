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

        protected Classificacao GetClassificacaoByID(int targetID)
        {
            string query = @"select * from dbo.Classificacao where id_avaliacao = @id_avaliacao";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_avaliacao", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Classificacao targetClassificacao = new Classificacao();
                        targetClassificacao.id_avaliacao = reader.GetInt32(0);
                        targetClassificacao.id_ginasio = reader.GetInt32(1);
                        targetClassificacao.id_cliente = reader.GetInt32(2);
                        targetClassificacao.avaliacao = reader.GetInt32(3);
                        targetClassificacao.comentario = reader.GetString(4);
                        targetClassificacao.data_avaliacao = reader.GetDateTime(5);

                        reader.Close();
                        databaseConnection.Close();

                        return targetClassificacao;
                    }
                }
            }
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

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Classificacao where id_avaliacao = @id_avaliacao";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_avaliacao", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Classificacao targetClassificao = new Classificacao();
                        targetClassificao.id_avaliacao = reader.GetInt32(0);
                        targetClassificao.id_ginasio = reader.GetInt32(1);
                        targetClassificao.id_cliente = reader.GetInt32(2);
                        targetClassificao.avaliacao = reader.GetInt32(3);
                        targetClassificao.comentario = reader.GetString(4);
                        targetClassificao.data_avaliacao = reader.GetDateTime(5);

                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetClassificao);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Classificacao newAtividade)
        {
            string query = @"
                            insert into dbo.Classificacao (id_ginasio, id_cliente, avaliacao, comentario, data_avaliacao)
                            values (@id_ginasio, @id_cliente, @avaliacao, @comentario, @data_avaliacao)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", newAtividade.id_ginasio);
                    myCommand.Parameters.AddWithValue("id_cliente", newAtividade.id_cliente);
                    myCommand.Parameters.AddWithValue("avaliacao", newAtividade.avaliacao);
                    myCommand.Parameters.AddWithValue("comentario", newAtividade.comentario);
                    myCommand.Parameters.AddWithValue("data_avaliacao", Convert.ToDateTime(newAtividade.data_avaliacao));
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Classificação adicionada com sucesso");
        }

        [HttpPatch("{targetID}")]
        public IActionResult Update(Classificacao classificacao, int targetID)
        {
            string query = @"
                            update dbo.Classificacao 
                            set id_ginasio = @id_ginasio, 
                            id_cliente = @id_cliente, 
                            avaliacao = @avaliacao, 
                            comentario = @comentario,
                            data_avaliacao = @data_avaliacao
                            where id_avaliacao = @id_avaliacao";

            Classificacao classificacaoAtual = GetClassificacaoByID(targetID);

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    if (classificacao.id_avaliacao != null) myCommand.Parameters.AddWithValue("id_avaliacao", classificacao.id_avaliacao);
                    else myCommand.Parameters.AddWithValue("id_avaliacao", classificacaoAtual.id_avaliacao);

                    if (classificacao.id_ginasio != null) myCommand.Parameters.AddWithValue("id_ginasio", classificacao.id_ginasio);
                    else myCommand.Parameters.AddWithValue("id_ginasio", classificacaoAtual.id_ginasio);

                    if (classificacao.id_cliente != null) myCommand.Parameters.AddWithValue("id_cliente", classificacao.id_cliente);
                    else myCommand.Parameters.AddWithValue("id_cliente", classificacaoAtual.id_cliente);

                    if (classificacao.avaliacao != null) myCommand.Parameters.AddWithValue("avaliacao", classificacao.avaliacao);
                    else myCommand.Parameters.AddWithValue("avaliacao", classificacaoAtual.avaliacao);

                    if (!string.IsNullOrEmpty(classificacao.comentario)) myCommand.Parameters.AddWithValue("comentario", classificacao.comentario);
                    else myCommand.Parameters.AddWithValue("comentario", classificacaoAtual.comentario);

                    if (classificacao.data_avaliacao != null) myCommand.Parameters.AddWithValue("data_avaliacao", Convert.ToDateTime(classificacao.data_avaliacao));
                    else myCommand.Parameters.AddWithValue("data_avaliacao", Convert.ToDateTime(classificacaoAtual.data_avaliacao));

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Classificação atualizada com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Classificacao 
                            where id_avaliacao = @id_avaliacao";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_avaliacao", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Classificação apagada com sucesso");
        }
    }
}
