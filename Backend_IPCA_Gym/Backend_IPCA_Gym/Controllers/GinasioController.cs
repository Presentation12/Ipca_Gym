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
    public class GinasioController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public GinasioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        protected Ginasio GetGinasioByID(int targetID)
        {
            string query = @"select * from dbo.Ginasio where id_ginasio = @id_ginasio";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_ginasio", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Ginasio targetGinasio = new Ginasio();
                        targetGinasio.id_ginasio = reader.GetInt32(0);
                        targetGinasio.instituicao = reader.GetString(1);
                        targetGinasio.contacto = reader.GetInt32(2);

                        if (!Convert.IsDBNull(reader["foto_ginasio"]))
                        {
                            targetGinasio.foto_ginasio = reader.GetString(3);
                        }
                        else
                        {
                            targetGinasio.foto_ginasio = null;
                        }

                        targetGinasio.estado = reader.GetString(4);

                        reader.Close();
                        databaseConnection.Close();

                        return targetGinasio;
                    }
                }
            }
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Ginasio";
            List<Ginasio> ginasios = new List<Ginasio>();

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
                        Ginasio ginasio = new Ginasio();

                        ginasio.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        ginasio.estado = dataReader["estado"].ToString();
                        ginasio.instituicao = dataReader["instituicao"].ToString();
                        if (!Convert.IsDBNull(dataReader["foto_ginasio"]))
                        {
                            ginasio.foto_ginasio = dataReader["foto_ginasio"].ToString();
                        }
                        else
                        {
                            ginasio.foto_ginasio = null;
                        }
                        ginasio.contacto = Convert.ToInt32(dataReader["contacto"]);

                        ginasios.Add(ginasio);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(ginasios);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Ginasio where id_ginasio = @id_ginasio";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_ginasio", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Ginasio targetGinasio = new Ginasio();
                        targetGinasio.id_ginasio = reader.GetInt32(0);
                        targetGinasio.instituicao = reader.GetString(1);
                        targetGinasio.contacto = reader.GetInt32(2);

                        if (!Convert.IsDBNull(reader["foto_ginasio"]))
                        {
                            targetGinasio.foto_ginasio = reader.GetString(3);
                        }
                        else
                        {
                            targetGinasio.foto_ginasio = null;
                        }

                        targetGinasio.estado = reader.GetString(4);

                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetGinasio);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Ginasio newGinasio)
        {
            string query = @"
                            insert into dbo.Ginasio (instituicao, contacto, foto_ginasio, estado)
                            values (@instituicao, @contacto, @foto_ginasio, @estado)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("instituicao", newGinasio.instituicao);
                    myCommand.Parameters.AddWithValue("contacto", newGinasio.contacto);
                    if (!string.IsNullOrEmpty(newGinasio.foto_ginasio)) myCommand.Parameters.AddWithValue("foto_ginasio", newGinasio.foto_ginasio);
                    else myCommand.Parameters.AddWithValue("foto_ginasio", DBNull.Value);
                    myCommand.Parameters.AddWithValue("estado", newGinasio.estado);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Ginásio adicionado com sucesso");
        }

        [HttpPatch("{targetID}")]
        public IActionResult Update(Ginasio ginasio, int targetID)
        {
            string query = @"
                            update dbo.Ginasio 
                            set instituicao = @instituicao, 
                            contacto = @contacto, 
                            foto_ginasio = @foto_ginasio, 
                            estado = @estado
                            where id_ginasio = @id_ginasio";

            Ginasio ginasioAtual = GetGinasioByID(targetID);

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    if (ginasio.id_ginasio != null) myCommand.Parameters.AddWithValue("id_ginasio", ginasio.id_ginasio);
                    else myCommand.Parameters.AddWithValue("id_ginasio", ginasioAtual.id_ginasio);

                    if (!string.IsNullOrEmpty(ginasio.instituicao)) myCommand.Parameters.AddWithValue("instituicao", ginasio.instituicao);
                    else myCommand.Parameters.AddWithValue("instituicao", ginasioAtual.instituicao);

                    if (ginasio.contacto != null) myCommand.Parameters.AddWithValue("contacto", ginasio.contacto);
                    else myCommand.Parameters.AddWithValue("contacto", ginasioAtual.contacto);

                    if (!string.IsNullOrEmpty(ginasio.foto_ginasio)) myCommand.Parameters.AddWithValue("foto_ginasio", ginasio.foto_ginasio);
                    else myCommand.Parameters.AddWithValue("foto_ginasio", ginasioAtual.foto_ginasio);

                    if (!string.IsNullOrEmpty(ginasio.estado)) myCommand.Parameters.AddWithValue("estado", ginasio.estado);
                    else myCommand.Parameters.AddWithValue("estado", ginasioAtual.estado);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Ginásio atualizado com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Ginasio 
                            where id_ginasio = @id_ginasio";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Ginásio apagado com sucesso");
        }
    }
}
