using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PedidoLojaController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public PedidoLojaController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Pedido_Loja";
            List<PedidoLoja> pedidosloja = new List<PedidoLoja>();

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
                        PedidoLoja pedidoloja = new PedidoLoja();

                        pedidoloja.id_pedido = Convert.ToInt32(dataReader["id_pedido"]);
                        pedidoloja.id_produto = Convert.ToInt32(dataReader["id_produto"]);
                        pedidoloja.quantidade = Convert.ToInt32(dataReader["quantidade"]);

                        pedidosloja.Add(pedidoloja);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(pedidosloja);
        }
    }
}
