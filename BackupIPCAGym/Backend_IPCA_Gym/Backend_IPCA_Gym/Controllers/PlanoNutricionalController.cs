using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PlanoNutricionalController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public PlanoNutricionalController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        protected PlanoNutricional GetPlanoNutricionalByID(int targetID)
        {
            string query = @"select * from dbo.Plano_Nutricional where id_plano_nutricional = @id_plano_nutricional";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_plano_nutricional", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        PlanoNutricional targetPlanoNutricional = new PlanoNutricional();
                        targetPlanoNutricional.id_plano_nutricional = reader.GetInt32(0);
                        targetPlanoNutricional.id_ginasio = reader.GetInt32(1);
                        targetPlanoNutricional.tipo = reader.GetString(2);
                        targetPlanoNutricional.calorias = reader.GetInt32(3);
                        if (!Convert.IsDBNull(reader["foto_plano_nutricional"]))
                        {
                            targetPlanoNutricional.foto_plano_nutricional = reader.GetString(4);
                        }
                        else
                        {
                            targetPlanoNutricional.foto_plano_nutricional = null;
                        }

                        reader.Close();
                        databaseConnection.Close();

                        return targetPlanoNutricional;
                    }
                }
            }
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Plano_Nutricional";
            List<PlanoNutricional> planosnutricionais = new List<PlanoNutricional>();

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
                        PlanoNutricional planoNutricional = new PlanoNutricional();

                        planoNutricional.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                        planoNutricional.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        planoNutricional.tipo = dataReader["tipo"].ToString();
                        planoNutricional.calorias = Convert.ToInt32(dataReader["calorias"]);

                        if (!Convert.IsDBNull(dataReader["foto_plano_nutricional"]))
                        {
                            planoNutricional.foto_plano_nutricional = dataReader["foto_plano_nutricional"].ToString();
                        }
                        else
                        {
                            planoNutricional.foto_plano_nutricional = null;
                        }

                        planosnutricionais.Add(planoNutricional);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(planosnutricionais);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Plano_Nutricional where id_plano_nutricional = @id_plano_nutricional";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_plano_nutricional", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        PlanoNutricional targetPlanoNutricional = new PlanoNutricional();
                        targetPlanoNutricional.id_plano_nutricional = reader.GetInt32(0);
                        targetPlanoNutricional.id_ginasio = reader.GetInt32(1);
                        targetPlanoNutricional.tipo = reader.GetString(2);
                        targetPlanoNutricional.calorias = reader.GetInt32(3);
                        if (!Convert.IsDBNull(reader["foto_plano_nutricional"]))
                        {
                            targetPlanoNutricional.foto_plano_nutricional = reader.GetString(4);
                        }
                        else
                        {
                            targetPlanoNutricional.foto_plano_nutricional = null;
                        }
                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetPlanoNutricional);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(PlanoNutricional newPlanoNutricional)
        {
            string query = @"
                            insert into dbo.Plano_Nutricional (id_ginasio, tipo, calorias, foto_plano_nutricional)
                            values (@id_ginasio, @tipo, @calorias, @foto_plano_nutricional)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", newPlanoNutricional.id_ginasio);
                    myCommand.Parameters.AddWithValue("tipo", newPlanoNutricional.tipo);
                    myCommand.Parameters.AddWithValue("calorias", newPlanoNutricional.calorias);
                    
                    if (!string.IsNullOrEmpty(newPlanoNutricional.foto_plano_nutricional)) myCommand.Parameters.AddWithValue("foto_plano_nutricional", newPlanoNutricional.foto_plano_nutricional);
                    else myCommand.Parameters.AddWithValue("foto_plano_nutricional", DBNull.Value);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Plano Nutricional adicionado com sucesso");
        }

        [HttpPatch("{targetID}")]
        public IActionResult Update(PlanoNutricional planoNutricional, int targetID)
        {
            string query = @"
                            update dbo.Plano_Nutricional 
                            set id_ginasio = @id_ginasio, 
                            tipo = @tipo,
                            calorias = @calorias,
                            foto_plano_nutricional = @foto_plano_nutricional
                            where id_plano_nutricional = @id_plano_nutricional";

            PlanoNutricional planoNutricionalAtual = GetPlanoNutricionalByID(targetID);

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    if (planoNutricional.id_plano_nutricional != null) myCommand.Parameters.AddWithValue("id_plano_nutricional", planoNutricional.id_plano_nutricional);
                    else myCommand.Parameters.AddWithValue("id_plano_nutricional", planoNutricionalAtual.id_plano_nutricional);

                    if (planoNutricional.id_ginasio != null) myCommand.Parameters.AddWithValue("id_ginasio", planoNutricional.id_ginasio);
                    else myCommand.Parameters.AddWithValue("id_ginasio", planoNutricionalAtual.id_ginasio);

                    if (!string.IsNullOrEmpty(planoNutricional.tipo)) myCommand.Parameters.AddWithValue("tipo", planoNutricional.tipo);
                    else myCommand.Parameters.AddWithValue("tipo", planoNutricionalAtual.tipo);

                    if (planoNutricional.calorias != null) myCommand.Parameters.AddWithValue("calorias", planoNutricional.calorias);
                    else myCommand.Parameters.AddWithValue("calorias", planoNutricionalAtual.calorias);

                    if (!string.IsNullOrEmpty(planoNutricional.foto_plano_nutricional)) myCommand.Parameters.AddWithValue("foto_plano_nutricional", planoNutricional.foto_plano_nutricional);
                    else myCommand.Parameters.AddWithValue("foto_plano_nutricional", planoNutricionalAtual.foto_plano_nutricional);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Plano Nutricional atualizado com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Plano_Nutricional 
                            where id_plano_nutricional = @id_plano_nutricional";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_plano_nutricional", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Plano Nutricional apagado com sucesso");
        }
    }
}
