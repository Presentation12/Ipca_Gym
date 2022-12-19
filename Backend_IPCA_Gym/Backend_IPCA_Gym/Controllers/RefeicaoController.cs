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
                        refeicao.hora = (TimeSpan)dataReader["hora"];
                        if (!Convert.IsDBNull(dataReader["foto_refeicao"]))
                        {
                            refeicao.foto_refeicao = dataReader["foto_refeicao"].ToString();
                        }
                        else
                        {
                            refeicao.foto_refeicao = null;
                        }


                        refeicoes.Add(refeicao);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(refeicoes);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Refeicao where id_refeicao = @id_refeicao";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_refeicao", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Refeicao targetRefeicao = new Refeicao();
                        targetRefeicao.id_refeicao = reader.GetInt32(0);
                        targetRefeicao.id_plano_nutricional = reader.GetInt32(1);
                        targetRefeicao.descricao = reader.GetString(2);
                        targetRefeicao.hora = reader.GetTimeSpan(4);
                        
                        if (!Convert.IsDBNull(reader["foto_refeicao"]))
                        {
                            targetRefeicao.foto_refeicao = reader.GetString(5);
                        }
                        else
                        {
                            targetRefeicao.foto_refeicao = null;
                        }

                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetRefeicao);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Refeicao newRefeicao)
        {
            string query = @"
                            insert into dbo.Refeicao (id_plano_nutricional, descricao, hora, foto_refeicao)
                            values (@id_plano_nutricional, @descricao, @hora, @foto_refeicao)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_plano_nutricional", newRefeicao.id_plano_nutricional);
                    myCommand.Parameters.AddWithValue("descricao", newRefeicao.descricao);
                    myCommand.Parameters.AddWithValue("hora", newRefeicao.hora);

                    if (!string.IsNullOrEmpty(newRefeicao.foto_refeicao)) myCommand.Parameters.AddWithValue("foto_refeicao", newRefeicao.foto_refeicao);
                    else myCommand.Parameters.AddWithValue("foto_refeicao", string.Empty);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Refeição adicionado com sucesso");
        }

        [HttpPatch]
        public IActionResult Update(Refeicao refeicao)
        {
            string query = @"
                            update dbo.Refeicao 
                            set id_plano_nutricional = @id_plano_nutricional, 
                            descricao = @descricao,
                            hora = @hora,
                            foto_refeicao = @foto_refeicao
                            where id_refeicao = @id_refeicao";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_refeicao", refeicao.id_refeicao);
                    myCommand.Parameters.AddWithValue("id_plano_nutricional", refeicao.id_plano_nutricional);
                    myCommand.Parameters.AddWithValue("descricao", refeicao.descricao);
                    myCommand.Parameters.AddWithValue("hora", refeicao.id_plano_nutricional);
                    
                    if (!string.IsNullOrEmpty(refeicao.foto_refeicao)) myCommand.Parameters.AddWithValue("foto_refeicao", refeicao.foto_refeicao);
                    else myCommand.Parameters.AddWithValue("foto_refeicao", string.Empty);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Refeição atualizado com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Refeicao 
                            where id_refeicao = @id_refeicao";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_refeicao", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Refeição apagado com sucesso");
        }
    }
}
