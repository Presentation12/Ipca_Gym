using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PlanoTreinoController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public PlanoTreinoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        protected PlanoTreino GetPlanoTreinoByID(int targetID)
        {
            string query = @"select * from dbo.Plano_Treino where id_plano_treino = @id_plano_treino";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_plano_treino", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        PlanoTreino targetPlanoTreino = new PlanoTreino();
                        targetPlanoTreino.id_plano_treino = reader.GetInt32(0);
                        targetPlanoTreino.id_ginasio = reader.GetInt32(1);
                        targetPlanoTreino.tipo = reader.GetString(2);

                        if (!Convert.IsDBNull(reader["foto_plano_treino"]))
                        {
                            targetPlanoTreino.foto_plano_treino = reader.GetString(3);
                        }
                        else
                        {
                            targetPlanoTreino.foto_plano_treino = null;
                        }

                        reader.Close();
                        databaseConnection.Close();

                        return targetPlanoTreino;
                    }
                }
            }
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Plano_Treino";
            List<PlanoTreino> planostreino = new List<PlanoTreino>();

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
                        PlanoTreino planotreino = new PlanoTreino();

                        planotreino.id_plano_treino = Convert.ToInt32(dataReader["id_plano_treino"]);
                        planotreino.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        planotreino.tipo = dataReader["tipo"].ToString();
                        
                        if (!Convert.IsDBNull(dataReader["foto_plano_treino"]))
                        {
                            planotreino.foto_plano_treino = dataReader["foto_plano_treino"].ToString();

                        }
                        else
                        {
                            planotreino.foto_plano_treino = null;
                        }

                        planostreino.Add(planotreino);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(planostreino);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Plano_Treino where id_plano_treino = @id_plano_treino";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_plano_treino", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        PlanoTreino targetPlanoTreino = new PlanoTreino();
                        targetPlanoTreino.id_plano_treino = reader.GetInt32(0);
                        targetPlanoTreino.id_ginasio = reader.GetInt32(1);
                        targetPlanoTreino.tipo = reader.GetString(2);

                        if (!Convert.IsDBNull(reader["foto_plano_treino"]))
                        {
                            targetPlanoTreino.foto_plano_treino = reader.GetString(3);
                        }
                        else
                        {
                            targetPlanoTreino.foto_plano_treino = null;
                        }

                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetPlanoTreino);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(PlanoTreino newPlanoTreino)
        {
            string query = @"
                            insert into dbo.Plano_Treino (id_ginasio, tipo, foto_plano_treino)
                            values (@id_ginasio, @tipo, @foto_plano_treino)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", newPlanoTreino.id_ginasio);
                    myCommand.Parameters.AddWithValue("tipo", newPlanoTreino.tipo);
                    if (!string.IsNullOrEmpty(newPlanoTreino.foto_plano_treino)) myCommand.Parameters.AddWithValue("foto_plano_treino", newPlanoTreino.foto_plano_treino);
                    else myCommand.Parameters.AddWithValue("foto_plano_treino", DBNull.Value);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Plano de Treino adicionado com sucesso");
        }

        [HttpPatch("{targetID}")]
        public IActionResult Update(PlanoTreino planoTreino, int targetID)
        {
            string query = @"
                            update dbo.Plano_Treino 
                            set id_ginasio = @id_ginasio, 
                            tipo = @tipo,
                            foto_plano_treino = @foto_plano_treino
                            where id_plano_treino = @id_plano_treino";

            PlanoTreino planoTreinoAtual = GetPlanoTreinoByID(targetID);

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    if (planoTreino.id_plano_treino != null) myCommand.Parameters.AddWithValue("id_plano_treino", planoTreino.id_plano_treino);
                    else myCommand.Parameters.AddWithValue("id_plano_treino", planoTreinoAtual.id_plano_treino);

                    if (planoTreino.id_ginasio != null) myCommand.Parameters.AddWithValue("id_ginasio", planoTreino.id_ginasio);
                    else myCommand.Parameters.AddWithValue("id_ginasio", planoTreinoAtual.id_ginasio);

                    if (!string.IsNullOrEmpty(planoTreino.tipo)) myCommand.Parameters.AddWithValue("tipo", planoTreino.tipo);
                    else myCommand.Parameters.AddWithValue("tipo", planoTreinoAtual.tipo);

                    if (!string.IsNullOrEmpty(planoTreino.foto_plano_treino)) myCommand.Parameters.AddWithValue("foto_plano_treino", planoTreino.foto_plano_treino);
                    else myCommand.Parameters.AddWithValue("foto_plano_treino", planoTreinoAtual.foto_plano_treino);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Plano de Treino atualizado com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Plano_Treino 
                            where id_plano_treino = @id_plano_treino";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_plano_treino", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Plano de Treino apagado com sucesso");
        }
    }
}
