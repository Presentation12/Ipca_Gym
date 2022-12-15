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
                        ginasio.foto_ginasio = dataReader["foto_ginasio"].ToString();
                        ginasio.contacto = Convert.ToInt32(dataReader["contacto"]);

                        ginasios.Add(ginasio);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(ginasios);
        }
    }
}
