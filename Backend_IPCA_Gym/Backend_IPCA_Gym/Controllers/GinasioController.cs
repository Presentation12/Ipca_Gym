using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Npgsql;
using System.Data;

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
                            select * from ginasio";

            DataTable table = new DataTable();

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            NpgsqlDataReader dataReader;
            using (NpgsqlConnection databaseConnection = new NpgsqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (NpgsqlCommand myCommand = new NpgsqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    table.Load(dataReader);

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(table);
        }
    }
}
