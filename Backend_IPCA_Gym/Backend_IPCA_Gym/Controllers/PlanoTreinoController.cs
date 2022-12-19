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
                        targetPlanoTreino.tipo = reader.GetString(3);

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
                            insert into dbo.Plano_Treino (id_ginasio, tipo)
                            values (@id_ginasio, @tipo)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", newPlanoTreino.id_ginasio);
                    myCommand.Parameters.AddWithValue("tipo", newPlanoTreino.tipo);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Plano de Treino adicionado com sucesso");
        }

        [HttpPatch]
        public IActionResult Update(PlanoTreino planoTreino)
        {
            string query = @"
                            update dbo.Plano_Treino 
                            set id_ginasio = @id_ginasio, 
                            tipo = @tipo
                            where id_plano_treino = @id_plano_treino";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_plano_treino", planoTreino.id_plano_treino);
                    myCommand.Parameters.AddWithValue("id_ginasio", planoTreino.id_ginasio);
                    myCommand.Parameters.AddWithValue("tipo", planoTreino.tipo);

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
